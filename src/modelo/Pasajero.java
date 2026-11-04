package modelosPOJO;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;

public class Pasajero {
	@CsvBindByPosition (position = 0)
	private String codPasajero;
	@CsvBindByPosition (position = 1)
	private String nombre;
	@CsvBindByPosition (position = 2)
	private String apellidos;
	@CsvBindByPosition (position = 3)
	private String documentoIdentificativo;
	@CsvBindByPosition (position = 4)
	private String pais;
	@CsvBindByPosition (position = 5)
	private String correo;
	@CsvBindByPosition (position = 6)
	private String tipo;
	
	public Pasajero () {
		
	}
	
	
	public Pasajero(String codPasajero, String nombre, String apellidos, String documentoIdentificativo, String pais,
			String correo, String tipo) {
		this.codPasajero = codPasajero;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.documentoIdentificativo = documentoIdentificativo;
		this.pais = pais;
		this.correo = correo;
		this.tipo = tipo;
	}


	@Override
	public String toString() {
		return "Pasajero " + codPasajero + ": " + nombre + " " + apellidos + " (" + tipo + ")\n"
				+ "  Documento: " + documentoIdentificativo + " | País: " + pais + " | Correo: " + correo;
	}
	public String getCodPasajero() {
		return codPasajero;
	}
	public void setCodPasajero(String codPasajero) {
		this.codPasajero = codPasajero;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDocumentoIdentificativo() {
		return documentoIdentificativo;
	}
	public void setDocumentoIdentificativo(String documentoIdentificativo) {
		this.documentoIdentificativo = documentoIdentificativo;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
}
