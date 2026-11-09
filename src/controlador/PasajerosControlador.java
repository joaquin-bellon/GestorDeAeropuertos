package controlador;

import vista.PasajerosPanel;

public class PasajerosControlador {
	private PrincipalControlador principalControlador;
	private PasajerosPanel pasajerosPanel;

	public PasajerosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.pasajerosPanel = principalControlador.getFrame().getPasajerosPanel();
	}

}
