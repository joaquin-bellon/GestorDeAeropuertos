package comparadores;

import java.util.Comparator;

import modelo.Reserva;

public class ComparadorReservas implements Comparator<Reserva> {
	private String criterio;

	public ComparadorReservas(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Reserva o1, Reserva o2) {
		switch (criterio) {
		case "Pasajero":
			return o1.getPasajero().compareTo(o2.getPasajero());
		case "Vuelo":
			return o1.getVuelo().compareTo(o2.getVuelo());
		case "Fecha":
			return o1.getFecha().compareTo(o2.getFecha());
		case "Precio":
			return Double.compare(o1.getPrecio(), o2.getPrecio());
		default:
			return o1.getIdReserva().compareTo(o2.getIdReserva());
		}
	}

}
