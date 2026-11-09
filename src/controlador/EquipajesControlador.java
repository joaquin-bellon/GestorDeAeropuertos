package controlador;

import vista.EquipajesPanel;

public class EquipajesControlador {
	private PrincipalControlador principalControlador;
	private EquipajesPanel equipajesPanel;

	public EquipajesControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.equipajesPanel = principalControlador.getFrame().getEquipajesPanel();
	}

}
