package main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import controlador.PrincipalControlador;

public class Main {
	public static void main(String[] args) {
		FlatLaf.registerCustomDefaultsSource("temas");
		FlatLightLaf.setup();
		PrincipalControlador controladorPrincipal = new PrincipalControlador();
		
	}
}
