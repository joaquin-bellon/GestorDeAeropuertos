package utilidades;

import javax.swing.JLabel;

public class HiloAvisos extends Thread {
	private JLabel label;
	private String texto;
	
	
	
	public HiloAvisos(JLabel label, String texto) {
		this.label = label;
		this.texto = texto;
	}



	public void run () {
		label.setText(texto);
		try {
			this.sleep(3000);
			if (label.getText().equals(texto)) {
				label.setText("");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
