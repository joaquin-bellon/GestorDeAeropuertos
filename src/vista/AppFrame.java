package vista;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private IniciarSesionPanel iniciarSesionPanel;
	private RegistrarsePanel registrarsePanel;


	public AppFrame(IniciarSesionPanel logInPanel) {
		setTitle("Gestor De Aeropuertos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(5, 5, 1200, 800);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/logo/logo32x32.png"));
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.iniciarSesionPanel = logInPanel;
		logInPanel.setVisible(true);
		
		contentPane.add(logInPanel);

	}

	public IniciarSesionPanel getIniciarSesionPanel() {
		return iniciarSesionPanel;
	}

	public void setIniciarSesionPanel(IniciarSesionPanel logInPanel) {
		this.iniciarSesionPanel = logInPanel;
	}

	public RegistrarsePanel getRegistrarsePanel() {
		return registrarsePanel;
	}

	public void setRegistrarsePanel(RegistrarsePanel registrarsePanel) {
		this.registrarsePanel = registrarsePanel;
	}
	
	

}
