package modelosPOJO;

import com.opencsv.bean.CsvBindByPosition;

public class Embarque {
	@CsvBindByPosition (position = 0)
	private String codEmbarque;
	@CsvBindByPosition (position = 1)
	private String codReserva;
	@CsvBindByPosition (position = 2)
	private String fechaHora;
	@CsvBindByPosition (position = 3)
	private String puerta;
	@CsvBindByPosition (position = 4)
	private String estado;
	@CsvBindByPosition (position = 5)
	private String observaciones;

	public Embarque() {

	}

	public Embarque(String codEmbarque, String codReserva, String fechaHora, String puerta, String estado,
			String observaciones) {
		this.codEmbarque = codEmbarque;
		this.codReserva = codReserva;
		this.fechaHora = fechaHora;
		this.puerta = puerta;
		this.estado = estado;
		this.observaciones = observaciones;
	}

	public String getCodEmbarque() {
		return codEmbarque;
	}

	public void setCodEmbarque(String codEmbarque) {
		this.codEmbarque = codEmbarque;
	}

	public String getCodReserva() {
		return codReserva;
	}

	public void setCodReserva(String codReserva) {
		this.codReserva = codReserva;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public String toString() {
		return "Embarque " + codEmbarque + " (reserva " + codReserva + ") [" + estado + "]\n"
				+ "  Fecha y hora: " + fechaHora + " | Puerta: " + puerta + "\n"
				+ "  Observaciones: " + observaciones;
	}
}
