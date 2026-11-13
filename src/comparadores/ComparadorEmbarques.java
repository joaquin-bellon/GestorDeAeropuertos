package comparadores;

import java.util.Comparator;

import modelo.Embarque;

public class ComparadorEmbarques implements Comparator<Embarque> {
	private String criterio;

	public ComparadorEmbarques(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Embarque o1, Embarque o2) {
		switch (criterio) {
		case "Fecha":
			return o1.getFechaHora().compareTo(o2.getFechaHora());
		case "Puerta":
			return o1.getPuerta().compareTo(o2.getPuerta());
		case "Estado":
			return o1.getEstado().compareTo(o2.getEstado());
		default:
			return o1.getCodEmbarque().compareTo(o2.getCodEmbarque());
		}
	}

}
