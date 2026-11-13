package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.LateralInterfazPanel;

public class LateralInterfazControlador implements ActionListener {
	private PrincipalControlador principalControlador;
	private LateralInterfazPanel lateralInterfazPanel;
	
	public LateralInterfazControlador (PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.lateralInterfazPanel = principalControlador.getFrame().getLateralInterfazPanel();
		
		implementarListeners();
	}

	private void implementarListeners() {
		lateralInterfazPanel.getBtnConfiguracion().addActionListener(this);
		
		lateralInterfazPanel.getTglbtnInicio().addActionListener(this);
		lateralInterfazPanel.getTglbtnPasajeros().addActionListener(this);
		lateralInterfazPanel.getTglbtnVuelos().addActionListener(this);
		lateralInterfazPanel.getTglbtnReservas().addActionListener(this);
		lateralInterfazPanel.getTglbtnEquipajes().addActionListener(this);
		lateralInterfazPanel.getTglbtnEmbarques().addActionListener(this);
		lateralInterfazPanel.getTglbtnIncidencias().addActionListener(this);
		lateralInterfazPanel.getTglbtnInformes().addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == lateralInterfazPanel.getBtnConfiguracion()) {
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getConfiguracionPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnInicio()) {
			principalControlador.getInicioControlador().cargarDatos();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getInicioPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnPasajeros()) {
			principalControlador.getPasajerosControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getPasajerosPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnVuelos()) {
			principalControlador.getVuelosControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getVuelosPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnReservas()) {
			principalControlador.getReservasControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getReservasPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnEquipajes()) {
			principalControlador.getEquipajesControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getEquipajesPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnEmbarques()) {
			principalControlador.getEmbarquesControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getEmbarquesPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnIncidencias()) {
			principalControlador.getIncidenciasControlador().recargarPanel();
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getIncidenciasPanel());
		}
		else if (e.getSource() == lateralInterfazPanel.getTglbtnInformes()) {
			principalControlador.getFrame().mostrarElPanelPorBoton(principalControlador.getFrame().getCuadroDeMandosPanel());
		}
	}
}
