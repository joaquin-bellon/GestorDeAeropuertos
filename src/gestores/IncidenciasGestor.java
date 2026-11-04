package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import modelo.Incidencia;
import modelo.Vuelo;

public class IncidenciasGestor {
	private ArrayList<Incidencia> incidencias;
	private VuelosGestor gestorVuelos;
	private ReservasGestor gestorReservas;
	private EquipajesGestor gestorEquipajes;
	private static IncidenciasGestor instancia;

	public static IncidenciasGestor getInstance() {
		if (instancia == null) {
			instancia = new IncidenciasGestor();
		}
		return instancia;
	}

	private IncidenciasGestor() {
		this.gestorVuelos = VuelosGestor.getInstance();
		this.gestorReservas = ReservasGestor.getInstance();
		this.gestorEquipajes = EquipajesGestor.getInstance();
		abrirTxt();
	}
	private void abrirTxt() {
		incidencias = new ArrayList<Incidencia>();
		try(BufferedReader reader = new BufferedReader(new FileReader(new File (Utilidades.RUTA_INCIDENCIAS)))) {
			String linea = reader.readLine();
			while (linea != null) {
				String [] datos = linea.split("\\|");
				incidencias.add(new Incidencia (datos[0], datos[1],datos[2],datos[3],Integer.parseInt(datos[4]), datos[5], datos[6], datos[7]));
				linea = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void mostrarIncidencias() {
		for (Incidencia incidencia : incidencias) {
			imprimirIncidencia(incidencia);
		}
	}

	private void imprimirIncidencia(Incidencia incidencia) {
		System.out.println(incidencia);
		System.out.println("---------------------------------------------------");
		Vuelo vueloAfectado = gestorVuelos.buscarPorCodigo(incidencia.getVuelo());
		System.out.println("Vuelo afectado: " + vueloAfectado.getCodigo());
		System.out.println("  Ruta: " + vueloAfectado.getOrigen() + " - " + vueloAfectado.getDestino());
		System.out.println("  Horario: " + String.valueOf(vueloAfectado.getSalida()));
		System.out.println("  Terminal: " + vueloAfectado.getTerminal() + " | Puerta: " + vueloAfectado.getPuerta());
		System.out.println("  Estado: " + vueloAfectado.getEstado() + " | Reservas Asociadas: " + gestorReservas.buscarResevaConfirmadasPorVuelo(vueloAfectado.getCodigo()).size());
		System.out.println("====================================================");
	}
	
	public void imprimirImpactoIncidencia(Incidencia incidencia) {
		System.out.println("====================================================");
		System.out.println("ANALISIS DE IMPACTO - INCIDENCIA " + incidencia.getCodigo());
		System.out.println("====================================================");
		imprimirIncidencia(incidencia);
	}

	public void buscarIncidencia(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio de busqueda: 1.Fecha 2.Codigo 3.Vuelo 4.Prioridad 5.Estado");
		opcion = Utilidades.leerOpcion(sc, 1, 5);
		System.out.println("--------------------------------");
		switch (opcion) {
		case 1:
			String fecha = String.valueOf(Utilidades.pedirFecha(sc, "Introduce la fecha por la que buscar"));
			ArrayList<Incidencia> arrayPorFechas = buscarPorFecha(fecha);
			if (arrayPorFechas.size() != 0) {
				for (Incidencia incidencia : arrayPorFechas) {
					System.out.println(incidencia);
				}
			}
			else {
				System.out.println("No se produjo ninguna incidencia en esa fecha");
			}

			break;
		case 2:
			System.out.println("Introduce el codigo de la incidencia");
			String codIncidencia = sc.nextLine().trim().toUpperCase();
			Incidencia incidencia = buscarIncidenciaPorCodigo(codIncidencia);
			if (incidencia != null) {
				imprimirIncidencia(incidencia);
			}
			else {
				System.out.println("No se ha encontrado ninguna incidencia con ese codigo.");
			}
			break;
		case 3:
			System.out.println("Introduce el codigo del vuelo");
			String codVuelo = sc.nextLine().trim().toUpperCase();
			ArrayList<Incidencia> incidenciasVuelo = buscarIncidenciaPorVuelo(codVuelo);
			if (incidenciasVuelo.size() != 0) {
				for (Incidencia incidenciaVuelo : incidenciasVuelo) {
					System.out.println(incidenciaVuelo);
				}

			}
			else {
				System.out.println("No se ha encontrado ninguna incidencia asociada a ese vuelo.");
			}
			break;
		case 4:
			ArrayList<Incidencia> incidenciasPrioridad = buscarPorPrioridad(escogerPrioridad(sc));
			if (incidenciasPrioridad.size() != 0) {
				for (Incidencia incidenciaPrioridad : incidenciasPrioridad) {
					System.out.println(incidenciaPrioridad);
				}
			}
			else {
				System.out.println("No hay incidencias con esa prioridad.");
			}
			break;
		case 5:
			ArrayList<Incidencia> incidenciasEstado = buscarPorEstado(escogerEstado(sc));
			if (incidenciasEstado.size() != 0) {
				for (Incidencia incidenciaEstado : incidenciasEstado) {
					System.out.println(incidenciaEstado);
				}
			}
			else {
				System.out.println("No hay incidencias en ese estado.");
			}
			break;
		}

	}
	public ArrayList<Incidencia> buscarIncidenciaPorVuelo(String codVuelo) {
		ArrayList<Incidencia> incidenciasVuelo = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getVuelo().equals(codVuelo)) {
				incidenciasVuelo.add(incidencia);
			}
		}
		return incidenciasVuelo;
	}
	private Incidencia buscarIncidenciaPorCodigo(String codIncidencia) {
		Incidencia incidencia = null;
		Iterator<Incidencia> iterator = incidencias.iterator();
		while (iterator.hasNext()) {
			Incidencia IncidenciaBusqueda = iterator.next();
			if (IncidenciaBusqueda.getCodigo().equals(codIncidencia)) {
				incidencia = IncidenciaBusqueda;
			}
		}
		return incidencia;
	}
	public Incidencia pedirIncidencia(Scanner sc, String accion) {
		Incidencia incidencia;
		System.out.println("Introduce el código de la incidencia " + accion + ":");
		do {
			incidencia = buscarIncidenciaPorCodigo(sc.nextLine().trim().toUpperCase());
			if (incidencia == null) {
				System.err.println("No se ha encontrado la incidencia. Inténtelo de nuevo:");
			}
		} while (incidencia == null);
		return incidencia;
	}
	private ArrayList<Incidencia> buscarPorFecha(String fecha) {
		ArrayList<Incidencia> arrayPorFechas = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getFecha().equals(fecha)) {
				arrayPorFechas.add(incidencia);
			}
		}
		return arrayPorFechas;
	}

	public ArrayList<Incidencia> buscarPorPrioridad(String prioridad) {
		ArrayList<Incidencia> incidenciasPrioridad = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getPrioridad().equals(prioridad)) {
				incidenciasPrioridad.add(incidencia);
			}
		}
		return incidenciasPrioridad;
	}

	public ArrayList<Incidencia> buscarPorEstado(String estado) {
		ArrayList<Incidencia> incidenciasEstado = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getEstado().equals(estado)) {
				incidenciasEstado.add(incidencia);
			}
		}
		return incidenciasEstado;
	}
	
	public ArrayList<Incidencia> buscarEnArrayAbiertas(ArrayList<Incidencia> arrayIncidencias) {
		ArrayList<Incidencia> incidenciasEstado = new ArrayList<Incidencia>();
		for (Incidencia incidencia : arrayIncidencias) {
			if (incidencia.getEstado().equals("ABIERTA")) {
				incidenciasEstado.add(incidencia);
			}
		}
		return incidenciasEstado;
	}

	private String escogerPrioridad(Scanner sc) {
		System.out.println("Escoja una prioridad: 1.BAJA - 2.MEDIA - 3.ALTA - 4.CRITICA");
		int opcion = Utilidades.leerOpcion(sc, 1, 4);
		String prioridad = "";
		switch (opcion) {
		case 1:
			prioridad = "BAJA";
			break;
		case 2:
			prioridad = "MEDIA";
			break;
		case 3:
			prioridad = "ALTA";
			break;
		case 4:
			prioridad = "CRITICA";
			break;
		}
		return prioridad;
	}

	private String escogerEstado(Scanner sc) {
		System.out.println("Escoja un estado: 1.ABIERTA - 2.CERRADA");
		int opcion = Utilidades.leerOpcion(sc, 1, 2);
		String estado = "";
		switch (opcion) {
		case 1:
			estado = "ABIERTA";
			break;
		case 2:
			estado = "CERRADA";
			break;
		}
		return estado;
	}

	public void volcarEnTxt () {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File (Utilidades.RUTA_INCIDENCIAS)))) {
			for (Incidencia incidencia : incidencias) {
				writer.write(incidencia.formatoTxt());
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void insertarIncidencia(Scanner sc) {
		String [] datos = new String [8];
		datos[0] = String.valueOf(Utilidades.pedirFecha(sc, "Introduce la fecha de la incidencia"));
		datos[1] = crearNuevoIdentificador();
		datos[2] = gestorVuelos.pedirVuelo(sc, "afectado").getCodigo();
		datos[3] = escogerTipo(sc);

		int num;
		System.out.println("Introduce la duracion en minutos:");
		do {
			num = Utilidades.leerEntero(sc);
			if (num < 0) {
				System.err.println("La duracion de la incidencia no puede ser negativa.");
			}
		}while (num < 0);
		datos[4] = String.valueOf(num);

		datos[5] = pedirDescripcion(sc, "Inserte la descripcion:");

		datos[6] = escogerPrioridad(sc);
		datos[7] = escogerEstado(sc);
		incidencias.add(new Incidencia (datos[0],datos[1],datos[2],datos[3],Integer.parseInt(datos[4]), datos[5], datos[6], datos[7]));
		System.out.printf("Incidencia %s creada\n", datos[1]);
		volcarEnTxt();
	}
	private String escogerTipo(Scanner sc) {
		System.out.println("Escoja un tipo: 1.RETRASO - 2.CANCELACION - 3.CAMBIO_PUERTA - 4.EQUIPAJE_PERDIDO - 5.AVERIA");
		int opcion = Utilidades.leerOpcion(sc, 1, 5);
		String tipo = "";
		switch (opcion) {
		case 1:
			tipo = "RETRASO";
			break;
		case 2:
			tipo = "CANCELACION";
			break;
		case 3:
			tipo = "CAMBIO_PUERTA";
			break;
		case 4:
			tipo = "EQUIPAJE_PERDIDO";
			break;
		case 5:
			tipo = "AVERIA";
			break;
		}
		return tipo;
	}
	private String crearNuevoIdentificador() {
		int dato = 0;
		for (Incidencia incidencia : incidencias) {
			int aux = Integer.parseInt(incidencia.getCodigo().substring(3));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("INC%03d", dato + 1);
	}
	public void actualizarIncidencia(Scanner sc) {
		Incidencia incidencia = pedirIncidencia(sc, "a actualizar");

		System.out.println("--------------------------------");
		System.out.println(incidencia);
		System.out.println("--------------------------------");
		System.out.println("Escoge el dato a cambiar: 1.descripcion 2.duracion 3.Prioridad 4.Estado");
		int opcion = Utilidades.leerOpcion(sc, 1, 4);

		switch(opcion) {
		case 1:
			incidencia.setDescripcion(pedirDescripcion(sc, "Introduce la nueva descripcion"));
			System.out.println("Descripcion cambiada correctamente");
			break;
		case 2:
			int duracion;
			System.out.println("Introduce la nueva duracion");
			do {
				duracion = Utilidades.leerEntero(sc);
				if (duracion < 0) {
					System.err.println("La duracion de la incidencia no puede ser negativa.");
				}
			}while (duracion < 0);
			incidencia.setDuracion(duracion);
			System.out.println("Duracion cambiada correctamente");
			break;
		case 3:
			incidencia.setPrioridad(escogerPrioridad(sc));
			System.out.println("Prioridad cambiada correctamente");
			break;
		case 4:
			incidencia.setEstado(escogerEstado(sc));
			System.out.println("Estado cambiado correctamente");
			break;
 		}

		volcarEnTxt();
	}
	private String pedirDescripcion(Scanner sc, String mensaje) {
		String descripcion;
		System.out.println(mensaje);
		do {
			descripcion = sc.nextLine().trim();
			if (descripcion.isEmpty()) {
				System.err.println("La descripcion no puede estar vacia.");
			}
			else if (descripcion.contains("|")) {
				System.err.println("La descripcion no puede contener el caracter |");
				descripcion = "";
			}
		}while (descripcion.isEmpty());
		return descripcion;
	}

	public void eliminarIncidencia (Scanner sc ) {
		mostrarIncidencias();
		Incidencia incidencia = pedirIncidencia(sc, "a eliminar");
		incidencias.remove(incidencia);
		System.out.println("Incidencia eliminada correctamente.");
		volcarEnTxt();

	}
	public void filtrarPorPrioridad(Scanner sc) {
		ArrayList <Incidencia> incidenciasAbiertasYUrgentes =devolverIncidenciasPrioritarias(devolverIncidenciasAbiertas());
		if (incidenciasAbiertasYUrgentes.size() != 0) {
			System.out.println("¿Como quieres filtrar las incidencias?: 1. Numero de reservas 2. Numero Equipajes");
			int opcion = Utilidades.leerOpcion(sc, 1, 2);
			switch(opcion) {
			case 1:
				mostrarIncidenciasPorNumeroDeReservas(incidenciasAbiertasYUrgentes);
				break;
			case 2:
				mostrarIncidenciasPorCantidadDeEquipajes(incidenciasAbiertasYUrgentes);
				break;
			}
		}else {
			System.out.println("Buen trabajo. No hay incidencias de prioridad ALTA o CRITICA");
		}
	}
	private void mostrarIncidenciasPorCantidadDeEquipajes(ArrayList<Incidencia> incidenciasAbiertasYUrgentes) {
		HashMap<Incidencia, Integer> mapNumEquipajes =new HashMap<Incidencia, Integer>();
		for (Incidencia incidencia : incidenciasAbiertasYUrgentes) {
			mapNumEquipajes.put(incidencia, gestorEquipajes.buscarEquipajesAsociadosAVuelo(incidencia.getVuelo()).size());
		}
		ArrayList<Integer> numeros = new ArrayList<Integer>(new HashSet<>(mapNumEquipajes.values()));
		Collections.sort(numeros);
		Collections.reverse(numeros);
		for (Integer numero : numeros) {
			ArrayList<Incidencia> incidenciasAQuitar = new ArrayList<Incidencia>();
			for (Map.Entry<Incidencia, Integer> k : mapNumEquipajes.entrySet()) {
				if (k.getValue().equals(numero)) {
					imprimirIncidencia(k.getKey());
					incidenciasAQuitar.add(k.getKey());
				}
			}
			for(Incidencia incidencia : incidenciasAQuitar) {
				mapNumEquipajes.remove(incidencia);
			}

		}

	}
	private void mostrarIncidenciasPorNumeroDeReservas(ArrayList<Incidencia> incidenciasAbiertasYUrgentes) {
		HashMap<Incidencia, Integer> mapNumReservas =new HashMap<Incidencia, Integer>();
		for (Incidencia incidencia : incidenciasAbiertasYUrgentes) {
			mapNumReservas.put(incidencia, gestorReservas.buscarResevaConfirmadasPorVuelo(incidencia.getVuelo()).size());
		}
		ArrayList<Integer> numeros = new ArrayList<Integer>(new HashSet<>(mapNumReservas.values()));
		Collections.sort(numeros);
		Collections.reverse(numeros);
		for (Integer numero : numeros) {
			ArrayList<Incidencia> incidenciasAQuitar = new ArrayList<Incidencia>();
			for (Map.Entry<Incidencia, Integer> k : mapNumReservas.entrySet()) {
				if (k.getValue().equals(numero)) {
					imprimirIncidencia(k.getKey());
					incidenciasAQuitar.add(k.getKey());
				}
			}
			for(Incidencia incidencia : incidenciasAQuitar) {
				mapNumReservas.remove(incidencia);
			}

		}

	}
	private ArrayList<Incidencia> devolverIncidenciasAbiertas() {
		ArrayList<Incidencia> incidenciasAbiertas = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getEstado().equals("ABIERTA")) {
				incidenciasAbiertas.add(incidencia);
			}
		}
		return incidenciasAbiertas;
	}

	public ArrayList<Incidencia> devolverIncidenciasPrioritarias (ArrayList<Incidencia> arrayIncidencias) {
		ArrayList<Incidencia> incidenciasADevolver = new ArrayList<Incidencia>();
		for (Incidencia incidencia : arrayIncidencias) {
			if (incidencia.getPrioridad().equals("ALTA") || incidencia.getPrioridad().equals("CRITICA")) {
				incidenciasADevolver.add(incidencia);
			}
		}
		return incidenciasADevolver;
	}
	public ArrayList<Incidencia> getIncidencias() {
		return incidencias;
	}
	public void setIncidencias(ArrayList<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}
	public ArrayList<Incidencia> buscarPorTipo(String tipo) {
		ArrayList<Incidencia> incidenciasTipo = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getTipo().equals(tipo)) {
				incidenciasTipo.add(incidencia);
			}
		}
		return incidenciasTipo;
	}
	
	
	
}
