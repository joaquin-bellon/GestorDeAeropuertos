package modelo;

import com.opencsv.bean.CsvBindByPosition;

public class Reserva {
	@CsvBindByPosition (position = 0)
	private String idReserva;
	@CsvBindByPosition (position = 1)
	private String pasajero;
	@CsvBindByPosition (position = 2)
	private String vuelo;
	@CsvBindByPosition (position = 3)
	private String asiento;
	@CsvBindByPosition (position = 4)
	private String clase;
	@CsvBindByPosition (position = 5)
	private double precio;
	@CsvBindByPosition (position = 6)
	private String moneda;
	@CsvBindByPosition (position = 7)
	private String estado;
	@CsvBindByPosition (position = 8)
	private String fecha;
	@CsvBindByPosition (position = 9)
	private String canal;
	
	public Reserva () {
		
	}
	
	
	public Reserva(String idReserva, String pasajero, String vuelo, String asiento, String clase, double precio,
			String moneda, String estado, String fecha, String canal) {
		this.idReserva = idReserva;
		this.pasajero = pasajero;
		this.vuelo = vuelo;
		this.asiento = asiento;
		this.clase = clase;
		this.precio = precio;
		this.moneda = moneda;
		this.estado = estado;
		this.fecha = fecha;
		this.canal = canal;
	}
	public String getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
	}
	public String getPasajero() {
		return pasajero;
	}
	public void setPasajero(String pasajero) {
		this.pasajero = pasajero;
	}
	public String getVuelo() {
		return vuelo;
	}
	public void setVuelo(String vuelo) {
		this.vuelo = vuelo;
	}
	public String getAsiento() {
		return asiento;
	}
	public void setAsiento(String asiento) {
		this.asiento = asiento;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}

	@Override
	public String toString() {
		return "Reserva " + idReserva + " [" + estado + "]\n"
				+ "  Asiento: " + asiento + " | Clase: " + clase + "\n"
				+ "  Precio: " + precio + " " + moneda + "\n"
				+ "  Fecha: " + fecha + " | Canal: " + canal;
	}

	
	
	
}


