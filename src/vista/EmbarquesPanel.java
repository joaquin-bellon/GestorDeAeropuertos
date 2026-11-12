package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EmbarquesPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblReserva;
	private JTextField textFieldReserva;
	private JLabel lblFechaHora;
	private JTextField textFieldFechaHora;
	private JLabel lblPuerta;
	private JTextField textFieldPuerta;
	private JLabel lblEstado;
	private JComboBox<String> comboBoxEstado;
	private JLabel lblObservaciones;
	private JTextField textFieldObservaciones;

	public EmbarquesPanel() {
		super("Embarques", "Consulta y gestiona los embarques", "Datos del embarque",
				new String[] {"Fecha"},
				new String[] {"Código", "Reserva", "Fecha y hora", "Puerta", "Estado"});

		getTextFieldBuscar().putClientProperty("JTextField.placeholderText", "Desde la fecha (aaaa-mm-dd)");

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

		lblFechaHora = new JLabel("Fecha y hora");
		lblFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblFechaHora.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblFechaHora);

		textFieldFechaHora = new JTextField();
		textFieldFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldFechaHora.setBounds(125, 139, 175, 30);
		textFieldFechaHora.putClientProperty("JTextField.placeholderText", "aaaa-mm-dd hh:mm");
		getPanelFormulario().add(textFieldFechaHora);

		lblPuerta = new JLabel("Puerta");
		lblPuerta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPuerta.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblPuerta);

		textFieldPuerta = new JTextField();
		textFieldPuerta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPuerta.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(textFieldPuerta);

		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblEstado.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblEstado);

		comboBoxEstado = new JComboBox<String>();
		comboBoxEstado.setModel(new DefaultComboBoxModel<String>(new String[] {"EMBARCANDO", "EMBARCADO", "NO_EMBARCADO"}));
		comboBoxEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxEstado.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(comboBoxEstado);

		lblObservaciones = new JLabel("Observaciones");
		lblObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblObservaciones.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblObservaciones);

		textFieldObservaciones = new JTextField();
		textFieldObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldObservaciones.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(textFieldObservaciones);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldReserva() {
		return textFieldReserva;
	}

	public JTextField getTextFieldFechaHora() {
		return textFieldFechaHora;
	}

	public JTextField getTextFieldPuerta() {
		return textFieldPuerta;
	}

	public JComboBox<String> getComboBoxEstado() {
		return comboBoxEstado;
	}

	public JTextField getTextFieldObservaciones() {
		return textFieldObservaciones;
	}

}
