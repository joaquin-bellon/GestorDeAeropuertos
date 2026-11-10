package vista;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PanelGenerico extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblTitulo;
	private JLabel lblSubtitulo;
	private JLabel lblBuscarPor;
	private JComboBox<String> comboBoxFiltro;
	private JTextField textFieldBuscar;
	private JScrollPane scrollPaneTabla;
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JPanel panelFormulario;
	private JLabel lblTituloFormulario;
	private JButton btnAniadir;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnLimpiar;
	private JButton btnGuardar;
	private JButton btnCancelar;

	public PanelGenerico(String titulo, String subtitulo, String tituloFormulario, String[] filtros, String[] columnas) {
		setBounds(220, 110, 980, 653);
		setLayout(null);

		lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setBounds(20, 12, 400, 32);
		add(lblTitulo);

		lblSubtitulo = new JLabel(subtitulo);
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSubtitulo.setBounds(20, 44, 600, 20);
		add(lblSubtitulo);

		lblBuscarPor = new JLabel("Buscar por");
		lblBuscarPor.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblBuscarPor.setBounds(20, 85, 80, 30);
		add(lblBuscarPor);

		comboBoxFiltro = new JComboBox<String>();
		comboBoxFiltro.setModel(new DefaultComboBoxModel<String>(filtros));
		comboBoxFiltro.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxFiltro.setBounds(100, 85, 130, 30);
		add(comboBoxFiltro);

		textFieldBuscar = new JTextField();
		textFieldBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldBuscar.setBounds(240, 85, 380, 30);
		textFieldBuscar.putClientProperty("JTextField.placeholderText", "Escribe para filtrar...");
		add(textFieldBuscar);

		modeloTabla = new DefaultTableModel(new Object[][] {}, columnas);
		tabla = new JTable(modeloTabla);
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tabla.setRowHeight(26);
		tabla.setDefaultEditor(Object.class, null);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

		scrollPaneTabla = new JScrollPane(tabla);
		scrollPaneTabla.setBounds(20, 130, 600, 450);
		add(scrollPaneTabla);

		btnAniadir = crearBoton("Añadir", 20);
		btnModificar = crearBoton("Modificar", 172);
		btnEliminar = crearBoton("Eliminar", 324);
		btnLimpiar = crearBoton("Limpiar", 476);
		btnGuardar = crearBoton("Guardar", 20);
		btnGuardar.putClientProperty("FlatLaf.style", "arc: 12; background: $Boton.guardarFondo; foreground: #FFFFFF");
		btnCancelar = crearBoton("Cancelar", 172);
		btnCancelar.putClientProperty("FlatLaf.style", "arc: 12; background: $Boton.cancelarFondo; foreground: #FFFFFF");
		btnGuardar.setVisible(false);
		btnCancelar.setVisible(false);

		panelFormulario = new JPanel();
		panelFormulario.setLayout(null);
		panelFormulario.setBorder(BorderFactory.createTitledBorder(""));
		panelFormulario.setBounds(640, 130, 320, 500);
		add(panelFormulario);

		lblTituloFormulario = new JLabel(tituloFormulario);
		lblTituloFormulario.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTituloFormulario.setBounds(20, 15, 280, 24);
		panelFormulario.add(lblTituloFormulario);
	}

	private JButton crearBoton(String texto, int x) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		boton.setBounds(x, 594, 144, 36);
		boton.putClientProperty("FlatLaf.style", "arc: 12");
		add(boton);
		return boton;
	}

	public void modoEdicion(boolean edicion) {
		btnAniadir.setVisible(!edicion);
		btnModificar.setVisible(!edicion);
		btnEliminar.setVisible(!edicion);
		btnLimpiar.setVisible(!edicion);
		btnGuardar.setVisible(edicion);
		btnCancelar.setVisible(edicion);
		tabla.setEnabled(!edicion);
		comboBoxFiltro.setEnabled(!edicion);
		textFieldBuscar.setEnabled(!edicion);
	}

	public JComboBox<String> getComboBoxFiltro() {
		return comboBoxFiltro;
	}

	public JTextField getTextFieldBuscar() {
		return textFieldBuscar;
	}

	public JTable getTabla() {
		return tabla;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JPanel getPanelFormulario() {
		return panelFormulario;
	}

	public JButton getBtnAniadir() {
		return btnAniadir;
	}

	public JButton getBtnModificar() {
		return btnModificar;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}

	public JButton getBtnLimpiar() {
		return btnLimpiar;
	}

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

}
