package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EquipajesPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblReserva;
	private JTextField textFieldReserva;
	private JLabel lblPasajero;
	private JTextField textFieldPasajero;
	private JLabel lblVuelo;
	private JTextField textFieldVuelo;
	private JLabel lblPeso;
	private JTextField textFieldPeso;
	private JLabel lblTipo;
	private JComboBox<String> comboBoxTipo;
	private JLabel lblEstado;
	private JComboBox<String> comboBoxEstado;
	private JLabel lblPrioridad;
	private JComboBox<String> comboBoxPrioridad;

	public EquipajesPanel() {
		super("Equipajes", "Consulta, registra, modifica y da de baja equipajes", "Datos del equipaje",
				new String[] {"Código", "Reserva", "Pasajero", "Vuelo"},
				new String[] {"Código", "Reserva", "Pasajero", "Vuelo", "Peso", "Tipo"});

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCodigo.setBounds(20, 55, 105, 30);
		getPanelFormulario().add(lblCodigo);

		textFieldCodigo = new JTextField();
		textFieldCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCodigo.setEditable(false);
		textFieldCodigo.setBounds(125, 55, 175, 30);
		getPanelFormulario().add(textFieldCodigo);

		lblReserva = new JLabel("Reserva");
		lblReserva.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblReserva.setBounds(20, 97, 105, 30);
		getPanelFormulario().add(lblReserva);

		textFieldReserva = new JTextField();
		textFieldReserva.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldReserva.setBounds(125, 97, 175, 30);
		getPanelFormulario().add(textFieldReserva);

		lblPasajero = new JLabel("Pasajero");
		lblPasajero.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPasajero.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblPasajero);

		textFieldPasajero = new JTextField();
		textFieldPasajero.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPasajero.setEditable(false);
		textFieldPasajero.setBounds(125, 139, 175, 30);
		getPanelFormulario().add(textFieldPasajero);

		lblVuelo = new JLabel("Vuelo");
		lblVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblVuelo.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblVuelo);

		textFieldVuelo = new JTextField();
		textFieldVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldVuelo.setEditable(false);
		textFieldVuelo.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(textFieldVuelo);

		lblPeso = new JLabel("Peso (kg)");
		lblPeso.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPeso.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblPeso);

		textFieldPeso = new JTextField();
		textFieldPeso.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPeso.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(textFieldPeso);

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTipo.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblTipo);

		comboBoxTipo = new JComboBox<String>();
		comboBoxTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"FACTURADO", "MANO", "ESPECIAL"}));
		comboBoxTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxTipo.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(comboBoxTipo);

		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblEstado.setBounds(20, 307, 105, 30);
		getPanelFormulario().add(lblEstado);

		comboBoxEstado = new JComboBox<String>();
		comboBoxEstado.setModel(new DefaultComboBoxModel<String>(new String[] {"REGISTRADO", "CARGADO", "EN_TRANSITO", "ENTREGADO", "PERDIDO"}));
		comboBoxEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxEstado.setBounds(125, 307, 175, 30);
		getPanelFormulario().add(comboBoxEstado);

		lblPrioridad = new JLabel("Prioridad");
		lblPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPrioridad.setBounds(20, 349, 105, 30);
		getPanelFormulario().add(lblPrioridad);

		comboBoxPrioridad = new JComboBox<String>();
		comboBoxPrioridad.setModel(new DefaultComboBoxModel<String>(new String[] {"NORMAL", "PRIORITARIO", "FRAGIL"}));
		comboBoxPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxPrioridad.setBounds(125, 349, 175, 30);
		getPanelFormulario().add(comboBoxPrioridad);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldReserva() {
		return textFieldReserva;
	}

	public JTextField getTextFieldPasajero() {
		return textFieldPasajero;
	}

	public JTextField getTextFieldVuelo() {
		return textFieldVuelo;
	}

	public JTextField getTextFieldPeso() {
		return textFieldPeso;
	}

	public JComboBox<String> getComboBoxTipo() {
		return comboBoxTipo;
	}

	public JComboBox<String> getComboBoxEstado() {
		return comboBoxEstado;
	}

	public JComboBox<String> getComboBoxPrioridad() {
		return comboBoxPrioridad;
	}

}
