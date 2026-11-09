package controlador;

import vista.OperativaPanel;

public class OperativaControlador {
	private PrincipalControlador principalControlador;
	private OperativaPanel operativaPanel;

	public OperativaControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.operativaPanel = principalControlador.getFrame().getOperativaPanel();
	}

}
