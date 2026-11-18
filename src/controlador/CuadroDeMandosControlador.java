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
import vista.CuadroDeMandosPanel;

public class CuadroDeMandosControlador implements ActionListener{
	private PrincipalControlador principalControlador;
	private CuadroDeMandosPanel cuadroDeMandosPanel;
	private String rolUsuario;

	public CuadroDeMandosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.cuadroDeMandosPanel = principalControlador.getFrame().getCuadroDeMandosPanel();
		rolUsuario = principalControlador.getUsuario().getRol();
		
		implementarListeners();
		recargarPanel();
		
		modoAdmin();
		
	}

	private void modoAdmin() {
		if (rolUsuario.equals("USER")) {
			cuadroDeMandosPanel.getBtnExportarCsv().setVisible(false);
			cuadroDeMandosPanel.getComboBoxInforme().setVisible(false);
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
		cuadroDeMandosPanel.mostrarVuelosPorEstado(new ArrayList<String>(numVuelosPorEstado.keySet()),
				new ArrayList<Integer> (numVuelosPorEstado.values()));
		
		LinkedHashMap<String, Integer> incidenciasAbiertasPorPrioridad = InformesGestor.getInstance().obtenerLinkedHashMapIncidenciasAbieretasPorPrioridad();
		cuadroDeMandosPanel.mostrarIncidenciasPorPrioridad(new ArrayList<String>(incidenciasAbiertasPorPrioridad.keySet()),
				new ArrayList<Integer>(incidenciasAbiertasPorPrioridad.values()));
		
		HashMap<String, ArrayList<Integer>> vuelosOcupacion = InformesGestor.getInstance().obtenerOcupacionDeLosVuelos();  
		ArrayList<String> vuelos = new ArrayList<String>(vuelosOcupacion.keySet());
		ArrayList<Integer> libresVuelos = new ArrayList<Integer>();
		ArrayList<Integer> reservasVuelos = new ArrayList<Integer>();
		for (Map.Entry<String, ArrayList<Integer>> k : vuelosOcupacion.entrySet()) {
			libresVuelos.add(k.getValue().getFirst());
			reservasVuelos.add(k.getValue().getLast());
		}
		
		cuadroDeMandosPanel.mostrarOcupacion(vuelos, reservasVuelos, libresVuelos);
		
	}

	private void rellenarVuelos() {
		DefaultTableModel modeloOcupacion = cuadroDeMandosPanel.getModeloOcupacion();
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
		cuadroDeMandosPanel.getBtnExportarCsv().addActionListener(this);
		
	}

	private void rellenarTarjetas() {
		cuadroDeMandosPanel.getLblTotalPasajeros().setText(String.valueOf(PasajerosGestor.getInstance().getPasajeros().size()));
		cuadroDeMandosPanel.getLblTotalVuelos().setText(String.valueOf(VuelosGestor.getInstance().getVuelos().size()));
		cuadroDeMandosPanel.getLblTotalReservas().setText(String.valueOf(ReservasGestor.getInstance().getReservas().size()));
		cuadroDeMandosPanel.getLblTotalEquipajes().setText(String.valueOf(EquipajesGestor.getInstance().getEquipajes().size()));
		cuadroDeMandosPanel.getLblTotalEmbarques().setText(String.valueOf(EmbarquesGestor.getInstance().getEmbarques().size()));
		cuadroDeMandosPanel.getLblTotalIncidencias().setText(String.valueOf(IncidenciasGestor.getInstance().getIncidencias().size()));
		
		
	}
	
	private void rellenarAlertas() {
		InformesGestor gestorInformes = InformesGestor.getInstance();
		DefaultTableModel modeloAlertas = cuadroDeMandosPanel.getModeloAlertas();
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
		for (Incidencia incidencia : gestorInformes.buscarIncidenciasAbiertasPorPrioridad("CRITICA")) {
			modeloAlertas.addRow(new Object[] {"Incidencia CRITICA", incidencia.getCodigo() + " en " + incidencia.getVuelo()});
		}
		for (Incidencia incidencia : gestorInformes.buscarIncidenciasAbiertasPorPrioridad("ALTA")) {
			modeloAlertas.addRow(new Object[] {"Incidencia ALTA", incidencia.getCodigo() + " en " + incidencia.getVuelo()});
		}
	}

	private void exportarCsv() {
		String informe = (String) cuadroDeMandosPanel.getComboBoxInforme().getSelectedItem();
		String error;
		String ruta;
		if (informe.equals("Totales")) {
			error = InformesGestor.getInstance().exportarTotales();
			ruta = "informes/totales.csv";
		}
		else if (informe.equals("Ocupación de vuelos")) {
			error = InformesGestor.getInstance().exportarOcupacion();
			ruta = "informes/ocupacion_vuelos.csv";
		}
		else {
			error = InformesGestor.getInstance().exportarAlertas();
			ruta = "informes/alertas.csv";
		}

		if (error.isEmpty()) {
			cuadroDeMandosPanel.mostrarExito("Informe exportado en " + ruta);
		}
		else {
			cuadroDeMandosPanel.mostrarError(error);
		}
	}

	public PrincipalControlador getPrincipalControlador() {
		return principalControlador;
	}

	public void setPrincipalControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
	}

	public CuadroDeMandosPanel getCuadroDeMandosPanel() {
		return cuadroDeMandosPanel;
	}

	public void setCuadroDeMandosPanel(CuadroDeMandosPanel cuadroDeMandosPanel) {
		this.cuadroDeMandosPanel = cuadroDeMandosPanel;
	}

	public String getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cuadroDeMandosPanel.getBtnExportarCsv()) {
			exportarCsv();
		}
		
	}
	
	
	
	
}
