package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

	public ReservasPanel() {
		super("Reservas", "Consulta, crea, modifica y cancela reservas", "Datos de la reserva",
				new String[] {"Código", "Pasajero", "Vuelo"},
				new String[] {"Código", "Pasajero", "Vuelo", "Asiento", "Clase", "Estado"});

		getBtnEliminar().setText("Cancelar");

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

}
