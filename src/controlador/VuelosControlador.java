package controlador;

import vista.VuelosPanel;

public class VuelosControlador {
	private PrincipalControlador principalControlador;
	private VuelosPanel vuelosPanel;

	public VuelosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.vuelosPanel = principalControlador.getFrame().getVuelosPanel();
	}

}
