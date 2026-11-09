package controlador;

import vista.SuperiorInterfazPanel;

public class SuperiorInterfazControlador {
	private PrincipalControlador principalControlador;
	private SuperiorInterfazPanel superiorInterfazPanel;
	
	public SuperiorInterfazControlador (PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.superiorInterfazPanel = principalControlador.getFrame().getSuperiorInterfazPanel();
	}
}
