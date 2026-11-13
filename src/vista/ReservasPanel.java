package vista;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ReservasPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblPasajero;
	private JTextField textFieldPasajero;
	private JLabel lblVuelo;
	private JTextField textFieldVuelo;
	private JLabel lblAsiento;
	private JTextField textFieldAsiento;
	private JLabel lblClase;
	private JComboBox<String> comboBoxClase;
	private JLabel lblPrecio;
	private JTextField textFieldPrecio;
	private JLabel lblFecha;
	private JTextField textFieldFecha;
	private JLabel lblCanal;
	private JComboBox<String> comboBoxCanal;
	private JLabel lblEstado;
	private JTextField textFieldEstado;
	private JPanel panelAsociados;
	private JLabel lblTituloAsociados;
	private JLabel lblInfoAsociados;
	private JLabel lblEquipajes;
	private JScrollPane scrollPaneEquipajes;
	private JTable tableEquipajes;
	private DefaultTableModel modeloEquipajes;
	private JLabel lblEmbarque;
	private JLabel lblEmbarqueCodigo;
	private JLabel lblEmbarqueFecha;
	private JLabel lblEmbarquePuerta;
	private JLabel lblEmbarqueEstado;

	public ReservasPanel() {
		super("Reservas", "Consulta, crea, modifica y cancela reservas", "Datos de la reserva",
				new String[] {"Código", "Pasajero", "Vuelo"},
				new String[] {"Código", "Pasajero", "Vuelo", "Asiento", "Clase", "Estado"},
				new String[] {"Código", "Pasajero", "Vuelo", "Fecha", "Precio"});

		getBtnEliminar().setText("Cancelar");
		getLblConfirmarEliminar().setText("¿Seguro que quieres cancelarla?");
		getBtnSiEliminar().setText("Sí, cancelar");

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCodigo.setBounds(20, 55, 105, 30);
		getPanelFormulario().add(lblCodigo);

		textFieldCodigo = new JTextField();
		textFieldCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCodigo.setEditable(false);
		textFieldCodigo.setBounds(125, 55, 175, 30);
		getPanelFormulario().add(textFieldCodigo);

		lblPasajero = new JLabel("Pasajero");
		lblPasajero.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPasajero.setBounds(20, 97, 105, 30);
		getPanelFormulario().add(lblPasajero);

		textFieldPasajero = new JTextField();
		textFieldPasajero.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPasajero.setBounds(125, 97, 175, 30);
		getPanelFormulario().add(textFieldPasajero);

		lblVuelo = new JLabel("Vuelo");
		lblVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblVuelo.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblVuelo);

		textFieldVuelo = new JTextField();
		textFieldVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldVuelo.setBounds(125, 139, 175, 30);
		getPanelFormulario().add(textFieldVuelo);

		lblAsiento = new JLabel("Asiento");
		lblAsiento.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblAsiento.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblAsiento);

		textFieldAsiento = new JTextField();
		textFieldAsiento.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldAsiento.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(textFieldAsiento);

		lblClase = new JLabel("Clase");
		lblClase.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblClase.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblClase);

		comboBoxClase = new JComboBox<String>();
		comboBoxClase.setModel(new DefaultComboBoxModel<String>(new String[] {"ECONOMICA", "BUSINESS", "PRIMERA"}));
		comboBoxClase.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxClase.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(comboBoxClase);

		lblPrecio = new JLabel("Precio (EUR)");
		lblPrecio.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPrecio.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblPrecio);

		textFieldPrecio = new JTextField();
		textFieldPrecio.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPrecio.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(textFieldPrecio);

		lblFecha = new JLabel("Fecha");
		lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblFecha.setBounds(20, 307, 105, 30);
		getPanelFormulario().add(lblFecha);

		textFieldFecha = new JTextField();
		textFieldFecha.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldFecha.setBounds(125, 307, 175, 30);
		getPanelFormulario().add(textFieldFecha);

		lblCanal = new JLabel("Canal");
		lblCanal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCanal.setBounds(20, 349, 105, 30);
		getPanelFormulario().add(lblCanal);

		comboBoxCanal = new JComboBox<String>();
		comboBoxCanal.setModel(new DefaultComboBoxModel<String>(new String[] {"WEB", "APP", "TAQUILLA", "AGENCIA"}));
		comboBoxCanal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxCanal.setBounds(125, 349, 175, 30);
		getPanelFormulario().add(comboBoxCanal);

		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblEstado.setBounds(20, 391, 105, 30);
		getPanelFormulario().add(lblEstado);

		textFieldEstado = new JTextField();
		textFieldEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldEstado.setEditable(false);
		textFieldEstado.setBounds(125, 391, 175, 30);
		getPanelFormulario().add(textFieldEstado);

		panelAsociados = new JPanel();
		panelAsociados.setLayout(null);
		panelAsociados.setBorder(BorderFactory.createTitledBorder(""));
		panelAsociados.setBounds(640, 130, 320, 500);
		panelAsociados.setVisible(false);
		add(panelAsociados);

		lblTituloAsociados = new JLabel("Reserva cancelada");
		lblTituloAsociados.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTituloAsociados.setBounds(20, 15, 280, 24);
		panelAsociados.add(lblTituloAsociados);

		lblInfoAsociados = new JLabel("Revisa sus asociados por si hay incidencias");
		lblInfoAsociados.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblInfoAsociados.setBounds(20, 42, 280, 20);
		panelAsociados.add(lblInfoAsociados);

		lblEquipajes = new JLabel("Equipajes");
		lblEquipajes.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblEquipajes.setBounds(20, 75, 280, 22);
		panelAsociados.add(lblEquipajes);

		modeloEquipajes = new DefaultTableModel(new Object[][] {}, new String[] {"Código", "Peso", "Tipo", "Estado"});
		tableEquipajes = new JTable(modeloEquipajes);
		tableEquipajes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tableEquipajes.setRowHeight(24);
		tableEquipajes.setDefaultEditor(Object.class, null);
		tableEquipajes.setRowSelectionAllowed(false);
		tableEquipajes.getTableHeader().setReorderingAllowed(false);
		tableEquipajes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tableEquipajes.getColumnModel().getColumn(0).setPreferredWidth(60);
		tableEquipajes.getColumnModel().getColumn(1).setPreferredWidth(42);
		tableEquipajes.getColumnModel().getColumn(2).setPreferredWidth(85);
		tableEquipajes.getColumnModel().getColumn(3).setPreferredWidth(93);

		scrollPaneEquipajes = new JScrollPane(tableEquipajes);
		scrollPaneEquipajes.setBounds(20, 100, 280, 200);
		panelAsociados.add(scrollPaneEquipajes);

		lblEmbarque = new JLabel("Embarque");
		lblEmbarque.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblEmbarque.setBounds(20, 320, 280, 22);
		panelAsociados.add(lblEmbarque);

		lblEmbarqueCodigo = new JLabel("");
		lblEmbarqueCodigo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblEmbarqueCodigo.setBounds(20, 348, 280, 22);
		panelAsociados.add(lblEmbarqueCodigo);

		lblEmbarqueFecha = new JLabel("");
		lblEmbarqueFecha.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblEmbarqueFecha.setBounds(20, 372, 280, 22);
		panelAsociados.add(lblEmbarqueFecha);

		lblEmbarquePuerta = new JLabel("");
		lblEmbarquePuerta.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblEmbarquePuerta.setBounds(20, 396, 280, 22);
		panelAsociados.add(lblEmbarquePuerta);

		lblEmbarqueEstado = new JLabel("");
		lblEmbarqueEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblEmbarqueEstado.setBounds(20, 420, 280, 22);
		panelAsociados.add(lblEmbarqueEstado);
	}

	public void mostrarAsociados(boolean mostrar) {
		getPanelFormulario().setVisible(!mostrar);
		panelAsociados.setVisible(mostrar);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldPasajero() {
		return textFieldPasajero;
	}

	public JTextField getTextFieldVuelo() {
		return textFieldVuelo;
	}

	public JTextField getTextFieldAsiento() {
		return textFieldAsiento;
	}

	public JComboBox<String> getComboBoxClase() {
		return comboBoxClase;
	}

	public JTextField getTextFieldPrecio() {
		return textFieldPrecio;
	}

	public JTextField getTextFieldFecha() {
		return textFieldFecha;
	}

	public JComboBox<String> getComboBoxCanal() {
		return comboBoxCanal;
	}

	public JTextField getTextFieldEstado() {
		return textFieldEstado;
	}

	public JLabel getLblTituloAsociados() {
		return lblTituloAsociados;
	}

	public DefaultTableModel getModeloEquipajes() {
		return modeloEquipajes;
	}

	public JLabel getLblEmbarqueCodigo() {
		return lblEmbarqueCodigo;
	}

	public JLabel getLblEmbarqueFecha() {
		return lblEmbarqueFecha;
	}

	public JLabel getLblEmbarquePuerta() {
		return lblEmbarquePuerta;
	}

	public JLabel getLblEmbarqueEstado() {
		return lblEmbarqueEstado;
	}

}
