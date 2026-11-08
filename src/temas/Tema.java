package temas;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import vista.ImagenesConTema;

public class Tema {
	public static void cambiarTema (List<ImagenesConTema> panelConImagenesConTema) {
		if (FlatLaf.isLafDark()) {
			FlatLightLaf.setup();
		}else {
			FlatDarkLaf.setup();
		}
		FlatLaf.updateUI();
		
		for (int i = 0 ; i < panelConImagenesConTema.size() ; i++) {
			ImagenesConTema panel = panelConImagenesConTema.get(i);
			panel.actualizarImagenes();
			
			
		}
	}
}
