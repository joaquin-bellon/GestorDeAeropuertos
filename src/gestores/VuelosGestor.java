package gestores;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

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

import modelo.Equipaje;
import modelo.Reserva;
import modelo.Servicio;
import modelo.Vuelo;

public class VuelosGestor {
	private Document doc;
	private Element nodoVuelos;
	private ArrayList<Vuelo> vuelos;
	private ConfiguracionGestor gestorConfiguracion;

	public VuelosGestor(ConfiguracionGestor gestorConfiguracion) {
		doc = abrirDoc();
		nodoVuelos = (Element) doc.getElementsByTagName("Vuelos").item(0);
		vuelos = cargarVuelos();
		this.gestorConfiguracion = gestorConfiguracion;
	}

	public void mostrarVueloPorReservaYEmbarque(Scanner sc, ReservasGestor gestorReservas, EmbarquesGestor gestorEmbarques) {
		mostrarVuelos();
		Vuelo vuelo = pedirVuelo(sc, "a consultar");
		ArrayList<Reserva> reservas = gestorReservas.buscarResevasPorVuelo(vuelo.getCodigo());
		if (reservas.size() != 0) {
			Iterator<Reserva> iterator = reservas.iterator();
			while (iterator.hasNext()) {
				Reserva reserva = iterator.next();
				if (reserva.getEstado().equals("CANCELADA")) {
					iterator.remove();
				}
			}
			System.out.println("EMBARCADO:");
			ArrayList<Reserva> reservasEmbarcadas = gestorReservas.filtrarReservasVueloPorEstado(reservas, "EMBARCADO", gestorEmbarques);
			if (reservasEmbarcadas.size() != 0) {
				gestorReservas.mostrarArrayReservas(reservasEmbarcadas);
			}
			else {
				System.out.println("No hay reservas embarcadas para este vuelo.");
			}


			System.out.println("EMBARCANDO:");
			ArrayList<Reserva> reservasEmbarcando = gestorReservas.filtrarReservasVueloPorEstado(reservas, "EMBARCANDO", gestorEmbarques);
			if (reservasEmbarcando.size() != 0) {
			gestorReservas.mostrarArrayReservas(reservasEmbarcando);
			}
			else {
				System.out.println("No hay reservas que estén siendo embarcadas.");
			}
			System.out.println("NO EMBARCADO:");
			ArrayList<Reserva> reservasNoEmbarcadas = gestorReservas.filtrarReservasVueloPorEstado(reservas, "NO_EMBARCADO", gestorEmbarques);
			if (reservasNoEmbarcadas.size() != 0) {
			gestorReservas.mostrarArrayReservas(reservasNoEmbarcadas);
			}
			else {
				System.out.println("No hay reservas que no hayan sido embarcadas.");
			}

			System.out.println("PENDIENTE:");
			ArrayList<Reserva> reservasPendientes = gestorReservas.filtrarReservasVueloPorEstado(reservas, "PENDIENTE", gestorEmbarques);
			if (reservasPendientes.size() != 0) {
				gestorReservas.mostrarArrayReservas(reservasPendientes);
				gestorReservas.comprobarPosibleEmbarquePendiente(sc, vuelo);

			}
			else {
				System.out.println("No hay reservas pendientes para este vuelo");
			}
		}
		else {
			System.out.println("Este vuelo no tiene reservas asociadas.");
		}

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

	public void volcarEnXML() {
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
			e.printStackTrace();
		}
	}

	public void mostrarVuelos() {
		for (Vuelo vuelo : vuelos) {
			System.out.println(vuelo);
		}
	}

	public Vuelo buscarPorCodigo(String codVuelo) {
		for (Vuelo vuelo : vuelos) {
			if (vuelo.getCodigo().equals(codVuelo)) {
				return vuelo;
			}
		}
		return null;
	}

	public Vuelo pedirVuelo(Scanner sc, String accion) {
		Vuelo vuelo;
		System.out.println("Introduce el código del vuelo " + accion + ":");
		do {
			vuelo = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (vuelo == null) {
				System.err.println("No existe un vuelo con ese código. Inténtelo de nuevo:");
			}
		} while (vuelo == null);
		return vuelo;
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

	public void buscarVuelos(Scanner sc) {
		int opcion = 0;
		System.out.println("--------------------------------");
		System.out.println("Escoja el criterio : 1.Codigo_vuelo 2. Destino");

		opcion = Utilidades.leerOpcion(sc, 1, 2);
		System.out.println("--------------------------------");

		switch (opcion) {
		case 1:
			System.out.println("Ingrese el código del vuelo:");
			Vuelo vuelo = buscarPorCodigo(sc.nextLine().trim().toUpperCase());
			if (vuelo == null) {
				System.out.println("No se ha encontrado ningún vuelo con ese código");
			} else {
				System.out.println(vuelo);
			}
			break;
		case 2:
			System.out.println("Inserte el código de destino:");
			ArrayList<Vuelo> vuelosDestino = buscarPorDestino(sc.nextLine().trim().toUpperCase());
			if (vuelosDestino.size() != 0) {
				for (Vuelo vueloDestino : vuelosDestino) {
					System.out.println(vueloDestino);
				}
			} else {
				System.out.println("No hay ningún vuelo con ese destino");
			}
			break;
		}
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


	public void aniadirVuelo(Scanner sc) {
		System.out.println("Creación de un Vuelo");
		System.out.println("------------------------------");

		//Aerolínea
		String aerolinea;
		do {
			System.out.println("Código de aerolínea:");
			aerolinea = sc.nextLine().trim().toUpperCase();
			if (buscarEn("Aerolineas", "Aerolinea", "codigo", aerolinea) == null) {
				System.err.println("Esa aerolínea no existe. Inténtelo de nuevo.");
				aerolinea = null;
			}
		} while (aerolinea == null);

		//Código de vuelo
		String codigo;
		do {
			System.out.println("Código de vuelo (" + aerolinea + " + 4 dígitos, ej: " + aerolinea + "1234):");
			codigo = sc.nextLine().trim().toUpperCase();
			if (!codigo.matches(aerolinea + "\\d{4}")) {
				System.err.println("Formato no válido.");
				codigo = null;
			} else if (buscarPorCodigo(codigo) != null) {
				System.err.println("Ya existe un vuelo con ese código.");
				codigo = null;
			}
		} while (codigo == null);

		//Avión
		String avion;
		do {
			System.out.println("Matrícula del avión:");
			avion = sc.nextLine().trim().toUpperCase();
			Element elAvion = buscarEn("Aviones", "Avion", "matricula", avion);
			if (elAvion == null) {
				System.err.println("Ese avión no existe.");
				avion = null;
			} else if (!elAvion.getAttribute("aerolinea").equals(aerolinea)) {
				System.err.println("Ese avión no pertenece a " + aerolinea + ".");
				avion = null;
			}
		} while (avion == null);

		//Destino
		String destino;
		do {
			System.out.println("Código de destino:");
			destino = sc.nextLine().trim().toUpperCase();
			if (destino.equals("MAD")) {
				System.err.println("El destino no puede ser el propio origen.");
				destino = null;
			} else if (buscarEn("Destinos", "Destino", "codigo", destino) == null) {
				System.err.println("Ese destino no existe.");
				destino = null;
			}
		} while (destino == null);

		//Terminal
		String terminal;
		do {
			System.out.println("Terminal de salida:");
			terminal = sc.nextLine().trim().toUpperCase();
			Element elTerminal = buscarEn("Terminales", "Terminal", "codigo", terminal);
			if (elTerminal == null || !elTerminal.getAttribute("estado").equals("OPERATIVA")) {
				System.err.println("Terminal no válida o no operativa.");
				terminal = null;
			}
		} while (terminal == null);

		//Puerta
		String puerta;
		do {
			System.out.println("Puerta de embarque:");
			puerta = sc.nextLine().trim().toUpperCase();
			Element elPuerta = buscarEn("Puertas", "Puerta", "codigo", puerta);
			if (elPuerta == null) {
				System.err.println("Esa puerta no existe.");
				puerta = null;
			} else if (!elPuerta.getAttribute("terminal").equals(terminal)) {
				System.err.println("Esa puerta no pertenece a la terminal " + terminal + ".");
				puerta = null;
			} else if (!elPuerta.getAttribute("estado").equals("OPERATIVA")) {
				System.err.println("Esa puerta no está operativa.");
				puerta = null;
			}
		} while (puerta == null);

		//Horario
		LocalDateTime salida;
		LocalDateTime llegada;
		do {
			salida = LocalDateTime.of(Utilidades.pedirFecha(sc, "Fecha de salida"), Utilidades.pedirHora(sc, "Hora de salida"));
			llegada = LocalDateTime.of(Utilidades.pedirFecha(sc, "Fecha de llegada"), Utilidades.pedirHora(sc, "Hora de llegada"));
			if (!llegada.isAfter(salida)) {
				System.err.println("La llegada debe ser posterior a la salida. Vuelva a introducir el horario.");
			}
		} while (!llegada.isAfter(salida));

		vuelos.add(new Vuelo(codigo, aerolinea, avion, terminal, puerta, destino, salida, llegada));
		System.out.println("Vuelo " + codigo + " creado correctamente.");
	}

	public void actualizarVuelo(Scanner sc) {
		Vuelo vuelo = pedirVuelo(sc, "a editar");

		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Dato a cambiar:\n"
				+ "1.Terminal y Puerta - 2.Estado - 3.HoraSalida - 4.HoraLlegada");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Escoge:");
		int num = -1;
		num = Utilidades.leerOpcion(sc, 1, 4);

		LocalTime hora;
		switch (num) {

		case 1: //Terminal y Puerta
			String terminal;
			do {
				System.out.println("Introduce la nueva terminal:");
				terminal = sc.nextLine().trim().toUpperCase();
				if (buscarEn("Terminales", "Terminal", "codigo", terminal) == null) {
					System.err.println("Esa terminal no existe. Inténtelo de nuevo");
					terminal = null;
				}
			} while (terminal == null);

			String puerta;
			do {
				System.out.println("Introduce el código de la nueva puerta");
				puerta = sc.nextLine().trim().toUpperCase();
				Element puertaNode = buscarEn("Puertas", "Puerta", "codigo", puerta);
				if (puertaNode == null) {
					System.err.println("Esa puerta no existe");
					puerta = null;
				} else if (!puertaNode.getAttribute("terminal").equals(terminal)) {
					System.err.println("Esa puerta no pertenece a la terminal " + terminal);
					puerta = null;
				} else if (!puertaNode.getAttribute("estado").equals("OPERATIVA")) {
					System.err.println("Esa puerta NO está Operativa.");
					puerta = null;
				}
			} while (puerta == null);

			vuelo.setTerminal(terminal);
			vuelo.setPuerta(puerta);
			System.out.println("Puerta cambiada correctamente");
			break;

		case 2: //Estado
			System.out.print("Escoge entre los siguientes. ");
			vuelo.setEstado(pedirEstadoVuelo(sc));
			System.out.println("Estado cambiado correctamente");
			break;

		case 3: //HoraSalida
			LocalDateTime nuevaSalida;
			do {
				hora = Utilidades.pedirHora(sc, "¿Cúal es la nueva hora?");
				nuevaSalida = LocalDateTime.of(vuelo.getSalida().toLocalDate(), hora);
				if (!vuelo.getLlegada().isAfter(nuevaSalida)) {
					System.err.println("La salida tiene que ser anterior a la llegada");
				}
			} while (!vuelo.getLlegada().isAfter(nuevaSalida));
			vuelo.setSalida(nuevaSalida);
			System.out.println("La hora de salida se ha cambiado correctamente.");
			break;

		case 4: //HoraLlegada
			LocalDateTime nuevaLlegada;
			do {
				hora = Utilidades.pedirHora(sc, "¿Cúal es la nueva hora?");
				nuevaLlegada = LocalDateTime.of(vuelo.getLlegada().toLocalDate(), hora);
				if (!nuevaLlegada.isAfter(vuelo.getSalida())) {
					System.err.println("La llegada tiene que ser posterior a la salida");
				}
			} while (!nuevaLlegada.isAfter(vuelo.getSalida()));
			vuelo.setLlegada(nuevaLlegada);
			System.out.println("La hora de llegada se ha cambiado correctamente.");
			break;
		}
	}

	public String pedirEstadoVuelo(Scanner sc) {
		String string = "";
		System.out.println("Estado: 1.RETRASADO - 2.PROGRAMADO - 3.CANCELADO - 4.EMBARCANDO - 5.CERRADO");
		int num = Utilidades.leerOpcion(sc, 1, 5);

		switch (num) {
		case 1:
			string = "RETRASADO";
			break;
		case 2:
			string = "PROGRAMADO";
			break;
		case 3:
			string = "CANCELADO";
			break;
		case 4:
			string = "EMBARCANDO";
			break;
		case 5:
			string = "CERRADO";
			break;
		}
		return string;
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

	public void consultarEquipajesDeVuelo(Scanner sc, EquipajesGestor gestorEquipajes) {
		Vuelo vuelo = pedirVuelo(sc, "a consultar");
		ArrayList<Equipaje> equipajesDelVuelo = gestorEquipajes.buscarEquipajesAsociadosAVuelo(vuelo.getCodigo());
		System.out.println("========================================");
		System.out.println("Resumen de equipajes del vuelo " + vuelo.getCodigo());
		System.out.println("========================================");
		if (equipajesDelVuelo.size() != 0) {
			System.out.println("Cantidad total de bultos: " + equipajesDelVuelo.size());
			System.out.printf("Peso total: %.2f kg%n", gestorEquipajes.calcularPesoTotal(equipajesDelVuelo));

			System.out.println("----------------------------------------");
			System.out.println("Bultos por tipo:");
			HashMap<String, Integer> cantidadPorTipos = gestorEquipajes.filtrarArrayEquipajesPorTipo(equipajesDelVuelo);
			for (Map.Entry<String,Integer> k : cantidadPorTipos.entrySet()) {
				System.out.println("   " + k.getKey() + ": " + k.getValue());
			}

			System.out.println("----------------------------------------");
			System.out.println("Bultos por estado:");
			HashMap<String, Integer> cantidadPorEstado = gestorEquipajes.filtrarArrayEquipajesPorEstado(equipajesDelVuelo);
			for (Map.Entry<String,Integer> k : cantidadPorEstado.entrySet()) {
				System.out.println("   " + k.getKey() + ": " + k.getValue());
			}

			System.out.println("----------------------------------------");
			ArrayList<Equipaje> equipajesPerdidos = gestorEquipajes.filtrarArrayEquipajesPorPerdidos(equipajesDelVuelo);
			System.out.println("Equipajes perdidos: " + equipajesPerdidos.size());
			for (Equipaje equipaje : equipajesPerdidos) {
				System.out.println("   " + equipaje);
			}

			System.out.println("----------------------------------------");
			ArrayList<Equipaje> equipajesConExcesoDePeso = gestorEquipajes.filtrarArrayEquipajesPorExcesoDePeso(equipajesDelVuelo);
			System.out.println("Equipajes con exceso de peso: " + equipajesConExcesoDePeso.size());
			for (Equipaje equipaje : equipajesConExcesoDePeso) {
				System.out.println("   " + equipaje);
			}
			System.out.println("========================================");
		}
		else {
			System.out.println("No hay equipajes asociados a este vuelo.");
		}
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
	
}
