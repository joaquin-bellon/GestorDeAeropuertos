package modelosPOJO;

public class Servicio {
	private String tipo;
	private String estado;

	public Servicio() {

	}

	public Servicio(String tipo, String estado) {
		this.tipo = tipo;
		this.estado = estado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Servicio [tipo=" + tipo + ", estado=" + estado + "]";
	}
}
