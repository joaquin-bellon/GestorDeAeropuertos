package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.CerrarSesionPanel;

public class CerrarSesionControlador implements ActionListener {
	private PrincipalControlador principalControlador;
	private CerrarSesionPanel cerrarSesionPanel;

	public CerrarSesionControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.cerrarSesionPanel = principalControlador.getFrame().getCerrarSesionPanel();
		implementarListeners();
	}

	private void implementarListeners() {
		cerrarSesionPanel.getBtnCancelar().addActionListener(this);
		cerrarSesionPanel.getBtnConfirmar().addActionListener(this);
	}

	public void mostrarConfirmacion() {
		cerrarSesionPanel.mostrar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cerrarSesionPanel.getBtnCancelar()) {
			cerrarSesionPanel.setVisible(false);
		}
		else if (e.getSource() == cerrarSesionPanel.getBtnConfirmar()) {
			cerrarSesionPanel.setVisible(false);
			principalControlador.cerrarSesion();
		}
	}

	public PrincipalControlador getPrincipalControlador() {
		return principalControlador;
	}

	public void setPrincipalControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
	}

	public CerrarSesionPanel getCerrarSesionPanel() {
		return cerrarSesionPanel;
	}

	public void setCerrarSesionPanel(CerrarSesionPanel cerrarSesionPanel) {
		this.cerrarSesionPanel = cerrarSesionPanel;
	}
}
