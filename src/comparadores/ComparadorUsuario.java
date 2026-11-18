package comparadores;

import java.util.Comparator;

import modelo.Usuario;

public class ComparadorUsuario implements Comparator<Usuario>{

	@Override
	public int compare(Usuario o1, Usuario o2) {
		return o1.getRol().compareToIgnoreCase(o2.getRol());
	}

}
