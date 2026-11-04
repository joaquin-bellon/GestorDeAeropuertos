package modelosPOJO;


public class Incidencia {
	private String fecha, codigo, vuelo, tipo;
	private int duracion;
	private String descripcion, prioridad, estado;
	
	
	
	
	
	public Incidencia(String fecha, String codigo, String vuelo, String tipo, int duracion, String descripcion,
			String prioridad, String estado) {
		this.fecha = fecha;
		this.codigo = codigo;
		this.vuelo = vuelo;
		this.tipo = tipo;
		this.duracion = duracion;
		this.descripcion = descripcion;
		this.prioridad = prioridad;
		this.estado = estado;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getVuelo() {
		return vuelo;
	}
	public void setVuelo(String vuelo) {
		this.vuelo = vuelo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Override
	public String toString() {
		return "Incidencia " + codigo + " [" + estado + "] - Vuelo " + vuelo + "\n"
				+ "  Fecha: " + fecha + " | Tipo: " + tipo + " | Duración: " + duracion + " min\n"
				+ "  Prioridad: " + prioridad + "\n"
				+ "  Descripción: " + descripcion;
	}
	
	public String formatoTxt() {
		return fecha + "|" + codigo + "|" + vuelo + "|" + tipo + "|" + duracion + "|"
				+ descripcion + "|" + prioridad + "|" + estado;
	}
}
