package gestores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import modelo.Equipaje;
import modelo.Pasajero;
import modelo.Usuario;
import utilidades.Utilidades;
import utilidades.Constantes;

public class UsuariosGestor {
	private ArrayList<Usuario> usuarios;
	
	private static UsuariosGestor instancia;
	
	public UsuariosGestor() {
		this.usuarios = abrirCSV();
	}

	private ArrayList<Usuario> abrirCSV() {
		usuarios = null;
		try (BufferedReader reader = new BufferedReader(new FileReader (new File(Utilidades.RUTA_USUARIOS)))) {
			CsvToBeanBuilder<Usuario> csvBulider = new CsvToBeanBuilder<Usuario>(reader);
			usuarios = (ArrayList<Usuario>)csvBulider.withType(Usuario.class)
					.withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).withSeparator(',').build().parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usuarios;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public static UsuariosGestor getInstance() {
		if (instancia == null) {
			instancia = new UsuariosGestor();
		}
		return instancia;
	}
	
	public Usuario buscarUsuarioPorNombre(String nombre) {
		boolean encontrado = false;
		Usuario usuario = null;
		Iterator<Usuario> iterator = usuarios.iterator();
		while(iterator.hasNext() && !encontrado) {
			Usuario usuarioIterado = iterator.next();
			if (usuarioIterado.getUsuario().equals(nombre)) {
				usuario = usuarioIterado;
				encontrado = true;
			}
		}
		return usuario;
	}
	public String añadirUsuario(Usuario usuario) {
		usuarios.add(usuario);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_USUARIOS);
		}
		return "";
	}
	
	public String hacerAdmin(String nombre, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un ADMIN puede cambiar roles.";
		}
		Usuario usuario = buscarUsuarioPorNombre(nombre);
		if (usuario == null) {
			return "El usuario " + nombre + " no existe.";
		}
		if (usuario.isAdmin()) {
			return nombre + " ya es ADMIN.";
		}
		usuario.setRol(Constantes.ROL_ADMIN);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_USUARIOS);
		}
		return "";
	}

	public String quitarAdmin(String nombre, String usuarioSesion, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un ADMIN puede cambiar roles.";
		}
		if (nombre.equals(usuarioSesion)) {
			return "No puedes quitarte el rol ADMIN a ti mismo.";
		}
		Usuario usuario = buscarUsuarioPorNombre(nombre);
		if (usuario == null) {
			return "El usuario " + nombre + " no existe.";
		}
		if (!usuario.isAdmin()) {
			return nombre + " ya es USER.";
		}
		usuario.setRol(Constantes.ROL_USER);
		if (!volcarEnCSV()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_USUARIOS);
		}
		return "";
	}

	public boolean volcarEnCSV() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Utilidades.RUTA_USUARIOS)))) {
			StatefulBeanToCsv<Usuario> beantoCsv = new StatefulBeanToCsvBuilder<Usuario>(writer).withOrderedResults(true).withApplyQuotesToAll(false).build();
			beantoCsv.write(usuarios);
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			return false;
		}
		return true;
	}
	
	public static void setInstancia(UsuariosGestor instancia) {
		UsuariosGestor.instancia = instancia;
	}
	
	
}
