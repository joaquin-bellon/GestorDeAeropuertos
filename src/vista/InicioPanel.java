package vista;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestores.EmbarquesGestor;
import gestores.EquipajesGestor;
import gestores.IncidenciasGestor;
import gestores.PasajerosGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Incidencia;
import modelo.Vuelo;

public class InicioPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblBienvenida;
	private JLabel lblSubtitulo;
	private JLabel lblTotalPasajeros;
	private JLabel lblTotalVuelos;
	private JLabel lblTotalReservas;
	private JLabel lblTotalEquipajes;
	private JLabel lblTotalEmbarques;
	private JLabel lblTotalIncidencias;
	private JLabel lblProximosVuelos;
	private JScrollPane scrollPaneVuelos;
	private JTable tableVuelos;
	private DefaultTableModel modeloVuelos;
	private JLabel lblIncidenciasAbiertas;
	private JScrollPane scrollPaneIncidencias;
	private JTable tableIncidencias;
	private DefaultTableModel modeloIncidencias;

	public InicioPanel(String nombreUsuario) {
		setBounds(220, 110, 980, 653);
		setLayout(null);

		lblBienvenida = new JLabel("Bienvenido/a, " + nombreUsuario);
		lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblBienvenida.setBounds(20, 12, 600, 32);
		add(lblBienvenida);

		lblSubtitulo = new JLabel("Resumen del estado actual del aeropuerto");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSubtitulo.setBounds(20, 44, 600, 20);
		add(lblSubtitulo);

		lblTotalPasajeros = crearTarjeta("Pasajeros", 20, 85);
		lblTotalVuelos = crearTarjeta("Vuelos", 340, 85);
		lblTotalReservas = crearTarjeta("Reservas confirmadas", 660, 85);
		lblTotalEquipajes = crearTarjeta("Equipajes", 20, 200);
		lblTotalEmbarques = crearTarjeta("Embarques", 340, 200);
		lblTotalIncidencias = crearTarjeta("Incidencias abiertas", 660, 200);

		lblProximosVuelos = new JLabel("Próximos vuelos");
		lblProximosVuelos.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblProximosVuelos.setBounds(20, 318, 300, 24);
		add(lblProximosVuelos);

		modeloVuelos = new DefaultTableModel(new Object[][] {}, new String[] {"Código", "Destino", "Salida", "Estado"});
		tableVuelos = crearTabla(modeloVuelos);
		scrollPaneVuelos = new JScrollPane(tableVuelos);
		scrollPaneVuelos.setBounds(20, 350, 460, 280);
		add(scrollPaneVuelos);

		lblIncidenciasAbiertas = new JLabel("Incidencias abiertas");
		lblIncidenciasAbiertas.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblIncidenciasAbiertas.setBounds(500, 318, 300, 24);
		add(lblIncidenciasAbiertas);

		modeloIncidencias = new DefaultTableModel(new Object[][] {}, new String[] {"Código", "Vuelo", "Tipo", "Prioridad"});
		tableIncidencias = crearTabla(modeloIncidencias);
		scrollPaneIncidencias = new JScrollPane(tableIncidencias);
		scrollPaneIncidencias.setBounds(500, 350, 460, 280);
		add(scrollPaneIncidencias);
		
		cargarDatos();
	}

	public void cargarDatos() {
		lblTotalPasajeros.setText(String.valueOf(PasajerosGestor.getInstance().getPasajeros().size()));
		lblTotalVuelos.setText(String.valueOf(VuelosGestor.getInstance().getVuelos().size()));
		lblTotalReservas.setText(String.valueOf(ReservasGestor.getInstance().obtenerReservasConfirmadas().size()));
		lblTotalEquipajes.setText(String.valueOf(EquipajesGestor.getInstance().getEquipajes().size()));
		lblTotalEmbarques.setText(String.valueOf(EmbarquesGestor.getInstance().getEmbarques().size()));
		
		ArrayList<Incidencia> incidenciasAbiertas = IncidenciasGestor.getInstance().buscarPorEstado("ABIERTA");
		
		lblTotalIncidencias.setText(String.valueOf(incidenciasAbiertas.size()));

		modeloVuelos.setRowCount(0);
		for (Vuelo vuelo : VuelosGestor.getInstance().obtenerVuelosActivos()) {
				modeloVuelos.addRow(new Object[] {vuelo.getCodigo(), vuelo.getDestino(), vuelo.getSalida().toString().replace("T", " "), vuelo.getEstado()});
			}

		modeloIncidencias.setRowCount(0);
		for (Incidencia incidencia : incidenciasAbiertas) {
			modeloIncidencias.addRow(new Object[] {incidencia.getCodigo(), incidencia.getVuelo(), incidencia.getTipo(), incidencia.getPrioridad()});
		}
	}

	private JLabel crearTarjeta(String titulo, int x, int y) {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(null);
		tarjeta.setBounds(x, y, 300, 100);
		tarjeta.putClientProperty("FlatLaf.style", "arc: 16; background: $Inicio.tarjetaFondo");
		add(tarjeta);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTitulo.setBounds(20, 14, 260, 20);
		tarjeta.add(lblTitulo);

		JLabel lblValor = new JLabel("0");
		lblValor.setFont(new Font("SansSerif", Font.BOLD, 34));
		lblValor.putClientProperty("FlatLaf.style", "foreground: $Inicio.valor");
		lblValor.setBounds(20, 40, 260, 44);
		tarjeta.add(lblValor);

		return lblValor;
	}

	private JTable crearTabla(DefaultTableModel modelo) {
		JTable tabla = new JTable(modelo);
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tabla.setRowHeight(26);
		tabla.setDefaultEditor(Object.class, null);
		tabla.setRowSelectionAllowed(false);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		return tabla;
	}

	public JLabel getLblBienvenida() {
		return lblBienvenida;
	}

	public JLabel getLblTotalPasajeros() {
		return lblTotalPasajeros;
	}

	public JLabel getLblTotalVuelos() {
		return lblTotalVuelos;
	}

	public JLabel getLblTotalReservas() {
		return lblTotalReservas;
	}

	public JLabel getLblTotalEquipajes() {
		return lblTotalEquipajes;
	}

	public JLabel getLblTotalEmbarques() {
		return lblTotalEmbarques;
	}

	public JLabel getLblTotalIncidencias() {
		return lblTotalIncidencias;
	}

	public DefaultTableModel getModeloVuelos() {
		return modeloVuelos;
	}

	public DefaultTableModel getModeloIncidencias() {
		return modeloIncidencias;
	}

}
