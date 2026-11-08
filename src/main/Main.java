package main;

import java.awt.EventQueue;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import controlador.PrincipalControlador;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				FlatLaf.registerCustomDefaultsSource("temas");
				FlatLightLaf.setup();
				new PrincipalControlador();
			}
		});
	}
}
