package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Pasajero;
import modelo.Reserva;
import modelo.Vuelo;
import utilidades.Utilidades;
import utilidades.Constantes;

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

	public boolean volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_RESERVAS)))) {
			StatefulBeanToCsv<Reserva> beantoCsv = new StatefulBeanToCsvBuilder<Reserva>(writer).withOrderedResults(true)
					.withApplyQuotesToAll(false).build();
			beantoCsv.write(reservas);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			return false;
		}
		return true;
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
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals(Constantes.RESERVA_CONFIRMADA)) {
				reservasVuelo.add(reserva);
			}
		}
		return reservasVuelo;
	}
	
	
	
	public ArrayList<Reserva> buscarResevaCanceladasPorVuelo(String vuelo) {
		ArrayList<Reserva> reservasVuelo = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals(Constantes.RESERVA_CANCELADA)) {
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



	public String insertarReserva(String codigo, String pasajero, String vuelo, String asiento, String clase, double precio, String fecha, String canal) {
		aniadirReservaAlArray(codigo, pasajero, vuelo, asiento, clase, precio, fecha, canal);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_RESERVAS);
		}
		return "";
	}



	private void aniadirReservaAlArray(String codigo, String pasajero, String vuelo, String asiento, String clase, double precio, String fecha, String canal) {
		reservas.add(new Reserva(codigo, pasajero, vuelo, asiento, clase, precio, Constantes.MONEDA, Constantes.RESERVA_CONFIRMADA, fecha, canal));
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
		if (reserva.getEstado().equals(Constantes.RESERVA_CANCELADA)) {
			return "La reserva " + codReserva + " ya esta cancelada.";
		}
		reserva.setEstado(Constantes.RESERVA_CANCELADA);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_RESERVAS);
		}
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



	public String modificarReserva(Reserva reserva, String asiento, double precio) {
		reserva.setAsiento(asiento);
		reserva.setPrecio(precio);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_RESERVAS);
		}
		return "";
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
			if (reserva.getVuelo().equals(vuelo) && reserva.getEstado().equals(Constantes.RESERVA_CONFIRMADA)) {
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

	public ArrayList<Reserva> obtenerReservasConfirmadas () {
		ArrayList<Reserva> reservasConfirmadas = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals(Constantes.RESERVA_CONFIRMADA)) {
				reservasConfirmadas.add(reserva);
			}
		}
		return reservasConfirmadas;
	}
	
	public ArrayList<Reserva> obtenerReservasCanceladas () {
		ArrayList<Reserva> reservasCanceladas = new ArrayList<Reserva>();
		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals(Constantes.RESERVA_CANCELADA)) {
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
				estadoDelEmbarque = Constantes.EMBARQUE_PENDIENTE;
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
			if (gestorEmbarques.buscarPorReserva(reserva.getIdReserva()) == null && !gestorVuelos.buscarPorCodigo(reserva.getVuelo()).getEstado().equals(Constantes.VUELO_CANCELADO)) {
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
