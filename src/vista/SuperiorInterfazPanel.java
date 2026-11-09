package vista;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.UIManager;

import utilidades.Utilidades;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class SuperiorInterfazPanel extends JPanel implements ImagenesConTema {

	private static final long serialVersionUID = 1L;
	private JLabel lblLogo;
	private JLabel lblFondo;
	private JButton btnTema;
	private JLabel lblGestorAeropuerto;
	private JLabel lblAplicacionParaGestionar;
	private JButton btnSalir;
	private JLabel lblImagenUsuario;
	private JLabel lblNombreUsuario;
	private JLabel lblRolUsuario;

	public SuperiorInterfazPanel() {
		setBounds(0,0, 1200, 110);
		setLayout(null);
		
		lblRolUsuario = new JLabel("");
		lblRolUsuario.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblRolUsuario.setBounds(898, 53, 118, 16);
		add(lblRolUsuario);
		
		lblNombreUsuario = new JLabel("");
		lblNombreUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblNombreUsuario.setBounds(898, 34, 118, 20);
		add(lblNombreUsuario);
		
		lblImagenUsuario = new JLabel("");
		lblImagenUsuario.setBounds(840, 24, 184, 58);
		add(lblImagenUsuario);
		
		lblGestorAeropuerto = new JLabel("Gestor Aeropuerto");
		lblGestorAeropuerto.setFont(new Font("SansSerif", Font.PLAIN, 24));
		lblGestorAeropuerto.setBounds(110, 25, 257, 40);
		add(lblGestorAeropuerto);
		
		lblLogo = new JLabel("");
		lblLogo.setBounds(10, 10, 90, 90);
		Utilidades.insertarImagen("logo/logo90x90.png", lblLogo);
		add(lblLogo);
		
		btnTema = new JButton("");
		btnTema.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnTema.setBounds(1100, 30, 40, 40);
		btnTema.putClientProperty("FlatLaf.style", "arc: 999");
		add(btnTema);
		
		btnSalir = new JButton("");
		btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSalir.setBounds(1050, 30, 40, 40);
		btnSalir.putClientProperty("FlatLaf.style", "arc: 999");
		add(btnSalir);
		
		lblAplicacionParaGestionar = new JLabel("Gestión total del aeropuerto");
		lblAplicacionParaGestionar.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblAplicacionParaGestionar.setBounds(110, 56, 257, 26);
		add(lblAplicacionParaGestionar);
		
		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1200, 110);
		add(lblFondo);
		
		
		actualizarImagenes();
	}

	@Override
	public void actualizarImagenes() {
		Utilidades.insertarImagen(UIManager.getString("Cabecera.Fondo"), lblFondo);
		Utilidades.insertarImagen(UIManager.getString("Cabecera.usuarioEtiqueta"), lblImagenUsuario);
		btnTema.setIcon(new ImageIcon(UIManager.getString("Login.btnTema")));
		btnSalir.setIcon(new ImageIcon(UIManager.getString("Cabecera.cerrarSesion")));
	}

	public JLabel getLblLogo() {
		return lblLogo;
	}

	public void setLblLogo(JLabel lblLogo) {
		this.lblLogo = lblLogo;
	}

	public JLabel getLblFondo() {
		return lblFondo;
	}

	public void setLblFondo(JLabel lblFondo) {
		this.lblFondo = lblFondo;
	}

	public JButton getBtnTema() {
		return btnTema;
	}

	public void setBtnTema(JButton btnTema) {
		this.btnTema = btnTema;
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}

	public void setBtnSalir(JButton btnSalir) {
		this.btnSalir = btnSalir;
	}

	public JLabel getLblNombreUsuario() {
		return lblNombreUsuario;
	}

	public void setLblNombreUsuario(JLabel lblNombreUsuario) {
		this.lblNombreUsuario = lblNombreUsuario;
	}

	public JLabel getLblRolUsuario() {
		return lblRolUsuario;
	}

	public void setLblRolUsuario(JLabel lblRolUsuario) {
		this.lblRolUsuario = lblRolUsuario;
	}
	
	
	
}
