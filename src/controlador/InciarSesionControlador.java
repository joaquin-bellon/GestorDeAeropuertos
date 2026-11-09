package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gestores.UsuariosGestor;
import modelo.Usuario;
import temas.Tema;
import utilidades.HiloErrores;
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
			String nombre = iniciarSesionPanel.getTextFieldUsuario().getText();
			String contrasenia = new String (iniciarSesionPanel.getPasswordFieldContrasenia().getPassword());
			
			if (nombre.isBlank() || contrasenia.isBlank()) {
				HiloErrores hiloErrores = new HiloErrores(iniciarSesionPanel.getLblError(), "Rellene todos los campos.");
				hiloErrores.start();
			} else {
				Usuario usuario = UsuariosGestor.getInstance().buscarUsuarioPorNombre(nombre);
				if (usuario == null) {
					HiloErrores hiloErrores = new HiloErrores(iniciarSesionPanel.getLblError(), "Ese Usuario no existe.");
					hiloErrores.start();
				}else if (!usuario.getContrasenia().equals(contrasenia)) {
					HiloErrores hiloErrores = new HiloErrores(iniciarSesionPanel.getLblError(), "La contraseña no coincide.");
					hiloErrores.start();
				}
				else {
					principalControlador.crearPanelesYControladores(usuario);
					iniciarSesionPanel.setVisible(false);
				}
			}
			
			
		}else if (e.getSource() == iniciarSesionPanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == iniciarSesionPanel.getLblRegistrarse()) {
			iniciarSesionPanel.setVisible(false);
			vaciarTextos();
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
	
	public void vaciarTextos () {
		iniciarSesionPanel.getTextFieldUsuario().setText("");
		iniciarSesionPanel.getPasswordFieldContrasenia().setText("");
	}
}
