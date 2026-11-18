package comparadores;

import java.util.Comparator;

import modelo.Incidencia;
import utilidades.Constantes;

public class ComparadorIncidencias implements Comparator<Incidencia> {
	private String criterio;

	public ComparadorIncidencias(String criterio) {
		this.criterio = criterio;
	}

	@Override
	public int compare(Incidencia o1, Incidencia o2) {
		switch (criterio) {
		case "Fecha":
			return o1.getFecha().compareTo(o2.getFecha());
		case "Prioridad":
			return Integer.compare(valorPrioridad(o2.getPrioridad()), valorPrioridad(o1.getPrioridad()));
		case "Estado":
			return o1.getEstado().compareTo(o2.getEstado());
		default:
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
	}

	private int valorPrioridad(String prioridad) {
		switch (prioridad) {
		case Constantes.PRIORIDAD_CRITICA:
			return 4;
		case Constantes.PRIORIDAD_ALTA:
			return 3;
		case Constantes.PRIORIDAD_MEDIA:
			return 2;
		case Constantes.PRIORIDAD_BAJA:
			return 1;
		default:
			return 0;
		}
	}

}
