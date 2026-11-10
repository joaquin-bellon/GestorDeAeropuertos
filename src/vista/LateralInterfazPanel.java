package vista;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import utilidades.Utilidades;

public class LateralInterfazPanel extends JPanel implements ImagenesConTema {

	private static final long serialVersionUID = 1L;
	private JLabel lblFondo;
	private JToggleButton tglbtnInicio;
	private JToggleButton tglbtnPasajeros;
	private JToggleButton tglbtnVuelos;
	private JToggleButton tglbtnReservas;
	private JToggleButton tglbtnEquipajes;
	private JToggleButton tglbtnEmbarques;
	private JToggleButton tglbtnIncidencias;
	private JToggleButton tglbtnOperativa;
	private JToggleButton tglbtnInformes;
	private JButton btnConfiguracion;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public LateralInterfazPanel() {
		setBounds(0, 110, 220, 653);
		setLayout(null);

		tglbtnInicio = new JToggleButton("Inicio");
		tglbtnInicio.setSelected(true);
		buttonGroup.add(tglbtnInicio);
		tglbtnInicio.setBounds(12, 12, 196, 40);
		darEstiloMenu(tglbtnInicio);
		add(tglbtnInicio);

		tglbtnPasajeros = new JToggleButton("Pasajeros");
		buttonGroup.add(tglbtnPasajeros);
		tglbtnPasajeros.setBounds(12, 58, 196, 40);
		darEstiloMenu(tglbtnPasajeros);
		add(tglbtnPasajeros);

		tglbtnVuelos = new JToggleButton("Vuelos");
		buttonGroup.add(tglbtnVuelos);
		tglbtnVuelos.setBounds(12, 104, 196, 40);
		darEstiloMenu(tglbtnVuelos);
		add(tglbtnVuelos);

		tglbtnReservas = new JToggleButton("Reservas");
		buttonGroup.add(tglbtnReservas);
		tglbtnReservas.setBounds(12, 150, 196, 40);
		darEstiloMenu(tglbtnReservas);
		add(tglbtnReservas);

		tglbtnEquipajes = new JToggleButton("Equipajes");
		buttonGroup.add(tglbtnEquipajes);
		tglbtnEquipajes.setBounds(12, 196, 196, 40);
		darEstiloMenu(tglbtnEquipajes);
		add(tglbtnEquipajes);

		tglbtnEmbarques = new JToggleButton("Embarques");
		buttonGroup.add(tglbtnEmbarques);
		tglbtnEmbarques.setBounds(12, 242, 196, 40);
		darEstiloMenu(tglbtnEmbarques);
		add(tglbtnEmbarques);

		tglbtnIncidencias = new JToggleButton("Incidencias");
		buttonGroup.add(tglbtnIncidencias);
		tglbtnIncidencias.setBounds(12, 288, 196, 40);
		darEstiloMenu(tglbtnIncidencias);
		add(tglbtnIncidencias);

		tglbtnOperativa = new JToggleButton("Operativa");
		buttonGroup.add(tglbtnOperativa);
		tglbtnOperativa.setBounds(12, 334, 196, 40);
		darEstiloMenu(tglbtnOperativa);
		add(tglbtnOperativa);

		tglbtnInformes = new JToggleButton("Informes");
		buttonGroup.add(tglbtnInformes);
		tglbtnInformes.setBounds(12, 380, 196, 40);
		darEstiloMenu(tglbtnInformes);
		add(tglbtnInformes);

		btnConfiguracion = new JButton("");
		btnConfiguracion.setBounds(12, 601, 40, 40);
		btnConfiguracion.putClientProperty("FlatLaf.style", "arc: 999");
		btnConfiguracion.setMargin(new Insets(0, 0, 0, 0));
		btnConfiguracion.setFocusPainted(false);
		btnConfiguracion.setToolTipText("Configuración");
		add(btnConfiguracion);

		lblFondo = new JLabel();
		lblFondo.setBounds(0, 0, 220, 653);
		add(lblFondo);

		actualizarImagenes();
	}

	private void darEstiloMenu(JToggleButton tglbtn) {
		tglbtn.putClientProperty("JButton.buttonType", "borderless");
		tglbtn.putClientProperty("FlatLaf.style", "arc: 12; toolbar.selectedForeground: #FFFFFF");
		tglbtn.setHorizontalAlignment(SwingConstants.LEFT);
		tglbtn.setIconTextGap(12);
		tglbtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
		tglbtn.setFocusPainted(false);
	}

	private void ponerIconoMenu(JToggleButton tglbtn, String archivo) {
		tglbtn.setIcon(new ImageIcon(UIManager.getString("Lateral.iconos") + archivo));
		tglbtn.setSelectedIcon(new ImageIcon(UIManager.getString("Lateral.iconosSeleccionado") + archivo));
		tglbtn.setForeground(UIManager.getColor("Lateral.texto"));
	}

	@Override
	public void actualizarImagenes() {
		Utilidades.insertarImagen(UIManager.getString("Lateral.fondo"), lblFondo);
		ponerIconoMenu(tglbtnInicio, "inicio.png");
		ponerIconoMenu(tglbtnPasajeros, "pasajeros.png");
		ponerIconoMenu(tglbtnVuelos, "vuelos.png");
		ponerIconoMenu(tglbtnReservas, "reservas.png");
		ponerIconoMenu(tglbtnEquipajes, "equipajes.png");
		ponerIconoMenu(tglbtnEmbarques, "embarques.png");
		ponerIconoMenu(tglbtnIncidencias, "incidencias.png");
		ponerIconoMenu(tglbtnOperativa, "operativa.png");
		ponerIconoMenu(tglbtnInformes, "informes.png");
		btnConfiguracion.setIcon(new ImageIcon(UIManager.getString("Lateral.iconos") + "configuracion.png"));
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public JToggleButton getTglbtnInicio() {
		return tglbtnInicio;
	}

	public JToggleButton getTglbtnPasajeros() {
		return tglbtnPasajeros;
	}

	public JToggleButton getTglbtnVuelos() {
		return tglbtnVuelos;
	}

	public JToggleButton getTglbtnReservas() {
		return tglbtnReservas;
	}

	public JToggleButton getTglbtnEquipajes() {
		return tglbtnEquipajes;
	}

	public JToggleButton getTglbtnEmbarques() {
		return tglbtnEmbarques;
	}

	public JToggleButton getTglbtnIncidencias() {
		return tglbtnIncidencias;
	}

	public JToggleButton getTglbtnOperativa() {
		return tglbtnOperativa;
	}

	public JToggleButton getTglbtnInformes() {
		return tglbtnInformes;
	}

	public JButton getBtnConfiguracion() {
		return btnConfiguracion;
	}
}