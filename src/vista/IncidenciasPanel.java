package vista;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IncidenciasPanel extends PanelGenerico {

	private static final long serialVersionUID = 1L;
	private JLabel lblCodigo;
	private JTextField textFieldCodigo;
	private JLabel lblFecha;
	private JTextField textFieldFecha;
	private JLabel lblVuelo;
	private JTextField textFieldVuelo;
	private JLabel lblTipo;
	private JComboBox<String> comboBoxTipo;
	private JLabel lblDuracion;
	private JTextField textFieldDuracion;
	private JLabel lblDescripcion;
	private JTextField textFieldDescripcion;
	private JLabel lblPrioridad;
	private JComboBox<String> comboBoxPrioridad;
	private JLabel lblEstado;
	private JTextField textFieldEstado;

	public IncidenciasPanel() {
		super("Incidencias", "Consulta, crea y gestiona incidencias", "Datos de la incidencia",
				new String[] {"Código", "Vuelo", "Tipo"},
				new String[] {"Código", "Fecha", "Vuelo", "Tipo", "Prioridad", "Estado"});

		lblCodigo = new JLabel("Código");
		lblCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCodigo.setBounds(20, 55, 105, 30);
		getPanelFormulario().add(lblCodigo);

		textFieldCodigo = new JTextField();
		textFieldCodigo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldCodigo.setEditable(false);
		textFieldCodigo.setBounds(125, 55, 175, 30);
		getPanelFormulario().add(textFieldCodigo);

		lblFecha = new JLabel("Fecha");
		lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblFecha.setBounds(20, 97, 105, 30);
		getPanelFormulario().add(lblFecha);

		textFieldFecha = new JTextField();
		textFieldFecha.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldFecha.setBounds(125, 97, 175, 30);
		getPanelFormulario().add(textFieldFecha);

		lblVuelo = new JLabel("Vuelo");
		lblVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblVuelo.setBounds(20, 139, 105, 30);
		getPanelFormulario().add(lblVuelo);

		textFieldVuelo = new JTextField();
		textFieldVuelo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldVuelo.setBounds(125, 139, 175, 30);
		getPanelFormulario().add(textFieldVuelo);

		lblTipo = new JLabel("Tipo");
		lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTipo.setBounds(20, 181, 105, 30);
		getPanelFormulario().add(lblTipo);

		comboBoxTipo = new JComboBox<String>();
		comboBoxTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"RETRASO", "CANCELACION", "CAMBIO_PUERTA", "EQUIPAJE_PERDIDO", "AVERIA"}));
		comboBoxTipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxTipo.setBounds(125, 181, 175, 30);
		getPanelFormulario().add(comboBoxTipo);

		lblDuracion = new JLabel("Duración (min)");
		lblDuracion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDuracion.setBounds(20, 223, 105, 30);
		getPanelFormulario().add(lblDuracion);

		textFieldDuracion = new JTextField();
		textFieldDuracion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldDuracion.setBounds(125, 223, 175, 30);
		getPanelFormulario().add(textFieldDuracion);

		lblDescripcion = new JLabel("Descripción");
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(20, 265, 105, 30);
		getPanelFormulario().add(lblDescripcion);

		textFieldDescripcion = new JTextField();
		textFieldDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldDescripcion.setBounds(125, 265, 175, 30);
		getPanelFormulario().add(textFieldDescripcion);

		lblPrioridad = new JLabel("Prioridad");
		lblPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblPrioridad.setBounds(20, 307, 105, 30);
		getPanelFormulario().add(lblPrioridad);

		comboBoxPrioridad = new JComboBox<String>();
		comboBoxPrioridad.setModel(new DefaultComboBoxModel<String>(new String[] {"BAJA", "MEDIA", "ALTA", "CRITICA"}));
		comboBoxPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxPrioridad.setBounds(125, 307, 175, 30);
		getPanelFormulario().add(comboBoxPrioridad);

		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblEstado.setBounds(20, 349, 105, 30);
		getPanelFormulario().add(lblEstado);

		textFieldEstado = new JTextField();
		textFieldEstado.setFont(new Font("SansSerif", Font.PLAIN, 14));
		textFieldEstado.setEditable(false);
		textFieldEstado.setBounds(125, 349, 175, 30);
		getPanelFormulario().add(textFieldEstado);
	}

	public JTextField getTextFieldCodigo() {
		return textFieldCodigo;
	}

	public JTextField getTextFieldFecha() {
		return textFieldFecha;
	}

	public JTextField getTextFieldVuelo() {
		return textFieldVuelo;
	}

	public JComboBox<String> getComboBoxTipo() {
		return comboBoxTipo;
	}

	public JTextField getTextFieldDuracion() {
		return textFieldDuracion;
	}

	public JTextField getTextFieldDescripcion() {
		return textFieldDescripcion;
	}

	public JComboBox<String> getComboBoxPrioridad() {
		return comboBoxPrioridad;
	}

	public JTextField getTextFieldEstado() {
		return textFieldEstado;
	}

}
