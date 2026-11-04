package gestores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import modelo.Equipaje;
import modelo.Incidencia;
import modelo.Pasajero;
import modelo.Reserva;
import modelo.Vuelo;

public class InformesGestor {
	private PasajerosGestor gestorPasajeros;
	private VuelosGestor gestorVuelos;
	private ReservasGestor gestorReservas;
	private EmbarquesGestor gestorEmbarques;
	private EquipajesGestor gestorEquipajes;
	private IncidenciasGestor gestorIncidencias;
	private ConfiguracionGestor gestorConfiguracion;

	private static InformesGestor instancia;

	public static InformesGestor getInstance() {
		if (instancia == null) {
			instancia = new InformesGestor();
		}
		return instancia;
	}

	private InformesGestor() {
		this.gestorPasajeros = PasajerosGestor.getInstance();
		this.gestorVuelos = VuelosGestor.getInstance();
		this.gestorReservas = ReservasGestor.getInstance();
		this.gestorEmbarques = EmbarquesGestor.getInstance();
		this.gestorEquipajes = EquipajesGestor.getInstance();
		this.gestorIncidencias = IncidenciasGestor.getInstance();
		this.gestorConfiguracion = ConfiguracionGestor.getInstance();
	}

	public void ControlGeneral(Scanner sc) {
		System.out.println("========================================");
		System.out.println("¿Quieres generar un informe con la informacion de la cantidad total de pasajeros, reservas, vuelos, equipajes, embarques e incidencias?");
		System.out.println("Escoge: 1.Si - 2.Mostrar en consola");
		int opcion = Utilidades.leerOpcion(sc, 1, 2);
			if (opcion == 1) {
				crearInformeTotal();
			}
			else {
				imprimirCantidadTotal();
			}
		System.out.println("========================================");
		System.out.println("¿Quieres generar un informe de las reservas?");
		System.out.println("Escoge: 1.Si - 2.Mostrar en consola");
		opcion = Utilidades.leerOpcion(sc, 1, 2);
		if (opcion == 1) {
			crearInformeReservas();
		}
		else {
			imprimirReservas();
		}
		System.out.println("========================================");
		
		imprimirPorVuelos();
		
		imprimirAlertas();
		
		imprimirAvisosDePasajerosEnTierra(sc);
	}

	private void imprimirAvisosDePasajerosEnTierra(Scanner sc) {
		boolean nadieEnTierra = true;
		LocalDateTime fechaSimulacion = LocalDateTime.of(Utilidades.pedirFecha(sc, "Introduce la fecha para la simulacion"), Utilidades.pedirHora(sc, "Introduce la hora para la simulacion"));
		ArrayList<Reserva> reservasConfirmadas = gestorReservas.obtenerReservasConfirmadas();
		ArrayList<Reserva> reservasSinEmbarqueYConVuelo = gestorReservas.obtenerReservasSinEmbarqueYConVueloAsignado(reservasConfirmadas, gestorEmbarques);
	
		if (reservasSinEmbarqueYConVuelo.size() != 0) {
			System.out.println("PASAJEROS EN TIERRA:");
			for (Reserva reserva: reservasSinEmbarqueYConVuelo) {
				LocalDateTime cierre = gestorVuelos.calcularCierreEmbarque(gestorVuelos.buscarPorCodigo(reserva.getVuelo()));
				if (cierre != null && fechaSimulacion.isAfter(cierre) ) {
					System.out.println("  Reserva: " + reserva.getIdReserva() + " | Pasajero: " + reserva.getPasajero() 
					+ " | Vuelo: " + reserva.getVuelo() + " | HoraCierre: " + cierre.toLocalDate() + " " + cierre.toLocalTime());
					nadieEnTierra = false;
				}
			}
		}
		if (nadieEnTierra) {
			System.out.println("No hay ningun pasajero que se haya quedado en tierra.");
		}
	}

	private void imprimirAlertas() {
		System.out.println("========================================");
		System.out.println("--ALERTAS--");
		String vuelosString = "";
		String equipajesSobrePesoString = "";
		String equipajesPerdidosString = "";
		String incidenciasPrioAltaString = "";
		String incidenciasPrioCriticaString = "";
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo());
			int tamanio = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			if(tamanio < reservasDelVuelo.size()) {
				vuelosString += " " + vuelo.getCodigo();
			}
			for (Reserva reserva : reservasDelVuelo) {
				ArrayList<Equipaje> equipajesDeReserva = gestorEquipajes.buscarEquipajesPorReserva(reserva.getIdReserva());
				ArrayList<Equipaje> equipajesSobrepesoDeReserva = gestorEquipajes.filtrarArrayEquipajesPorExcesoDePeso(equipajesDeReserva);
				for(Equipaje equipaje : equipajesSobrepesoDeReserva) {
					equipajesSobrePesoString += " " + equipaje.getCodEquipaje(); 
				}
				ArrayList<Equipaje> equipajesPerdidosDeReserva = gestorEquipajes.filtrarArrayEquipajesPorPerdidos(equipajesDeReserva);
				for (Equipaje equipaje : equipajesPerdidosDeReserva) {
					equipajesPerdidosString += " " + equipaje.getCodEquipaje();
				}
			}
		}
		
		ArrayList<Incidencia> incidenciasPrioridadAlta = gestorIncidencias.buscarEnArrayAbiertas(gestorIncidencias.buscarPorPrioridad("ALTA")); 
		for (Incidencia incidencia : incidenciasPrioridadAlta) {
			incidenciasPrioAltaString += " " + incidencia.getCodigo();
		}
		ArrayList<Incidencia> incidenciasPrioridadCritica = gestorIncidencias.buscarEnArrayAbiertas(gestorIncidencias.buscarPorPrioridad("CRITICA"));
		for (Incidencia incidencia : incidenciasPrioridadCritica) {
			incidenciasPrioCriticaString += " " + incidencia.getCodigo();
		}
		
		System.out.println("Vuelos en sobreventa:" + vuelosString);
		System.out.println("Equipajes Perdidos:" + equipajesPerdidosString);
		System.out.println("Equipajes con SobrePeso:" + equipajesSobrePesoString);
		System.out.println("Incidencias Abiertas de Prioridad Alta:" + incidenciasPrioAltaString);
		System.out.println("Incidencias Abiertas de Prioridad Critica:" + incidenciasPrioCriticaString);
		System.out.println("========================================");
	}

	private void imprimirPorVuelos() {
		String [] estados = { "PROGRAMADO",
                "RETRASADO",
                "CANCELADO",
                "EMBARCANDO",
          		"CERRADO"};
		String [] nivelesPrioridad = {"BAJA", "MEDIA", "ALTA", "CRITICA"};
		HashMap<String, Integer> HashMapEstados = new HashMap<String, Integer>();
		System.out.println("========================================");
		System.out.println("VUELOS POR ESTADO OPERATIVO");
		for (String estado : estados) {
			HashMap<String, Integer> prioridades = new HashMap<String, Integer>();
			prioridades.put("BAJA", 0);
			prioridades.put("MEDIA", 0);
			prioridades.put("ALTA", 0);
			prioridades.put("CRITICA", 0);


			int totalReservas = 0;
			ArrayList<Vuelo> vuelosEstado = gestorVuelos.buscarVuelosPorEstado(estado);
			HashMapEstados.put(estado, vuelosEstado.size());
			for (Vuelo vuelo : vuelosEstado) {
				totalReservas+=gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
				ArrayList<Incidencia> incidenciasAbiertasDelVuelo = gestorIncidencias.buscarEnArrayAbiertas(gestorIncidencias.buscarIncidenciaPorVuelo(vuelo.getCodigo()));
				for (Incidencia incidencia : incidenciasAbiertasDelVuelo) {
					prioridades.put(incidencia.getPrioridad(), prioridades.get(incidencia.getPrioridad())+1);
				}
			}

			System.out.println("========================================");
			System.out.println(estado + ": " + HashMapEstados.get(estado) + " vuelos");
			if (vuelosEstado.size() != 0) {
				String codigos = "";
				for (Vuelo vuelo : vuelosEstado) {
					codigos += " " + vuelo.getCodigo();
				}
				System.out.println("   Vuelos:" + codigos);
				System.out.println("   Reservas confirmadas: " + totalReservas);
				System.out.println("   Incidencias abiertas por prioridad:");
				for (String nivel : nivelesPrioridad) {
					System.out.println("      " + nivel + ": " + prioridades.get(nivel));
				}
			}
		}
		System.out.println("========================================");
	}

	private void crearInformeReservas() {
		new File("informes").mkdir();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("informes/desgloseReservas.csv")))) {
			writer.write(generarStringParaReservas());
			System.out.println("Informe de las reservas creado en informes/desgloseReservas.csv.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	private void imprimirReservas() {
		String[] tiposVuelo = {"NACIONAL", "INTERNACIONAL"};
		String[] criterios = {"ESTADO", "CLASE", "CANAL"};
		System.out.println("========================================");
		System.out.println("DESGLOSE DE RESERVAS");
		for (String tipo : tiposVuelo) {
			ArrayList<Reserva> reservasTipo = gestorReservas.buscarReservasPorNacionalOInternacional(tipo);
			System.out.println("========================================");
			System.out.println("Vuelos " + tipo + ": " + reservasTipo.size() + " reservas");
			for (String criterio : criterios) {
				System.out.println("----------------------------------------");
				System.out.println("Por " + criterio.toLowerCase() + ":");
				HashMap<String, Integer> cantidades = contarReservasPor(reservasTipo, criterio);
				for (Map.Entry<String, Integer> k : cantidades.entrySet()) {
					System.out.println("   " + k.getKey() + ": " + k.getValue());
				}
			}
		}
		
	}

	private String generarStringParaReservas() {
		String[] tiposVuelo = {"NACIONAL", "INTERNACIONAL"};
		String[] criterios = {"ESTADO", "CLASE", "CANAL"};
		String texto = "";
		for (String tipo : tiposVuelo) {
			ArrayList<Reserva> reservasTipo = gestorReservas.buscarReservasPorNacionalOInternacional(tipo);
			texto += tipo + ",TOTAL,RESERVAS," + reservasTipo.size() + "\n";
			for (String criterio : criterios) {
				HashMap<String, Integer> cantidades = contarReservasPor(reservasTipo, criterio);
				for (Map.Entry<String, Integer> k : cantidades.entrySet()) {
					texto += tipo + "," + criterio + "," + k.getKey() + "," + k.getValue() + "\n";
				}
			}
		}
		return texto;
	}
	
	private HashMap<String, Double> sumarImportesPor(ArrayList<Reserva> reservas, String criterio) {
		HashMap<String, Double> importes = new HashMap<String, Double>();
		for (Reserva reserva : reservas) {
			String clave;
			if (criterio.equals("CANAL")) {
				clave = reserva.getCanal();
			}
			else if (criterio.equals("CLASE")) {
				clave = reserva.getClase();
			}
			else {
				clave = gestorVuelos.buscarPorCodigo(reserva.getVuelo()).getDestino();
			}
			if (importes.containsKey(clave)) {
				importes.put(clave, importes.get(clave) + reserva.getPrecio());
			}
			else {
				importes.put(clave, reserva.getPrecio());
			}
		}
		return importes;
	}

	private HashMap<String, Integer> contarReservasPor(ArrayList<Reserva> reservas, String criterio) {
		HashMap<String, Integer> cantidades = new HashMap<String, Integer>();
		for (Reserva reserva : reservas) {
			String clave;
			if (criterio.equals("ESTADO")) {
				clave = reserva.getEstado();
			}
			else if (criterio.equals("CLASE")) {
				clave = reserva.getClase();
			}
			else {
				clave = reserva.getCanal();
			}
			if (cantidades.containsKey(clave)) {
				cantidades.put(clave, cantidades.get(clave) + 1);
			}
			else {
				cantidades.put(clave, 1);
			}
		}
		return cantidades;
	}

	private void imprimirCantidadTotal() {
		String texto = generarStringParaCantidadTotal();
		texto = texto.replace(",", "\n");
		System.out.println(texto);
		
	}

	private void crearInformeTotal() {
		new File("informes").mkdir();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("informes/total.csv")))) {
			writer.write(generarStringParaCantidadTotal());
			System.out.println("Informe de la cantidad total de objetos creados en informes/total.csv.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private String generarStringParaCantidadTotal() {
		return "pasajeros: "+ gestorPasajeros.getPasajeros().size() + ",reservas: " + gestorReservas.getReservas().size() + ",vuelos: " + gestorVuelos.getVuelos().size()
		+ ",equipajes: " + gestorEquipajes.getEquipajes().size() + ",embarques: " + gestorEmbarques.getEmbarques().size() + ",incidencias: " + gestorIncidencias.getIncidencias().size();
	}

	public void vuelosEmbarquesYReservas() {
		imprimirVuelosPorCantidad();
		imprimirDesgloseReservasPorEmbarque();
		imprimirVuelosRetrasados();
		imprimirDesgloseReservas();
		
	}

	private void imprimirDesgloseReservas() {
		String [] criterios = { "CANAL","CLASE"};
		ArrayList<Reserva> reservas = gestorReservas.getReservas();
		
		System.out.println("====================================");
		for (String criterio : criterios) {
			System.out.println("-----------------------------------");
			System.out.println("Filtrar por Criterio " + criterio);
			HashMap<String, Integer> cantidades = contarReservasPor(reservas, criterio);
			HashMap<String, Double> importes = sumarImportesPor(reservas, criterio);
			for (Map.Entry<String, Integer> k : cantidades.entrySet()) {
				System.out.println("  " + k.getKey() + ": " + k.getValue() + " reservas | Importe: " + String.format("%.2f", importes.get(k.getKey())) + " EUR");
			}
		}
		System.out.println("-----------------------------------");
		System.out.println("Filtrar por Criterio RUTAS");
		HashMap<String,Integer> cantidades = contarReservasPorRuta(reservas);
		HashMap<String, Double> importesRuta = sumarImportesPor(reservas, "RUTA");
		for(Map.Entry<String,Integer> k : cantidades.entrySet()) {
			if (k.getValue() != 0) {
				System.out.println("  " + k.getKey() + ": " + k.getValue() + " reservas | Importe: " + String.format("%.2f", importesRuta.get(k.getKey())) + " EUR");
			}
		}
		System.out.println("-----------------------------------");
		System.out.println("Numero De Reservas: " + reservas.size());
		System.out.println("Importe generado: " + String.format("%.2f", gestorReservas.obtenerGastoTotalDeLasReservas(reservas)) + " EUR");
		System.out.println("====================================");
	}
	

	private HashMap<String, Integer> contarReservasPorRuta(ArrayList<Reserva> reservas) {
		HashMap<String, Integer> cantidades = new HashMap<String, Integer>();
		ArrayList<String> rutas = gestorVuelos.obtenerRutas();
		for (String ruta : rutas) {
			cantidades.put(ruta, 0);
		}
		for  (Reserva reserva : reservas) {
			Vuelo vuelo = gestorVuelos.buscarPorCodigo(reserva.getVuelo());
			String destinoVuelo = vuelo.getDestino();
			cantidades.put(destinoVuelo, (cantidades.get(destinoVuelo) +1));
		}
		return cantidades;
	}

	private void imprimirVuelosRetrasados() {
		System.out.println("====================================");
		System.out.println("--Vuelos Retrasados--");
		ArrayList<Vuelo> vuelosRetrasados = gestorVuelos.buscarVuelosPorEstado("RETRASADO");
		String vuelosRetrasadosString = "";
		double totalRetraso = 0;
		ArrayList<Incidencia> incidenciasConRetraso = gestorIncidencias.buscarPorTipo("RETRASO");
		for (Vuelo vuelo : vuelosRetrasados) {
			vuelosRetrasadosString += " " + vuelo.getCodigo();
		}
		for (Incidencia incidencia : incidenciasConRetraso) {
			totalRetraso += incidencia.getDuracion();
		}
		System.out.println("Vuelos Retrasados:" + vuelosRetrasadosString);
		System.out.println("Duracion Total De los Retrasos : " + ((int)totalRetraso) + " minutos.");
		if (incidenciasConRetraso.size() != 0) {
			System.out.println("Duracion media de los retrasos: " + String.format("%.1f minutos.", (totalRetraso/incidenciasConRetraso.size())));
		}
		else {
			System.out.println("No hay incidencias de tipo RETRASO.");
		}
		System.out.println("====================================");
		
		
	}

	private void imprimirDesgloseReservasPorEmbarque() {
		System.out.println("====================================");
		System.out.println("--DESGLOSE DE RESERVAS POR ESTADO DEL EMBARQUE--");
		ArrayList<Reserva> reservas = gestorReservas.obtenerReservasConfirmadas();
		int numReservasEmbarcadas = gestorReservas.filtrarReservasVueloPorEstado(reservas, "EMBARCADO", gestorEmbarques).size();
		int numReservasEmbarcando = gestorReservas.filtrarReservasVueloPorEstado(reservas, "EMBARCANDO", gestorEmbarques).size();
		System.out.println("Reservas Embarcadas: " + numReservasEmbarcadas);
		System.out.println("Reservas Embarcando: " + numReservasEmbarcando);
		System.out.println("Reservas No_Embarcadas: " + gestorReservas.filtrarReservasVueloPorEstado(reservas, "NO_EMBARCADO", gestorEmbarques).size());
		System.out.println("Reservas Pendientes: " + gestorReservas.filtrarReservasVueloPorEstado(reservas, "PENDIENTE",gestorEmbarques).size());
		System.out.println("Porcentaje De Embarque: " + (numReservasEmbarcadas+numReservasEmbarcando) + " / " + reservas.size() 
		+ " (" + String.format("%.1f%%", ((numReservasEmbarcadas+numReservasEmbarcando) * 100.0 / reservas.size())) + ")");
	}

	private void imprimirVuelosPorCantidad() {
		HashMap<Vuelo, Double> ocupaciones = new HashMap<Vuelo, Double>();
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			int tamanioAvion = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int numReservasConfirmadas = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			ocupaciones.put(vuelo, numReservasConfirmadas * 100.0 / tamanioAvion);
		}
		ArrayList<Double> porcentajes = new ArrayList<Double>(new HashSet<>(ocupaciones.values()));
		Collections.sort(porcentajes);
		Collections.reverse(porcentajes);

		Vuelo vueloMasSobreventa = null;
		Vuelo vueloMasLibres = null;
		int menosPlazasLibres = 0;
		int masPlazasLibres = 0;
		for (Double porcentaje : porcentajes) {
			for (Map.Entry<Vuelo, Double> k : ocupaciones.entrySet()) {
				if (k.getValue().equals(porcentaje)) {
					Vuelo vuelo = k.getKey();
					int tamanioAvion = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
					int numReservasConfirmadas = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
					int plazasLibres = tamanioAvion - numReservasConfirmadas;
					System.out.println("------------------------------------");
					System.out.println("Vuelo: " + vuelo.getCodigo() + " | Capacidad: " 
						+ tamanioAvion + " | ReservasConfirmadas: " + numReservasConfirmadas
						+ " | ReservasCanceladas: " + gestorReservas.buscarResevaCanceladasPorVuelo(vuelo.getCodigo()).size() + " | PlazasDisponibles: " + plazasLibres
						+ " | PorcentajeOcupacion:" + String.format("%.1f%%", porcentaje));
					if (vueloMasSobreventa == null || plazasLibres < menosPlazasLibres) {
						vueloMasSobreventa = vuelo;
						menosPlazasLibres = plazasLibres;
					}
					if (vueloMasLibres == null || plazasLibres > masPlazasLibres) {
						vueloMasLibres = vuelo;
						masPlazasLibres = plazasLibres;
					}
				}
			}
		}
		System.out.println("====================================");
		if (menosPlazasLibres < 0) {
			System.out.println("Vuelo con mas sobreventa: " + vueloMasSobreventa.getCodigo() + " (" + (-menosPlazasLibres) + " reservas por encima de la capacidad)");
		}
		else {
			System.out.println("Ningun vuelo tiene sobreventa.");
		}
		System.out.println("Vuelo con mas plazas libres: " + vueloMasLibres.getCodigo() + " (" + masPlazasLibres + " plazas)");
		System.out.println("====================================");
	}


	public void pasajerosYReservas(Scanner sc) {
		imprimirPasajerosConMasReservas();
		imprimirPasajerosConReservasCanceladas();
		imprimirPasajerosConReservasConfirmadasEnVueloCancelado();
		imprimirPasajerosVipYMenorPorVuelo();
		imprimirReservasPendientesYNoEmbarcadas();
		imprimirReservasPendientesProximasACierre(sc);
		imprimirPorcentajeCancelacionEnVueloYCanal();
		
		
	}

	private void imprimirPorcentajeCancelacionEnVueloYCanal() {
		System.out.println("====================================");
		System.out.println("Porcentajes Cancelacion Por Vuelo:");
		ArrayList<Vuelo> vuelos = gestorVuelos.getVuelos();
		for (Vuelo vuelo : vuelos) {
			System.out.println("-----------------------------------");
			String vueloString = vuelo.getCodigo();
			int reservasVuelo = gestorReservas.buscarResevasPorVuelo(vueloString).size();
			int reservasCanceladasVuelo = gestorReservas.buscarResevaCanceladasPorVuelo(vueloString).size();
			System.out.println("Vuelo: " + vueloString);
			System.out.println("Tasa Cancelacion: " + reservasCanceladasVuelo + " / " + reservasVuelo +
					" (" + String.format("%.1f%%)", (reservasCanceladasVuelo*100.0 /reservasVuelo)));
			System.out.println("-----------------------------------");
		}
		System.out.println("====================================");
		System.out.println("====================================");
		System.out.println("Porcentajes Cancelacion Por Canal:");
		String [] canales = {"WEB",
                "APP",
                "TAQUILLA",
                "AGENCIA"
                };
		for(String canal : canales) {
			int reservasCanal = gestorReservas.obtenerReservasPorCanal(canal).size();
			int reservasCanceladasCanal = gestorReservas.obtenerReservasCanceladasPorCanal(canal).size();
			System.out.println("Canal: " + canal);
			System.out.println("Tasa Cancelacion: " + reservasCanceladasCanal + " / " + reservasCanal +
					" (" + String.format("%.1f%%)", (reservasCanceladasCanal * 100.0 /reservasCanal)));
			System.out.println("-----------------------------------");
		}
		
	}

	private void imprimirReservasPendientesProximasACierre(Scanner sc) {

		int margenMinutos = 10;
		boolean ningunaProxima = true;
		System.out.println("====================================");
		LocalDateTime fechaSimulacion = LocalDateTime.of(Utilidades.pedirFecha(sc, "Introduce la fecha para la simulacion"), Utilidades.pedirHora(sc, "Introduce la hora para la simulacion"));
		ArrayList<Reserva> reservasConfirmadas = gestorReservas.obtenerReservasConfirmadas();
		ArrayList<Reserva> reservasSinEmbarqueYConVuelo = gestorReservas.obtenerReservasSinEmbarqueYConVueloAsignado(reservasConfirmadas, gestorEmbarques);
		System.out.println("RESERVAS PENDIENTES PROXIMAS AL CIERRE (" + margenMinutos + " min o menos):");
		for (Reserva reserva : reservasSinEmbarqueYConVuelo) {
			LocalDateTime cierre = gestorVuelos.calcularCierreEmbarque(gestorVuelos.buscarPorCodigo(reserva.getVuelo()));
			if (cierre != null) {
				long minutosRestantes = ChronoUnit.MINUTES.between(fechaSimulacion, cierre);
				if (minutosRestantes >= 0 && minutosRestantes <= margenMinutos) {
					System.out.println("  Reserva: " + reserva.getIdReserva() + " | Pasajero: " + reserva.getPasajero()
					+ " | Vuelo: " + reserva.getVuelo() + " | HoraCierre: " + cierre.toLocalDate() + " " + cierre.toLocalTime()
					+ " | Quedan: " + minutosRestantes + " min");
					ningunaProxima = false;
				}
			}
		}
		if (ningunaProxima) {
			System.out.println("No hay reservas pendientes proximas al cierre de la ventana.");
		}
		System.out.println("====================================");
	}

	private void imprimirReservasPendientesYNoEmbarcadas() {
		ArrayList<Reserva> reservas = gestorReservas.obtenerReservasConfirmadas();
		System.out.println("====================================");
		ArrayList<Reserva> reservasPendientes = gestorReservas.filtrarReservasVueloPorEstado(reservas, "PENDIENTE", gestorEmbarques);
		System.out.println("Reservas Confirmadas Pendientes de Embarque:");
		for (Reserva reserva : reservasPendientes) {
			System.out.println("   IdReserva: " + reserva.getIdReserva() + " | Pasajero: " + reserva.getPasajero() + " | Vuelo: " + reserva.getVuelo());
		}
		System.out.println("------------------------------------");
		System.out.println("Reservas Confirmadas Embarcando:");
		ArrayList<Reserva> reservasEmbarcando = gestorReservas.filtrarReservasVueloPorEstado(reservas, "EMBARCANDO", gestorEmbarques);
		for (Reserva reserva : reservasEmbarcando) {
			System.out.println("   IdReserva: " + reserva.getIdReserva() + " | Pasajero: " + reserva.getPasajero() + " | Vuelo: " + reserva.getVuelo());
		}
		System.out.println("------------------------------------");
		System.out.println("Reservas Confirmadas No Embarcadas:");
		ArrayList<Reserva> reservasNoEmbarcadas = gestorReservas.filtrarReservasVueloPorEstado(reservas, "NO_EMBARCADO", gestorEmbarques);
		for (Reserva reserva : reservasNoEmbarcadas) {
			System.out.println("   IdReserva: " + reserva.getIdReserva() + " | Pasajero: " + reserva.getPasajero() + " | Vuelo: " + reserva.getVuelo());
		}
		System.out.println("====================================");
	}

	private void imprimirPasajerosVipYMenorPorVuelo() {
		System.out.println("====================================");
		System.out.println("--Numero pasajeros VIPs Y menores por Vuelo--");
		ArrayList<Vuelo> vuelos = gestorVuelos.getVuelos();
		for (Vuelo vuelo : vuelos) {
			System.out.println("-----------------------------");
			System.out.println("Vuelo: " + vuelo.getCodigo());
			System.out.println("  Cantidad VIPs: " + gestorPasajeros.obtenerPasajerosConfirmadosPorTipoDelVuelo(vuelo, "VIP", gestorReservas).size());
			System.out.println("  Cantidad Menores: " + gestorPasajeros.obtenerPasajerosConfirmadosPorTipoDelVuelo(vuelo, "MENOR", gestorReservas).size());
			
		}
		System.out.println("====================================");
		
	}

	private void imprimirPasajerosConReservasConfirmadasEnVueloCancelado() {
		System.out.println("====================================");
		HashSet<Pasajero> pasajerosConReservaConfirmadaEnVueloCancelado = 
				gestorPasajeros.obtenerPasajerosConReservaConfirmadaEnVueloCancelado(gestorVuelos, gestorReservas);
		String texto = "";
		Iterator<Pasajero> iterator = pasajerosConReservaConfirmadaEnVueloCancelado.iterator();
		while (iterator.hasNext()) {
			Pasajero pasajero = iterator.next();
			texto += " " + pasajero.getCodPasajero();
		}
		System.out.println("Pasajeros con Reservas Confirmadas en vuelo cancelado:" + texto );
		System.out.println("====================================");
		
	}

	private void imprimirPasajerosConMasReservas() {
		System.out.println("====================================");
		HashMap<Pasajero, ArrayList<Reserva>> pasajerosYReservas = gestorPasajeros.ObtenerPasajerosConSusReservas(gestorReservas);
		HashSet<Integer> numReservasDePasajerosHashSet = new HashSet<Integer>();
				for (Map.Entry<Pasajero, ArrayList<Reserva>> k : pasajerosYReservas.entrySet()) {
					numReservasDePasajerosHashSet.add(k.getValue().size());
				}
		ArrayList<Integer> numReservasDePasajeros = new ArrayList<Integer>(numReservasDePasajerosHashSet);
				Collections.sort(numReservasDePasajeros);
				Collections.reverse(numReservasDePasajeros);
				System.out.println("Pasajeros con mas reservas:");
				for (Map.Entry<Pasajero, ArrayList<Reserva>> k : pasajerosYReservas.entrySet()) {
					if (numReservasDePasajeros.getFirst() == k.getValue().size()) {
						System.out.println("   " + k.getKey().getCodPasajero() + " | Numero Reservas: " + k.getValue().size());
					}
				}
		System.out.println("====================================");
	}
	
	private void imprimirPasajerosConReservasCanceladas() {
		System.out.println("====================================");
		HashSet<Pasajero> pasajerosConReservasCanceladas = gestorPasajeros.obtenerPasajerosConReservaCancelada(gestorReservas);
		String texto = "";
		Iterator<Pasajero> iterator = pasajerosConReservasCanceladas.iterator();
		while (iterator.hasNext()) {
			Pasajero pasajero = iterator.next();
			texto += " " + pasajero.getCodPasajero();
		}
		System.out.println("Pasajeros con Reservas Canceladas:" + texto );
		System.out.println("====================================");
	}

}
