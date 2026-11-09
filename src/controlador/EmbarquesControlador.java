package controlador;

import vista.EmbarquesPanel;

public class EmbarquesControlador {
	private PrincipalControlador principalControlador;
	private EmbarquesPanel embarquesPanel;

	public EmbarquesControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.embarquesPanel = principalControlador.getFrame().getEmbarquesPanel();
	}

}
