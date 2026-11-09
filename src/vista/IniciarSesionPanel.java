package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import utilidades.Utilidades;
import javax.swing.SwingConstants;

public class IniciarSesionPanel extends JPanel implements ImagenesConTema {

	private static final long serialVersionUID = 1L;
	private JLabel lblFondo;
	private JLabel lblIniciarSesion;
	private JLabel lblUsuario;
	private JLabel lblContrasenia;
	private JTextField textFieldUsuario;
	private JPasswordField passwordFieldContrasenia;
	private JButton btnInciarSesion;
	private JButton btnTema;
	private JLabel lblNoTienesCuenta;
	private JLabel lblRegistrarse;
	private JLabel lblError;

	public IniciarSesionPanel() {
		setBounds(0, 0, 1200, 800);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(null);
		
		btnTema = new JButton("");
		btnTema.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnTema.setBounds(1100, 30, 40, 40);
		btnTema.putClientProperty("FlatLaf.style", "arc: 999");
		
		lblError = new JLabel("");
		lblError.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(478, 485, 244, 12);
		add(lblError);
		
		lblRegistrarse = new JLabel("<html><u>Registrarse</u></html>");
		lblRegistrarse.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblRegistrarse.setBounds(620, 583, 79, 25);
		add(lblRegistrarse);
		
		lblNoTienesCuenta = new JLabel("¿No tienes cuenta?");
		lblNoTienesCuenta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblNoTienesCuenta.setBounds(490, 583, 140, 25);
		add(lblNoTienesCuenta);
		btnTema.setMargin(new Insets(0, 0, 0, 0));
		add(btnTema);
		
		btnInciarSesion = new JButton("Iniciar Sesión");
		btnInciarSesion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnInciarSesion.setBounds(524, 522, 150, 35);
		add(btnInciarSesion);
		
		passwordFieldContrasenia = new JPasswordField();
		passwordFieldContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 14));
		passwordFieldContrasenia.setEchoChar('*');
		passwordFieldContrasenia.setBounds(478, 439, 244, 25);
		add(passwordFieldContrasenia);
		
		textFieldUsuario = new JTextField();
		textFieldUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldUsuario.setBounds(478, 315, 244, 25);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		
		lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblUsuario.setBounds(478, 258, 221, 62);
		add(lblUsuario);
		
		lblContrasenia = new JLabel("Contraseña");
		lblContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblContrasenia.setBounds(478, 381, 221, 62);
		add(lblContrasenia);
		
		lblIniciarSesion = new JLabel("Iniciar Sesión");
		lblIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblIniciarSesion.setBounds(478, 196, 221, 62);
		add(lblIniciarSesion);
		
		lblFondo = new JLabel();
		lblFondo.setBackground(new Color(240, 240, 240));
		lblFondo.setBounds(0, 0, 1200, 800);
		add(lblFondo);
		
		actualizarImagenes();
	}
	public void actualizarImagenes () {
		btnTema.setIcon(new ImageIcon(UIManager.getString("Login.btnTema")));
		Utilidades.insertarImagen(UIManager.getString("Login.fondo"), lblFondo);
		lblRegistrarse.setForeground(UIManager.getColor("Login.enlace"));
		lblError.setForeground(UIManager.getColor("Login.errorColor"));
	}
	public JLabel getLblFondo() {
		return lblFondo;
	}
	public void setLblFondo(JLabel lblFondo) {
		this.lblFondo = lblFondo;
	}
	public JLabel getLblIniciarSesion() {
		return lblIniciarSesion;
	}
	public void setLblIniciarSesion(JLabel lblIniciarSesion) {
		this.lblIniciarSesion = lblIniciarSesion;
	}
	public JLabel getLblUsuario() {
		return lblUsuario;
	}
	public void setLblUsuario(JLabel lblUsuario) {
		this.lblUsuario = lblUsuario;
	}
	public JLabel getLblContrasenia() {
		return lblContrasenia;
	}
	public void setLblContrasenia(JLabel lblContrasenia) {
		this.lblContrasenia = lblContrasenia;
	}
	public JTextField getTextFieldUsuario() {
		return textFieldUsuario;
	}
	public void setTextFieldUsuario(JTextField textFieldUsuario) {
		this.textFieldUsuario = textFieldUsuario;
	}
	public JPasswordField getPasswordFieldContrasenia() {
		return passwordFieldContrasenia;
	}
	public void setPasswordField(JPasswordField passwordField) {
		this.passwordFieldContrasenia = passwordField;
	}
	public JButton getBtnInciarSesion() {
		return btnInciarSesion;
	}
	public void setBtnInciarSesion(JButton btnInciarSecion) {
		this.btnInciarSesion = btnInciarSecion;
	}
	public JButton getBtnTema() {
		return btnTema;
	}
	public void setBtnTema(JButton btnTema) {
		this.btnTema = btnTema;
	}
	public JLabel getLblRegistrarse() {
		return lblRegistrarse;
	}
	public JLabel getLblError() {
		return lblError;
	}
	public void setLblError(JLabel lblError) {
		this.lblError = lblError;
	}
	public void setPasswordFieldContrasenia(JPasswordField passwordFieldContrasenia) {
		this.passwordFieldContrasenia = passwordFieldContrasenia;
	}
	
	
	
	
}
