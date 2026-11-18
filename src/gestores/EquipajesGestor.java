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

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Equipaje;
import modelo.Reserva;
import utilidades.Utilidades;
import utilidades.Constantes;

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

	public boolean volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_EQUIPAJES)))) {
			StatefulBeanToCsv<Equipaje> beantoCsv = new StatefulBeanToCsvBuilder<Equipaje>(writer).withOrderedResults(true)
					.withApplyQuotesToAll(false).build();
			beantoCsv.write(equipajes);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			return false;
		}
		return true;
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

	public String insertarEquipaje(String codigo, String codReserva, double peso, String tipo, String estado, String prioridad) {
		aniadirEquipajeAlArray(codigo, codReserva, peso, tipo, estado, prioridad);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_EQUIPAJES);
		}
		return "";
	}

	private void aniadirEquipajeAlArray(String codigo, String codReserva, double peso, String tipo, String estado, String prioridad) {
		equipajes.add(new Equipaje(codigo, codReserva, peso, tipo, estado, prioridad));
	}

	public String modificarEquipaje(Equipaje equipaje, double peso, String tipo, String estado, String prioridad) {
		equipaje.setPeso(peso);
		equipaje.setTipo(tipo);
		equipaje.setEstado(estado);
		equipaje.setPrioridad(prioridad);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_EQUIPAJES);
		}
		return "";
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

	public String eliminarEquipaje(String codEquipaje) {
		Equipaje equipaje = buscarPorCodigo(codEquipaje);
		if (equipaje == null) {
			return "No existe ningun equipaje con el codigo " + codEquipaje + ".";
		}
		equipajes.remove(equipaje);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_EQUIPAJES);
		}
		return "";
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
			if (equipaje.getEstado().equals(Constantes.EQUIPAJE_PERDIDO) ) {
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
