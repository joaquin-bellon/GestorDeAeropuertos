package modelo;

import com.opencsv.bean.CsvBindByPosition;
import utilidades.Constantes;

public class Usuario {
@CsvBindByPosition (position = 0)
private String usuario;
@CsvBindByPosition (position = 1)
private String contrasenia;
@CsvBindByPosition (position = 2)
private String rol;

public Usuario () {
	
}

public Usuario(String usuario, String contrasenia, String rol) {
	this.usuario = usuario;
	this.contrasenia = contrasenia;
	this.rol = rol;
}
public String getUsuario() {
	return usuario;
}
public void setUsuario(String usuario) {
	this.usuario = usuario;
}
public String getContrasenia() {
	return contrasenia;
}
public void setContrasenia(String contrasenia) {
	this.contrasenia = contrasenia;
}
public String getRol() {
	return rol;
}
public void setRol(String rol) {
	this.rol = rol;
}

public boolean isAdmin() {
	if(this.rol.equals(Constantes.ROL_ADMIN)) {
		return true;
	}
	else {
		return false;
	}
}

}
