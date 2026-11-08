package controlador;

import java.awt.EventQueue;
import java.util.ArrayList;


import vista.AppFrame;
import vista.ImagenesConTema;
import vista.IniciarSesionPanel;

public class PrincipalControlador {
	private ArrayList<ImagenesConTema> listPanelesConImagenesConTema;
	private AppFrame frame;
	private InciarSesionControlador iniciarSesionControlador;
	
	
	public PrincipalControlador () {
		listPanelesConImagenesConTema = new ArrayList<ImagenesConTema>();
		IniciarSesionPanel iniciarSesionPanel = new IniciarSesionPanel();
		
		listPanelesConImagenesConTema.add(iniciarSesionPanel);

				try {
					frame = new AppFrame(iniciarSesionPanel);
				} catch (Exception e) {
					e.printStackTrace();
				}
		iniciarSesionControlador = new InciarSesionControlador(this);
		frame.setVisible(true);
	}

	public AppFrame getFrame() {
		return frame;
	}


	public InciarSesionControlador getLogInControlador() {
		return iniciarSesionControlador;
	}

	public void setLogInControlador(InciarSesionControlador logInControlador) {
		this.iniciarSesionControlador = logInControlador;
	}

	public ArrayList<ImagenesConTema> getListPanelesConImagenesConTema() {
		return listPanelesConImagenesConTema;
	}

	
	
	
}
