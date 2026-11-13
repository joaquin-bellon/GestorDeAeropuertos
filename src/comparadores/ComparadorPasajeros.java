package comparadores;

import java.util.Comparator;

import modelo.Pasajero;

public class ComparadorPasajeros implements Comparator<Pasajero> {
	private String criterio;

	public ComparadorPasajeros(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Pasajero o1, Pasajero o2) {
		switch (criterio) {
		case "Nombre":
			return o1.getNombre().compareToIgnoreCase(o2.getNombre());
		case "Apellidos":
			return o1.getApellidos().compareToIgnoreCase(o2.getApellidos());
		case "Tipo":
			return o1.getTipo().compareTo(o2.getTipo());
		default:
			return o1.getCodPasajero().compareTo(o2.getCodPasajero());
		}
	}

}
