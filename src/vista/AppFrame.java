package vista;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel contenido;
	private IniciarSesionPanel iniciarSesionPanel;
	private RegistrarsePanel registrarsePanel;
	private PasajerosPanel pasajerosPanel;
	private VuelosPanel vuelosPanel;
	private ReservasPanel reservasPanel;
	private EquipajesPanel equipajesPanel;
	private EmbarquesPanel embarquesPanel;
	private IncidenciasPanel incidenciasPanel;
	private OperativaPanel operativaPanel;
	private CuadroDeMandosPanel cuadroDeMandosPanel;
	private InicioPanel inicioPanel;
	private LateralInterfazPanel lateralInterfazPanel;
	private SuperiorInterfazPanel superiorInterfazPanel;
	private ConfiguracionPanel configuracionPanel;

	public AppFrame(IniciarSesionPanel logInPanel) {
		setTitle("Gestor De Aeropuertos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(5, 5, 1200, 800);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/logo/logo32x32.png"));
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.iniciarSesionPanel = logInPanel;
		logInPanel.setVisible(true);
		
		contentPane.add(logInPanel);

	}

	public IniciarSesionPanel getIniciarSesionPanel() {
		return iniciarSesionPanel;
	}

	public void setIniciarSesionPanel(IniciarSesionPanel logInPanel) {
		this.iniciarSesionPanel = logInPanel;
	}

	public RegistrarsePanel getRegistrarsePanel() {
		return registrarsePanel;
	}

	public void setRegistrarsePanel(RegistrarsePanel registrarsePanel) {
		this.registrarsePanel = registrarsePanel;
	}

	public PasajerosPanel getPasajerosPanel() {
		return pasajerosPanel;
	}

	public void setPasajerosPanel(PasajerosPanel pasajerosPanel) {
		this.pasajerosPanel = pasajerosPanel;
	}

	public VuelosPanel getVuelosPanel() {
		return vuelosPanel;
	}

	public void setVuelosPanel(VuelosPanel vuelosPanel) {
		this.vuelosPanel = vuelosPanel;
	}

	public ReservasPanel getReservasPanel() {
		return reservasPanel;
	}

	public void setReservasPanel(ReservasPanel reservasPanel) {
		this.reservasPanel = reservasPanel;
	}

	public EquipajesPanel getEquipajesPanel() {
		return equipajesPanel;
	}

	public void setEquipajesPanel(EquipajesPanel equipajesPanel) {
		this.equipajesPanel = equipajesPanel;
	}

	public EmbarquesPanel getEmbarquesPanel() {
		return embarquesPanel;
	}

	public void setEmbarquesPanel(EmbarquesPanel embarquesPanel) {
		this.embarquesPanel = embarquesPanel;
	}

	public IncidenciasPanel getIncidenciasPanel() {
		return incidenciasPanel;
	}

	public void setIncidenciasPanel(IncidenciasPanel incidenciasPanel) {
		this.incidenciasPanel = incidenciasPanel;
	}

	public OperativaPanel getOperativaPanel() {
		return operativaPanel;
	}

	public void setOperativaPanel(OperativaPanel operativaPanel) {
		this.operativaPanel = operativaPanel;
	}

	public CuadroDeMandosPanel getCuadroDeMandosPanel() {
		return cuadroDeMandosPanel;
	}

	public void setCuadroDeMandosPanel(CuadroDeMandosPanel cuadroDeMandosPanel) {
		this.cuadroDeMandosPanel = cuadroDeMandosPanel;
	}

	public InicioPanel getInicioPanel() {
		return inicioPanel;
	}

	public void setInicioPanel(InicioPanel inicioPanel) {
		this.inicioPanel = inicioPanel;
	}

	public LateralInterfazPanel getLateralInterfazPanel() {
		return lateralInterfazPanel;
	}

	public void setLateralInterfazPanel(LateralInterfazPanel lateralInterfazPanel) {
		this.lateralInterfazPanel = lateralInterfazPanel;
	}

	public SuperiorInterfazPanel getSuperiorInterfazPanel() {
		return superiorInterfazPanel;
	}

	public void setSuperiorInterfazPanel(SuperiorInterfazPanel superiorInterfazPanel) {
		this.superiorInterfazPanel = superiorInterfazPanel;
	}

	public JPanel getContenido() {
		return contenido;
	}

	public void setContenido(JPanel contenido) {
		this.contenido = contenido;
	}
	
	
	
	public ConfiguracionPanel getConfiguracionPanel() {
		return configuracionPanel;
	}

	public void setConfiguracionPanel(ConfiguracionPanel configuracionPanel) {
		this.configuracionPanel = configuracionPanel;
	}

	public void mostrarElPanelPorBoton (JPanel jPanel) {
		inicioPanel.setVisible(false);
		pasajerosPanel.setVisible(false);
		vuelosPanel.setVisible(false);
		reservasPanel.setVisible(false);
		embarquesPanel.setVisible(false);
		equipajesPanel.setVisible(false);
		incidenciasPanel.setVisible(false);
		operativaPanel.setVisible(false);
		cuadroDeMandosPanel.setVisible(false);
		configuracionPanel.setVisible(false);
		
		jPanel.setVisible(true);
		
		contentPane.revalidate();
		contentPane.repaint();
		
	}

}
