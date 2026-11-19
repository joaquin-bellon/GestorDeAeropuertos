package vista;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import utilidades.HiloAvisos;

public class ConfiguracionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitulo;
	private JLabel lblSubtitulo;
	private JLabel lblAdvertencia;
	private DefaultTableModel modeloUsuarios;
	private JTable tablaUsuarios;
	private JScrollPane scrollPaneUsuarios;
	private JPanel panelUsuario;
	private JLabel lblTituloUsuario;
	private JLabel lblUsuario;
	private JLabel lblValorUsuario;
	private JLabel lblRol;
	private JLabel lblValorRol;
	private JLabel lblExplicacion;
	private JButton btnCambiarRol;
	private JLabel lblConfirmar;
	private JButton btnSiConfirmar;
	private JButton btnNoConfirmar;

	public ConfiguracionPanel() {
		setBounds(220, 110, 980, 653);
		setLayout(null);

		lblTitulo = new JLabel("Configuración");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setBounds(20, 12, 400, 32);
		add(lblTitulo);

		lblSubtitulo = new JLabel("Gestión de los roles de los usuarios");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSubtitulo.setBounds(20, 44, 600, 20);
		add(lblSubtitulo);

		lblAdvertencia = new JLabel("");
		lblAdvertencia.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblAdvertencia.putClientProperty("FlatLaf.style", "foreground: $Login.errorColor");
		lblAdvertencia.setBounds(640, 85, 320, 30);
		add(lblAdvertencia);

		modeloUsuarios = new DefaultTableModel(new Object[][] {}, new String[] {"Usuario", "Rol"});
		tablaUsuarios = new JTable(modeloUsuarios);
		tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tablaUsuarios.setRowHeight(26);
		tablaUsuarios.setDefaultEditor(Object.class, null);
		tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaUsuarios.getTableHeader().setReorderingAllowed(false);
		tablaUsuarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

		scrollPaneUsuarios = new JScrollPane(tablaUsuarios);
		scrollPaneUsuarios.setBounds(20, 85, 600, 495);
		add(scrollPaneUsuarios);

		panelUsuario = new JPanel();
		panelUsuario.setLayout(null);
		panelUsuario.setBorder(BorderFactory.createTitledBorder(""));
		panelUsuario.setBounds(640, 130, 320, 200);
		add(panelUsuario);

		lblTituloUsuario = new JLabel("Usuario seleccionado");
		lblTituloUsuario.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTituloUsuario.setBounds(20, 15, 280, 24);
		panelUsuario.add(lblTituloUsuario);

		lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblUsuario.setBounds(20, 55, 80, 26);
		panelUsuario.add(lblUsuario);

		lblValorUsuario = new JLabel("-");
		lblValorUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblValorUsuario.setBounds(100, 55, 200, 26);
		panelUsuario.add(lblValorUsuario);

		lblRol = new JLabel("Rol");
		lblRol.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblRol.setBounds(20, 90, 80, 26);
		panelUsuario.add(lblRol);

		lblValorRol = new JLabel("-");
		lblValorRol.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblValorRol.setBounds(100, 90, 200, 26);
		panelUsuario.add(lblValorRol);

		lblExplicacion = new JLabel("<html>Selecciona un usuario para cambiar su rol entre USER y ADMIN. No puedes cambiar tu propio rol.</html>");
		lblExplicacion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblExplicacion.setBounds(20, 130, 280, 50);
		panelUsuario.add(lblExplicacion);

		btnCambiarRol = crearBoton("Cambiar rol", 20);
		btnCambiarRol.setEnabled(false);

		lblConfirmar = new JLabel("");
		lblConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblConfirmar.setBounds(20, 594, 296, 36);
		lblConfirmar.setVisible(false);
		add(lblConfirmar);

		btnSiConfirmar = crearBoton("Sí, cambiar", 324);
		btnSiConfirmar.putClientProperty("FlatLaf.style", "arc: 12; background: $Boton.guardarFondo; foreground: #FFFFFF");
		btnSiConfirmar.setVisible(false);

		btnNoConfirmar = crearBoton("No", 476);
		btnNoConfirmar.setVisible(false);
	}

	private JButton crearBoton(String texto, int x) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		boton.setBounds(x, 594, 144, 36);
		boton.putClientProperty("FlatLaf.style", "arc: 12");
		add(boton);
		return boton;
	}

	public void modoConfirmar(boolean confirmando, String pregunta) {
		btnCambiarRol.setVisible(!confirmando);
		lblConfirmar.setText(pregunta);
		lblConfirmar.setVisible(confirmando);
		btnSiConfirmar.setVisible(confirmando);
		btnNoConfirmar.setVisible(confirmando);
		tablaUsuarios.setEnabled(!confirmando);
	}

	public void mostrarError(String texto) {
		lblAdvertencia.putClientProperty("FlatLaf.style", "foreground: $Login.errorColor");
		new HiloAvisos(lblAdvertencia, texto).start();
	}

	public void mostrarExito(String texto) {
		lblAdvertencia.putClientProperty("FlatLaf.style", "foreground: $Mensaje.exito");
		new HiloAvisos(lblAdvertencia, texto).start();
	}

	public DefaultTableModel getModeloUsuarios() {
		return modeloUsuarios;
	}

	public JTable getTablaUsuarios() {
		return tablaUsuarios;
	}

	public JLabel getLblValorUsuario() {
		return lblValorUsuario;
	}

	public JLabel getLblValorRol() {
		return lblValorRol;
	}

	public JButton getBtnCambiarRol() {
		return btnCambiarRol;
	}

	public JButton getBtnSiConfirmar() {
		return btnSiConfirmar;
	}

	public JButton getBtnNoConfirmar() {
		return btnNoConfirmar;
	}

}
