package controlador;

import vista.CuadroDeMandosPanel;

public class CuadroDeMandosControlador {
	private PrincipalControlador principalControlador;
	private CuadroDeMandosPanel cuadroDeMandosPanel;

	public CuadroDeMandosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.cuadroDeMandosPanel = principalControlador.getFrame().getCuadroDeMandosPanel();
	}

}
