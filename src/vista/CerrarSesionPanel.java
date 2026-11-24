package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CerrarSesionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel tarjeta;
	private JLabel lblTitulo;
	private JLabel lblMensaje;
	private JButton btnCancelar;
	private JButton btnConfirmar;

	public CerrarSesionPanel() {
		setLayout(null);
		setOpaque(false);
		setVisible(false);
		addMouseListener(new MouseAdapter() {});

		tarjeta = new JPanel();
		tarjeta.setLayout(null);
		tarjeta.setSize(420, 190);
		tarjeta.putClientProperty("FlatLaf.style", "arc: 16; background: $Inicio.tarjetaFondo");
		add(tarjeta);

		lblTitulo = new JLabel("Cerrar sesión");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(20, 22, 380, 28);
		tarjeta.add(lblTitulo);

		lblMensaje = new JLabel("¿Seguro que quieres cerrar sesión?");
		lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 15));
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setBounds(20, 62, 380, 24);
		tarjeta.add(lblMensaje);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnCancelar.setBounds(40, 118, 160, 38);
		btnCancelar.putClientProperty("FlatLaf.style", "arc: 12; background: $Boton.cancelarFondo; foreground: #FFFFFF");
		tarjeta.add(btnCancelar);

		btnConfirmar = new JButton("Cerrar sesión");
		btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnConfirmar.setBounds(220, 118, 160, 38);
		btnConfirmar.putClientProperty("FlatLaf.style", "arc: 12; background: $Boton.eliminarFondo; foreground: #FFFFFF");
		tarjeta.add(btnConfirmar);
	}

	public void mostrar() {
		tarjeta.setLocation((getWidth() - tarjeta.getWidth()) / 2, (getHeight() - tarjeta.getHeight()) / 2);
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0, 0, 0, 120));
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JButton getBtnConfirmar() {
		return btnConfirmar;
	}

}
