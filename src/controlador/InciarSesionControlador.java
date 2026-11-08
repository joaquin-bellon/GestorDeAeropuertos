package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import temas.Tema;
import vista.IniciarSesionPanel;

public class InciarSesionControlador implements ActionListener {
	private PrincipalControlador principalControlador;
	private IniciarSesionPanel iniciarSesionPanel;
	
	
	public InciarSesionControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.iniciarSesionPanel = principalControlador.getFrame().getLogInPanel();
		
		implementarActionListener();
	}
	
	public void implementarActionListener () {
		iniciarSesionPanel.getBtnInciarSesion().addActionListener(this);
		iniciarSesionPanel.getBtnTema().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == iniciarSesionPanel.getBtnInciarSesion()) {
			
		}else if (e.getSource() == iniciarSesionPanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		}
	}
	
	
}
