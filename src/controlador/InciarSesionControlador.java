package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import temas.Tema;
import vista.IniciarSesionPanel;

public class InciarSesionControlador implements ActionListener, MouseListener {
	private PrincipalControlador principalControlador;
	private IniciarSesionPanel iniciarSesionPanel;
	
	
	public InciarSesionControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.iniciarSesionPanel = principalControlador.getFrame().getIniciarSesionPanel();
		
		implementarListeners();
	}
	
	public void implementarListeners () {
		iniciarSesionPanel.getBtnInciarSesion().addActionListener(this);
		iniciarSesionPanel.getBtnTema().addActionListener(this);
		iniciarSesionPanel.getLblRegistrarse().addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == iniciarSesionPanel.getBtnInciarSesion()) {
			
		}else if (e.getSource() == iniciarSesionPanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == iniciarSesionPanel.getLblRegistrarse()) {
			iniciarSesionPanel.setVisible(false);
			principalControlador.getFrame().getRegistrarsePanel().setVisible(true);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
