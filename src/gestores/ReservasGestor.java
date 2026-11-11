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



	public void insertarReserva(String codigo, String pasajero, String vuelo, String asiento, String clase, double precio, String fecha, String canal) {
		aniadirReservaAlArray(codigo, pasajero, vuelo, asiento, clase, precio, fecha, canal);
		volcarEnCSV();
	}



	private void aniadirReservaAlArray(String codigo, String pasajero, String vuelo, String asiento, String clase, double precio, String fecha, String canal) {
		reservas.add(new Reserva(codigo, pasajero, vuelo, asiento, clase, precio, "EUR", "CONFIRMADA", fecha, canal));
	}



	public ArrayList<String> obtenerClasesDelVuelo(Vuelo vuelo) {
		Element avion = gestorVuelos.sacarAvionVuelo(vuelo);
		NodeList clases = avion.getElementsByTagName("Clase");
		ArrayList<String> clasesStrings = new ArrayList<String>();
		for (int i = 0 ; i < clases.getLength() ; i++) {
			String clase = ((Element) clases.item(i)).getAttribute("tipo");
			if (!clasesStrings.contains(clase)) {
				clasesStrings.add(clase);
			}
		}
		return clasesStrings;
	}



	public String crearNuevoIdentificador() {
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



	public String cancelarReserva(String codReserva) {
		Reserva reserva = buscarPorCodigo(codReserva);
		if (reserva == null) {
			return "No existe ninguna reserva con el codigo " + codReserva + ".";
		}
		if (reserva.getEstado().equals("CANCELADA")) {
			return "La reserva " + codReserva + " ya esta cancelada.";
		}
		reserva.setEstado("CANCELADA");
		volcarEnCSV();
		return "";
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



	public void modificarReserva(Reserva reserva, String asiento, double precio) {
		reserva.setAsiento(asiento);
		reserva.setPrecio(precio);
		volcarEnCSV();
	}



	public ArrayList<Reserva> filtrarReservas(String criterio, String texto) {
		ArrayList<Reserva> arrayReservas = new ArrayList<Reserva>();
		texto = texto.toUpperCase();

		for (Reserva reserva : reservas) {
			switch (criterio) {
			case "Código":
				if (reserva.getIdReserva().toUpperCase().contains(texto)) {
					arrayReservas.add(reserva);
				}
				break;
			case "Pasajero":
				if (reserva.getPasajero().toUpperCase().contains(texto)) {
					arrayReservas.add(reserva);
				}
				break;
			case "Vuelo":
				if (reserva.getVuelo().toUpperCase().contains(texto)) {
					arrayReservas.add(reserva);
				}
				break;
			}
		}

		return arrayReservas;
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
