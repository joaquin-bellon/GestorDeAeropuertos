package controlador;

import vista.InicioPanel;
import vista.PasajerosPanel;

public class InicioControlador {
	private PrincipalControlador principalControlador;
	private InicioPanel inicioPanel;
	public InicioControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.inicioPanel = principalControlador.getFrame().getInicioPanel();
	}
	
	
}
