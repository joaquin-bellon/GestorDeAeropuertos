package controlador;

import vista.IncidenciasPanel;

public class IncidenciasControlador {
	private PrincipalControlador principalControlador;
	private IncidenciasPanel incidenciasPanel;

	public IncidenciasControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.incidenciasPanel = principalControlador.getFrame().getIncidenciasPanel();
	}

}
