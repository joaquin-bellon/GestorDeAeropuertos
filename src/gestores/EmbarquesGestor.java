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
import java.util.ArrayList;
import java.util.Iterator;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Embarque;
import modelo.Reserva;
import modelo.Vuelo;
import utilidades.Utilidades;

public class EmbarquesGestor {
	private ReservasGestor gestorReservas;
	private VuelosGestor gestorVuelos;
	private ArrayList<Embarque> embarques;

	private static EmbarquesGestor instancia;

	public static EmbarquesGestor getInstance() {
		if (instancia == null) {
			instancia = new EmbarquesGestor();
		}
		return instancia;
	}

	private EmbarquesGestor() {
		this.gestorReservas = ReservasGestor.getInstance();
		this.gestorVuelos = VuelosGestor.getInstance();
		embarques = abrirCSV();
	}

	private ArrayList<Embarque> abrirCSV() {
		embarques = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(Utilidades.RUTA_EMBARQUES)))) {
			CsvToBeanBuilder<Embarque> csvBulider = new CsvToBeanBuilder<Embarque>(reader);
			embarques = (ArrayList<Embarque>) csvBulider.withType(Embarque.class)
					.withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).withSeparator(',').build().parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return embarques;
	}

	public void volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_EMBARQUES)))) {
			StatefulBeanToCsv<Embarque> beantoCsv = new StatefulBeanToCsvBuilder<Embarque>(writer).withOrderedResults(true)
					.withApplyQuotesToAll(false).build();
			beantoCsv.write(embarques);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	}

	public void imprimirEmbarque(Embarque embarque) {
		System.out.println(embarque);
		Reserva reserva = gestorReservas.buscarPorCodigo(embarque.getCodReserva());
		if (reserva != null) {
			gestorReservas.imprimirReserva(reserva);
		}
		else {
			System.out.println("Reserva " + embarque.getCodReserva() + " no encontrada");
		}
	}

	public Embarque buscarPorCodigo(String cod) {
		Iterator<Embarque> iterator = embarques.iterator();
		Embarque embarque = null;
		boolean encontrado = false;
		while (iterator.hasNext() && !encontrado) {
			embarque = iterator.next();
			if (embarque.getCodEmbarque().equals(cod)) {
				encontrado = true;
			}
		}
		if (encontrado) {
			return embarque;
		}
		else {
			return null;
		}
	}

	public Embarque buscarPorReserva(String codReserva) {
		Iterator<Embarque> iterator = embarques.iterator();
		Embarque embarque = null;
		boolean encontrado = false;
		while (iterator.hasNext() && !encontrado) {
			embarque = iterator.next();
			if (embarque.getCodReserva().equals(codReserva)) {
				encontrado = true;
			}
		}
		if (encontrado) {
			return embarque;
		}
		else {
			return null;
		}
	}

	public ArrayList<Embarque> buscarPorEstado(String estado) {
		ArrayList<Embarque> embarquesEstado = new ArrayList<Embarque>();
		for (Embarque embarque : embarques) {
			if (embarque.getEstado().equals(estado)) {
				embarquesEstado.add(embarque);
			}
		}
		return embarquesEstado;
	}

	public void insertarEmbarque(String codigo, String codReserva, String fechaHora, String puerta, String estado, String observaciones) {
		aniadirEmbarqueAlArray(codigo, codReserva, fechaHora, puerta, estado, observaciones);
		volcarEnCSV();
	}

	private void aniadirEmbarqueAlArray(String codigo, String codReserva, String fechaHora, String puerta, String estado, String observaciones) {
		embarques.add(new Embarque(codigo, codReserva, fechaHora, puerta, estado, observaciones));
	}

	public String comprobarHorarioEmbarque(Vuelo vuelo, LocalDateTime fechaHora) {
		LocalDateTime inicio = vuelo.getSalida().minusMinutes(Integer.parseInt(vuelo.getMinutosInicioEmbarque()));
		LocalDateTime cierre = vuelo.getSalida().minusMinutes(Integer.parseInt(vuelo.getMinutosCierreEmbarque()));
		if (fechaHora.isBefore(inicio) || fechaHora.isAfter(cierre)) {
			return "Fuera del horario de embarque (" + inicio.toLocalTime() + "-" + cierre.toLocalTime() + ").";
		}
		return "";
	}

	public String crearIdentificador() {
		int dato = 0;
		for (Embarque embarque : embarques) {
			int aux = Integer.parseInt(embarque.getCodEmbarque().substring(1));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("E%04d", dato + 1);
	}

	public void modificarEmbarque(Embarque embarque, String fechaHora, String puerta, String estado, String observaciones) {
		embarque.setFechaHora(fechaHora);
		embarque.setPuerta(puerta);
		embarque.setEstado(estado);
		embarque.setObservaciones(observaciones);
		volcarEnCSV();
	}

	public boolean eliminarEmbarque(String codEmbarque) {
		Embarque embarque = buscarPorCodigo(codEmbarque);
		if (embarque == null) {
			return false;
		}
		embarques.remove(embarque);
		volcarEnCSV();
		return true;
	}

	public ArrayList<Embarque> filtrarPorFecha(LocalDate fecha) {
		ArrayList<Embarque> embarquesFiltrados = new ArrayList<Embarque>();
		for (Embarque embarque : embarques) {
			LocalDate fechaEmbarque = LocalDateTime.parse(embarque.getFechaHora()).toLocalDate();
			if (!fechaEmbarque.isBefore(fecha)) {
				embarquesFiltrados.add(embarque);
			}
		}
		return embarquesFiltrados;
	}

	public ArrayList<Embarque> getEmbarques() {
		return embarques;
	}

	
}
