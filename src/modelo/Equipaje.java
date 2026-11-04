package modelo;

import com.opencsv.bean.CsvBindByPosition;

public class Equipaje {
	 @CsvBindByPosition (position = 0)
	 private String codEquipaje;
	 @CsvBindByPosition (position = 1)
	 private String codReserva;
	 @CsvBindByPosition (position = 2)
	 private double peso;
	 @CsvBindByPosition (position = 3)
	 private String tipo;
	 @CsvBindByPosition (position = 4)
	 private String estado;
	 @CsvBindByPosition (position = 5)
	 private String prioridad;
	 
	 
	 
	 
	 
	 public Equipaje() {
		 
	 }
	 
	 public Equipaje(String codEquipaje, String codReserva, double peso, String tipo, String estado, String prioridad) {
		this.codEquipaje = codEquipaje;
		this.codReserva = codReserva;
		this.peso = peso;
		this.tipo = tipo;
		this.estado = estado;
		this.prioridad = prioridad;
	}
	 public String getCodEquipaje() {
		 return codEquipaje;
	 }
	 public void setCodEquipaje(String codEquipaje) {
		 this.codEquipaje = codEquipaje;
	 }
	 public String getCodReserva() {
		 return codReserva;
	 }
	 public void setCodReserva(String codReserva) {
		 this.codReserva = codReserva;
	 }
	 public double getPeso() {
		 return peso;
	 }
	 public void setPeso(double peso) {
		 this.peso = peso;
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
	 public String getPrioridad() {
		 return prioridad;
	 }
	 public void setPrioridad(String prioridad) {
		 this.prioridad = prioridad;
	 }
	 
	 
	 
	 @Override
	 public String toString() {
		 return "Equipaje " + codEquipaje + " (reserva " + codReserva + ")\n"
				 + "  Peso: " + peso + " kg | Tipo: " + tipo + "\n"
				 + "  Estado: " + estado + " | Prioridad: " + prioridad;
	 }
	 
	 
}
