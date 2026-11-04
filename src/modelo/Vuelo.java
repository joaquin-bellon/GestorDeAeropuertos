package modelosPOJO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Vuelo {
	private String codigo;
	private String aerolinea;
	private String avion;
	private String estado;

	private String origen;
	private String terminal;
	private String puerta;
	private String destino;

	private LocalDateTime salida;
	private String zonaHorariaSalida;
	private LocalDateTime llegada;
	private String zonaHorariaLlegada;

	private String estadoEmbarque;
	private String minutosInicioEmbarque;
	private String minutosCierreEmbarque;

	private ArrayList<Servicio> servicios;

	public Vuelo() {
		servicios = new ArrayList<Servicio>();
	}

	public Vuelo(String codigo, String aerolinea, String avion, String terminal, String puerta, String destino,
			LocalDateTime salida, LocalDateTime llegada) {
		this.codigo = codigo;
		this.aerolinea = aerolinea;
		this.avion = avion;
		this.estado = "PROGRAMADO";
		this.origen = "MAD";
		this.terminal = terminal;
		this.puerta = puerta;
		this.destino = destino;
		this.salida = salida;
		this.zonaHorariaSalida = "Europe/Madrid";
		this.llegada = llegada;
		this.zonaHorariaLlegada = "Europe/Madrid";
		this.estadoEmbarque = "PENDIENTE";
		this.minutosInicioEmbarque = "45";
		this.minutosCierreEmbarque = "15";
		this.servicios = new ArrayList<Servicio>();
		servicios.add(new Servicio("CATERING", "PENDIENTE"));
		servicios.add(new Servicio("LIMPIEZA", "PENDIENTE"));
		servicios.add(new Servicio("COMBUSTIBLE", "PENDIENTE"));
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getAerolinea() {
		return aerolinea;
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}

	public String getAvion() {
		return avion;
	}

	public void setAvion(String avion) {
		this.avion = avion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public LocalDateTime getSalida() {
		return salida;
	}

	public void setSalida(LocalDateTime salida) {
		this.salida = salida;
	}

	public String getZonaHorariaSalida() {
		return zonaHorariaSalida;
	}

	public void setZonaHorariaSalida(String zonaHorariaSalida) {
		this.zonaHorariaSalida = zonaHorariaSalida;
	}

	public LocalDateTime getLlegada() {
		return llegada;
	}

	public void setLlegada(LocalDateTime llegada) {
		this.llegada = llegada;
	}

	public String getZonaHorariaLlegada() {
		return zonaHorariaLlegada;
	}

	public void setZonaHorariaLlegada(String zonaHorariaLlegada) {
		this.zonaHorariaLlegada = zonaHorariaLlegada;
	}

	public String getEstadoEmbarque() {
		return estadoEmbarque;
	}

	public void setEstadoEmbarque(String estadoEmbarque) {
		this.estadoEmbarque = estadoEmbarque;
	}

	public String getMinutosInicioEmbarque() {
		return minutosInicioEmbarque;
	}

	public void setMinutosInicioEmbarque(String minutosInicioEmbarque) {
		this.minutosInicioEmbarque = minutosInicioEmbarque;
	}

	public String getMinutosCierreEmbarque() {
		return minutosCierreEmbarque;
	}

	public void setMinutosCierreEmbarque(String minutosCierreEmbarque) {
		this.minutosCierreEmbarque = minutosCierreEmbarque;
	}

	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}

	@Override
	public String toString() {
		String texto = "-----------------------------------------\n"
				+ "Vuelo: " + codigo + "\n"
				+ "  Aerolínea: " + aerolinea + "\n"
				+ "  Avión: " + avion + "\n"
				+ "  Estado: " + estado + "\n"
				+ "  Ruta:\n"
				+ "    Origen: " + origen + " | Terminal: " + terminal + " | Puerta: " + puerta + "\n"
				+ "    Destino: " + destino + "\n"
				+ "  Horario:\n"
				+ "    Salida: " + salida.toLocalDate() + " " + salida.toLocalTime() + " (" + zonaHorariaSalida + ")\n"
				+ "    Llegada: " + llegada.toLocalDate() + " " + llegada.toLocalTime() + " (" + zonaHorariaLlegada + ")\n"
				+ "  Embarque: " + estadoEmbarque + "\n"
				+ "    Inicio: " + minutosInicioEmbarque + " min antes\n"
				+ "    Cierre: " + minutosCierreEmbarque + " min antes\n"
				+ "  Operaciones:";
		for (Servicio servicio : servicios) {
			texto += "\n    - " + servicio.getTipo() + ": " + servicio.getEstado();
		}
		texto += "\n-----------------------------------------";
		return texto;
	}
}
