package gestores;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import modelo.Equipaje;
import modelo.Incidencia;
import modelo.Reserva;
import modelo.Vuelo;
import utilidades.Constantes;
import vista.InformesPanel;

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

	public ArrayList<Vuelo> buscarVuelosEnSobreventa() {
		ArrayList<Vuelo> vuelosEnSobreventa = new ArrayList<Vuelo>();
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			ArrayList<Reserva> reservasDelVuelo = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo());
			int tamanio = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			if (tamanio < reservasDelVuelo.size()) {
				vuelosEnSobreventa.add(vuelo);
			}
		}
		return vuelosEnSobreventa;
	}

	public ArrayList<Equipaje> buscarEquipajesPerdidos() {
		return gestorEquipajes.filtrarArrayEquipajesPorPerdidos(buscarEquipajesDeReservasConfirmadas());
	}

	public ArrayList<Equipaje> buscarEquipajesConSobrepeso() {
		return gestorEquipajes.filtrarArrayEquipajesPorExcesoDePeso(buscarEquipajesDeReservasConfirmadas());
	}

	public ArrayList<Incidencia> buscarIncidenciasAbiertasPorPrioridad(String prioridad) {
		return gestorIncidencias.buscarEnArrayAbiertas(gestorIncidencias.buscarPorPrioridad(prioridad));
	}

	private ArrayList<Equipaje> buscarEquipajesDeReservasConfirmadas() {
		ArrayList<Equipaje> equipajes = new ArrayList<Equipaje>();
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			for (Reserva reserva : gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo())) {
				equipajes.addAll(gestorEquipajes.buscarEquipajesPorReserva(reserva.getIdReserva()));
			}
		}
		return equipajes;
	}

	public LinkedHashMap<String, Integer> contarVuelosPorEstado() {
		LinkedHashMap<String, Integer> vuelosPorEstado = new LinkedHashMap<String, Integer>();
		for (String estado : Constantes.ESTADOS_VUELO) {
			vuelosPorEstado.put(estado, gestorVuelos.buscarVuelosPorEstado(estado).size());
		}
		return vuelosPorEstado;
	}

	public int contarReservasConfirmadas(ArrayList<Vuelo> vuelos) {
		int totalReservas = 0;
		for (Vuelo vuelo : vuelos) {
			totalReservas += gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
		}
		return totalReservas;
	}

	public LinkedHashMap<String, Integer> contarIncidenciasAbiertasPorPrioridad(ArrayList<Vuelo> vuelos) {
		LinkedHashMap<String, Integer> prioridades = new LinkedHashMap<String, Integer>();
		for (String nivel : Constantes.PRIORIDADES_INCIDENCIA) {
			prioridades.put(nivel, 0);
		}
		for (Vuelo vuelo : vuelos) {
			ArrayList<Incidencia> incidenciasAbiertasDelVuelo = gestorIncidencias.buscarEnArrayAbiertas(gestorIncidencias.buscarIncidenciaPorVuelo(vuelo.getCodigo()));
			for (Incidencia incidencia : incidenciasAbiertasDelVuelo) {
				prioridades.put(incidencia.getPrioridad(), prioridades.get(incidencia.getPrioridad()) + 1);
			}
		}
		return prioridades;
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
		ArrayList<Vuelo> vuelosRetrasados = gestorVuelos.buscarVuelosPorEstado(Constantes.VUELO_RETRASADO);
		String vuelosRetrasadosString = "";
		double totalRetraso = 0;
		ArrayList<Incidencia> incidenciasConRetraso = gestorIncidencias.buscarPorTipo(Constantes.TIPO_INCIDENCIA_RETRASO);
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
		int numReservasEmbarcadas = gestorReservas.filtrarReservasVueloPorEstado(reservas, Constantes.EMBARQUE_EMBARCADO, gestorEmbarques).size();
		int numReservasEmbarcando = gestorReservas.filtrarReservasVueloPorEstado(reservas, Constantes.EMBARQUE_EMBARCANDO, gestorEmbarques).size();
		System.out.println("Reservas Embarcadas: " + numReservasEmbarcadas);
		System.out.println("Reservas Embarcando: " + numReservasEmbarcando);
		System.out.println("Reservas No_Embarcadas: " + gestorReservas.filtrarReservasVueloPorEstado(reservas, Constantes.EMBARQUE_NO_EMBARCADO, gestorEmbarques).size());
		System.out.println("Reservas Pendientes: " + gestorReservas.filtrarReservasVueloPorEstado(reservas, Constantes.EMBARQUE_PENDIENTE,gestorEmbarques).size());
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

	public LinkedHashMap<String, Integer> obtenerLinkedHashMapIncidenciasAbieretasPorPrioridad() {
		LinkedHashMap<String, Integer> incidenciasAbiertasPorPrioridad = new LinkedHashMap<String, Integer>();
		for (String prioridad : Constantes.PRIORIDADES_INCIDENCIA) {
			incidenciasAbiertasPorPrioridad.put(prioridad, buscarIncidenciasAbiertasPorPrioridad(prioridad).size());
		}
		return incidenciasAbiertasPorPrioridad;
	}
	
	public LinkedHashMap<String, ArrayList<Integer>> obtenerOcupacionDeLosVuelos () {
		LinkedHashMap<String, ArrayList<Integer>> vuelosOcupacion = new LinkedHashMap<String, ArrayList<Integer>>();
		
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			int tamanioVuelo = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int numeroReservas = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			int plazasLibres = tamanioVuelo - numeroReservas;
			vuelosOcupacion.put(vuelo.getCodigo(), new ArrayList<Integer>(List.of(plazasLibres,numeroReservas)));
			
		}
		
		
		
		return vuelosOcupacion;
		
	}

	public String exportarTotalesCsv(String ruta) {
		String texto = "";
		texto += "Pasajeros," + gestorPasajeros.getPasajeros().size() + "\n";
		texto += "Vuelos," + gestorVuelos.getVuelos().size() + "\n";
		texto += "Reservas," + gestorReservas.getReservas().size() + "\n";
		texto += "Equipajes," + gestorEquipajes.getEquipajes().size() + "\n";
		texto += "Embarques," + gestorEmbarques.getEmbarques().size() + "\n";
		texto += "Incidencias," + gestorIncidencias.getIncidencias().size() + "\n";
		return escribirInforme(ruta, texto);
	}

	public String exportarOcupacionCsv(String ruta) {
		String texto = "";
		for (Vuelo vuelo : gestorVuelos.getVuelos()) {
			int capacidad = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int reservas = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			int porcentaje = reservas * 100 / capacidad;
			texto += vuelo.getCodigo() + "," + capacidad + "," + reservas + "," + (capacidad - reservas) + "," + porcentaje + "%\n";
		}
		return escribirInforme(ruta, texto);
	}

	public String exportarAlertasCsv(String ruta) {
		String texto = "";
		for (Vuelo vuelo : buscarVuelosEnSobreventa()) {
			int capacidad = gestorVuelos.recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
			int reservas = gestorReservas.buscarResevaConfirmadasPorVuelo(vuelo.getCodigo()).size();
			texto += "Sobreventa," + vuelo.getCodigo() + ",\"" + reservas + " reservas de " + capacidad + "\"\n";
		}
		for (Equipaje equipaje : buscarEquipajesPerdidos()) {
			texto += "Equipaje perdido," + equipaje.getCodEquipaje() + ",\"Reserva " + equipaje.getCodReserva() + "\"\n";
		}
		for (Equipaje equipaje : buscarEquipajesConSobrepeso()) {
			texto += "Exceso de peso," + equipaje.getCodEquipaje() + ",\"" + equipaje.getTipo() + ", " + equipaje.getPeso() + " kg\"\n";
		}
		for (Incidencia incidencia : buscarIncidenciasAbiertasPorPrioridad(Constantes.PRIORIDAD_CRITICA)) {
			texto += "Incidencia CRITICA," + incidencia.getCodigo() + ",\"Vuelo " + incidencia.getVuelo() + "\"\n";
		}
		for (Incidencia incidencia : buscarIncidenciasAbiertasPorPrioridad(Constantes.PRIORIDAD_ALTA)) {
			texto += "Incidencia ALTA," + incidencia.getCodigo() + ",\"Vuelo " + incidencia.getVuelo() + "\"\n";
		}
		return escribirInforme(ruta, texto);
	}

	private String escribirInforme(String ruta, String texto) {
		new File(ruta).getParentFile().mkdirs();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ruta)))) {
			writer.write(texto);
			return "";
		} catch (IOException e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
	}

	private Document abrirDocumentoPdf(String ruta, String titulo) throws IOException {
		new File(ruta).getParentFile().mkdirs();
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(ruta));
		document.open();

		Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
		String fecha = LocalDateTime.now().format(Constantes.FORMATO_FECHA_TITULO);
		document.add(new Paragraph(titulo + " a " + fecha, tituloFont));
		document.add(new Paragraph("\n"));
		return document;
	}

	private Image crearImagenPdf(BufferedImage imagen, float ancho, float alto) throws IOException {
		Image imagenPdf = Image.getInstance(imagen, null);
		imagenPdf.scaleToFit(ancho, alto);
		imagenPdf.setAlignment(Image.MIDDLE);
		return imagenPdf;
	}

	public String exportarTotalesPdf(String ruta) {
		try {
			Document document = abrirDocumentoPdf(ruta, "Datos totales del Aeropuerto");
			Font cuerpoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

			document.add(new Paragraph("- Pasajeros: " + gestorPasajeros.getPasajeros().size(), cuerpoFont));
			document.add(new Paragraph("- Vuelos: " + gestorVuelos.getVuelos().size(), cuerpoFont));
			document.add(new Paragraph("- Reservas: " + gestorReservas.getReservas().size(), cuerpoFont));
			document.add(new Paragraph("- Equipajes: " + gestorEquipajes.getEquipajes().size(), cuerpoFont));
			document.add(new Paragraph("- Embarques: " + gestorEmbarques.getEmbarques().size(), cuerpoFont));
			document.add(new Paragraph("- Incidencias: " + gestorIncidencias.getIncidencias().size(), cuerpoFont));

			document.close();
		} catch (Exception e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
		return "";
	}

	public String exportarOcupacionPdf(String ruta, InformesPanel informesPanel) {
		try {
			Document document = abrirDocumentoPdf(ruta, "Datos de ocupación del Aeropuerto");

			document.add(crearTablaPdf(informesPanel.getModeloOcupacion()));
			document.add(new Paragraph("\n\n"));

			BufferedImage imagen = informesPanel.obtenerImagenClara(informesPanel.getGraficoOcupacion());
			document.add(crearImagenPdf(imagen, 500, 300));

			document.close();
		} catch (Exception e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
		return "";
	}

	public String exportarAlertasPdf(String ruta, InformesPanel informesPanel) {
		try {
			Document document = abrirDocumentoPdf(ruta, "Datos de alertas del Aeropuerto");

			document.add(crearTablaPdf(informesPanel.getModeloAlertas()));

			document.close();
		} catch (Exception e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
		return "";
	}

	public String exportarVuelosEstadoPdf(String ruta, InformesPanel informesPanel) {
		try {
			Document document = abrirDocumentoPdf(ruta, "Datos del estado de los vuelos del Aeropuerto");

			BufferedImage imagen = informesPanel.obtenerImagenClara(informesPanel.getGraficoEstados());
			document.add(crearImagenPdf(imagen, 500, 300));

			document.close();
		} catch (Exception e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
		return "";
	}

	public String exportarIncidenciasAbiertasPdf(String ruta, InformesPanel informesPanel) {
		try {
			Document document = abrirDocumentoPdf(ruta, "Incidencias abiertas por prioridad del Aeropuerto");

			BufferedImage imagen = informesPanel.obtenerImagenClara(informesPanel.getGraficoPrioridades());
			document.add(crearImagenPdf(imagen, 400, 300));

			document.close();
		} catch (Exception e) {
			return "No se ha podido escribir el informe en " + ruta;
		}
		return "";
	}

	public static PdfPTable crearTablaPdf(DefaultTableModel modelo) {
		Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
		Font fuenteCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10);

		PdfPTable tabla = new PdfPTable(modelo.getColumnCount());
		tabla.setWidthPercentage(100);
		tabla.setHeaderRows(1);

		for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
			PdfPCell celda = new PdfPCell(new Phrase(modelo.getColumnName(columna), fuenteCabecera));
			celda.setBackgroundColor(new Color(37, 99, 235));
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);
			celda.setPadding(6);
			tabla.addCell(celda);
		}

		for (int fila = 0; fila < modelo.getRowCount(); fila++) {
			for (int columna = 0; columna < modelo.getColumnCount(); columna++) {
				PdfPCell celda = new PdfPCell(new Phrase(String.valueOf(modelo.getValueAt(fila, columna)), fuenteCuerpo));
				celda.setHorizontalAlignment(Element.ALIGN_CENTER);
				celda.setPadding(5);
				if (fila % 2 == 1) {
					celda.setBackgroundColor(new Color(241, 245, 249));
				}
				tabla.addCell(celda);
			}
		}

		return tabla;
	}

}
