package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import temas.Tema;
import vista.SuperiorInterfazPanel;

public class SuperiorInterfazControlador implements ActionListener {
	private PrincipalControlador principalControlador;
	private SuperiorInterfazPanel superiorInterfazPanel;
	
	public SuperiorInterfazControlador (PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.superiorInterfazPanel = principalControlador.getFrame().getSuperiorInterfazPanel();
		superiorInterfazPanel.getLblNombreUsuario().setText(principalControlador.getUsuario().getUsuario());
		superiorInterfazPanel.getLblRolUsuario().setText(principalControlador.getUsuario().getRol());
		implementarListeners();
		
		
	}

	private void implementarListeners() {
		superiorInterfazPanel.getBtnTema().addActionListener(this);
		superiorInterfazPanel.getBtnSalir().addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == superiorInterfazPanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		} 
		else if (e.getSource() == superiorInterfazPanel.getBtnSalir()) {
			principalControlador.cerrarSesion();
			
		}
		
	}
}
