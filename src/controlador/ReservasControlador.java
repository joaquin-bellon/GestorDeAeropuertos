package controlador;

import vista.ReservasPanel;

public class ReservasControlador {
	private PrincipalControlador principalControlador;
	private ReservasPanel reservasPanel;

	public ReservasControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.reservasPanel = principalControlador.getFrame().getReservasPanel();
	}

}
