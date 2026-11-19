package gestores;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;

import modelo.Servicio;
import modelo.Vuelo;
import utilidades.Utilidades;
import utilidades.Constantes;

public class VuelosGestor {
	private Document doc;
	private Element nodoVuelos;
	private ArrayList<Vuelo> vuelos;
	private ConfiguracionGestor gestorConfiguracion;

	private static VuelosGestor instancia;

	public static VuelosGestor getInstance() {
		if (instancia == null) {
			instancia = new VuelosGestor();
		}
		return instancia;
	}

	private VuelosGestor() {
		doc = abrirDoc();
		nodoVuelos = (Element) doc.getElementsByTagName("Vuelos").item(0);
		vuelos = cargarVuelos();
		this.gestorConfiguracion = ConfiguracionGestor.getInstance();
	}

	private Document abrirDoc() {
		doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			doc = factory.newDocumentBuilder().parse(new File(Utilidades.RUTA_AEROPUERTO));
			limpiarEspacios(doc.getDocumentElement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}


	private void limpiarEspacios(Node nodo) {
		NodeList hijos = nodo.getChildNodes();
		for (int i = hijos.getLength() - 1; i >= 0; i--) {
			Node hijo = hijos.item(i);
			if (hijo.getNodeType() == Node.TEXT_NODE && hijo.getTextContent().trim().isEmpty()) {
				nodo.removeChild(hijo);
			} else if (hijo.getNodeType() == Node.ELEMENT_NODE) {
				limpiarEspacios(hijo);
			}
		}
	}

	private ArrayList<Vuelo> cargarVuelos() {
		ArrayList<Vuelo> lista = new ArrayList<Vuelo>();
		NodeList listaVuelos = nodoVuelos.getElementsByTagName("Vuelo");
		for (int i = 0; i < listaVuelos.getLength(); i++) {
			Element elementoVuelo = (Element) listaVuelos.item(i);
			lista.add(elementoAVuelo(elementoVuelo));
		}
		return lista;
	}


	private Vuelo elementoAVuelo(Element elementoVuelo) {
		Vuelo vuelo = new Vuelo();
		vuelo.setCodigo(elementoVuelo.getAttribute("codigo"));
		vuelo.setAerolinea(elementoVuelo.getAttribute("aerolinea"));
		vuelo.setAvion(elementoVuelo.getAttribute("avion"));
		vuelo.setEstado(elementoVuelo.getAttribute("estado"));

		//Ruta
		Element origen = getHijo(elementoVuelo, "Origen");
		vuelo.setOrigen(origen.getAttribute("codigo"));
		vuelo.setTerminal(getTexto(origen, "Terminal"));
		vuelo.setPuerta(getTexto(origen, "Puerta"));
		vuelo.setDestino(getHijo(elementoVuelo, "Destino").getAttribute("codigo"));

		//Horario
		Element salida = getHijo(elementoVuelo, "Salida");
		vuelo.setSalida(LocalDateTime.of(LocalDate.parse(salida.getAttribute("fecha")),
				LocalTime.parse(getTexto(salida, "Hora"))));
		vuelo.setZonaHorariaSalida(salida.getAttribute("zonaHoraria"));

		Element llegada = getHijo(elementoVuelo, "Llegada");
		vuelo.setLlegada(LocalDateTime.of(LocalDate.parse(llegada.getAttribute("fecha")),
				LocalTime.parse(getTexto(llegada, "Hora"))));
		vuelo.setZonaHorariaLlegada(llegada.getAttribute("zonaHoraria"));

		//Embarque
		Element embarque = getHijo(elementoVuelo, "Embarque");
		vuelo.setEstadoEmbarque(embarque.getAttribute("estado"));
		vuelo.setMinutosInicioEmbarque(getHijo(embarque, "Inicio").getAttribute("minutosAntes"));
		vuelo.setMinutosCierreEmbarque(getHijo(embarque, "Cierre").getAttribute("minutosAntes"));

		//Operaciones
		NodeList servicios = elementoVuelo.getElementsByTagName("Servicio");
		for (int i = 0; i < servicios.getLength(); i++) {
			Element servicio = (Element) servicios.item(i);
			vuelo.getServicios().add(new Servicio(servicio.getAttribute("tipo"), servicio.getAttribute("estado")));
		}

		return vuelo;
	}

	private Element vueloAElemento(Vuelo vuelo) {
		Element elementoVuelo = doc.createElement("Vuelo");
		elementoVuelo.setAttribute("codigo", vuelo.getCodigo());
		elementoVuelo.setAttribute("aerolinea", vuelo.getAerolinea());
		elementoVuelo.setAttribute("avion", vuelo.getAvion());
		elementoVuelo.setAttribute("estado", vuelo.getEstado());

		Element ruta = crearHijo(elementoVuelo, "Ruta");
		Element origen = crearHijo(ruta, "Origen");
		origen.setAttribute("codigo", vuelo.getOrigen());
		crearHijo(origen, "Terminal").setTextContent(vuelo.getTerminal());
		crearHijo(origen, "Puerta").setTextContent(vuelo.getPuerta());
		crearHijo(ruta, "Destino").setAttribute("codigo", vuelo.getDestino());

		Element horario = crearHijo(elementoVuelo, "Horario");
		Element salida = crearHijo(horario, "Salida");
		salida.setAttribute("fecha", vuelo.getSalida().toLocalDate().toString());
		salida.setAttribute("zonaHoraria", vuelo.getZonaHorariaSalida());
		crearHijo(salida, "Hora").setTextContent(vuelo.getSalida().toLocalTime().toString());
		Element llegada = crearHijo(horario, "Llegada");
		llegada.setAttribute("fecha", vuelo.getLlegada().toLocalDate().toString());
		llegada.setAttribute("zonaHoraria", vuelo.getZonaHorariaLlegada());
		crearHijo(llegada, "Hora").setTextContent(vuelo.getLlegada().toLocalTime().toString());

		Element embarque = crearHijo(elementoVuelo, "Embarque");
		embarque.setAttribute("estado", vuelo.getEstadoEmbarque());
		crearHijo(embarque, "Inicio").setAttribute("minutosAntes", vuelo.getMinutosInicioEmbarque());
		crearHijo(embarque, "Cierre").setAttribute("minutosAntes", vuelo.getMinutosCierreEmbarque());

		Element operaciones = crearHijo(elementoVuelo, "Operaciones");
		for (Servicio servicio : vuelo.getServicios()) {
			Element elementoServicio = crearHijo(operaciones, "Servicio");
			elementoServicio.setAttribute("tipo", servicio.getTipo());
			elementoServicio.setAttribute("estado", servicio.getEstado());
		}

		return elementoVuelo;
	}

	public boolean volcarEnXML() {
		NodeList hijos = nodoVuelos.getChildNodes();
		for (int i = hijos.getLength() - 1; i >= 0; i--) {
			nodoVuelos.removeChild(hijos.item(i));
		}
		for (Vuelo vuelo : vuelos) {
			nodoVuelos.appendChild(vueloAElemento(vuelo));
		}

		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(new File(Utilidades.RUTA_AEROPUERTO)));
		} catch (TransformerException e) {
			return false;
		}
		return true;
	}

	public Vuelo buscarPorCodigo(String codVuelo) {
		for (Vuelo vuelo : vuelos) {
			if (vuelo.getCodigo().equals(codVuelo)) {
				return vuelo;
			}
		}
		return null;
	}

	public ArrayList<Vuelo> buscarPorDestino(String destino) {
		ArrayList<Vuelo> vuelosDestino = new ArrayList<Vuelo>();
		for (Vuelo vuelo : vuelos) {
			if (vuelo.getDestino().equals(destino)) {
				vuelosDestino.add(vuelo);
			}
		}
		return vuelosDestino;
	}

	public Element buscarEn(String contenedor, String etiqueta, String atributo, String valor) {
		Element padre = (Element) doc.getElementsByTagName(contenedor).item(0);
		NodeList lista = padre.getElementsByTagName(etiqueta);
		for (int i = 0; i < lista.getLength(); i++) {
			Element el = (Element) lista.item(i);
			if (el.getAttribute(atributo).equals(valor)) {
				return el;
			}
		}
		return null;
	}

	public Element sacarAvionVuelo(Vuelo vuelo) {
		return buscarEn("Aviones", "Avion", "matricula", vuelo.getAvion());
	}


	public String insertarVuelo(String codigo, String aerolinea, String avion, String terminal, String puerta, String destino,
			LocalDateTime salida, LocalDateTime llegada, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un administrador puede añadir vuelos.";
		}
		vuelos.add(new Vuelo(codigo, aerolinea, avion, terminal, puerta, destino, salida, llegada));
		if (!volcarEnXML()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_AEROPUERTO);
		}
		return "";
	}

	public String modificarVuelo(Vuelo vuelo, String terminal, String puerta, LocalDateTime salida, LocalDateTime llegada,
			String estado, String rolUsuario) {
		if (!Utilidades.esAdmin(rolUsuario)) {
			return "Solo un administrador puede modificar vuelos.";
		}
		vuelo.setTerminal(terminal);
		vuelo.setPuerta(puerta);
		vuelo.setSalida(salida);
		vuelo.setLlegada(llegada);
		vuelo.setEstado(estado);
		if (!volcarEnXML()) {
			return Utilidades.errorGuardar(Utilidades.RUTA_AEROPUERTO);
		}
		return "";
	}

	public ArrayList<Vuelo> filtrarVuelos(String criterio, String texto) {
		ArrayList<Vuelo> arrayVuelos = new ArrayList<Vuelo>();
		texto = texto.toUpperCase();

		for (Vuelo vuelo : vuelos) {
			switch (criterio) {
			case "Código":
				if (vuelo.getCodigo().toUpperCase().contains(texto)) {
					arrayVuelos.add(vuelo);
				}
				break;
			case "Origen":
				if (vuelo.getOrigen().toUpperCase().contains(texto)) {
					arrayVuelos.add(vuelo);
				}
				break;
			case "Destino":
				if (vuelo.getDestino().toUpperCase().contains(texto)) {
					arrayVuelos.add(vuelo);
				}
				break;
			case "Avión":
				if (vuelo.getAvion().toUpperCase().contains(texto)) {
					arrayVuelos.add(vuelo);
				}
				break;
			case "Terminal":
				if (vuelo.getTerminal().toUpperCase().contains(texto)) {
					arrayVuelos.add(vuelo);
				}
				break;
			}
		}

		return arrayVuelos;
	}

	private Element crearHijo(Element padre, String etiqueta) {
		Element hijo = doc.createElement(etiqueta);
		padre.appendChild(hijo);
		return hijo;
	}

	private static Element getHijo(Element padre, String etiqueta) {
		NodeList lista = padre.getElementsByTagName(etiqueta);
		if (lista.getLength() > 0) {
			return (Element) lista.item(0);
		} else {
			return null;
		}
	}

	private static String getTexto(Element padre, String etiqueta) {
		Element hijo = getHijo(padre, etiqueta);
		if (hijo != null) {
			return hijo.getTextContent().trim();
		} else {
			return null;
		}
	}

	public ArrayList<Vuelo> getVuelos() {
		return vuelos;
	}

	public int recogerTamanioDelAvionDeUnVuelo (String avionString) {
		Element avion = buscarEn("Aviones", "Avion", "matricula", avionString);
		int numPasajeros = Integer.parseInt(avion.getElementsByTagName("Pasajeros").item(0).getTextContent().trim());
		return numPasajeros;
	}

	
	public String comprobarVueloNacionaleInternacional(Vuelo vuelo) {
		String tipo = "";
		Element destino = buscarEn("Destinos", "Destino", "codigo", vuelo.getDestino());
		if (destino != null) {
			tipo = destino.getAttribute("tipo");
		}
		return tipo;
	}
	
	public ArrayList<Vuelo> buscarVuelosPorEstado (String estado) {
		ArrayList<Vuelo> vuelosPorEstado = new ArrayList<Vuelo>();
		for (Vuelo vuelo : vuelos) {
			if(vuelo.getEstado().equals(estado)) {
				vuelosPorEstado.add(vuelo);
			}
		}
		return vuelosPorEstado;
	}
	
	public LocalDateTime calcularAperturaEmbarque(Vuelo vuelo) {
		LocalDateTime apertura = null;
		JsonNode franja = gestorConfiguracion.buscarFranja(comprobarVueloNacionaleInternacional(vuelo));
		if (franja != null) {
			apertura = vuelo.getSalida().minusMinutes(franja.get("inicioMinutosAntes").asInt());
		}
		return apertura;
	}

	public LocalDateTime calcularCierreEmbarque(Vuelo vuelo) {
		LocalDateTime cierre = null;
		JsonNode franja = gestorConfiguracion.buscarFranja(comprobarVueloNacionaleInternacional(vuelo));
		if (franja != null) {
			cierre = vuelo.getSalida().minusMinutes(franja.get("cierreMinutosAntes").asInt());
		}
		return cierre;
	}
	
	public ArrayList<String> obtenerRutas () {
		HashSet<String> rutas = new HashSet<String>();
			NodeList nodeDestinos = doc.getElementsByTagName("Destino");
			for (int i = 0 ; i < nodeDestinos.getLength() ; i++ ) {
				rutas.add(((Element)nodeDestinos.item(i)).getAttribute("codigo"));
			}
		return new ArrayList<String>(rutas);
	}
	
	public ArrayList<Vuelo> obtenerVuelosActivos() {
		ArrayList<Vuelo> vuelosActivos = new ArrayList<Vuelo>();
		for (Vuelo vuelo : vuelos) {
			if (!vuelo.getEstado().equals(Constantes.VUELO_CANCELADO) && !vuelo.getEstado().equals(Constantes.VUELO_CERRADO)) {
				vuelosActivos.add(vuelo);
			}
		}
		return vuelosActivos;
	}
	
}
