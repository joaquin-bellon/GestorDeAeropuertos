package controlador;

import java.util.ArrayList;

import javax.swing.JPanel;

import modelo.Usuario;
import vista.AppFrame;
import vista.ConfiguracionPanel;
import vista.CuadroDeMandosPanel;
import vista.EmbarquesPanel;
import vista.EquipajesPanel;
import vista.ImagenesConTema;
import vista.IncidenciasPanel;
import vista.IniciarSesionPanel;
import vista.InicioPanel;
import vista.LateralInterfazPanel;
import vista.OperativaPanel;
import vista.PasajerosPanel;
import vista.RegistrarsePanel;
import vista.ReservasPanel;
import vista.SuperiorInterfazPanel;
import vista.VuelosPanel;

public class PrincipalControlador {
	private ArrayList<ImagenesConTema> listPanelesConImagenesConTema;
	private AppFrame frame;
	private InciarSesionControlador iniciarSesionControlador;
	private RegistrarseControlador registrarseControlador;
	private Usuario usuario;
	private PasajerosControlador pasajerosControlador;
	private VuelosControlador vuelosControlador;
	private ReservasControlador reservasControlador;
	private EquipajesControlador equipajesControlador;
	private EmbarquesControlador embarquesControlador;
	private IncidenciasControlador incidenciasControlador;
	private OperativaControlador operativaControlador;
	private CuadroDeMandosControlador cuadroDeMandosControlador;
	private InicioControlador inicioControlador;
	private LateralInterfazControlador lateralInterfazControlador;
	private SuperiorInterfazControlador superiorInterfazControlador;
	private ConfiguracionControlador configuracionControlador;
	
	
	public PrincipalControlador () {
		listPanelesConImagenesConTema = new ArrayList<ImagenesConTema>();
		IniciarSesionPanel iniciarSesionPanel = new IniciarSesionPanel();

		listPanelesConImagenesConTema.add(iniciarSesionPanel);

		frame = new AppFrame(iniciarSesionPanel);
		iniciarSesionControlador = new InciarSesionControlador(this);
		
		RegistrarsePanel registrarsePanel = new RegistrarsePanel();
		frame.setRegistrarsePanel(registrarsePanel);
		frame.getContentPane().add(frame.getRegistrarsePanel());
		frame.getRegistrarsePanel().setVisible(false);
		listPanelesConImagenesConTema.add(registrarsePanel);
		
		registrarseControlador = new RegistrarseControlador(this);
		
		frame.setVisible(true);
	}
	
	public void crearPanelesYControladores(Usuario usuario) {
		setUsuario(usuario);

		frame.setPasajerosPanel(new PasajerosPanel());
		frame.setVuelosPanel(new VuelosPanel());
		frame.setReservasPanel(new ReservasPanel());
		frame.setEquipajesPanel(new EquipajesPanel());
		frame.setEmbarquesPanel(new EmbarquesPanel());
		frame.setIncidenciasPanel(new IncidenciasPanel());
		frame.setOperativaPanel(new OperativaPanel());
		frame.setCuadroDeMandosPanel(new CuadroDeMandosPanel());
		frame.setConfiguracionPanel(new ConfiguracionPanel());
		
		frame.setInicioPanel(new InicioPanel(usuario.getUsuario()));
		frame.setLateralInterfazPanel(new LateralInterfazPanel());
		frame.setSuperiorInterfazPanel(new SuperiorInterfazPanel());

		anadirPanelOculto(frame.getPasajerosPanel());
		anadirPanelOculto(frame.getVuelosPanel());
		anadirPanelOculto(frame.getReservasPanel());
		anadirPanelOculto(frame.getEquipajesPanel());
		anadirPanelOculto(frame.getEmbarquesPanel());
		anadirPanelOculto(frame.getIncidenciasPanel());
		anadirPanelOculto(frame.getOperativaPanel());
		anadirPanelOculto(frame.getCuadroDeMandosPanel());
		anadirPanelOculto(frame.getConfiguracionPanel());
		
		anadirPanelVisible(frame.getInicioPanel());
		anadirPanelVisible(frame.getLateralInterfazPanel());
		anadirPanelVisible(frame.getSuperiorInterfazPanel());

		pasajerosControlador = new PasajerosControlador(this);
		vuelosControlador = new VuelosControlador(this);
		reservasControlador = new ReservasControlador(this);
		equipajesControlador = new EquipajesControlador(this);
		embarquesControlador = new EmbarquesControlador(this);
		incidenciasControlador = new IncidenciasControlador(this);
		operativaControlador = new OperativaControlador(this);
		cuadroDeMandosControlador = new CuadroDeMandosControlador(this);
		inicioControlador = new InicioControlador(this);
		lateralInterfazControlador = new LateralInterfazControlador(this);
		superiorInterfazControlador = new SuperiorInterfazControlador(this);
		configuracionControlador = new ConfiguracionControlador(this);
		
		listPanelesConImagenesConTema.add(frame.getSuperiorInterfazPanel());
		listPanelesConImagenesConTema.add(frame.getLateralInterfazPanel());
		
	}
	
	public void cerrarSesion() {
		listPanelesConImagenesConTema.remove(frame.getSuperiorInterfazPanel());
		listPanelesConImagenesConTema.remove(frame.getLateralInterfazPanel());
		
		
		frame.getContentPane().remove(frame.getPasajerosPanel());
		frame.getContentPane().remove(frame.getVuelosPanel());
		frame.getContentPane().remove(frame.getReservasPanel());
		frame.getContentPane().remove(frame.getEquipajesPanel());
		frame.getContentPane().remove(frame.getEmbarquesPanel());
		frame.getContentPane().remove(frame.getIncidenciasPanel());
		frame.getContentPane().remove(frame.getOperativaPanel());
		frame.getContentPane().remove(frame.getCuadroDeMandosPanel());
		frame.getContentPane().remove(frame.getInicioPanel());
		frame.getContentPane().remove(frame.getLateralInterfazPanel());
		frame.getContentPane().remove(frame.getSuperiorInterfazPanel());
		frame.getContentPane().remove(frame.getConfiguracionPanel());

		frame.setPasajerosPanel(null);
		frame.setVuelosPanel(null);
		frame.setReservasPanel(null);
		frame.setEquipajesPanel(null);
		frame.setEmbarquesPanel(null);
		frame.setIncidenciasPanel(null);
		frame.setOperativaPanel(null);
		frame.setCuadroDeMandosPanel(null);
		frame.setInicioPanel(null);
		frame.setLateralInterfazPanel(null);
		frame.setSuperiorInterfazPanel(null);
		frame.setConfiguracionPanel(null);

		pasajerosControlador = null;
		vuelosControlador = null;
		reservasControlador = null;
		equipajesControlador = null;
		embarquesControlador = null;
		incidenciasControlador = null;
		operativaControlador = null;
		cuadroDeMandosControlador = null;
		inicioControlador = null;
		lateralInterfazControlador = null;
		superiorInterfazControlador = null;
		configuracionControlador = null;

		setUsuario(null);

		iniciarSesionControlador.vaciarTextos();
		registrarseControlador.vaciarTextos();
		frame.getIniciarSesionPanel().setVisible(true);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	private void anadirPanelOculto(JPanel panel) {
		panel.setVisible(false);
		frame.getContentPane().add(panel);
	}
		
	private void anadirPanelVisible(JPanel panel) {
		panel.setVisible(true);
		frame.getContentPane().add(panel);
	}
	

	public AppFrame getFrame() {
		return frame;
	}


	public InciarSesionControlador getLogInControlador() {
		return iniciarSesionControlador;
	}

	public void setLogInControlador(InciarSesionControlador logInControlador) {
		this.iniciarSesionControlador = logInControlador;
	}

	public ArrayList<ImagenesConTema> getListPanelesConImagenesConTema() {
		return listPanelesConImagenesConTema;
	}


	public RegistrarseControlador getRegistrarseControlador() {
		return registrarseControlador;
	}




	public InciarSesionControlador getIniciarSesionControlador() {
		return iniciarSesionControlador;
	}




	public void setIniciarSesionControlador(InciarSesionControlador iniciarSesionControlador) {
		this.iniciarSesionControlador = iniciarSesionControlador;
	}




	public Usuario getUsuario() {
		return usuario;
	}




	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}




	public void setRegistrarseControlador(RegistrarseControlador registrarseControlador) {
		this.registrarseControlador = registrarseControlador;
	}

	public PasajerosControlador getPasajerosControlador() {
		return pasajerosControlador;
	}

	public void setPasajerosControlador(PasajerosControlador pasajerosControlador) {
		this.pasajerosControlador = pasajerosControlador;
	}

	public VuelosControlador getVuelosControlador() {
		return vuelosControlador;
	}

	public void setVuelosControlador(VuelosControlador vuelosControlador) {
		this.vuelosControlador = vuelosControlador;
	}

	public ReservasControlador getReservasControlador() {
		return reservasControlador;
	}

	public void setReservasControlador(ReservasControlador reservasControlador) {
		this.reservasControlador = reservasControlador;
	}

	public EquipajesControlador getEquipajesControlador() {
		return equipajesControlador;
	}

	public void setEquipajesControlador(EquipajesControlador equipajesControlador) {
		this.equipajesControlador = equipajesControlador;
	}

	public EmbarquesControlador getEmbarquesControlador() {
		return embarquesControlador;
	}

	public void setEmbarquesControlador(EmbarquesControlador embarquesControlador) {
		this.embarquesControlador = embarquesControlador;
	}

	public IncidenciasControlador getIncidenciasControlador() {
		return incidenciasControlador;
	}

	public void setIncidenciasControlador(IncidenciasControlador incidenciasControlador) {
		this.incidenciasControlador = incidenciasControlador;
	}

	public OperativaControlador getOperativaControlador() {
		return operativaControlador;
	}

	public void setOperativaControlador(OperativaControlador operativaControlador) {
		this.operativaControlador = operativaControlador;
	}

	public CuadroDeMandosControlador getCuadroDeMandosControlador() {
		return cuadroDeMandosControlador;
	}

	public void setCuadroDeMandosControlador(CuadroDeMandosControlador cuadroDeMandosControlador) {
		this.cuadroDeMandosControlador = cuadroDeMandosControlador;
	}

	public void setListPanelesConImagenesConTema(ArrayList<ImagenesConTema> listPanelesConImagenesConTema) {
		this.listPanelesConImagenesConTema = listPanelesConImagenesConTema;
	}

	public void setFrame(AppFrame frame) {
		this.frame = frame;
	}

	public InicioControlador getInicioControlador() {
		return inicioControlador;
	}

	public void setInicioControlador(InicioControlador inicioControlador) {
		this.inicioControlador = inicioControlador;
	}

	public LateralInterfazControlador getLateralInterfazControlador() {
		return lateralInterfazControlador;
	}

	public void setLateralInterfazControlador(LateralInterfazControlador lateralInterfazControlador) {
		this.lateralInterfazControlador = lateralInterfazControlador;
	}

	public SuperiorInterfazControlador getSuperiorInterfazControlador() {
		return superiorInterfazControlador;
	}

	public void setSuperiorInterfazControlador(SuperiorInterfazControlador superiorInterfazControlador) {
		this.superiorInterfazControlador = superiorInterfazControlador;
	}
	
	

	
	
	
}
