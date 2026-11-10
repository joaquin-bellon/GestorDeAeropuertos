package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Embarque;
import modelo.Equipaje;
import modelo.Pasajero;
import modelo.Reserva;
import modelo.Vuelo;
import utilidades.Utilidades;

public class PasajerosGestor {
	private ArrayList<Pasajero> pasajeros;
	private static PasajerosGestor instancia;

	public static PasajerosGestor getInstance() {
		if (instancia == null) {
			instancia = new PasajerosGestor();
		}
		return instancia;
	}

	private PasajerosGestor() {
		pasajeros =(ArrayList<Pasajero>) abrirCSV();
	}



	private ArrayList<Pasajero> abrirCSV() {
		pasajeros = null;
		try (BufferedReader reader = new BufferedReader(new FileReader (new File(Utilidades.RUTA_PASAJEROS)))) {
			CsvToBeanBuilder<Pasajero> csvBulider = new CsvToBeanBuilder<Pasajero>(reader);
			pasajeros = (ArrayList<Pasajero>)csvBulider.withType(Pasajero.class).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).withSeparator(',').build().parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pasajeros;
	}



	public void mostrarPasajeros () {
		for (Pasajero pasajero : pasajeros) {
			System.out.println(pasajero);
		}

	}



	public void volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_PASAJEROS)))) {
			StatefulBeanToCsv<Pasajero> beantoCsv = new StatefulBeanToCsvBuilder<Pasajero>(writer).withOrderedResults(true).withApplyQuotesToAll(false).build();
			beantoCsv.write(pasajeros);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}

	}



	public void buscarPasajeros(Scanner sc) {
		System.out.println("------------------------");
		System.out.println("Criterios:");
		System.out.println("1.Codigo - 2.Documento Identificativo - 3.Nacionalidad - 4.Tipo");
		System.out.println("Inserte su criterio de búsqueda:");
		int opcion;
		opcion = Utilidades.leerOpcion(sc, 1, 4);
		System.out.println("------------------------");
		switch (opcion) {
		case 1:
			System.out.println("¿Qué código quiere buscar?");
			String cod = sc.nextLine().trim().toUpperCase();
			boolean existe = existeCodPasajero(cod);
			if (existe) {
				System.out.println(buscarPorCodPasajero(cod));
			}
			else {
				System.out.println("No se ha encontrado ningún pasajero con ese código.");
			}
			break;
		case 2:
			System.out.println("¿Qué Documento Identificativo quiere buscar?");
			String documento = sc.nextLine().trim();
			existe = existeDNI(documento);
			if (existe) {
				System.out.println(buscarporDNI(documento));
			}
			else {
				System.out.println("No se ha encontrado ningún pasajero con ese documento identificativo.");
			}
			break;
		case 3:
			System.out.println("¿Por qué nacionalidad quieres filtrar?:");
			String pais = sc.nextLine().trim();
			ArrayList<Pasajero> pasajerosPais = filtrarPorPais(pais);
			if (pasajerosPais.size() != 0) {
				for (Pasajero pasajero : pasajerosPais) {
					System.out.println(pasajero);
				}
			}
			else {
				System.out.println("No hay ningún pasajero con esa nacionalidad");
			}
			break;
		case 4:
			ArrayList<Pasajero> pasajerosTipo = null;
			System.out.println("Tipos: 1.ADULTO - 2.VIP - 3.MENOR");
			int aux = Utilidades.leerOpcion(sc, 1, 3);
			switch (aux) {
			case 1:
				pasajerosTipo = filtrarPorTipo("ADULTO");
				System.out.println("Pasajeros ADULTOS:");
				break;
			case 2:
				pasajerosTipo = filtrarPorTipo("VIP");
				System.out.println("Pasajeros VIP:");
				break;
			case 3:
				pasajerosTipo = filtrarPorTipo("MENOR");
				System.out.println("Pasajeros MENOR:");
				break;
			}
			System.out.println("-----------------------------------");
			for ( Pasajero pasajero : pasajerosTipo) {
				System.out.println(pasajero);
			}
			break;
		}
		System.out.println("-----------------------------------");
	}

	public void insertarPasajero(Scanner sc) {
		aniadirPasajeroAlArray(sc);
		volcarEnCSV();
		System.out.println("Usuario introducido correctamente");
	}

	public boolean eliminarPasajero(String codPasajero) {
		Pasajero pasajero = buscarPorCodPasajero(codPasajero);
		if (pasajero == null) {
			return false;
		}

		ReservasGestor gestorReservas = ReservasGestor.getInstance();
		EquipajesGestor gestorEquipajes = EquipajesGestor.getInstance();
		EmbarquesGestor gestorEmbarques = EmbarquesGestor.getInstance();
		boolean reservaEliminada = false, equipajeEliminado = false, embarqueEliminado = false;

		ArrayList<Reserva> reservasAEliminar = gestorReservas.buscarResevasPorPasajero(pasajero.getCodPasajero());
		for (Reserva reserva : reservasAEliminar) {
			reservaEliminada = true;

			ArrayList<Equipaje> equipajesAEliminar = gestorEquipajes.buscarEquipajesPorReserva(reserva.getIdReserva());
			if (equipajesAEliminar.size() != 0) {
				equipajeEliminado = true;
				gestorEquipajes.getEquipajes().removeAll(equipajesAEliminar);
			}

			Embarque embarqueAEliminar = gestorEmbarques.buscarPorReserva(reserva.getIdReserva());
			if (embarqueAEliminar != null) {
				embarqueEliminado = true;
				gestorEmbarques.getEmbarques().remove(embarqueAEliminar);
			}

			gestorReservas.getReservas().remove(reserva);
		}

		if (reservaEliminada) {
			gestorReservas.volcarEnCSV();
		}
		if (equipajeEliminado) {
			gestorEquipajes.volcarEnCSV();
		}
		if (embarqueEliminado) {
			gestorEmbarques.volcarEnCSV();
		}
		pasajeros.remove(pasajero);
		volcarEnCSV();
		return true;
	}

	private void aniadirPasajeroAlArray(Scanner sc) {
		String [] datos = new String [7];
		System.out.println("Creación de un Pasajero");
		System.out.println("------------------------------");
		//Nombre
		System.out.println("Nombre:");
		datos[1] = sc.nextLine().trim();

		//Apellidos
		System.out.println("Apellidos:");
		datos[2] = sc.nextLine().trim();

		//DocumentoIdentificativo
		boolean existe = false;
		do {
		System.out.println("Documento identificativo:");
		datos[3] = sc.nextLine().trim();
		existe = existeDNI(datos[3]);
		if (existe) {
			System.err.println("Este Documento identificativo ya está en uso");
		}
		}while(existe);

		//Nacionalidad
		System.out.println("Nacionalidad:");
		datos[4] = sc.nextLine().trim();

		//Correo
		existe = false;
		do {
		System.out.println("Correo:");
		datos[5] = sc.nextLine().trim();
		existe = existeCorreo(datos[5]);
		if(existe) {
			System.err.println("Este correo ya está en uso");
		}
		}while(existe);

		//Tipo
		System.out.println("Escoja el tipo: 1.ADULTO - 2.VIP - 3.MENOR");
		int num = Utilidades.leerOpcion(sc, 1, 3);
		switch(num) {
		case 1:
			datos[6] = "ADULTO";
			break;
		case 2:
			datos[6] = "VIP";
			break;
		case 3:
			datos[6] = "MENOR";
			break;
		}

		datos [0] = crearNuevoIdentificador();

		pasajeros.add(new Pasajero (datos[0],datos[1],datos[2],datos[3],datos[4],datos[5],datos[6]));

	}



	private String crearNuevoIdentificador() {
		int dato = 0;
		for (Pasajero pasajero : pasajeros) {
			int aux = Integer.parseInt(pasajero.getCodPasajero().substring(1));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("P%03d", dato + 1);
	}

	public ArrayList<Pasajero> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(ArrayList<Pasajero> pasajeros) {
		this.pasajeros = pasajeros;
	}

	public Pasajero buscarporDNI (String dni) {
		Pasajero pasajeroARetornar = null;
		for (Pasajero pasajero : pasajeros) {
			if (dni.equals(pasajero.getDocumentoIdentificativo())) {
				pasajeroARetornar = pasajero;
			}
		}
		return pasajeroARetornar;
	}
	public boolean existeDNI (String dni) {
		boolean existe = false;
		for (Pasajero pasajero : pasajeros) {
			if (dni.equals(pasajero.getDocumentoIdentificativo())) {
				existe = true;
			}
		}
		return existe;
	}

	public boolean existeCorreo (String correo) {
		boolean existe = false;
		for (Pasajero pasajero : pasajeros) {
			if (correo.equals(pasajero.getCorreo())) {
				existe = true;
			}
		}
		return existe;
	}

	public Pasajero buscarPorCodPasajero (String codPasajero) {
		Pasajero pasajeroAReturn = null;
		for (Pasajero pasajero : pasajeros) {
			if (codPasajero.equals(pasajero.getCodPasajero())) {
				pasajeroAReturn = pasajero;
			}
		}
		return pasajeroAReturn;
	}

	public boolean existeCodPasajero (String codPasajero) {
		boolean existe = false;
		for (Pasajero pasajero : pasajeros) {
			if (codPasajero.equals(pasajero.getCodPasajero())) {
				existe = true;
			}
		}
		return existe;
	}

	public Pasajero pedirPasajero(Scanner sc, String accion) {
		Pasajero pasajero;
		System.out.println("Introduce el código del pasajero " + accion + ":");
		do {
			pasajero = buscarPorCodPasajero(sc.nextLine().trim().toUpperCase());
			if (pasajero == null) {
				System.err.println("Este código no es válido. Inténtelo de nuevo:");
			}
		} while (pasajero == null);
		return pasajero;
	}

	private Pasajero escogerPasajero (Scanner sc, String accion) {
		System.out.println("---------------------------------------");
		System.out.println("Pasajeros:");
		mostrarPasajeros();
		return pedirPasajero(sc, "a " + accion);
	}

	public void actualizarPasajero(Scanner sc) {
		Pasajero pasajero = escogerPasajero(sc, "actualizar");
		System.out.println("---------------------------------");
		System.out.println("""
				1. Nombre
				2. Apellidos
				3. Documento Identificativo
				4. Pais
				5. correo electronico
				6. Tipo
				¿Que dato quieres editar?:""");
		int opcion = Utilidades.leerOpcion(sc, 1, 6);
		switch (opcion-1) {

			case 0://Nombre
				System.out.println("Introduce el nuevo nombre:");
				pasajero.setNombre(sc.nextLine().trim());
				System.out.println("Nombre cambiado con éxito");
				break;

			case 1://Apellidos
				System.out.println("Introduce los nuevos apellidos:");
				pasajero.setApellidos(sc.nextLine().trim());
				System.out.println("Apellidos cambiados con éxito");
				break;

			case 2://Documento Identificativo
				System.out.println("Introduce el nuevo documento identificativo:");
				boolean documentoExistente = false;
				String documento;
				System.out.println("Documento identificativo:");
				do {
				documento = sc.nextLine().trim();
				documentoExistente = existeDNI(documento);
				if (documentoExistente) {
					System.err.println("Este Documento identificativo ya está en uso.Inténtelo con otro:");
				}
				}while(documentoExistente);
				pasajero.setDocumentoIdentificativo(documento);
				System.out.println("Documento Identificativo cambiado con éxito");
				break;

			case 3://Pais
				System.out.println("Introduce el nuevo país:");
				pasajero.setPais(sc.nextLine().trim());
				System.out.println("País cambiada con éxito");
				break;

			case 4://Correo
				System.out.println("Introduce el nuevo correo electrónico:");
				boolean correoExistente= false;
				String correo="";
				correoExistente = false;
				do {
				correo = sc.nextLine().trim();
				correoExistente = existeCorreo(correo);
				if(correoExistente) {
					System.err.println("Este correo ya está en uso. Inténtelo de nuevo:");
				}
				}while(correoExistente);
				pasajero.setCorreo(correo);
				System.out.println("Correo electrónico cambiado con éxito");
				break;

			case 5://Tipo
				String tipo = "";
				System.out.println("Escoja el nuevo tipo: 1.ADULTO - 2.VIP - 3.MENOR");
				int num = Utilidades.leerOpcion(sc, 1, 3);
				switch(num) {
				case 1:
					tipo = "ADULTO";
					break;
				case 2:
					tipo = "VIP";
					break;
				case 3:
					tipo = "MENOR";
					break;
				}

				pasajero.setTipo(tipo);
				System.out.println("Tipo actualizado correctamente");

				break;
		}
		volcarEnCSV();
	}



	public ArrayList<Pasajero> filtrarPorPais(String pais) {
		ArrayList<Pasajero> pasajerosNacionalidad = new ArrayList<Pasajero>();
		for (Pasajero pasajero : pasajeros) {
			if ( pasajero.getPais().equalsIgnoreCase(pais)) {
				pasajerosNacionalidad.add(pasajero);
			}
		}
		return pasajerosNacionalidad;
	}



	public ArrayList<Pasajero> filtrarPorTipo(String tipo) {
		ArrayList<Pasajero> pasajerosTipo = new ArrayList<Pasajero>();
		for (Pasajero pasajero : pasajeros) {
			if ( pasajero.getTipo().equalsIgnoreCase(tipo)) {
				pasajerosTipo.add(pasajero);
			}
		}
		return pasajerosTipo;
	}



	public HashMap<Pasajero, ArrayList<Reserva>> ObtenerPasajerosConSusReservas(ReservasGestor gestorReservas) {
		HashMap<Pasajero, ArrayList<Reserva>> cantidadReserva = new HashMap<Pasajero, ArrayList<Reserva>>();
		for (Pasajero pasajero : pasajeros) {
			cantidadReserva.put(pasajero, new ArrayList<Reserva>());
		}
		for (Reserva reserva : gestorReservas.getReservas()) {
			Pasajero pasajero = buscarPorCodPasajero(reserva.getPasajero());
			cantidadReserva.get(pasajero).add(reserva);
		}
		return cantidadReserva;
	}
	
	public HashSet<Pasajero> obtenerPasajerosConReservaCancelada(ReservasGestor gestorReservas) {
		HashSet<Pasajero> pasajerosconReservaCancelada = new HashSet<Pasajero>();
		ArrayList<Reserva> reservasCanceladas = gestorReservas.obtenerReservasCanceladas();
		for (Reserva reserva : reservasCanceladas) {
			pasajerosconReservaCancelada.add(buscarPorCodPasajero(reserva.getPasajero()));
		}
		return pasajerosconReservaCancelada;
	}
		
	public HashSet<Pasajero> obtenerPasajerosConReservaConfirmadaEnVueloCancelado (VuelosGestor gestorVuelos, ReservasGestor gestorReservas) {
		HashSet<Pasajero> pasajerosConReservaConfirmadaEnVueloCancelado = new HashSet<Pasajero>();
		ArrayList<Vuelo> vuelosCancelados = gestorVuelos.buscarVuelosPorEstado("CANCELADO");
		for (Vuelo vueloCancelado : vuelosCancelados) {
			ArrayList<Reserva> reservasConfirmadasDelVuelo = gestorReservas.buscarResevaConfirmadasPorVuelo(vueloCancelado.getCodigo());
			for (Reserva reserva : reservasConfirmadasDelVuelo) {
				pasajerosConReservaConfirmadaEnVueloCancelado.add(buscarPorCodPasajero(reserva.getPasajero()));
			}
		}
		return pasajerosConReservaConfirmadaEnVueloCancelado;
	}
	
	public ArrayList<Pasajero> obtenerPasajerosConfirmadosPorTipoDelVuelo(Vuelo vuelo, String tipo, ReservasGestor gestorReservas ) {
		HashSet<Pasajero> pasajerosTipoDelVuelo = new HashSet<Pasajero>();
		ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo());
		for (Reserva reserva : reservasDelVuelo) {
			Pasajero pasajeroReserva = buscarPorCodPasajero(reserva.getPasajero());
			if (pasajeroReserva.getTipo().equals(tipo)) {
				pasajerosTipoDelVuelo.add(pasajeroReserva);
			}
		}
 		return new ArrayList<Pasajero>(pasajerosTipoDelVuelo);
	}
}
