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
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Equipaje;
import modelo.Reserva;
import utilidades.Utilidades;

public class EquipajesGestor {
	private ReservasGestor gestorReservas;
	private PasajerosGestor gestorPasajeros;
	private VuelosGestor gestorVuelos;
	private ConfiguracionGestor gestorConfiguracion;
	private ArrayList<Equipaje> equipajes;
	private static EquipajesGestor instancia;

	public static EquipajesGestor getInstance() {
		if (instancia == null) {
			instancia = new EquipajesGestor();
		}
		return instancia;
	}

	private EquipajesGestor() {
		this.gestorReservas = ReservasGestor.getInstance();
		this.gestorPasajeros = PasajerosGestor.getInstance();
		this.gestorVuelos = VuelosGestor.getInstance();
		this.gestorConfiguracion = ConfiguracionGestor.getInstance();
		equipajes = abrirCSV();
	}

	private ArrayList<Equipaje> abrirCSV() {
		equipajes = null;
		try (BufferedReader reader = new BufferedReader(new FileReader (new File(Utilidades.RUTA_EQUIPAJES)))) {
			CsvToBeanBuilder<Equipaje> csvBulider = new CsvToBeanBuilder<Equipaje>(reader);
			equipajes = (ArrayList<Equipaje>)csvBulider.withType(Equipaje.class)
					.withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).withSeparator(',').build().parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return equipajes;
	}

	public void volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_EQUIPAJES)))) {
			StatefulBeanToCsv<Equipaje> beantoCsv = new StatefulBeanToCsvBuilder<Equipaje>(writer).withOrderedResults(true)
					.withApplyQuotesToAll(false).build();
			beantoCsv.write(equipajes);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}

	}

	public void mostrarEquipajes() {
		for (Equipaje equipaje : equipajes) {
			System.out.println(equipaje);
		}
		System.out.println("========================================");
	}

	public void buscarEquipajes(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio de busqueda: 1.ID_Equipaje 2.Reserva 3.Estado 4.Tipo 5.Prioridad 6. Vuelo 7. Pasajero");
		opcion = Utilidades.leerOpcion(sc, 1, 7);
		System.out.println("--------------------------------");


		switch (opcion) {
		case 1:
			System.out.println("Inserte el código del equipaje:");
			Equipaje equipaje = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (equipaje != null) {
				Reserva reserva = gestorReservas.buscarPorCodigo(equipaje.getCodReserva());
				System.out.println(equipaje);
				System.out.println("Pasajero Asociado:");
				System.out.println(gestorPasajeros.buscarPorCodPasajero(reserva.getPasajero()));
				System.out.println("Vuelo Asociado: ");
				System.out.println(gestorVuelos.buscarPorCodigo(reserva.getVuelo()));
			}
			else {
				System.out.println("No existe ese equipaje");
			}
			break;
		case 2:
			System.out.println("Inserte el codigo de la reserva:");
			String codReserva = sc.nextLine().trim().toUpperCase();
			ArrayList<Equipaje>equipajesPorReserva = buscarEquipajesPorReserva(codReserva);
			if (equipajesPorReserva.size() != 0) {
				for (Equipaje equipajePorReserva : equipajesPorReserva) {
					System.out.println(equipajePorReserva);
				}
			}else {
				System.out.println("No hay equipajes asociados a esa reserva.");
			}
			break;
		case 3:
			ArrayList<Equipaje> equipajesEstado = buscarPorEstado(sc);
			if (equipajesEstado.size() != 0) {
				for (Equipaje equipajeEstado : equipajesEstado) {
					System.out.println(equipajeEstado);
				}
			}else {
				System.out.println("No hay equipaje en ese estado.");
			}
			break;
			
		case 4:
			String tipo = escogerTipo(sc);
			ArrayList<Equipaje> equipajesTipo = buscarPorTipo(tipo);
			if (equipajesTipo.size() != 0 ) {
				for (Equipaje equipajeTipo : equipajesTipo) {
					System.out.println(equipajeTipo);
				}
			}else {
				System.out.println("No hay equipajes de ese tipo");
			}
			break;
			
		case 5:
			String prioridad = escogerPrioridad(sc);
			ArrayList<Equipaje> equipajesPrioridad = buscarPorPrioridad(prioridad);
			if (equipajesPrioridad.size() != 0) {
				for (Equipaje equipajePrioridad : equipajesPrioridad) {
					System.out.println(equipajePrioridad);
				}
			}else {
				System.out.println("No hay equipajes en ese nivel de prioridad.");
			}
			break;
			
		case 6:
			System.out.println("Inserte el codigo del vuelo:");
			String codVuelo = sc.nextLine().trim().toUpperCase();
			if (gestorVuelos.buscarPorCodigo(codVuelo) == null) {
				System.out.println("No existe ese vuelo.");
			}
			else {
				ArrayList<Equipaje> equipajesVuelo = buscarEquipajesAsociadosAVuelo(codVuelo);
				if (equipajesVuelo.size() != 0) {
					for (Equipaje equipajeVuelo : equipajesVuelo) {
						System.out.println(equipajeVuelo);
					}
				}
				else {
					System.out.println("No hay equipajes asociados a ese vuelo.");
				}
			}
			break;
		case 7:
			System.out.println("Inserte el codigo del pasajero:");
			String codPasajero = sc.nextLine().trim().toUpperCase();
			if (!gestorPasajeros.existeCodPasajero(codPasajero)) {
				System.out.println("No existe ese pasajero.");
			}
			else {
				ArrayList<Equipaje> equipajesPasajero = new ArrayList<Equipaje>();
				for (Reserva reservaPasajero : gestorReservas.buscarResevasPorPasajero(codPasajero)) {
					equipajesPasajero.addAll(buscarEquipajesPorReserva(reservaPasajero.getIdReserva()));
				}
				if (equipajesPasajero.size() != 0) {
					for (Equipaje equipajePasajero : equipajesPasajero) {
						System.out.println(equipajePasajero);
					}
				}
				else {
					System.out.println("Ese pasajero no tiene equipajes asociados.");
				}
			}
			break;
		}

	}


	private ArrayList<Equipaje> buscarPorTipo(String tipo) {
		ArrayList<Equipaje> equipajesTipo = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajes) {
			if (equipaje.getTipo().equals(tipo)) {
				equipajesTipo.add(equipaje);
			}
		}
		return equipajesTipo;
	}

	public ArrayList<Equipaje> buscarEquipajesPorReserva( String codReserva) {
		ArrayList<Equipaje> equipajesPorReserva = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajes) {
			if (equipaje.getCodReserva().equals(codReserva)) {
				equipajesPorReserva.add(equipaje);
			}
		}
		return equipajesPorReserva;
	}

	public ArrayList<Equipaje> buscarPorPrioridad(String prioridad) {
		
		ArrayList<Equipaje> equipajesPrioridad = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajes) {
			if (equipaje.getPrioridad().equals(prioridad)) {
				equipajesPrioridad.add(equipaje);
			}
		}
		return equipajesPrioridad;
	}

	private String escogerPrioridad(Scanner sc) {
		int opcion;
		System.out.println("Escoja un nivel de prioridad: 1.NORMAL - 2.FRAGIL - 3.PRIORITARIO");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		String prioridad ="";
		switch (opcion) {
		case 1:
			prioridad = "NORMAL";
			break;
		case 2:
			prioridad = "FRAGIL";
			break;
		case 3:
			prioridad = "PRIORITARIO";
			break;
		}
		return prioridad;
	}

	public ArrayList<Equipaje> buscarPorEstado(Scanner sc) {
		String estado = escogerEstado(sc);
		ArrayList<Equipaje> equipajesEstado = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajes) {
			if (equipaje.getEstado().equals(estado)) {
				equipajesEstado.add(equipaje);
			}
		}
		return equipajesEstado;
	}

	private String escogerEstado(Scanner sc) {
		int opcion;
		System.out.println("Escoja un estado: 1.REGISTRADO - 2.CARGADO - 3.EN_TRANSITO - 4.ENTREGADO - 5.PERDIDO");
		opcion = Utilidades.leerOpcion(sc, 1, 5);
		String estado ="";
		switch (opcion) {
		case 1:
			estado = "REGISTRADO";
			break;
		case 2:
			estado = "CARGADO";
			break;
		case 3:
			estado = "EN_TRANSITO";
			break;
		case 4:
			estado = "ENTREGADO";
			break;
		case 5:
			estado = "PERDIDO";
			break;
		}
		return estado;
	}

	public Equipaje buscarPorCodigo(String cod) {
		Iterator<Equipaje> iterator = equipajes.iterator();
		Equipaje equipaje = null;
		boolean encontrado = false;
		while (iterator.hasNext() && !encontrado) {
			equipaje = iterator.next();
			if (equipaje.getCodEquipaje().equals(cod)) {
				encontrado = true;
			}
		}
		if (encontrado) {
			return equipaje;
		}
		else {
			return null;
		}

	}

	public Equipaje pedirEquipaje(Scanner sc, String accion) {
		Equipaje equipaje;
		System.out.println("Introduce el código del equipaje " + accion + ":");
		do {
			equipaje = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (equipaje == null) {
				System.err.println("No se ha encontrado el equipaje. Inténtelo de nuevo:");
			}
		} while (equipaje == null);
		return equipaje;
	}

	public void insertarEquipaje(String codigo, String codReserva, double peso, String tipo, String estado, String prioridad) {
		aniadirEquipajeAlArray(codigo, codReserva, peso, tipo, estado, prioridad);
		volcarEnCSV();
	}

	private void aniadirEquipajeAlArray(String codigo, String codReserva, double peso, String tipo, String estado, String prioridad) {
		equipajes.add(new Equipaje(codigo, codReserva, peso, tipo, estado, prioridad));
	}

	private String escogerTipo(Scanner sc) {
		int opcion;
		System.out.println("Escoja un tipo: 1.FACTURADO - 2.MANO - 3.ESPECIAL");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		String tipo = "";
		switch (opcion) {
		case 1:
			tipo = "FACTURADO";
			break;
		case 2:
			tipo = "MANO";
			break;
		case 3:
			tipo = "ESPECIAL";
			break;
		}
		return tipo;
	}

	public void modificarEquipaje(Equipaje equipaje, double peso, String tipo, String estado, String prioridad) {
		equipaje.setPeso(peso);
		equipaje.setTipo(tipo);
		equipaje.setEstado(estado);
		equipaje.setPrioridad(prioridad);
		volcarEnCSV();
	}

	public String crearIdentificador() {
		int dato = 0;
		for (Equipaje equipaje : equipajes) {
			int aux = Integer.parseInt(equipaje.getCodEquipaje().substring(2));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("EQ%04d", dato + 1);
	}

	public boolean eliminarEquipaje(String codEquipaje) {
		Equipaje equipaje = buscarPorCodigo(codEquipaje);
		if (equipaje == null) {
			return false;
		}
		equipajes.remove(equipaje);
		volcarEnCSV();
		return true;
	}

	public double obtenerPesoMaximo(String tipo) {
		JsonNode regla = gestorConfiguracion.buscarTipoEquipaje(tipo);
		return regla.get("pesoMaximo").asDouble();
	}

	public ArrayList<Equipaje> filtrarEquipajes(String criterio, String texto) {
		ArrayList<Equipaje> arrayEquipajes = new ArrayList<Equipaje>();
		texto = texto.toUpperCase();

		for (Equipaje equipaje : equipajes) {
			Reserva reserva = gestorReservas.buscarPorCodigo(equipaje.getCodReserva());
			switch (criterio) {
			case "Código":
				if (equipaje.getCodEquipaje().toUpperCase().contains(texto)) {
					arrayEquipajes.add(equipaje);
				}
				break;
			case "Reserva":
				if (equipaje.getCodReserva().toUpperCase().contains(texto)) {
					arrayEquipajes.add(equipaje);
				}
				break;
			case "Pasajero":
				if (reserva != null && reserva.getPasajero().toUpperCase().contains(texto)) {
					arrayEquipajes.add(equipaje);
				}
				break;
			case "Vuelo":
				if (reserva != null && reserva.getVuelo().toUpperCase().contains(texto)) {
					arrayEquipajes.add(equipaje);
				}
				break;
			}
		}

		return arrayEquipajes;
	}

	private boolean pesoEsValido(String tipo, double peso) {
		boolean valido = true;
		JsonNode  regla= gestorConfiguracion.buscarTipoEquipaje(tipo);
		double pesoMaximo = regla.get("pesoMaximo").asDouble();
		if (peso > pesoMaximo) {
			valido = false;
		}
		return valido;
	}


	public ArrayList<Equipaje> buscarEquipajesAsociadosAVuelo(String vuelo) {
		ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevasPorVuelo(vuelo);
		ArrayList<Equipaje> equipajesDelVuelo = new ArrayList<Equipaje>();
		for (Reserva reserva : reservasDelVuelo) {
			for (Equipaje equipaje: equipajes) {
				if (equipaje.getCodReserva().equals(reserva.getIdReserva())) {
					equipajesDelVuelo.add(equipaje);
				}
			}
		}

		return equipajesDelVuelo;
	}

	public ArrayList<Equipaje> getEquipajes() {
		return equipajes;
	}

	public void setEquipajes(ArrayList<Equipaje> equipajes) {
		this.equipajes = equipajes;
	}

	public HashMap<String, Integer> filtrarArrayEquipajesPorTipo(ArrayList<Equipaje> equipajesDelVuelo) {
		HashMap<String, Integer> equipajesPorTipo = new HashMap<String, Integer>();
		for (Equipaje equipaje : equipajesDelVuelo) {
			String tipo = equipaje.getTipo();
			if (equipajesPorTipo.containsKey(tipo)) {
				equipajesPorTipo.put(tipo, (equipajesPorTipo.get(tipo)+1));
			}
			else {
				equipajesPorTipo.put(tipo, 1);
			}
		}
		return equipajesPorTipo;
	}

	public HashMap<String, Integer> filtrarArrayEquipajesPorEstado(ArrayList<Equipaje> equipajesDelVuelo) {
		HashMap<String, Integer> equipajesPorEstado = new HashMap<String, Integer>();
		for (Equipaje equipaje : equipajesDelVuelo) {
			String estado = equipaje.getEstado();
			if (equipajesPorEstado.containsKey(estado)) {
				equipajesPorEstado.put(estado, (equipajesPorEstado.get(estado)+1));
			}
			else {
				equipajesPorEstado.put(estado, 1);
			}
		}
		return equipajesPorEstado;
	}

	public ArrayList<Equipaje> filtrarArrayEquipajesPorPerdidos(ArrayList<Equipaje> equipajesDelVuelo) {
		ArrayList<Equipaje> equipajesPerdidos = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajesDelVuelo) {
			if (equipaje.getEstado().equals("PERDIDO") ) {
				equipajesPerdidos.add(equipaje);
			}
		}
		return equipajesPerdidos;
	}

	public ArrayList<Equipaje> filtrarArrayEquipajesPorExcesoDePeso(ArrayList<Equipaje> equipajesDelVuelo) {
		ArrayList<Equipaje> equipajesConExceso = new ArrayList<Equipaje>();
		for (Equipaje equipaje : equipajesDelVuelo) {
			if (!pesoEsValido(equipaje.getTipo(), equipaje.getPeso())) {
				equipajesConExceso.add(equipaje);
			}
		}
		return equipajesConExceso;
	}

	public double calcularPesoTotal(ArrayList<Equipaje> equipajesDelVuelo) {
		double pesoTotal = 0;
		for (Equipaje equipaje : equipajesDelVuelo) {
			pesoTotal += equipaje.getPeso();
		}
		return pesoTotal;
	}
}
