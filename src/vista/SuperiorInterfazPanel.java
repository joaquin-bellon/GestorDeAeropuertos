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

	public SuperiorInterfazPanel() {
		setBounds(0,0, 1200, 110);
		setLayout(null);
		
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
	
	
}
