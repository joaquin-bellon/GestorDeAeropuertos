package controlador;

import java.util.ArrayList;

import javax.swing.JPanel;

import modelo.Usuario;
import vista.AppFrame;
import vista.CuadroDeMandosPanel;
import vista.EmbarquesPanel;
import vista.EquipajesPanel;
import vista.ImagenesConTema;
import vista.IncidenciasPanel;
import vista.IniciarSesionPanel;
import vista.OperativaPanel;
import vista.PasajerosPanel;
import vista.RegistrarsePanel;
import vista.ReservasPanel;
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

		anadirPanelOculto(frame.getPasajerosPanel());
		anadirPanelOculto(frame.getVuelosPanel());
		anadirPanelOculto(frame.getReservasPanel());
		anadirPanelOculto(frame.getEquipajesPanel());
		anadirPanelOculto(frame.getEmbarquesPanel());
		anadirPanelOculto(frame.getIncidenciasPanel());
		anadirPanelOculto(frame.getOperativaPanel());
		anadirPanelOculto(frame.getCuadroDeMandosPanel());

		pasajerosControlador = new PasajerosControlador();
		vuelosControlador = new VuelosControlador();
		reservasControlador = new ReservasControlador();
		equipajesControlador = new EquipajesControlador();
		embarquesControlador = new EmbarquesControlador();
		incidenciasControlador = new IncidenciasControlador();
		operativaControlador = new OperativaControlador();
		cuadroDeMandosControlador = new CuadroDeMandosControlador();
	}

	private void anadirPanelOculto(JPanel panel) {
		panel.setVisible(false);
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

	
	
	
}
