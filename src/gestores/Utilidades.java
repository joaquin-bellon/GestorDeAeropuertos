package gestores;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Utilidades {
	public static final String RUTA_PASAJEROS = "archivos/pasajeros.csv";
	public static final String RUTA_EMBARQUES = "archivos/embarques.csv";
	public static final String RUTA_EQUIPAJES = "archivos/equipajes.csv";
	public static final String RUTA_RESERVAS = "archivos/reservas.csv";
	
	public static final String RUTA_AEROPUERTO = "archivos/aeropuerto.xml";
	
	public static final String RUTA_CONFIGURACION = "archivos/configuracion.json";
	
	public static final String RUTA_INCIDENCIAS = "archivos/incidencias.txt";
	
	public static int leerEntero(Scanner sc) {
	      while (true) {
	          try {
	              return Integer.parseInt(sc.nextLine().trim());
	          } catch (NumberFormatException e) {
	              System.err.println("Introduce un número válido:");
	          }
	      }
	  }
	
	public static int leerOpcion(Scanner sc, int min, int max) {
		int opcion;
		do {
			opcion = leerEntero(sc);
			if (opcion < min || opcion > max) {
				System.err.println("Opción no válida. Introduzca un número entre " + min + " y " + max + ":");
			}
		}while (opcion < min || opcion > max);
		return opcion;
	}

	public static LocalDate pedirFecha(Scanner sc, String mensaje) {
	    LocalDate fecha = null;
	    while (fecha == null) {
	        System.out.println(mensaje + " (AAAA-MM-DD):");
	        try {
	            fecha = LocalDate.parse(sc.nextLine().trim());
	        } catch (DateTimeParseException e) {
	            System.err.println("Fecha no válida.");
	        }
	    }
	    return fecha;
	}

	public static LocalTime pedirHora(Scanner sc, String mensaje) {
		LocalTime hora = null;
	    while (hora == null) {
	        System.out.println(mensaje + " (HH:MM):");
	        try {
	            hora =  LocalTime.parse(sc.nextLine().trim());
	        } catch (DateTimeParseException e) {
	            System.err.println("Hora no válida.");
	        }
	    }
	    return hora;
	}
}
