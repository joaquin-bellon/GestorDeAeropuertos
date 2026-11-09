package controlador;

import vista.LateralInterfazPanel;
import vista.PasajerosPanel;

public class LateralInterfazControlador {
	private PrincipalControlador principalControlador;
	private LateralInterfazPanel lateralInterfazPanel;
	
	public LateralInterfazControlador (PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.lateralInterfazPanel = principalControlador.getFrame().getLateralInterfazPanel();
	}
}
