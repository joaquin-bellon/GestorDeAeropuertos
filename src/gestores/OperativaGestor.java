package gestores;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import modelo.Embarque;
import modelo.Equipaje;
import modelo.Incidencia;
import modelo.Pasajero;
import modelo.Reserva;
import modelo.Vuelo;

public class OperativaGestor {
	private PasajerosGestor gestorPasajeros;
	private VuelosGestor gestorVuelos;
	private ReservasGestor gestorReservas;
	private EmbarquesGestor gestorEmbarques;
	private EquipajesGestor gestorEquipajes;
	private IncidenciasGestor gestorIncidencias;
	private ConfiguracionGestor gestorConfiguracion;
	public OperativaGestor(PasajerosGestor gestorPasajeros, VuelosGestor gestorVuelos, ReservasGestor gestorReservas,
			EmbarquesGestor gestorEmbarques, EquipajesGestor gestorEquipajes, IncidenciasGestor gestorIncidencias,
			ConfiguracionGestor gestorConfiguracion) {
		this.gestorPasajeros = gestorPasajeros;
		this.gestorVuelos = gestorVuelos;
		this.gestorReservas = gestorReservas;
		this.gestorEmbarques = gestorEmbarques;
		this.gestorEquipajes = gestorEquipajes;
		this.gestorIncidencias = gestorIncidencias;
		this.gestorConfiguracion = gestorConfiguracion;
	}
	public void comprobarOcupacionDelVuelo(Scanner sc) {
		Vuelo vuelo = gestorVuelos.pedirVuelo(sc, "a comprobar");
		int plazas = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
		ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo());

		if (plazas < reservasDelVuelo.size()) {
			System.out.println("Hay sobreventa de plazas para el vuelo. "
					+ "\nSe han contado las siguientes reservas:");
			for (Reserva reserva : reservasDelVuelo) {
				System.out.println("  ID:" + reserva.getIdReserva() + "  -  Pasajero: " + reserva.getPasajero() + "  -  Asiento:" + reserva.getAsiento());
			}
		}
		else  {
			System.out.println("Todo esta en orden");
		}
		   System.out.printf("Reservas confirmadas: %d / Capacidad: %d (%.1f%%)%n", reservasDelVuelo.size(), plazas, reservasDelVuelo.size() * 100.0 / plazas);
	}

	public void comprobarCheckIn(Scanner sc) {
		Reserva reserva = gestorReservas.pedirReserva(sc, "a hacer check-in");
		if (reserva.getEstado().equals("CONFIRMADA")) {
			LocalDateTime fecha = LocalDateTime.of(Utilidades.pedirFecha(sc, "Introduce tu fecha de llegada"),
					Utilidades.pedirHora(sc, "Introduce tu hora de llegada"));
			Vuelo vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
			if (vuelo.getEstado().equals("CANCELADO")) {
				System.out.println("El vuelo ha sido cancelado.");
			}
			else {
				String tipoVuelo = gestorVuelos.comprobarVueloNacionaleInternacional(vuelo);
				LocalDateTime apertura = gestorVuelos.calcularAperturaEmbarque(vuelo);
				LocalDateTime cierre = gestorVuelos.calcularCierreEmbarque(vuelo);
				if (cierre == null) {
					System.out.println("No hay ventana configurada para vuelos de tipo " + tipoVuelo + ".");
				}
				else {
					System.out.println("----------------------------------------");
					System.out.println("Vuelo " + vuelo.getCodigo() + " (" + tipoVuelo + ") - Salida: "
							+ vuelo.getSalida().toLocalDate() + " " + vuelo.getSalida().toLocalTime());
					System.out.println("Ventana de check-in: de " + apertura.toLocalTime() + " a " + cierre.toLocalTime()
							+ " del " + apertura.toLocalDate());
					System.out.println("----------------------------------------");
					if (fecha.isBefore(apertura)) {
						System.out.println("Aun es pronto. El check-in se abrira en "
								+ ChronoUnit.MINUTES.between(fecha, apertura) + " minutos.");
					}
					else if (fecha.isAfter(cierre)) {
						System.out.println("La ventana de check-in esta cerrada.");
					}
					else {
						System.out.println("El check-in se puede iniciar.");
					}
				}
			}
		}
		else {
			System.out.println("La reserva no esta confirmada.");
		}

	}

	public void analizarImpactoIncidencia(Scanner sc) {
		Incidencia incidencia = gestorIncidencias.pedirIncidencia(sc, "a analizar su impacto");
		gestorIncidencias.imprimirImpactoIncidencia(incidencia);
		ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevasPorVuelo(incidencia.getVuelo());
		if (reservasDelVuelo.size() == 0) {
			System.out.println("El vuelo no tiene reservas. La incidencia no afecta a ningun pasajero.");
		}
		else {
			int confirmadas = 0;
			int totalEquipajes = 0;
			int totalEmbarques = 0;
			System.out.println("RESERVAS DEL VUELO " + incidencia.getVuelo() + ":");
			for (Reserva reserva : reservasDelVuelo) {
				System.out.println("----------------------------------------");
				System.out.println(reserva);
				if (reserva.getEstado().equals("CONFIRMADA")) {
					confirmadas++;
				}

				Pasajero pasajero = gestorPasajeros.buscarPorCodPasajero(reserva.getPasajero());
				if (pasajero != null) {
					System.out.println(pasajero);
				}
				else {
					System.out.println("Pasajero " + reserva.getPasajero() + " no encontrado");
				}

				ArrayList<Equipaje> equipajesDeLaReserva = gestorEquipajes.buscarEquipajesPorReserva(reserva.getIdReserva());
				if (equipajesDeLaReserva.size() != 0) {
					System.out.println("Equipajes de la reserva:");
					for (Equipaje equipaje : equipajesDeLaReserva) {
						System.out.println("   " + equipaje);
					}
					totalEquipajes += equipajesDeLaReserva.size();
				}
				else {
					System.out.println("Sin equipajes registrados.");
				}

				Embarque embarque = gestorEmbarques.buscarPorReserva(reserva.getIdReserva());
				if (embarque != null) {
					System.out.println(embarque);
					totalEmbarques++;
				}
				else if (reserva.getEstado().equals("CONFIRMADA")) {
					System.out.println("Embarque: PENDIENTE (sin registro)");
				}
				else {
					System.out.println("Embarque: sin registro");
				}
			}
			System.out.println("========================================");
			System.out.println("RESUMEN DEL IMPACTO");
			System.out.println("  Reservas del vuelo: " + reservasDelVuelo.size() + " (confirmadas: " + confirmadas + ")");
			System.out.println("  Equipajes asociados: " + totalEquipajes);
			System.out.println("  Registros de embarque: " + totalEmbarques);
			System.out.println("========================================");
		}
	}

}
