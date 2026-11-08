package vista;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private IniciarSesionPanel logInPanel;


	public AppFrame(IniciarSesionPanel logInPanel) {
		setTitle("Gestor De Aeropuertos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(5, 5, 1200, 800);
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/logo/logo32x32.png"));
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.logInPanel = logInPanel;
		logInPanel.setVisible(true);
		
		contentPane.add(logInPanel);

	}

	public IniciarSesionPanel getLogInPanel() {
		return logInPanel;
	}

	public void setLogInPanel(IniciarSesionPanel logInPanel) {
		this.logInPanel = logInPanel;
	}
	
	

}
