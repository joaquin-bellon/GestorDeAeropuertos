package controlador;

import vista.ConfiguracionPanel;
import vista.CuadroDeMandosPanel;

public class ConfiguracionControlador {
	private PrincipalControlador principalControlador;
	private ConfiguracionPanel configuracionPanel;

	public ConfiguracionControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.configuracionPanel = principalControlador.getFrame().getConfiguracionPanel();
	}
}
