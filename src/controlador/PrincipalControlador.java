package controlador;

import java.util.ArrayList;

import modelo.Usuario;
import vista.AppFrame;
import vista.ImagenesConTema;
import vista.IniciarSesionPanel;
import vista.RegistrarsePanel;

public class PrincipalControlador {
	private ArrayList<ImagenesConTema> listPanelesConImagenesConTema;
	private AppFrame frame;
	private InciarSesionControlador iniciarSesionControlador;
	private RegistrarseControlador registrarseControlador;
	private Usuario usuario;
	
	
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




	public InciarSesionControlador getIniciarSesionControlador() {
		return iniciarSesionControlador;
	}




	public void setIniciarSesionControlador(InciarSesionControlador iniciarSesionControlador) {
		this.iniciarSesionControlador = iniciarSesionControlador;
	}




	public Usuario getUsuario() {
		return usuario;
	}




	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}




	public void setRegistrarseControlador(RegistrarseControlador registrarseControlador) {
		this.registrarseControlador = registrarseControlador;
	}

	
	
	
}
