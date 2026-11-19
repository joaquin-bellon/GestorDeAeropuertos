package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import modelo.Incidencia;
import modelo.Vuelo;
import utilidades.Utilidades;
import utilidades.Constantes;

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
	public ArrayList<Incidencia> buscarIncidenciaPorVuelo(String codVuelo) {
		ArrayList<Incidencia> incidenciasVuelo = new ArrayList<Incidencia>();
		for (Incidencia incidencia : incidencias) {
			if (incidencia.getVuelo().equals(codVuelo)) {
				incidenciasVuelo.add(incidencia);
			}
		}
		return incidenciasVuelo;
	}
	public Incidencia buscarIncidenciaPorCodigo(String codIncidencia) {
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
			if (incidencia.getEstado().equals(Constantes.INCIDENCIA_ABIERTA)) {
				incidenciasEstado.add(incidencia);
			}
		}
		return incidenciasEstado;
	}

	public boolean volcarEnTxt () {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File (Utilidades.RUTA_INCIDENCIAS)))) {
			for (Incidencia incidencia : incidencias) {
				writer.write(incidencia.formatoTxt());
				writer.newLine();
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	public String insertarIncidencia(String fecha, String codigo, String vuelo, String tipo, int duracion, String descripcion, String prioridad) {
		incidencias.add(new Incidencia(fecha, codigo, vuelo, tipo, duracion, descripcion, prioridad, Constantes.INCIDENCIA_ABIERTA));
		if (!volcarEnTxt()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_INCIDENCIAS);
		}
		return "";
	}
	public String crearNuevoIdentificador() {
		int dato = 0;
		for (Incidencia incidencia : incidencias) {
			int aux = Integer.parseInt(incidencia.getCodigo().substring(3));
			if (aux > dato) {
				dato = aux;
			}
		}
		return String.format("INC%03d", dato + 1);
	}
	public String modificarIncidencia(Incidencia incidencia, String fecha, String vuelo, String tipo, int duracion, String descripcion, String prioridad, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un administrador puede modificar incidencias.";
		}
		incidencia.setFecha(fecha);
		incidencia.setVuelo(vuelo);
		incidencia.setTipo(tipo);
		incidencia.setDuracion(duracion);
		incidencia.setDescripcion(descripcion);
		incidencia.setPrioridad(prioridad);
		if (!volcarEnTxt()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_INCIDENCIAS);
		}
		return "";
	}

	public String eliminarIncidencia(String codigo, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un administrador puede eliminar incidencias.";
		}
		Incidencia incidencia = buscarIncidenciaPorCodigo(codigo);
		if (incidencia == null) {
			return "No existe ninguna incidencia con el codigo " + codigo + ".";
		}
		incidencias.remove(incidencia);
		if (!volcarEnTxt()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_INCIDENCIAS);
		}
		return "";
	}

	public String cerrarIncidencia(String codigo, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un administrador puede cerrar incidencias.";
		}
		Incidencia incidencia = buscarIncidenciaPorCodigo(codigo);
		if (incidencia == null) {
			return "No existe ninguna incidencia con el codigo " + codigo + ".";
		}
		if (incidencia.getEstado().equals(Constantes.INCIDENCIA_CERRADA)) {
			return "La incidencia " + codigo + " ya esta cerrada.";
		}
		incidencia.setEstado(Constantes.INCIDENCIA_CERRADA);
		if (!volcarEnTxt()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_INCIDENCIAS);
		}
		return "";
	}

	public ArrayList<Incidencia> filtrarIncidencias(String criterio, String texto) {
		ArrayList<Incidencia> arrayIncidencias = new ArrayList<Incidencia>();
		texto = texto.toUpperCase();

		for (Incidencia incidencia : incidencias) {
			switch (criterio) {
			case "Código":
				if (incidencia.getCodigo().toUpperCase().contains(texto)) {
					arrayIncidencias.add(incidencia);
				}
				break;
			case "Vuelo":
				if (incidencia.getVuelo().toUpperCase().contains(texto)) {
					arrayIncidencias.add(incidencia);
				}
				break;
			case "Tipo":
				if (incidencia.getTipo().toUpperCase().contains(texto)) {
					arrayIncidencias.add(incidencia);
				}
				break;
			}
		}

		return arrayIncidencias;
	}

	public ArrayList<Incidencia> devolverIncidenciasPrioritarias (ArrayList<Incidencia> arrayIncidencias) {
		ArrayList<Incidencia> incidenciasADevolver = new ArrayList<Incidencia>();
		for (Incidencia incidencia : arrayIncidencias) {
			if (incidencia.getPrioridad().equals(Constantes.PRIORIDAD_ALTA) || incidencia.getPrioridad().equals(Constantes.PRIORIDAD_CRITICA)) {
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
