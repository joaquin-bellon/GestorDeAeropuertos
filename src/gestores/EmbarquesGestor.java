package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Embarque;
import modelo.Reserva;
import modelo.Vuelo;

public class EmbarquesGestor {
	private ReservasGestor gestorReservas;
	private VuelosGestor gestorVuelos;
	private ArrayList<Embarque> embarques;

	public EmbarquesGestor(ReservasGestor gestorReservas, VuelosGestor gestorVuelos) {
		this.gestorReservas = gestorReservas;
		this.gestorVuelos = gestorVuelos;
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

	public void mostrarEmbarques() {
		for (Embarque embarque : embarques) {
			System.out.println(embarque);
		}
		System.out.println("========================================");
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

	public void buscarEmbarques(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio de busqueda: 1.Código embarque 2.Reserva 3.Vuelo 4.Estado");
		opcion = Utilidades.leerOpcion(sc, 1, 4);
		System.out.println("--------------------------------");

		Embarque embarque;
		switch (opcion) {
		case 1:
			System.out.println("Inserte el código del embarque:");
			embarque = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (embarque != null) {
				imprimirEmbarque(embarque);
			}
			else {
				System.out.println("No existe ese embarque.");
			}
			break;
		case 2:
			System.out.println("Inserte el código de la reserva:");
			embarque = buscarPorReserva(sc.nextLine().trim().toUpperCase());
			if (embarque != null) {
				imprimirEmbarque(embarque);
			}
			else {
				System.out.println("Esa reserva no tiene registro de embarque.");
			}
			break;
		case 3:
			System.out.println("Inserte el código del vuelo:");
			mostrarEmbarquesVuelo(sc.nextLine().trim().toUpperCase());
			break;
		case 4:
			ArrayList<Embarque> embarquesEstado = buscarPorEstado(escogerEstado(sc));
			if (embarquesEstado.size() != 0) {
				for (Embarque embarqueEstado : embarquesEstado) {
					System.out.println(embarqueEstado);
				}
			}
			else {
				System.out.println("No hay embarques en ese estado.");
			}
			break;
		}
	}

	public void mostrarEmbarquesVuelo(String codVuelo) {
		Vuelo vuelo = gestorVuelos.buscarPorCodigo(codVuelo);
		if (vuelo == null) {
			System.out.println("No se ha encontrado el vuelo.");
		}
		else {
			boolean hayEmbarque = false;
			System.out.println("Embarques asociados al vuelo " + codVuelo + ":");
			ArrayList<Reserva> reservasVuelo = gestorReservas.buscarResevasPorVuelo(codVuelo);
			if (reservasVuelo.size() != 0) {
				for (Reserva reserva :reservasVuelo ) {
						Embarque embarque = buscarPorReserva(reserva.getIdReserva());
						if (embarque != null) {
							System.out.println(embarque);
							hayEmbarque = true;
					}
				}
			}
			
			if (!hayEmbarque) {
				System.out.println("No hay Embarques asociados al vuelo");
			}
			
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

	public Embarque pedirEmbarque(Scanner sc, String accion) {
		Embarque embarque;
		System.out.println("Introduce el código del embarque " + accion + ":");
		do {
			embarque = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (embarque == null) {
				System.err.println("No se ha encontrado el embarque. Inténtelo de nuevo:");
			}
		} while (embarque == null);
		return embarque;
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

	public void insertarEmbarque(Scanner sc) {
		aniadirEmbarqueAlArray(sc);
		volcarEnCSV();
	}

	private void aniadirEmbarqueAlArray(Scanner sc) {
		String [] datos = new String [6];
		System.out.println("Creación de un Embarque");
		System.out.println("------------------------------");
		//Reserva
		Reserva reserva;
		Vuelo vuelo = null;

			System.out.println("Código de la reserva:");
			datos[1] = sc.nextLine().trim().toUpperCase();
			reserva = gestorReservas.buscarPorCodigo(datos[1]);
			if (reserva == null) {
				System.err.println("Esa reserva no existe.");
				return;
			}
			else if (reserva.getEstado().equals("CANCELADA")) {
				System.err.println("Esa reserva está cancelada.");

				return;
			}
			else {
				vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
				if (vuelo.getEstado().equals("CANCELADO")) {
					System.err.println("El vuelo de esa reserva está cancelado.");
					return;
				}
			}

		Embarque existente = buscarPorReserva(datos[1]);
		if (existente != null) {
			System.out.println("Esa reserva ya tiene un registro de embarque:");
			System.out.println(existente);
			System.out.println("¿Desea actualizarlo? 1.Sí 2.No (Si se escoge no, se mostrará el embarque)");
			int opcion;
			opcion = Utilidades.leerOpcion(sc, 1, 2);
			if (opcion == 1) {
				modificarEmbarque(sc, existente);
			}
			else {
				System.out.println(existente);
			}
		}
		else {
			//Fecha y hora
			datos[2] = pedirFechaHora(sc, vuelo);
			//Puerta
			datos[3] = pedirPuerta(sc, vuelo);
			//Estado
			datos[4] = escogerEstado(sc);
			//Observaciones
			datos[5] = pedirObservaciones(sc);

			datos[0] = crearIdentificador();
			embarques.add(new Embarque(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]));
			System.out.println("Embarque " + datos[0] + " creado correctamente.");
		}
	}

	private String pedirFechaHora(Scanner sc, Vuelo vuelo) {
		LocalDateTime fechaHora = LocalDateTime.of(Utilidades.pedirFecha(sc, "Fecha del embarque"),
				Utilidades.pedirHora(sc, "Hora del embarque"));
		LocalDateTime inicio = vuelo.getSalida().minusMinutes(Integer.parseInt(vuelo.getMinutosInicioEmbarque()));
		LocalDateTime cierre = vuelo.getSalida().minusMinutes(Integer.parseInt(vuelo.getMinutosCierreEmbarque()));
		if (fechaHora.isBefore(inicio) || fechaHora.isAfter(cierre)) {
			System.out.println("Aviso: la hora queda fuera del intervalo de embarque del vuelo ("
					+ inicio.toLocalTime() + " - " + cierre.toLocalTime() + " del " + inicio.toLocalDate() + ").");
		}
		return fechaHora.toString();
	}

	private String pedirPuerta(Scanner sc, Vuelo vuelo) {
		String puerta;
		do {
			System.out.println("Puerta de embarque (asignada al vuelo: " + vuelo.getPuerta() + "):");
			puerta = sc.nextLine().trim().toUpperCase();
			if (!puerta.equals(vuelo.getPuerta())) {
				System.err.println("Esa no es la puerta asignada al vuelo. Inténtelo de nuevo.");
				puerta = null;
			}
		}while (puerta == null);
		return puerta;
	}

	private String escogerEstado(Scanner sc) {
		int opcion;
		System.out.println("Escoja un estado: 1.EMBARCADO - 2.EMBARCANDO - 3.NO_EMBARCADO");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		String estado = "";
		switch (opcion) {
		case 1:
			estado = "EMBARCADO";
			break;
		case 2:
			estado = "EMBARCANDO";
			break;
		case 3:
			estado = "NO_EMBARCADO";
			break;
		}
		return estado;
	}

	private String pedirObservaciones(Scanner sc) {
		System.out.println("Observaciones (deje en blanco si no hay):");
		String observaciones = sc.nextLine().trim();
		if (observaciones.isEmpty()) {
			observaciones = "Sin incidencias";
		}
		return observaciones;
	}

	private String crearIdentificador() {
		int dato = 0;
		for (Embarque embarque : embarques) {
			int aux = Integer.parseInt(embarque.getCodEmbarque().substring(1));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("E%04d", dato + 1);
	}

	public void actualizarEmbarque(Scanner sc) {
		Embarque embarque = pedirEmbarque(sc, "a actualizar");
		modificarEmbarque(sc, embarque);
		volcarEnCSV();
	}


	private void modificarEmbarque(Scanner sc, Embarque embarque) {
		Reserva reserva = gestorReservas.buscarPorCodigo(embarque.getCodReserva());
		Vuelo vuelo = null;
		if (reserva != null) {
			vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
		}

		System.out.println("--------------------------------");
		System.out.println(embarque);
		System.out.println("--------------------------------");
		System.out.println("Escoge el dato a cambiar: 1.Fecha y hora 2.Puerta 3.Estado 4.Observaciones");
		int opcion;
		opcion = Utilidades.leerOpcion(sc, 1, 4);
		System.out.println("--------------------------------");

		if ((opcion == 1 || opcion == 2) && vuelo == null) {
			System.err.println("No se ha encontrado el vuelo de este embarque, no se puede cambiar ese dato.");
		}
		else {
			switch (opcion) {
			case 1:
				embarque.setFechaHora(pedirFechaHora(sc, vuelo));
				System.out.println("Fecha y hora actualizadas correctamente.");
				break;
			case 2:
				embarque.setPuerta(pedirPuerta(sc,vuelo ));
				System.out.println("Puerta actualizada correctamente.");
				break;
			case 3:
				embarque.setEstado(escogerEstado(sc));
				System.out.println("Estado actualizado correctamente.");
				break;
			case 4:
				embarque.setObservaciones(pedirObservaciones(sc));
				System.out.println("Observaciones actualizadas correctamente.");
				break;
			}
		}
	}

	public void eliminarEmbarque(Scanner sc) {
		Embarque embarque = pedirEmbarque(sc, "a eliminar");
		embarques.remove(embarque);
		volcarEnCSV();
		System.out.println("Embarque eliminado correctamente.");
	}

	public ArrayList<Embarque> getEmbarques() {
		return embarques;
	}

	
}
