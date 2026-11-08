package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import temas.Tema;
import vista.RegistrarsePanel;

public class RegistrarseControlador implements ActionListener, MouseListener {
	private PrincipalControlador principalControlador;
	private RegistrarsePanel registrarsePanel;
	
	
	public RegistrarseControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.registrarsePanel = principalControlador.getFrame().getRegistrarsePanel();
		
		implementarListeners();
	}
	
	public void implementarListeners () {
		registrarsePanel.getBtnRegistrarse().addActionListener(this);
		registrarsePanel.getBtnTema().addActionListener(this);
		registrarsePanel.getLblIniciarSesion().addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == registrarsePanel.getBtnRegistrarse()) {
			
		}else if (e.getSource() == registrarsePanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == registrarsePanel.getLblIniciarSesion()) {
			registrarsePanel.setVisible(false);
			principalControlador.getFrame().getIniciarSesionPanel().setVisible(true);
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
