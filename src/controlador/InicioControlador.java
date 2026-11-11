package controlador;

import java.util.ArrayList;

import gestores.EmbarquesGestor;
import gestores.EquipajesGestor;
import gestores.IncidenciasGestor;
import gestores.PasajerosGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Incidencia;
import modelo.Vuelo;
import vista.InicioPanel;

public class InicioControlador {
	private PrincipalControlador principalControlador;
	private InicioPanel inicioPanel;
	public InicioControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.inicioPanel = principalControlador.getFrame().getInicioPanel();
		
		cargarDatos();
	}
	
	public void cargarDatos() {
		inicioPanel.getLblTotalPasajeros().setText(String.valueOf(PasajerosGestor.getInstance().getPasajeros().size()));
		inicioPanel.getLblTotalVuelos().setText(String.valueOf(VuelosGestor.getInstance().getVuelos().size()));
		inicioPanel.getLblTotalReservas().setText(String.valueOf(ReservasGestor.getInstance().obtenerReservasConfirmadas().size()));
		inicioPanel.getLblTotalEquipajes().setText(String.valueOf(EquipajesGestor.getInstance().getEquipajes().size()));
		inicioPanel.getLblTotalEmbarques().setText(String.valueOf(EmbarquesGestor.getInstance().getEmbarques().size()));

		ArrayList<Incidencia> incidenciasAbiertas = IncidenciasGestor.getInstance().buscarPorEstado("ABIERTA");

		inicioPanel.getLblTotalIncidencias().setText(String.valueOf(incidenciasAbiertas.size()));

		inicioPanel.getModeloVuelos().setRowCount(0);
		for (Vuelo vuelo : VuelosGestor.getInstance().obtenerVuelosActivos()) {
			inicioPanel.getModeloVuelos().addRow(new Object[] {vuelo.getCodigo(), vuelo.getDestino(), vuelo.getSalida().toString().replace("T", " "), vuelo.getEstado()});
		}

		inicioPanel.getModeloIncidencias().setRowCount(0);
		for (Incidencia incidencia : incidenciasAbiertas) {
			inicioPanel.getModeloIncidencias().addRow(new Object[] {incidencia.getCodigo(), incidencia.getVuelo(), incidencia.getTipo(), incidencia.getPrioridad()});
		}
	}
	
	
}
