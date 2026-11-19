package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import gestores.EmbarquesGestor;
import gestores.EquipajesGestor;
import gestores.IncidenciasGestor;
import gestores.InformesGestor;
import gestores.PasajerosGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Equipaje;
import modelo.Incidencia;
import modelo.Vuelo;
import vista.InformesPanel;
import utilidades.Constantes;
import utilidades.Utilidades;

public class InformesControlador implements ActionListener{
	private PrincipalControlador principalControlador;
	private InformesPanel informesPanel;
	private String rolUsuario;

	public InformesControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.informesPanel = principalControlador.getFrame().getInformesPanel();
		rolUsuario = principalControlador.getUsuario().getRol();
		
		implementarListeners();
		recargarPanel();
		
		aplicarPermisos();
		
	}

	private void aplicarPermisos() {
		if (!Utilidades.esAdmin(rolUsuario)) {
			informesPanel.getBtnExportarCsv().setVisible(false);
			informesPanel.getBtnExportarPdf().setVisible(false);
			informesPanel.getComboBoxInforme().setVisible(false);
		}
		
	}

	public void recargarPanel() {
		rellenarTarjetas();
		rellenarAlertas();
		rellenarVuelos();
		rellenarGraficos();
	}

	private void rellenarGraficos() {
		LinkedHashMap<String, Integer> numVuelosPorEstado = InformesGestor.getInstance().contarVuelosPorEstado();
		informesPanel.mostrarVuelosPorEstado(new ArrayList<String>(numVuelosPorEstado.keySet()),
				new ArrayList<Integer> (numVuelosPorEstado.values()));
		
		LinkedHashMap<String, Integer> incidenciasAbiertasPorPrioridad = InformesGestor.getInstance().obtenerLinkedHashMapIncidenciasAbieretasPorPrioridad();
		informesPanel.mostrarIncidenciasPorPrioridad(new ArrayList<String>(incidenciasAbiertasPorPrioridad.keySet()),
				new ArrayList<Integer>(incidenciasAbiertasPorPrioridad.values()));
		
		HashMap<String, ArrayList<Integer>> vuelosOcupacion = InformesGestor.getInstance().obtenerOcupacionDeLosVuelos();  
		ArrayList<String> vuelos = new ArrayList<String>(vuelosOcupacion.keySet());
		ArrayList<Integer> libresVuelos = new ArrayList<Integer>();
		ArrayList<Integer> reservasVuelos = new ArrayList<Integer>();
		for (Map.Entry<String, ArrayList<Integer>> k : vuelosOcupacion.entrySet()) {
			libresVuelos.add(k.getValue().getFirst());
			reservasVuelos.add(k.getValue().getLast());
		}
		
		informesPanel.mostrarOcupacion(vuelos, reservasVuelos, libresVuelos);
		
	}

	private void rellenarVuelos() {
		DefaultTableModel modeloOcupacion = informesPanel.getModeloOcupacion();
		VuelosGestor vuelosGestor =VuelosGestor.getInstance(); 
		
		modeloOcupacion.setRowCount(0);
		
		for (Vuelo vuelo : vuelosGestor.getVuelos()) {
			int tamaño = vuelosGestor.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int numReservas = ReservasGestor.getInstance().buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			int plazasLibres = tamaño - numReservas;
			int porcentaje = numReservas*100/tamaño;
			modeloOcupacion.addRow(new Object [] {vuelo.getCodigo(),tamaño,numReservas,plazasLibres, String.format("%d %%", porcentaje)});
		}
		
	}

	private void implementarListeners() {
		informesPanel.getBtnExportarCsv().addActionListener(this);
		informesPanel.getBtnExportarPdf().addActionListener(this);
		informesPanel.getComboBoxInforme().addActionListener(this);
		
	}

	private void rellenarTarjetas() {
		informesPanel.getLblTotalPasajeros().setText(String.valueOf(PasajerosGestor.getInstance().getPasajeros().size()));
		informesPanel.getLblTotalVuelos().setText(String.valueOf(VuelosGestor.getInstance().getVuelos().size()));
		informesPanel.getLblTotalReservas().setText(String.valueOf(ReservasGestor.getInstance().getReservas().size()));
		informesPanel.getLblTotalEquipajes().setText(String.valueOf(EquipajesGestor.getInstance().getEquipajes().size()));
		informesPanel.getLblTotalEmbarques().setText(String.valueOf(EmbarquesGestor.getInstance().getEmbarques().size()));
		informesPanel.getLblTotalIncidencias().setText(String.valueOf(IncidenciasGestor.getInstance().getIncidencias().size()));
		
		
	}
	
	private void rellenarAlertas() {
		InformesGestor gestorInformes = InformesGestor.getInstance();
		DefaultTableModel modeloAlertas = informesPanel.getModeloAlertas();
		modeloAlertas.setRowCount(0);

		for (Vuelo vuelo : gestorInformes.buscarVuelosEnSobreventa()) {
			int capacidad = VuelosGestor.getInstance().recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int reservas = ReservasGestor.getInstance().buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			modeloAlertas.addRow(new Object[] {"Sobreventa", vuelo.getCodigo() + ": " + reservas + " reservas de " + capacidad});
		}
		for (Equipaje equipaje : gestorInformes.buscarEquipajesPerdidos()) {
			modeloAlertas.addRow(new Object[] {"Equipaje perdido", equipaje.getCodEquipaje() + " (" + equipaje.getCodReserva() + ")"});
		}
		for (Equipaje equipaje : gestorInformes.buscarEquipajesConSobrepeso()) {
			modeloAlertas.addRow(new Object[] {"Exceso de peso", equipaje.getCodEquipaje() + ": " + equipaje.getTipo() + ", " + equipaje.getPeso() + " kg"});
		}
		for (Incidencia incidencia : gestorInformes.buscarIncidenciasAbiertasPorPrioridad(Constantes.PRIORIDAD_CRITICA)) {
			modeloAlertas.addRow(new Object[] {"Incidencia CRITICA", incidencia.getCodigo() + " en " + incidencia.getVuelo()});
		}
		for (Incidencia incidencia : gestorInformes.buscarIncidenciasAbiertasPorPrioridad(Constantes.PRIORIDAD_ALTA)) {
			modeloAlertas.addRow(new Object[] {"Incidencia ALTA", incidencia.getCodigo() + " en " + incidencia.getVuelo()});
		}
	}

	private void habilitarBtnExportarCsv() {
		String informe = (String) informesPanel.getComboBoxInforme().getSelectedItem();
		boolean tieneCsv = !informe.equals(Constantes.INFORME_VUELOS_ESTADO) && !informe.equals(Constantes.INFORME_INCIDENCIAS_ABIERTAS);
		informesPanel.getBtnExportarCsv().setEnabled(tieneCsv);
	}

	private void exportarCsv() {
		String informe = (String) informesPanel.getComboBoxInforme().getSelectedItem();
		String error;
		String ruta;
		if (informe.equals(Constantes.INFORME_TOTALES)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_CSV, Constantes.ARCHIVO_TOTALES, "csv");
			error = InformesGestor.getInstance().exportarTotalesCsv(ruta);
		}
		else if (informe.equals(Constantes.INFORME_OCUPACION)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_CSV, Constantes.ARCHIVO_OCUPACION, "csv");
			error = InformesGestor.getInstance().exportarOcupacionCsv(ruta);
		}
		else if (informe.equals(Constantes.INFORME_ALERTAS)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_CSV, Constantes.ARCHIVO_ALERTAS, "csv");
			error = InformesGestor.getInstance().exportarAlertasCsv(ruta);
		}
		else {
			return;
		}

		if (error.isEmpty()) {
			informesPanel.mostrarExito("Informe exportado en " + ruta);
		}
		else {
			informesPanel.mostrarError(error);
		}
	}

	public PrincipalControlador getPrincipalControlador() {
		return principalControlador;
	}

	public void setPrincipalControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
	}

	public InformesPanel getInformesPanel() {
		return informesPanel;
	}

	public void setInformesPanel(InformesPanel informesPanel) {
		this.informesPanel = informesPanel;
	}

	public String getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == informesPanel.getBtnExportarCsv()) {
			exportarCsv();
		}
		else if (e.getSource() == informesPanel.getComboBoxInforme()) {
			habilitarBtnExportarCsv();
		}
		else if (e.getSource() == informesPanel.getBtnExportarPdf()) {
			exportarPdf();
		}
		
	}

	private void exportarPdf() {
		String informe = (String) informesPanel.getComboBoxInforme().getSelectedItem();
		String error;
		String ruta;
		if (informe.equals(Constantes.INFORME_TOTALES)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_PDF, Constantes.ARCHIVO_TOTALES, "pdf");
			error = InformesGestor.getInstance().exportarTotalesPdf(ruta);
		}
		else if (informe.equals(Constantes.INFORME_OCUPACION)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_PDF, Constantes.ARCHIVO_OCUPACION, "pdf");
			error = InformesGestor.getInstance().exportarOcupacionPdf(ruta, informesPanel);
		}
		else if (informe.equals(Constantes.INFORME_ALERTAS)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_PDF, Constantes.ARCHIVO_ALERTAS, "pdf");
			error = InformesGestor.getInstance().exportarAlertasPdf(ruta, informesPanel);
		}
		else if (informe.equals(Constantes.INFORME_VUELOS_ESTADO)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_PDF, Constantes.ARCHIVO_ESTADO_VUELOS, "pdf");
			error = InformesGestor.getInstance().exportarVuelosEstadoPdf(ruta, informesPanel);
		}
		else if (informe.equals(Constantes.INFORME_INCIDENCIAS_ABIERTAS)) {
			ruta = Utilidades.crearRutaInforme(Constantes.CARPETA_PDF, Constantes.ARCHIVO_INCIDENCIAS_ABIERTAS, "pdf");
			error = InformesGestor.getInstance().exportarIncidenciasAbiertasPdf(ruta, informesPanel);
		}
		else {
			return;
		}

		if (error.isEmpty()) {
			informesPanel.mostrarExito("Informe exportado en " + ruta);
		}
		else {
			informesPanel.mostrarError(error);
		}
	}
	
	
	
	
}
