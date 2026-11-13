package comparadores;

import java.util.Comparator;

import modelo.Vuelo;

public class ComparadorVuelos implements Comparator<Vuelo> {
	private String criterio;

	public ComparadorVuelos(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Vuelo o1, Vuelo o2) {
		switch (criterio) {
		case "Salida":
			return o1.getSalida().compareTo(o2.getSalida());
		case "Destino":
			return o1.getDestino().compareTo(o2.getDestino());
		case "Estado":
			return o1.getEstado().compareTo(o2.getEstado());
		default:
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
	}

}
