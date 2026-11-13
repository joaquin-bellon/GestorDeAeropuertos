package comparadores;

import java.util.Comparator;

import modelo.Equipaje;

public class ComparadorEquipajes implements Comparator<Equipaje> {
	private String criterio;

	public ComparadorEquipajes(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Equipaje o1, Equipaje o2) {
		switch (criterio) {
		case "Reserva":
			return o1.getCodReserva().compareTo(o2.getCodReserva());
		case "Peso":
			return Double.compare(o1.getPeso(), o2.getPeso());
		case "Tipo":
			return o1.getTipo().compareTo(o2.getTipo());
		default:
			return o1.getCodEquipaje().compareTo(o2.getCodEquipaje());
		}
	}

}
