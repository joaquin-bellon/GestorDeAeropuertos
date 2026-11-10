package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class VuelosPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblAerolinea;
	private JTextField textFieldAerolinea;
	private JLabel lblAvion;
	private JTextField textFieldAvion;
	private JLabel lblOrigen;
	private JTextField textFieldOrigen;
	private JLabel lblDestino;
	private JTextField textFieldDestino;
	private JLabel lblSalida;
	private JTextField textFieldSalida;
	private JLabel lblLlegada;
	private JTextField textFieldLlegada;
	private JLabel lblTerminal;
	private JTextField textFieldTerminal;
	private JLabel lblPuerta;
	private JTextField textFieldPuerta;
	private JLabel lblEstado;
	private JComboBox<String> comboBoxEstado;

	public VuelosPanel() {
		super("Vuelos", "Consulta, añade y modifica vuelos", "Datos del vuelo",
				new String[] {"Código", "Origen", "Destino", "Avión", "Terminal"},
				new String[] {"Código", "Origen", "Destino", "Salida", "Avión", "Estado"});

		remove(getBtnEliminar());
		getBtnLimpiar().setLocation(324, 594);

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCodigo.setBounds(20, 55, 105, 30);
		getPanelFormulario().add(lblCodigo);

		textFieldCodigo = new JTextField();
		textFieldCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCodigo.setEditable(false);
		textFieldCodigo.setBounds(125, 55, 175, 30);
		getPanelFormulario().add(textFieldCodigo);

		lblAerolinea = new JLabel("Aerolínea");
		lblAerolinea.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblAerolinea.setBounds(20, 97, 105, 30);
		getPanelFormulario().add(lblAerolinea);

		textFieldAerolinea = new JTextField();
		textFieldAerolinea.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldAerolinea.setBounds(125, 97, 175, 30);
		getPanelFormulario().add(textFieldAerolinea);

		lblAvion = new JLabel("Avión");
		lblAvion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblAvion.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblAvion);

		textFieldAvion = new JTextField();
		textFieldAvion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldAvion.setBounds(125, 139, 175, 30);
		getPanelFormulario().add(textFieldAvion);

		lblOrigen = new JLabel("Origen");
		lblOrigen.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblOrigen.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblOrigen);

		textFieldOrigen = new JTextField();
		textFieldOrigen.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldOrigen.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(textFieldOrigen);

		lblDestino = new JLabel("Destino");
		lblDestino.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDestino.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblDestino);

		textFieldDestino = new JTextField();
		textFieldDestino.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldDestino.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(textFieldDestino);

		lblSalida = new JLabel("Salida");
		lblSalida.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSalida.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblSalida);

		textFieldSalida = new JTextField();
		textFieldSalida.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldSalida.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(textFieldSalida);

		lblLlegada = new JLabel("Llegada");
		lblLlegada.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblLlegada.setBounds(20, 307, 105, 30);
		getPanelFormulario().add(lblLlegada);

		textFieldLlegada = new JTextField();
		textFieldLlegada.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldLlegada.setBounds(125, 307, 175, 30);
		getPanelFormulario().add(textFieldLlegada);

		lblTerminal = new JLabel("Terminal");
		lblTerminal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTerminal.setBounds(20, 349, 105, 30);
		getPanelFormulario().add(lblTerminal);

		textFieldTerminal = new JTextField();
		textFieldTerminal.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldTerminal.setBounds(125, 349, 175, 30);
		getPanelFormulario().add(textFieldTerminal);

		lblPuerta = new JLabel("Puerta");
		lblPuerta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPuerta.setBounds(20, 391, 105, 30);
		getPanelFormulario().add(lblPuerta);

		textFieldPuerta = new JTextField();
		textFieldPuerta.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPuerta.setBounds(125, 391, 175, 30);
		getPanelFormulario().add(textFieldPuerta);

		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblEstado.setBounds(20, 433, 105, 30);
		getPanelFormulario().add(lblEstado);

		comboBoxEstado = new JComboBox<String>();
		comboBoxEstado.setModel(new DefaultComboBoxModel<String>(new String[] {"PROGRAMADO", "RETRASADO", "CANCELADO", "EMBARCANDO", "CERRADO"}));
		comboBoxEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxEstado.setBounds(125, 433, 175, 30);
		getPanelFormulario().add(comboBoxEstado);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldAerolinea() {
		return textFieldAerolinea;
	}

	public JTextField getTextFieldAvion() {
		return textFieldAvion;
	}

	public JTextField getTextFieldOrigen() {
		return textFieldOrigen;
	}

	public JTextField getTextFieldDestino() {
		return textFieldDestino;
	}

	public JTextField getTextFieldSalida() {
		return textFieldSalida;
	}

	public JTextField getTextFieldLlegada() {
		return textFieldLlegada;
	}

	public JTextField getTextFieldTerminal() {
		return textFieldTerminal;
	}

	public JTextField getTextFieldPuerta() {
		return textFieldPuerta;
	}

	public JComboBox<String> getComboBoxEstado() {
		return comboBoxEstado;
	}

}
