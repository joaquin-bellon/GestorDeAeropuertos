package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

public class ReservasGestor {

	private ArrayList<Reserva> reservas;
	private PasajerosGestor gestorPasajeros;
	private VuelosGestor gestorVuelos;

	private static ReservasGestor instancia;

	public static ReservasGestor getInstance() {
		if (instancia == null) {
			instancia = new ReservasGestor();
		}
		return instancia;
	}

	private ReservasGestor() {
		this.gestorPasajeros = PasajerosGestor.getInstance();
		this.gestorVuelos = VuelosGestor.getInstance();
		reservas =(ArrayList<Reserva>) abrirCSV();
	}



	private ArrayList<Reserva> abrirCSV() {
		reservas = null;
		try (BufferedReader reader = new BufferedReader(new FileReader (new File(Utilidades.RUTA_RESERVAS)))) {
			CsvToBeanBuilder<Reserva> csvBulider = new CsvToBeanBuilder<Reserva>(reader);
			reservas = (ArrayList<Reserva>)csvBulider.withType(Reserva.class)
					.withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).withSeparator(',').build().parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reservas;
	}

	public void volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_RESERVAS)))) {
			StatefulBeanToCsv<Reserva> beantoCsv = new StatefulBeanToCsvBuilder<Reserva>(writer).withOrderedResults(true)
					.withApplyQuotesToAll(false).build();
			beantoCsv.write(reservas);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}

	}

	public void mostrarReservas () {
		for (Reserva reserva : reservas) {
			imprimirReserva(reserva);
		}
	}

	public void mostrarArrayReservas (ArrayList<Reserva> arrayReservas) {
		for (Reserva reserva : arrayReservas) {
			System.out.println("---------------------------------");
			System.out.println(reserva);
		}
	}

	public void imprimirReserva(Reserva reserva) {
		System.out.println("==================================");
		System.out.println(reserva);
		Pasajero pasajero = gestorPasajeros.buscarPorCodPasajero(reserva.getPasajero());
		if (pasajero != null) {
			System.out.println(pasajero);
		}
		else {
			System.out.println("Pasajero " + reserva.getPasajero() + " no encontrado");
		}
		Vuelo vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
		if (vuelo != null) {
			System.out.println(vuelo);
		}
		else {
			System.out.println("Vuelo " + reserva.getVuelo() + " no encontrado");
		}
		System.out.println("==================================");
	}
	public void buscarReservas (Scanner sc) {
		int opcion;
		Reserva reserva;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio de busqueda: 1.Id Reserva 2.Pasajero 3.Vuelos");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		System.out.println("--------------------------------");

		switch (opcion) {
		case 1:
			System.out.println("Introduce el id de la reserva");
			String idReserva = sc.nextLine().trim().toUpperCase();
			reserva = buscarPorCodigo(idReserva);
			if (reserva == null) {
				System.out.println("Reserva no encontrada");
			}
			else {
				imprimirReserva(reserva);
			}
			break;
		case 2:
			System.out.println("Inserte el id del pasajero:");
			String pasajero = sc.nextLine().trim().toUpperCase();
			if (!gestorPasajeros.existeCodPasajero(pasajero)) {
				System.out.println("No se ha encontrado el pasajero.");
			}
			else {
				ArrayList<Reserva> reservasPasajero = buscarResevasPorPasajero(pasajero);
				if (reservasPasajero.size() == 0) {
					System.out.println("Este usuario no ha efectuado ninguna reserva.");
				}
				else {
					for (Reserva reservaPasajero : reservasPasajero) {
						imprimirReserva(reservaPasajero);
					}
				}
			}
			break;
		case 3:
			System.out.println("Inserte el codigo del vuelo:");
			String vueloString = sc.nextLine().trim().toUpperCase();
			Vuelo vuelo = gestorVuelos.buscarPorCodigo(vueloString);
			if (vuelo== null) {
				System.out.println("No se ha encontrado el vuelo.");
			}
			else {
				ArrayList<Reserva> reservasVuelo = buscarResevasPorVuelo(vuelo.getCodigo());
				if (reservasVuelo.size() == 0) {
					System.out.println("Este vuelo no tiene asociada ninguna reserva.");
				}
				else {
					for (Reserva reservaVuelo : reservasVuelo) {
						imprimirReserva(reservaVuelo);
					}
				}
			}
			break;
		}

	}



	public ArrayList<Reserva> buscarResevasPorVuelo(String vuelo) {
		ArrayList<Reserva> reservasVuelo = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getVuelo().equals(vuelo)) {
				reservasVuelo.add(reserva);
			}
		}
		return reservasVuelo;
	}

	public ArrayList<Reserva> buscarResevaConfirmadasPorVuelo(String vuelo) {
		ArrayList<Reserva> reservasVuelo = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals("CONFIRMADA")) {
				reservasVuelo.add(reserva);
			}
		}
		return reservasVuelo;
	}
	
	
	
	public ArrayList<Reserva> buscarResevaCanceladasPorVuelo(String vuelo) {
		ArrayList<Reserva> reservasVuelo = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals("CANCELADA")) {
				reservasVuelo.add(reserva);
			}
		}
		return reservasVuelo;
	}



	public ArrayList<Reserva> buscarReservasPorNacionalOInternacional(String tipo) {
		ArrayList<Reserva> reservasTipo = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			Vuelo vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
			if (vuelo != null && gestorVuelos.comprobarVueloNacionaleInternacional(vuelo).equals(tipo)) {
				reservasTipo.add(reserva);
			}
		}
		return reservasTipo;
	}

	public ArrayList<Reserva> buscarResevasPorPasajero(String pasajero) {
		ArrayList<Reserva> reservasPasajero = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getPasajero().equals(pasajero)) {
				reservasPasajero.add(reserva);
			}
		}
		return reservasPasajero;
	}



	public void insertarReserva(Scanner sc) {
		aniadirReservaAlArray(sc);
		volcarEnCSV();

	}



	private void aniadirReservaAlArray(Scanner sc) {

		String [] datos = new String [10];
		System.out.println("Creación de una Reserva");
		System.out.println("------------------------------");
		//Pasajero
		Vuelo vuelo;
		datos[1] = gestorPasajeros.pedirPasajero(sc, "de la reserva").getCodPasajero();
		//Vuelo

		do {
			System.out.println("Código de vuelo:");
			datos[2] = sc.nextLine().trim().toUpperCase();
			vuelo = gestorVuelos.buscarPorCodigo(datos[2]);
			if ( vuelo == null) {
				System.err.println("Ese vuelo no existe. Inténtelo de nuevo.");
				datos[2] = null;
			}else if (vuelo.getEstado().equals("CANCELADO")) {
				System.err.println("Ese vuelo está cancelado. Inténtelo de nuevo.");
				datos[2] = null;
			}
			}while(datos[2] == null);

		//fecha
		LocalDate fecha;
		do {
			fecha = Utilidades.pedirFecha(sc, "¿Cúal es la fecha de la reserva?:");
			if (fecha.isAfter(vuelo.getSalida().toLocalDate())) {
				System.err.println("La fecha de reserva no puede ser posterior a la salida del vuelo ("
						+ vuelo.getSalida().toLocalDate() + "). Inténtelo de nuevo.");
			}
		}while (fecha.isAfter(vuelo.getSalida().toLocalDate()));
		datos [8] = String.valueOf(fecha);
		//Asiento
		do {
		System.out.println("Asiento:");
		datos [3] = sc.nextLine().trim().toUpperCase();
		if (datos[3].isEmpty()) {
			System.err.println("No se puede ir de pie, inténtelo de nuevo");
		}
		else if (comprobarAsiento(vuelo.getCodigo(), datos[3])) {
			System.err.println("Ese asiento está ocupado. Inténtelo de nuevo.");
			datos[3] = "";
		}

		}while (datos[3].isEmpty());
		//Clase
		datos[4] = seleccionarClase(sc, vuelo);
		//precio
		datos [5] = String.valueOf(pedirPrecio(sc));

		//Moneda
		datos [6] = "EUR";
		//Estado
		datos [7] = "CONFIRMADA";
		//Canal
		datos [9] = escogerCanal(sc);

		datos [0] = crearNuevoIdentificador();
		reservas.add(new Reserva (datos[0],datos[1],datos[2],datos[3],datos[4],Double.parseDouble(datos[5]),datos[6],datos[7],datos[8],datos[9]));
		System.out.println("Reserva " + datos[0] + " creada correctamente.");
	}



	private double pedirPrecio(Scanner sc) {
		double precio = -1;
		do {
			try {
				System.out.println("¿Cúal es el precio de la reserva?");
				precio = Double.parseDouble(sc.nextLine().trim());
				if (precio < 100 ) {
					System.err.println("Introduzca un precio realista.");
				}
			}catch (NumberFormatException e) {
				System.err.println("Introduzca un número.");
			}
		}while(precio < 100);
		return precio;
	}



	private String escogerCanal(Scanner sc) {
		int opcion;
		System.out.println("Escoja un canal: 1.WEB - 2. APP - 3.TAQUILLA - 4.AGENCIA");
		opcion = Utilidades.leerOpcion(sc, 1, 4);
		String canal ="";
		switch (opcion) {
		case 1:
			canal = "WEB";
			break;
		case 2:
			canal = "APP";
			break;
		case 3:
			canal = "TAQUILLA";
			break;
		case 4:
			canal = "AGENCIA";
		}
		return canal;
	}



	private String seleccionarClase(Scanner sc, Vuelo vuelo) {
		int opcion;
		Element avion = gestorVuelos.sacarAvionVuelo(vuelo);
		NodeList clases= avion.getElementsByTagName("Clase");
		ArrayList<String> clasesStrings = new ArrayList<String>();
		for (int i = 0 ; i < clases.getLength() ; i++) {
			String clase = ((Element) clases.item(i)).getAttribute("tipo");
			if (!clasesStrings.contains(clase)) {
				clasesStrings.add(clase);
			}

		}
		System.out.print("Escoja clase:");
		for (int i = 0 ; i < clasesStrings.size() ; i++) {
			System.out.print(" " + (i+1) + ". " + clasesStrings.get(i));
		}
		System.out.println("");
		opcion = Utilidades.leerOpcion(sc, 1, clasesStrings.size());
		opcion--;

		return clasesStrings.get(opcion);
	}



	private String crearNuevoIdentificador() {
		int dato = 0;
		for (int i = 0 ; i < reservas.size() ; i++) {
			String id = reservas.get(i).getIdReserva();
			if (!id.startsWith("RS")) {
				int aux = Integer.parseInt(id.substring(1));
				if (aux > dato) {
					dato = aux;
				}
			}
		}
		return String.format("R%04d", dato + 1);
	}



	public void cancelarReservasManual(Scanner sc, EquipajesGestor gestorEquipajes, EmbarquesGestor gestorEmbarques) {
		String cod = null;
		ArrayList<Reserva> reservasEstado = buscarPorEstado("CONFIRMADA");
		for (Reserva reserva: reservasEstado) {
			imprimirReserva(reserva);
		}
		do {
			System.out.println("Introduce el código de la reserva a cancelar:");
			cod = sc.nextLine().trim().toUpperCase();
			Reserva reserva = buscarPorCodigo(cod);
			if (reserva == null) {
				System.out.println("Esa reserva no existe");
				cod = null;
			}
			else if (reserva.getEstado().equals("CANCELADA")) {
				System.out.println("Esa reserva ya está cancelada");
				cod = null;
			}
			else {
				reserva.setEstado("CANCELADA");
			}
		}while (cod == null);
		volcarEnCSV();

		System.out.println("Equipaje asociado a la Reserva:");
		ArrayList<Equipaje> equipajesDeLaReserva = gestorEquipajes.buscarEquipajesPorReserva(cod);
		if (equipajesDeLaReserva.size() != 0) {
			for (Equipaje equipaje : equipajesDeLaReserva) {
				System.out.println ("  " + equipaje);
			}
		}
		else {
			System.out.println("  No hay equipaje asociado a la reserva.");
		}



		System.out.println("==================================");
		System.out.println("Embarque asociado a la Reserva:");
		Embarque embarqueDeLaReserva = gestorEmbarques.buscarPorReserva(cod);
		if ( embarqueDeLaReserva != null) {
			System.out.println ("  " + embarqueDeLaReserva);
		}
		else {
			System.out.println("  No hay embarques asociados a la reserva.");
		}


		System.out.println("Reserva " + cod + " cancelada correctamente");


	}

	public Reserva buscarPorCodigo(String cod) {
		Iterator<Reserva> iterator = reservas.iterator();
		Reserva reserva = null;
		boolean encontrado = false;
		while (iterator.hasNext() && !encontrado) {
			reserva = iterator.next();
			if (reserva.getIdReserva().equals(cod)) {
				encontrado = true;
			}
		}
		if (encontrado) {
			return reserva;
		}
		else {
			return null;
		}

	}

	public Reserva pedirReserva(Scanner sc, String accion) {
		Reserva reserva;
		System.out.println("Introduce el código de la reserva " + accion + ":");
		do {
			reserva = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (reserva == null) {
				System.err.println("No se ha encontrado la reserva. Inténtelo de nuevo:");
			}
		} while (reserva == null);
		return reserva;
	}

	public Reserva pedirReservaConfirmada(Scanner sc, String accion) {
		Reserva reserva;
		System.out.println("Introduce el código de la reserva " + accion + ":");
		do {
			reserva = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (reserva == null) {
				System.err.println("No se ha encontrado la reserva. Inténtelo de nuevo:");
			}
			else if (!reserva.getEstado().equals("CONFIRMADA")) {
				System.err.println("La reserva no esta confirmada. Introduce otra:");
				reserva = null;
			}
		} while (reserva == null);
		return reserva;
	}


	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public ArrayList<Reserva> buscarPorEstado (String estado) {
		ArrayList<Reserva> reservasEstado = new ArrayList<Reserva>();
		for ( Reserva reserva : reservas) {
			if (reserva.getEstado().equals(estado)) {
				reservasEstado.add(reserva);
			}
		}
		return reservasEstado;
	}



	public void actualizarReserva(Scanner sc) {
		Reserva reserva = pedirReserva(sc, "a actualizar");

		int opcion = 0;
		System.out.println("--------------------------------");
		System.out.println(reserva);
		System.out.println("--------------------------------");
		System.out.println("Escoge el criterio : 1.Asiento 2. Precio");

		opcion = Utilidades.leerOpcion(sc, 1, 2);
		System.out.println("--------------------------------");

		switch (opcion) {
		case 1:
			System.out.println("Introduce el nuevo asiento.");
			String asiento = sc.nextLine().trim().toUpperCase();
			if (asiento.isBlank()) {
				System.err.println("No se puede ir de pie");
			}
			else {
				boolean ocupado = comprobarAsiento(reserva.getVuelo(), asiento);
				if (ocupado) {
					System.out.println("Lo siento. Ese asiento está ocupado.");
				}
				else {
					reserva.setAsiento(asiento);
					System.out.println("Cambio de asiento efectuado con éxito.");
				}
			}
			break;
		case 2:
			System.out.println("Introduce el nuevo precio");
			double precio = pedirPrecio(sc);
			reserva.setPrecio(precio);
			System.out.println("Precio actualizado correctamente.");
			break;
		}
		volcarEnCSV();

	}



	public boolean comprobarAsiento(String vuelo, String asiento) {
		boolean ocupado = false;
		for (Reserva reserva : reservas) {
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals("CONFIRMADA")) {
				if (reserva.getAsiento().equals(asiento)) {
					ocupado = true;
				}
			}
		}
		return ocupado;
	}



	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}



	public void comprobarPosibleEmbarquePendiente(Scanner sc,Vuelo vuelo) {
		System.out.println("-------------------------------------------");
		System.out.println("Comprobando si es posible embarcar:");
		LocalDateTime horaSalidaDelVuelo = vuelo.getSalida();
		Long apertura = Long.parseLong(vuelo.getMinutosInicioEmbarque());
		Long cierre = Long.parseLong(vuelo.getMinutosCierreEmbarque());
		LocalDateTime fechaUsuario = LocalDateTime.of(Utilidades.pedirFecha(sc, "Introduce la fecha actual"), Utilidades.pedirHora(sc, "Introduce la hora actual"));
		long difMinutos = ChronoUnit.MINUTES.between(fechaUsuario, horaSalidaDelVuelo);
		if (difMinutos > apertura) {
			System.out.println("Todavia es demasiado pronto para embarcar. La ventana se abrirá en " + (difMinutos-apertura) + " minutos");
		}
		else if (difMinutos < cierre) {
			System.out.println("Ya se ha pasado la hora del embarque. La ventana esta cerrada");
		}
		else if (difMinutos-cierre <= 10) {
			System.out.println("La puerta está próxima a cerrar");
		}
		else {
			System.out.println("La ventana está abierta y cerrara en " + (difMinutos-cierre) + " minutos");
		}

	}

	public ArrayList<Reserva> obtenerReservasConfirmadas () {
		ArrayList<Reserva> reservasConfirmadas = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals("CONFIRMADA")) {
				reservasConfirmadas.add(reserva);
			}
		}
		return reservasConfirmadas;
	}
	
	public ArrayList<Reserva> obtenerReservasCanceladas () {
		ArrayList<Reserva> reservasCanceladas = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals("CANCELADA")) {
				reservasCanceladas.add(reserva);
			}
		}
		return reservasCanceladas;
	}
	
	public double obtenerGastoTotalDeLasReservas(ArrayList<Reserva> arrayReservas) {
		double total=0;
		for (Reserva reserva : arrayReservas) {
			total += reserva.getPrecio();
		}
				return total;
		
	}
	
	public ArrayList<Reserva> filtrarReservasVueloPorEstado(ArrayList<Reserva> reservasDelVuelo, String estado, EmbarquesGestor gestorEmbarques) {
		ArrayList<Reserva> reservasADevolver = new ArrayList<Reserva>();
		for (Reserva reserva : reservasDelVuelo) {
			String estadoDelEmbarque;
			try {
				estadoDelEmbarque= gestorEmbarques.buscarPorReserva(reserva.getIdReserva()).getEstado();
			}catch(NullPointerException e) {
				estadoDelEmbarque = "PENDIENTE";
			}

			if (estadoDelEmbarque.equals(estado)) {
				reservasADevolver.add(reserva);
			}
					}

		return reservasADevolver;

	}
	
	public ArrayList<Reserva> obtenerReservasSinEmbarqueYConVueloAsignado (ArrayList<Reserva> arrayReservas, EmbarquesGestor gestorEmbarques) {
		ArrayList<Reserva> reservasSinEmbarqueYConVuelo = new ArrayList<Reserva>();
		for (Reserva reserva : arrayReservas) {
			if (gestorEmbarques.buscarPorReserva(reserva.getIdReserva()) == null && !gestorVuelos.buscarPorCodigo(reserva.getVuelo()).getEstado().equals("CANCELADO")) {
				reservasSinEmbarqueYConVuelo.add(reserva);
			}
		}
		return reservasSinEmbarqueYConVuelo;
	}
	
	public ArrayList<Reserva> obtenerReservasPorCanal(String canal) {
		ArrayList<Reserva> reservasDelCanal = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getCanal().equals(canal)) {
				reservasDelCanal.add(reserva);
			}
		}
		return reservasDelCanal;
	}



	public ArrayList<Reserva> obtenerReservasCanceladasPorCanal(String canal) {
		ArrayList<Reserva> reservasDelCanal = new ArrayList<Reserva>();
		ArrayList<Reserva> reservasCanceladas = obtenerReservasCanceladas();
		for (Reserva reserva : reservasCanceladas) {
			if (reserva.getCanal().equals(canal)) {
				reservasDelCanal.add(reserva);
			}
		}
		return reservasDelCanal;
	}
	
}
