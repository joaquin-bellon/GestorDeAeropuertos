package controlador;

import java.util.ArrayList;

import vista.AppFrame;
import vista.ImagenesConTema;
import vista.IniciarSesionPanel;
import vista.RegistrarsePanel;

public class PrincipalControlador {
	private ArrayList<ImagenesConTema> listPanelesConImagenesConTema;
	private AppFrame frame;
	private InciarSesionControlador iniciarSesionControlador;
	private RegistrarseControlador registrarseControlador;
	
	
	public PrincipalControlador () {
		listPanelesConImagenesConTema = new ArrayList<ImagenesConTema>();
		IniciarSesionPanel iniciarSesionPanel = new IniciarSesionPanel();

		listPanelesConImagenesConTema.add(iniciarSesionPanel);

		frame = new AppFrame(iniciarSesionPanel);
		iniciarSesionControlador = new InciarSesionControlador(this);
		
		RegistrarsePanel registrarsePanel = new RegistrarsePanel();
		frame.setRegistrarsePanel(registrarsePanel);
		frame.getContentPane().add(frame.getRegistrarsePanel());
		frame.getRegistrarsePanel().setVisible(false);
		listPanelesConImagenesConTema.add(registrarsePanel);
		
		registrarseControlador = new RegistrarseControlador(this);
		
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


	public RegistrarseControlador getRegistrarseControlador() {
		return registrarseControlador;
	}

	
	
	
}
