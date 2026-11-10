package vista;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.UIManager;

import utilidades.Utilidades;

import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;

public class LateralInterfazPanel extends JPanel implements ImagenesConTema {

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
		add(tglbtnPasajeros);

		tglbtnVuelos = new JToggleButton("Vuelos");
		buttonGroup.add(tglbtnVuelos);
		tglbtnVuelos.setBounds(12, 104, 196, 40);
		add(tglbtnVuelos);

		tglbtnReservas = new JToggleButton("Reservas");
		buttonGroup.add(tglbtnReservas);
		tglbtnReservas.setBounds(12, 150, 196, 40);
		add(tglbtnReservas);

		tglbtnEquipajes = new JToggleButton("Equipajes");
		buttonGroup.add(tglbtnEquipajes);
		tglbtnEquipajes.setBounds(12, 196, 196, 40);
		add(tglbtnEquipajes);

		tglbtnEmbarques = new JToggleButton("Embarques");
		buttonGroup.add(tglbtnEmbarques);
		tglbtnEmbarques.setBounds(12, 242, 196, 40);
		add(tglbtnEmbarques);

		tglbtnIncidencias = new JToggleButton("Incidencias");
		buttonGroup.add(tglbtnIncidencias);
		tglbtnIncidencias.setBounds(12, 288, 196, 40);
		add(tglbtnIncidencias);

		tglbtnOperativa = new JToggleButton("Operativa");
		buttonGroup.add(tglbtnOperativa);
		tglbtnOperativa.setBounds(12, 334, 196, 40);
		add(tglbtnOperativa);

		tglbtnInformes = new JToggleButton("Informes");
		buttonGroup.add(tglbtnInformes);
		tglbtnInformes.setBounds(12, 380, 196, 40);
		add(tglbtnInformes);

		lblFondo = new JLabel();
		lblFondo.setBounds(0, 0, 220, 653);
		add(lblFondo);

		actualizarImagenes();
	}

	private void darEstiloMenu(JToggleButton tglbtn) {
		tglbtn.putClientProperty("JButton.buttonType", "borderless");
		tglbtn.putClientProperty("FlatLaf.style", "arc: 12");
		tglbtn.setHorizontalAlignment(SwingConstants.LEFT);
		tglbtn.setIconTextGap(12);
		tglbtn.setFocusPainted(false);
		
	}

	@Override
	public void actualizarImagenes() {
		Utilidades.insertarImagen(UIManager.getString("Lateral.fondo"), lblFondo);
		
	}
}
