package utilidades;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Utilidades {
	public static final String RUTA_PASAJEROS = "archivos/pasajeros.csv";
	public static final String RUTA_EMBARQUES = "archivos/embarques.csv";
	public static final String RUTA_EQUIPAJES = "archivos/equipajes.csv";
	public static final String RUTA_RESERVAS = "archivos/reservas.csv";
	
	public static final String RUTA_AEROPUERTO = "archivos/aeropuerto.xml";
	
	public static final String RUTA_CONFIGURACION = "archivos/configuracion.json";
	
	public static final String RUTA_INCIDENCIAS = "archivos/incidencias.txt";
	
	public static final String RUTA_USUARIOS = "archivos/usuarios.csv";

	public static boolean esAdmin(String rol) {
		return rol.equals(Constantes.ROL_ADMIN);
	}

	public static String errorGuardar(String ruta) {
		return "No se han podido guardar los cambios en " + ruta + ".";
	}
	
	public static void insertarImagen(String ruta, JLabel label) {
		ImageIcon original = new ImageIcon("resources/"+ ruta);
		Image imagen = original.getImage().getScaledInstance(
				label.getWidth(),
				label.getHeight(), 
				Image.SCALE_DEFAULT);
		label.setIcon(new ImageIcon(imagen));
	}
}
