package vista;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler;

public class CuadroDeMandosPanel extends JPanel implements ImagenesConTema {

	private static final long serialVersionUID = 1L;
	private static final Color[] COLORES = {new Color(31, 111, 178), new Color(67, 160, 71), new Color(239, 108, 0),
			new Color(198, 40, 40), new Color(123, 31, 162)};

	private JLabel lblTitulo;
	private JLabel lblSubtitulo;
	private JComboBox<String> comboBoxInforme;
	private JButton btnExportarCsv;
	private JLabel lblTotalPasajeros;
	private JLabel lblTotalVuelos;
	private JLabel lblTotalReservas;
	private JLabel lblTotalEquipajes;
	private JLabel lblTotalEmbarques;
	private JLabel lblTotalIncidencias;
	private CategoryChart graficoEstados;
	private XChartPanel<CategoryChart> panelGraficoEstados;
	private PieChart graficoPrioridades;
	private XChartPanel<PieChart> panelGraficoPrioridades;
	private JLabel lblAlertas;
	private DefaultTableModel modeloAlertas;
	private JLabel lblOcupacion;
	private DefaultTableModel modeloOcupacion;
	private CategoryChart graficoOcupacion;
	private XChartPanel<CategoryChart> panelGraficoOcupacion;
	private ArrayList<String> seriesPrioridades = new ArrayList<String>();

	public CuadroDeMandosPanel() {
		setBounds(220, 110, 980, 653);
		setLayout(null);

		lblTitulo = new JLabel("Informes");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setBounds(20, 12, 400, 32);
		add(lblTitulo);

		lblSubtitulo = new JLabel("Estadísticas del aeropuerto y exportación de informes");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSubtitulo.setBounds(20, 44, 450, 20);
		add(lblSubtitulo);

		comboBoxInforme = new JComboBox<String>();
		comboBoxInforme.setModel(new DefaultComboBoxModel<String>(new String[] {"Totales", "Ocupación de vuelos", "Alertas"}));
		comboBoxInforme.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboBoxInforme.setBounds(590, 22, 190, 32);
		add(comboBoxInforme);

		btnExportarCsv = new JButton("Exportar a CSV");
		btnExportarCsv.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnExportarCsv.setBounds(790, 22, 170, 32);
		btnExportarCsv.putClientProperty("FlatLaf.style", "arc: 12");
		add(btnExportarCsv);

		lblTotalPasajeros = crearTarjeta("Pasajeros", 0);
		lblTotalVuelos = crearTarjeta("Vuelos", 1);
		lblTotalReservas = crearTarjeta("Reservas", 2);
		lblTotalEquipajes = crearTarjeta("Equipajes", 3);
		lblTotalEmbarques = crearTarjeta("Embarques", 4);
		lblTotalIncidencias = crearTarjeta("Incidencias", 5);

		graficoEstados = new CategoryChartBuilder().width(300).height(225).title("Vuelos por estado").build();
		graficoEstados.getStyler().setLegendVisible(false);
		graficoEstados.getStyler().setLabelsVisible(true);
		graficoEstados.getStyler().setXAxisLabelRotation(45);
		graficoEstados.getStyler().setXAxisTickMarkSpacingHint(20);
		graficoEstados.getStyler().setAxisTickLabelsFont(new Font("SansSerif", Font.PLAIN, 10));
		darEstiloComun(graficoEstados.getStyler());
		panelGraficoEstados = new XChartPanel<CategoryChart>(graficoEstados);
		panelGraficoEstados.setBounds(20, 170, 300, 225);
		add(panelGraficoEstados);

		graficoPrioridades = new PieChartBuilder().width(300).height(225).title("Incidencias abiertas").build();
		graficoPrioridades.getStyler().setLabelType(LabelType.NameAndValue);
		graficoPrioridades.getStyler().setLegendVisible(false);
		graficoPrioridades.getStyler().setLabelsDistance(1.2);
		graficoPrioridades.getStyler().setPlotContentSize(0.7);
		graficoPrioridades.getStyler().setDecimalPattern("#");
		darEstiloComun(graficoPrioridades.getStyler());
		panelGraficoPrioridades = new XChartPanel<PieChart>(graficoPrioridades);
		panelGraficoPrioridades.setBounds(330, 170, 300, 225);
		add(panelGraficoPrioridades);

		lblAlertas = new JLabel("Alertas");
		lblAlertas.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblAlertas.setBounds(640, 168, 320, 22);
		add(lblAlertas);

		modeloAlertas = new DefaultTableModel(new Object[][] {}, new String[] {"Tipo", "Detalle"});
		JTable tablaAlertas = crearTabla(modeloAlertas);
		tablaAlertas.getColumnModel().getColumn(0).setPreferredWidth(120);
		tablaAlertas.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaAlertas.setDefaultEditor(Object.class, null);
		JScrollPane scrollPaneAlertas = new JScrollPane(tablaAlertas);
		scrollPaneAlertas.setBounds(640, 195, 320, 200);
		add(scrollPaneAlertas);

		lblOcupacion = new JLabel("Ocupación de los vuelos");
		lblOcupacion.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblOcupacion.setBounds(20, 408, 450, 22);
		add(lblOcupacion);

		modeloOcupacion = new DefaultTableModel(new Object[][] {}, new String[] {"Vuelo", "Capacidad", "Reservas", "Libres", "Ocupación"});
		JScrollPane scrollPaneOcupacion = new JScrollPane(crearTabla(modeloOcupacion));
		scrollPaneOcupacion.setBounds(20, 435, 450, 198);
		add(scrollPaneOcupacion);

		graficoOcupacion = new CategoryChartBuilder().width(480).height(225).title("Reservas (azul) y libres (verde)").build();
		graficoOcupacion.getStyler().setStacked(true);
		graficoOcupacion.getStyler().setLegendVisible(false);
		graficoOcupacion.getStyler().setXAxisLabelRotation(45);
		graficoOcupacion.getStyler().setXAxisTickMarkSpacingHint(20);
		graficoOcupacion.getStyler().setAxisTickLabelsFont(new Font("SansSerif", Font.PLAIN, 10));
		darEstiloComun(graficoOcupacion.getStyler());
		panelGraficoOcupacion = new XChartPanel<CategoryChart>(graficoOcupacion);
		panelGraficoOcupacion.setBounds(480, 408, 480, 225);
		add(panelGraficoOcupacion);

		actualizarImagenes();
	}

	private JLabel crearTarjeta(String titulo, int posicion) {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(null);
		tarjeta.setBounds(20 + posicion * 158, 80, 146, 74);
		tarjeta.putClientProperty("FlatLaf.style", "arc: 16; background: $Inicio.tarjetaFondo");
		add(tarjeta);

		JLabel lblTituloTarjeta = new JLabel(titulo);
		lblTituloTarjeta.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblTituloTarjeta.setBounds(14, 8, 125, 18);
		tarjeta.add(lblTituloTarjeta);

		JLabel lblValor = new JLabel("0");
		lblValor.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblValor.putClientProperty("FlatLaf.style", "foreground: $Inicio.valor");
		lblValor.setBounds(14, 28, 125, 36);
		tarjeta.add(lblValor);

		return lblValor;
	}

	private JTable crearTabla(DefaultTableModel modelo) {
		JTable tabla = new JTable(modelo);
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tabla.setRowHeight(24);
		tabla.setDefaultEditor(Object.class, null);
		tabla.setRowSelectionAllowed(false);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		return tabla;
	}

	private void darEstiloComun(Styler styler) {
		styler.setChartTitleFont(new Font("SansSerif", Font.BOLD, 13));
		styler.setLegendFont(new Font("SansSerif", Font.PLAIN, 11));
		styler.setChartTitleBoxVisible(false);
		styler.setLegendBorderColor(new Color(0, 0, 0, 0));
		styler.setPlotBorderVisible(false);
		styler.setSeriesColors(COLORES);
	}

	public void mostrarVuelosPorEstado(ArrayList<String> estados, ArrayList<Integer> cantidades) {
		graficoEstados.removeSeries("Vuelos");
		if (!estados.isEmpty()) {
			graficoEstados.addSeries("Vuelos", estados, cantidades);
		}
		panelGraficoEstados.repaint();
	}

	public void mostrarIncidenciasPorPrioridad(ArrayList<String> prioridades, ArrayList<Integer> cantidades) {
		for (String serie : seriesPrioridades) {
			graficoPrioridades.removeSeries(serie);
		}
		seriesPrioridades.clear();
		for (int i = 0; i < prioridades.size(); i++) {
			graficoPrioridades.addSeries(prioridades.get(i), cantidades.get(i));
			seriesPrioridades.add(prioridades.get(i));
		}
		panelGraficoPrioridades.repaint();
	}

	public void mostrarOcupacion(ArrayList<String> vuelos, ArrayList<Integer> reservas, ArrayList<Integer> libres) {
		graficoOcupacion.removeSeries("Reservas");
		graficoOcupacion.removeSeries("Libres");
		if (!vuelos.isEmpty()) {
			graficoOcupacion.addSeries("Reservas", vuelos, reservas);
			graficoOcupacion.addSeries("Libres", vuelos, libres);
		}
		panelGraficoOcupacion.repaint();
	}

	@Override
	public void actualizarImagenes() {
		Color fondo = UIManager.getColor("Panel.background");
		Color texto = UIManager.getColor("Label.foreground");
		Color rejilla = UIManager.getColor("Component.borderColor");

		graficoEstados.getStyler().setAxisTickLabelsColor(texto);
		graficoEstados.getStyler().setPlotGridLinesColor(rejilla);
		graficoOcupacion.getStyler().setAxisTickLabelsColor(texto);
		graficoOcupacion.getStyler().setPlotGridLinesColor(rejilla);

		colorearGrafico(graficoEstados.getStyler(), fondo, texto);
		colorearGrafico(graficoPrioridades.getStyler(), fondo, texto);
		colorearGrafico(graficoOcupacion.getStyler(), fondo, texto);
		graficoPrioridades.getStyler().setLabelsFontColor(texto);

		panelGraficoEstados.repaint();
		panelGraficoPrioridades.repaint();
		panelGraficoOcupacion.repaint();
	}

	private void colorearGrafico(Styler styler, Color fondo, Color texto) {
		styler.setChartBackgroundColor(fondo);
		styler.setPlotBackgroundColor(fondo);
		styler.setLegendBackgroundColor(fondo);
		styler.setChartFontColor(texto);
	}

	public JComboBox<String> getComboBoxInforme() {
		return comboBoxInforme;
	}

	public JButton getBtnExportarCsv() {
		return btnExportarCsv;
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

	public DefaultTableModel getModeloAlertas() {
		return modeloAlertas;
	}

	public DefaultTableModel getModeloOcupacion() {
		return modeloOcupacion;
	}

}
