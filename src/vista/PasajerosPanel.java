package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PasajerosPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblNombre;
	private JTextField textFieldNombre;
	private JLabel lblApellidos;
	private JTextField textFieldApellidos;
	private JLabel lblDocumento;
	private JTextField textFieldDocumento;
	private JLabel lblPais;
	private JTextField textFieldPais;
	private JLabel lblCorreo;
	private JTextField textFieldCorreo;
	private JLabel lblTipo;
	private JComboBox<String> comboBoxTipo;

	public PasajerosPanel() {
		super("Pasajeros", "Consulta, añade, modifica y elimina pasajeros", "Datos del pasajero",
				new String[] {"Código", "Nombre", "Apellidos"},
				new String[] {"Código", "Nombre", "Apellidos", "Tipo"},
				new String[] {"Código", "Nombre", "Apellidos", "Tipo"});

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCodigo.setBounds(20, 55, 105, 30);
		getPanelFormulario().add(lblCodigo);

		textFieldCodigo = new JTextField();
		textFieldCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCodigo.setEditable(false);
		textFieldCodigo.setBounds(125, 55, 175, 30);
		getPanelFormulario().add(textFieldCodigo);

		lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblNombre.setBounds(20, 97, 105, 30);
		getPanelFormulario().add(lblNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldNombre.setBounds(125, 97, 175, 30);
		getPanelFormulario().add(textFieldNombre);

		lblApellidos = new JLabel("Apellidos");
		lblApellidos.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblApellidos.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblApellidos);

		textFieldApellidos = new JTextField();
		textFieldApellidos.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldApellidos.setBounds(125, 139, 175, 30);
		getPanelFormulario().add(textFieldApellidos);

		lblDocumento = new JLabel("Documento");
		lblDocumento.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDocumento.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblDocumento);

		textFieldDocumento = new JTextField();
		textFieldDocumento.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldDocumento.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(textFieldDocumento);

		lblPais = new JLabel("País");
		lblPais.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPais.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblPais);

		textFieldPais = new JTextField();
		textFieldPais.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldPais.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(textFieldPais);

		lblCorreo = new JLabel("Correo");
		lblCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCorreo.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblCorreo);

		textFieldCorreo = new JTextField();
		textFieldCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCorreo.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(textFieldCorreo);

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTipo.setBounds(20, 307, 105, 30);
		getPanelFormulario().add(lblTipo);

		comboBoxTipo = new JComboBox<String>();
		comboBoxTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"ADULTO", "VIP", "MENOR"}));
		comboBoxTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxTipo.setBounds(125, 307, 175, 30);
		getPanelFormulario().add(comboBoxTipo);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldNombre() {
		return textFieldNombre;
	}

	public JTextField getTextFieldApellidos() {
		return textFieldApellidos;
	}

	public JTextField getTextFieldDocumento() {
		return textFieldDocumento;
	}

	public JTextField getTextFieldPais() {
		return textFieldPais;
	}

	public JTextField getTextFieldCorreo() {
		return textFieldCorreo;
	}

	public JComboBox<String> getComboBoxTipo() {
		return comboBoxTipo;
	}
	
	

}
