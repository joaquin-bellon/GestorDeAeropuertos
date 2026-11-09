package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gestores.UsuariosGestor;
import modelo.Usuario;
import temas.Tema;
import utilidades.HiloErrores;
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
			String nombre = registrarsePanel.getTextFieldUsuario().getText();
			String contrasenia = new String(registrarsePanel.getPasswordFieldContrasenia().getPassword());
			String contraseniaRepetida = new String(registrarsePanel.getPasswordFieldRepetir().getPassword());
			
			if (nombre.isBlank() || contrasenia.isBlank() || contrasenia.isBlank()) {
				HiloErrores hiloErrores = new HiloErrores(registrarsePanel.getLblError(), "Rellene todos los campos.");
				hiloErrores.start();
			}else {
				Usuario usuario= UsuariosGestor.getInstance().buscarUsuarioPorNombre(nombre);
				if (usuario != null) {
					HiloErrores hiloErrores = new HiloErrores(registrarsePanel.getLblError(), "Ya existe un usuario con ese nombre.");
					hiloErrores.start();
				}else if (!contrasenia.equals(contraseniaRepetida)) {
					HiloErrores hiloErrores = new HiloErrores(registrarsePanel.getLblError(), "Las contraseñas no coinciden.");
					hiloErrores.start();
				}
				else if (contrasenia.length() < 4) {
					HiloErrores hiloErrores = new HiloErrores(registrarsePanel.getLblError(), "La contraseña debe tener al menos 4 caracteres.");
					hiloErrores.start();
				}
				else {
					Usuario usuarioCreado = new Usuario(nombre,contrasenia,"USER");
					UsuariosGestor.getInstance().añadirUsuario(usuarioCreado);
					principalControlador.crearPanelesYControladores(usuarioCreado);
					registrarsePanel.setVisible(false);
				}
			}
			
		}else if (e.getSource() == registrarsePanel.getBtnTema()) {
			Tema.cambiarTema(principalControlador.getListPanelesConImagenesConTema());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == registrarsePanel.getLblIniciarSesion()) {
			registrarsePanel.setVisible(false);
			vaciarTextos();
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
	
	public void vaciarTextos () {
		registrarsePanel.getTextFieldUsuario().setText("");
		registrarsePanel.getPasswordFieldContrasenia().setText("");
		registrarsePanel.getPasswordFieldRepetir().setText("");
	}
	
}
