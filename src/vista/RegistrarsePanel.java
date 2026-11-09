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

public class RegistrarsePanel extends JPanel implements ImagenesConTema {

	private static final long serialVersionUID = 1L;
	private JLabel lblFondo;
	private JLabel lblRegistrarse;
	private JLabel lblUsuario;
	private JLabel lblContrasenia;
	private JTextField textFieldUsuario;
	private JPasswordField passwordFieldContrasenia;
	private JButton btnRegistrarse;
	private JButton btnTema;
	private JLabel lblNoTienesCuenta;
	private JLabel lblIniciarSesion;
	private JLabel lblRepetirContrasenia;
	private JPasswordField passwordFieldRepetir;
	private JLabel lblError;

	public RegistrarsePanel() {
		setBounds(0, 0, 1200, 800);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(null);
		
		btnTema = new JButton("");
		btnTema.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnTema.setBounds(1100, 35, 40, 40);
		btnTema.putClientProperty("FlatLaf.style", "arc: 999");
		
		lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setForeground((Color) null);
		lblError.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblError.setBounds(477, 498, 244, 12);
		add(lblError);
		
		lblRepetirContrasenia = new JLabel("Repetir Contraseña");
		lblRepetirContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblRepetirContrasenia.setBounds(477, 402, 221, 62);
		add(lblRepetirContrasenia);
		
		passwordFieldRepetir = new JPasswordField();
		passwordFieldRepetir.setFont(new Font("SansSerif", Font.PLAIN, 14));
		passwordFieldRepetir.setEchoChar('*');
		passwordFieldRepetir.setBounds(477, 463, 244, 25);
		add(passwordFieldRepetir);
		
		lblIniciarSesion = new JLabel("<html><u>Iniciar Sesion</u></html>");
		lblIniciarSesion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblIniciarSesion.setBounds(620, 582, 91, 25);
		add(lblIniciarSesion);
		
		lblNoTienesCuenta = new JLabel("¿Ya tienes cuenta?");
		lblNoTienesCuenta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblNoTienesCuenta.setBounds(490, 582, 130, 25);
		add(lblNoTienesCuenta);
		btnTema.setMargin(new Insets(0, 0, 0, 0));
		add(btnTema);
		
		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnRegistrarse.setBounds(524, 522, 150, 35);
		add(btnRegistrarse);
		
		passwordFieldContrasenia = new JPasswordField();
		passwordFieldContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 14));
		passwordFieldContrasenia.setEchoChar('*');
		passwordFieldContrasenia.setBounds(477, 383, 244, 25);
		add(passwordFieldContrasenia);
		
		textFieldUsuario = new JTextField();
		textFieldUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldUsuario.setBounds(478, 308, 244, 25);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		
		lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblUsuario.setBounds(478, 258, 221, 62);
		add(lblUsuario);
		
		lblContrasenia = new JLabel("Contraseña");
		lblContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblContrasenia.setBounds(477, 330, 221, 62);
		add(lblContrasenia);
		
		lblRegistrarse = new JLabel("Registrarse");
		lblRegistrarse.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblRegistrarse.setBounds(478, 196, 221, 62);
		add(lblRegistrarse);
		
		lblFondo = new JLabel();
		lblFondo.setBackground(new Color(240, 240, 240));
		lblFondo.setBounds(0, 0, 1200, 800);
		add(lblFondo);
		
		actualizarImagenes();
	}
	public void actualizarImagenes () {
		btnTema.setIcon(new ImageIcon(UIManager.getString("Login.btnTema")));
		Utilidades.insertarImagen(UIManager.getString("Login.fondo"), lblFondo);
		lblIniciarSesion.setForeground(UIManager.getColor("Login.enlace"));
		lblError.setForeground(UIManager.getColor("Login.errorColor"));
	}
	public JLabel getLblFondo() {
		return lblFondo;
	}
	public void setLblFondo(JLabel lblFondo) {
		this.lblFondo = lblFondo;
	}
	public JLabel getLblRegistrarse() {
		return lblRegistrarse;
	}
	public void setLblRegistrarse(JLabel lblRegistrarse) {
		this.lblRegistrarse = lblRegistrarse;
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
	public void setPasswordFieldContrasenia(JPasswordField passwordField) {
		this.passwordFieldContrasenia = passwordField;
	}
	public JButton getBtnRegistrarse() {
		return btnRegistrarse;
	}
	public void setBtnRegistrarse(JButton btnRegistrarse) {
		this.btnRegistrarse = btnRegistrarse;
	}
	public JButton getBtnTema() {
		return btnTema;
	}
	public void setBtnTema(JButton btnTema) {
		this.btnTema = btnTema;
	}
	public JLabel getLblNoTienesCuenta() {
		return lblNoTienesCuenta;
	}
	public void setLblNoTienesCuenta(JLabel lblNoTienesCuenta) {
		this.lblNoTienesCuenta = lblNoTienesCuenta;
	}
	public JLabel getLblIniciarSesion() {
		return lblIniciarSesion;
	}
	public void setLblIniciarSesion(JLabel lblIniciarSesion) {
		this.lblIniciarSesion = lblIniciarSesion;
	}
	public JPasswordField getPasswordFieldRepetir() {
		return passwordFieldRepetir;
	}
	public void setPasswordFieldRepetir(JPasswordField passwordFieldRepetir) {
		this.passwordFieldRepetir = passwordFieldRepetir;
	}
	public JLabel getLblError() {
		return lblError;
	}
	public void setLblError(JLabel lblError) {
		this.lblError = lblError;
	}
	
	
	
	
	
	
	
}

