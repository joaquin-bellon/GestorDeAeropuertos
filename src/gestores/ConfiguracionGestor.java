package gestores;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import utilidades.Utilidades;

public class ConfiguracionGestor {
	private ObjectMapper objectMapper;
	private JsonNode raiz;
	private JsonNode aeropuerto;

	private static ConfiguracionGestor instancia;

	public static ConfiguracionGestor getInstance() {
		if (instancia == null) {
			instancia = new ConfiguracionGestor();
		}
		return instancia;
	}

	private ConfiguracionGestor() {
		this.objectMapper = new ObjectMapper();
		this.raiz = abrirJson();
		this.aeropuerto = raiz.get("aeropuerto");
	}
	

	private JsonNode abrirJson() {
		JsonNode nodeRoot = null;
		try {
			nodeRoot = objectMapper.readTree(new File(Utilidades.RUTA_CONFIGURACION));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodeRoot;
		
	}


	public void mostrarConfiguracion() {
		System.out.println("Aeropuerto: - Codigo: ["
	+ aeropuerto.get("codigo").asText() + "] - Nombre: " +  aeropuerto.get("nombre").asText());
		System.out.println("VersiónConfiguración: " + aeropuerto.get("versionConfiguracion").asText() + " - UltimaActualizacion: " + aeropuerto.get("ultimaActualizacion").asText());
		System.out.println("Moneda base: " + aeropuerto.get("monedaBase").asText() + " - Zona horaria: " + aeropuerto.get("zonaHoraria").asText());
		System.out.println("Idiomas: " + listar(aeropuerto.get("idiomas")));
 
		//Terminales
		imprimirTerminales();
 
		//Equipaje
		imprimirEquipaje();
 
		//Vuelos
		imprimirVuelos();
 
		//Embarque
		imprimirEmbarques();
 
		//Reservas
		JsonNode reservas = aeropuerto.get("reservas");
		System.out.println("========================================");
		System.out.println("RESERVAS");
		System.out.println("Clases:");
		for (JsonNode clase : reservas.get("clases")) {
			System.out.println("- " + clase.get("nombre").asText() + ": factor precio " + clase.get("factorPrecio").asDouble()
					+ " - Equipaje incluido: " + clase.get("equipajeIncluido").asInt() + " - Cambios permitidos: " + clase.get("cambiosPermitidos").asBoolean());
		}
		System.out.println("Estados permitidos: " + listar(reservas.get("estadosPermitidos")));
		System.out.println("Canales: " + listar(reservas.get("canales")));
		JsonNode politica = reservas.get("politicaCambios");
		System.out.println("Política de cambios: límite " + politica.get("limiteCambios").asInt() + " - Requiere confirmación: "
				+ politica.get("requiereConfirmacion").asBoolean() + " - Registrar auditoría: " + politica.get("registrarAuditoria").asBoolean());
		JsonNode asientos = reservas.get("asientos");
		System.out.println("Asientos: duplicados por vuelo " + asientos.get("permitirDuplicadosPorVuelo").asBoolean()
				+ " - Zonas: " + listar(asientos.get("zonas")));
		System.out.println("Salidas de emergencia: edad mínima " + asientos.get("salidasEmergencia").get("edadMinima").asInt()
				+ " - Requiere aceptación: " + asientos.get("salidasEmergencia").get("requiereAceptacion").asBoolean());
 
		//Pasajeros
		JsonNode pasajeros = aeropuerto.get("pasajeros");
		System.out.println("========================================");
		System.out.println("PASAJEROS");
		System.out.println("Tipos: " + listar(pasajeros.get("tipos")));
		JsonNode validacionesPasajeros = pasajeros.get("validaciones");
		System.out.println("Validaciones: DNI obligatorio adultos " + validacionesPasajeros.get("dniObligatorioAdultos").asBoolean()
				+ " - Email obligatorio " + validacionesPasajeros.get("emailObligatorio").asBoolean()
				+ " - Edad menor: de " + validacionesPasajeros.get("edadMinimaMenor").asInt() + " a " + validacionesPasajeros.get("edadMaximaMenor").asInt() + " años");
		System.out.println("Asistencia: " + listar(pasajeros.get("asistencia").get("tipos"))
				+ " - Anticipación: " + pasajeros.get("asistencia").get("anticipacionMinutos").asInt() + " min");
 
		//Tarifas
		System.out.println("========================================");
		System.out.println("TARIFAS");
		for (JsonNode tarifa : aeropuerto.get("tarifas")) {
			System.out.print("- " + tarifa.get("tipo").asText() + ":");
			Iterator<String> condiciones = tarifa.get("condiciones").fieldNames();
			while (condiciones.hasNext()) {
				String condicion = condiciones.next();
				System.out.print(" " + condicion + "=" + tarifa.get("condiciones").get(condicion).asText());
			}
			System.out.println();
		}
 
		//Notificaciones
		JsonNode notificaciones = aeropuerto.get("notificaciones");
		System.out.println("========================================");
		System.out.println("NOTIFICACIONES");
		System.out.println("Canales: " + listar(notificaciones.get("canales")));
		System.out.println("Eventos: " + listar(notificaciones.get("eventos")));
		System.out.println("Reintentos: máximo " + notificaciones.get("reintentos").get("maximos").asInt()
				+ " - Espera (s): " + listar(notificaciones.get("reintentos").get("esperaSegundos")));
		System.out.println("Plantillas: idioma por defecto " + notificaciones.get("plantillas").get("idiomaPorDefecto").asText()
				+ " - Personalización: " + notificaciones.get("plantillas").get("permitirPersonalizacion").asBoolean());
 
		//Auditoría
		JsonNode auditoria = aeropuerto.get("auditoria");
		System.out.println("========================================");
		System.out.println("AUDITORÍA");
		System.out.println("Habilitada: " + auditoria.get("habilitada").asBoolean() + " - Retención: " + auditoria.get("retencionDias").asInt()
				+ " días - Anonimizar tras: " + auditoria.get("anonimizarDatosTrasDias").asInt() + " días");
		System.out.println("Eventos: " + listar(auditoria.get("eventos")));
		System.out.println("========================================");
	}
 
	private String listar(JsonNode array) {
		String texto = "";
		for (int i = 0; i < array.size(); i++) {
			if (i > 0) {
				texto += ", ";
			}
			texto += array.get(i).asText();
		}
		return texto;
	}


	public void buscarConfiguracion(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio de busqueda: 1.Terminales 2.Equipaje 3.Vuelos 4. Embarques");
		opcion = Utilidades.leerOpcion(sc, 1, 4);
		System.out.println("--------------------------------");
		switch (opcion) {
		case 1:
			imprimirTerminales();
			break;
		case 2:
			imprimirEquipaje();
			break;
		case 3:
			imprimirVuelos();
			break;
		case 4:
			imprimirEmbarques();
			break;
		}
		
		
	}

	private void imprimirEmbarques() {
		JsonNode embarque = aeropuerto.get("embarque");
		System.out.println("========================================");
		System.out.println("EMBARQUE");
		System.out.println("Franjas:");
		for (JsonNode franja : embarque.get("franjas")) {
			System.out.print("- " + franja.get("tipo").asText() + ": inicio " + franja.get("inicioMinutosAntes").asInt()
					+ " min antes - Cierre " + franja.get("cierreMinutosAntes").asInt() + " min antes - Prioridad:");
			Iterator<String> prioridadesFranja = franja.get("prioridad").fieldNames();
			while (prioridadesFranja.hasNext()) {
				String prioridad = prioridadesFranja.next();
				System.out.print(" " + prioridad + "=" + franja.get("prioridad").get(prioridad).asBoolean());
			}
			System.out.println();
		}
		System.out.println("Estados permitidos: " + listar(embarque.get("estadosPermitidos")));
		JsonNode validaciones = embarque.get("validaciones");
		System.out.println("Validaciones: reserva confirmada " + validaciones.get("requiereReservaConfirmada").asBoolean()
				+ " - Un embarque por reserva " + validaciones.get("unEmbarquePorReserva").asBoolean()
				+ " - Bloquear si vuelo cancelado " + validaciones.get("bloquearSiVueloCancelado").asBoolean());
	}


	private void imprimirVuelos() {
		JsonNode vuelos = aeropuerto.get("vuelos");
		System.out.println("========================================");
		System.out.println("VUELOS");
		System.out.println("Niveles de retraso:");
		for (JsonNode nivel : vuelos.get("retrasos").get("niveles")) {
			System.out.print("- " + nivel.get("nombre").asText() + ": de " + nivel.get("minutosMinimos").asInt() + " a "
					+ nivel.get("minutosMaximos").asInt() + " min - Notificar pasajeros: " + nivel.get("notificarPasajeros").asBoolean());
			if (nivel.has("activarAsistencia")) {
				System.out.print(" - Activar asistencia: " + nivel.get("activarAsistencia").asBoolean());
			}
			System.out.println();
		}
		System.out.println("Causas de retraso: " + listar(vuelos.get("retrasos").get("causas")));
		JsonNode ocupacion = vuelos.get("ocupacion");
		System.out.println("Ocupación: alerta al " + ocupacion.get("alerta").asInt() + "% - Máxima: " + ocupacion.get("maxima").asInt() + "%"
				+ " - Cuenta reservas: " + listar(ocupacion.get("contarReservas")) + " - Excluye: " + listar(ocupacion.get("excluirEstados")));
		System.out.println("Estados permitidos: " + listar(vuelos.get("estadosPermitidos")));
		JsonNode cambiosPuerta = vuelos.get("cambiosPuerta");
		System.out.println("Cambios de puerta: permitidos " + cambiosPuerta.get("permitidos").asBoolean() + " - Avisar con "
				+ cambiosPuerta.get("avisarConMinutos").asInt() + " min - Canales: " + listar(cambiosPuerta.get("canales")));
		JsonNode ventanas = vuelos.get("ventanasOperacion");
		System.out.println("Ventana de operación (check-in): apertura " + ventanas.get("aperturaMinutosAntes").asInt()
				+ " min antes - Cierre " + ventanas.get("cierreMinutosDespues").asInt() + " min después");
		
	}


	private void imprimirEquipaje() {
		JsonNode equipaje = aeropuerto.get("equipaje");
		System.out.println("========================================");
		System.out.println("EQUIPAJE");
		System.out.println("Reglas:");
		for (JsonNode regla : equipaje.get("reglas")) {
			System.out.println("- " + regla.get("tipo").asText() + ": peso máximo " + regla.get("pesoMaximo").asDouble() + " " + regla.get("unidad").asText()
					+ " - Coste exceso: " + regla.get("costeExcesoKg").asDouble() + " EUR/kg - Permite cabina: " + regla.get("permiteCabina").asBoolean());
			if (regla.has("etiquetado")) {
				System.out.println("  Etiquetado obligatorio: " + regla.get("etiquetado").get("obligatorio").asBoolean()
						+ " - Formato: " + regla.get("etiquetado").get("formato").asText());
			}
			if (regla.has("dimensionesMaximas")) {
				JsonNode dimensiones = regla.get("dimensionesMaximas");
				System.out.println("  Dimensiones máximas: " + dimensiones.get("alto").asInt() + "x" + dimensiones.get("ancho").asInt()
						+ "x" + dimensiones.get("fondo").asInt() + " " + dimensiones.get("unidad").asText());
			}
			if (regla.has("requiereAutorizacion")) {
				System.out.println("  Requiere autorización: " + regla.get("requiereAutorizacion").asBoolean());
			}
			if (regla.has("categorias")) {
				System.out.println("  Categorías: " + listar(regla.get("categorias")));
			}
		}
		System.out.println("Estados permitidos: " + listar(equipaje.get("estadosPermitidos")));
		System.out.print("Prioridades:");
		Iterator<String> prioridades = equipaje.get("prioridades").fieldNames();
		while (prioridades.hasNext()) {
			String prioridad = prioridades.next();
			System.out.print(" " + prioridad + "=" + equipaje.get("prioridades").get(prioridad).asInt());
		}
		System.out.println();
		JsonNode perdido = equipaje.get("incidencias").get("equipajePerdido");
		System.out.println("Equipaje perdido: plazo de reclamación " + perdido.get("plazoReclamacionHoras").asInt() + " h - Indemnización máxima: "
				+ perdido.get("indemnizacionMaxima").asInt() + " EUR - Requiere parte: " + perdido.get("requiereParte").asBoolean());
		JsonNode danado = equipaje.get("incidencias").get("equipajeDañado");
		System.out.println("Equipaje dañado: plazo de reclamación " + danado.get("plazoReclamacionHoras").asInt() + " h - Requiere fotografías: "
				+ danado.get("requiereFotografias").asBoolean());
		
	}


	private void imprimirTerminales() {
		System.out.println("========================================");
		System.out.println("TERMINALES");
		for (JsonNode terminal : aeropuerto.get("terminales")) {
			System.out.println("- " + terminal.get("codigo").asText() + " [" + terminal.get("estado").asText() + "]"
					+ " - Puertas: " + terminal.get("puertas").get("min").asText() + " a " + terminal.get("puertas").get("max").asText());
			System.out.println("  Servicios: " + listar(terminal.get("servicios")));
		}
	}
	
	public void volcarJSON () {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File ("archivos/configuracion_nuevo.json"), raiz);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void insertarAConfiguracion(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("¿Qué quieres añadir?: 1.Terminal 2.ClaseDeLaReserva 3.TipoPasajeros  - (Los cambios se añadirán a archivos/configuracion_nuevo.json)");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		System.out.println("--------------------------------");
		switch (opcion) {
		case 1:
			aniadirTerminal(sc);
			break;
		case 2:
			aniadirClaseReserva(sc);
			break;
		case 3:
			aniadirTipoPasajero(sc);
			break;
		}
		
		
		volcarJSON();
	}


	private void aniadirTerminal(Scanner sc) {
		ArrayNode terminales = (ArrayNode) aeropuerto.get("terminales");
		ObjectNode terminal  = objectMapper.createObjectNode();
		System.out.println("Introduce el codigo de la terminal:");
		String codTerminal = null;
		do {
		codTerminal = sc.nextLine().trim().toUpperCase();
		if (!codTerminal.matches("T\\d+")) {
			System.err.println("Formato no válido (T + número, ej: T5). Intentelo de nuevo:");
			codTerminal = null;
		}
		else if (existeTerminal(terminales, codTerminal) ) {
			System.err.println("Ya existe una terminal con ese codigo. Intentelo de nuevo:");
			codTerminal = null;
		}

		}while (codTerminal == null);
		terminal.put("codigo", codTerminal);
		terminal.put("estado", "OPERATIVA");
		ObjectNode puertas = objectMapper.createObjectNode();
		String maxPuertas = escogerMinyMaxDePuertas(sc, terminales);
		String minPuertas = maxPuertas.substring(0,1) + "01";
		puertas.put("min", minPuertas);
		puertas.put("max", maxPuertas);
		terminal.set("puertas", puertas);
		terminal.set("servicios", escogerServicios(sc));
		terminales.add(terminal);
		System.out.println("Terminal " + codTerminal + " añadida correctamente.");
	}

	private ArrayNode escogerServicios(Scanner sc) {
		String[] serviciosPosibles = {"CHECKIN", "SEGURIDAD", "EMBARQUE", "VIP", "ASISTENCIA"};
		ArrayNode servicios = objectMapper.createArrayNode();
		for (String servicio : serviciosPosibles) {
			System.out.println("¿La terminal tiene el servicio " + servicio + "? 1.Sí 2.No");
			if (Utilidades.leerOpcion(sc, 1, 2) == 1) {
				servicios.add(servicio);
			}
		}
		return servicios;
	}




	private String escogerMinyMaxDePuertas(Scanner sc,ArrayNode terminales) {
		System.out.println("Introduce la letra identificativa de las puertas:");
		String letra;
		do {
		letra = sc.nextLine().trim().toUpperCase();
		if (!letra.matches("[A-Z]")) {
			System.err.println("Formato no válido. Inténtelo de nuevo.");
			letra = null;
		} 
		else {
			Iterator<JsonNode> iterator = terminales.iterator();
			boolean coincide = false;
			while (iterator.hasNext() && !coincide) {
				JsonNode terminal = iterator.next();
				if (terminal.get("puertas").get("min").asText().substring(0, 1).equals(letra)) {
					System.err.println("Ese identificador ya existe.");
					letra = null;
					coincide = true;
			}
			}
		}
		}while (letra == null);
		System.out.println("Introduce el numero de puertas (entre 1 y 99):");
		int num = Utilidades.leerOpcion(sc, 1, 99);
		
		return String.format("%s%02d", letra, num);

	}


	private void aniadirClaseReserva(Scanner sc) {
		ArrayNode clases = (ArrayNode) aeropuerto.get("reservas").get("clases");
		ObjectNode clase = objectMapper.createObjectNode();
		System.out.println("Clases actuales:");
		for (JsonNode claseExistente : clases) {
			System.out.println("- " + claseExistente.get("nombre").asText());
		}
		System.out.println("Introduce el nombre de la nueva clase:");
		String nombre;
		do {
			nombre = sc.nextLine().trim().toUpperCase();
			if (!nombre.matches("[A-Z_]+")) {
				System.err.println("Formato no válido (solo letras y _, ej: PREMIUM_ECONOMICA). Inténtelo de nuevo:");
				nombre = null;
			}
			else if (existeClase(clases, nombre)) {
				System.err.println("Ya existe una clase con ese nombre. Inténtelo de nuevo:");
				nombre = null;
			}
		}while (nombre == null);
		clase.put("nombre", nombre);
		clase.put("factorPrecio", pedirFactorPrecio(sc));
		System.out.println("Número de equipajes incluidos (entre 0 y 5):");
		clase.put("equipajeIncluido", Utilidades.leerOpcion(sc, 0, 5));
		System.out.println("¿Se permiten cambios? 1.Sí 2.No");
		clase.put("cambiosPermitidos", Utilidades.leerOpcion(sc, 1, 2) == 1);
		clases.add(clase);
		System.out.println("Clase " + nombre + " añadida correctamente.");
	}

	private double pedirFactorPrecio(Scanner sc) {
		double factor = -1;
		do {
			try {
				System.out.println("Factor de precio (ej: 1.25):");
				factor = Double.parseDouble(sc.nextLine().trim());
				if (factor <= 0) {
					System.err.println("El factor tiene que ser mayor que 0.");
				}
			}catch (NumberFormatException e) {
				System.err.println("Introduzca un número.");
			}
		}while (factor <= 0);
		return factor;
	}

	private boolean existeClase(ArrayNode clases, String nombre) {
		boolean existe = false;
		for (JsonNode clase : clases) {
			if (clase.get("nombre").asText().equals(nombre)) {
				existe = true;
			}
		}
		return existe;
	}

	private void aniadirTipoPasajero(Scanner sc) {
		ArrayNode tipos = (ArrayNode) aeropuerto.get("pasajeros").get("tipos");
		System.out.println("Tipos actuales: " + listar(tipos));
		System.out.println("Introduce el nuevo tipo de pasajero:");
		String tipo;
		do {
			tipo = sc.nextLine().trim().toUpperCase();
			if (!tipo.matches("[A-Z_]+")) {
				System.err.println("Formato no válido (solo letras y _, ej: ESTUDIANTE). Inténtelo de nuevo:");
				tipo = null;
			}
			else if (existeTipoPasajero(tipos, tipo)) {
				System.err.println("Ese tipo ya existe. Inténtelo de nuevo:");
				tipo = null;
			}
		}while (tipo == null);
		tipos.add(tipo);
		System.out.println("Tipo de pasajero " + tipo + " añadido correctamente.");
	}

	private boolean existeTipoPasajero(ArrayNode tipos, String tipo) {
		boolean existe = false;
		for (JsonNode tipoExistente : tipos) {
			if (tipoExistente.asText().equals(tipo)) {
				existe = true;
			}
		}
		return existe;
	}

	private boolean existeTerminal(ArrayNode terminales, String codTerminal) {
		boolean existe = false;
		for (JsonNode terminal : terminales) {
			if (terminal.get("codigo").asText().equals(codTerminal)) {
				existe = true;
			}
		}
		return existe;
	}


	public void eliminarDeConfiguracion(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("¿Qué quieres eliminar?: 1.Terminal 2.ClaseDeLaReserva 3.TipoPasajeros  - (Los cambios se añadirán a archivos/configuracion_nuevo.json)");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		System.out.println("--------------------------------");
		switch(opcion) {
		case 1:
			eliminarTerminal(sc);
			break;
		case 2:
			eliminarClaseReserva(sc);
			break;
		case 3:
			eliminarTipoPasajeros(sc);
			break;
		}
		
		volcarJSON();
	}


	private void eliminarTipoPasajeros(Scanner sc) {
		ArrayNode tipos = (ArrayNode) aeropuerto.get("pasajeros").get("tipos");
		System.out.println("Tipos de Pasajero:");
		int cont = 0;
		for (JsonNode tipo: tipos) {
			System.out.println(" - " + (cont+1) + ". " + tipo.asText());
			cont++;
		}
		
		if (tipos.size() != 0) {
			System.out.println("Escoge su opcion:");
			int num = Utilidades.leerOpcion(sc, 1, tipos.size());
			num--;
			System.out.println("Se ha eliminado el tipo de pasajero " + tipos.remove(num).asText());
		}else {
			System.out.println("No quedan tipos de pasajeros para eliminar");
		}
		
	}


	private void eliminarClaseReserva(Scanner sc) {
		ArrayNode clases = (ArrayNode) aeropuerto.get("reservas").get("clases");
		int cont = 0;
		System.out.println("Clases de reserva:");
		for (JsonNode clase: clases) {
			System.out.println(" - " + (cont+1) + ". " + clase.get("nombre").asText());
			cont++;
		}
		if (clases.size() != 0) {
			System.out.println("Escoge su opcion:");
			int num = Utilidades.leerOpcion(sc, 1, clases.size());
			num--;
			System.out.println("Se ha eliminado la clase de reserva " + clases.remove(num).get("nombre").asText());
		}
		else {
			System.out.println("No quedan clases de reserva para eliminar.");
		}
		
	}


	private void eliminarTerminal(Scanner sc) {
		ArrayNode terminales = (ArrayNode) aeropuerto.get("terminales");
		int cont = 0;
		System.out.println("Terminales:");
		for (JsonNode terminal: terminales) {
			System.out.println(" - " + (cont+1) + ". " + terminal.get("codigo").asText());
			cont++;
		}
		if (terminales.size() != 0) {
			System.out.println("Escoge su opcion:");
			int num = Utilidades.leerOpcion(sc, 1, terminales.size());
			num--;
			System.out.println("Se ha eliminado la terminal " + terminales.remove(num).get("codigo").asText());
		}else {
			System.out.println("No quedan terminales disponibles para eliminar.");
		}
	}


	public void actualizarConfiguracion(Scanner sc) {
		int opcion;
		System.out.println("--------------------------------");
		System.out.println("Escoge un criterio a actualizar: 1.Nombre 2.VersionConfiguracion 3.UltimaActualizacion");
		opcion = Utilidades.leerOpcion(sc, 1, 3);
		System.out.println("--------------------------------");
		String dato = null;
		switch (opcion) {
		case 1:
			System.out.println("Introduce el nuevo nombre del aeropuerto:");
			do {
			dato = sc.nextLine().trim();
			if (dato.isBlank()) {
				dato = null;
				System.err.println("Introduzca Un nombre válido");
			}
			}while (dato == null);
			((ObjectNode) aeropuerto).put("nombre", dato);
			System.out.println("El nombre ha sido cambiado a " +dato);
			break;
		case 2:
			System.out.println("Introduce la nueva versión (d.d):");
			do {
				dato = sc.nextLine().trim();
				if ( dato.isBlank() || !dato.matches("\\d\\.\\d")) {
					dato = null;
					System.err.println("Formato no válido. Formato de ejemplo. 3.5. Inténtelo de nuevo.");
				}
			}while (dato == null);
			((ObjectNode)aeropuerto).put("versionConfiguracion", dato);
			System.out.println("Versión cambiada con éxito a la " + dato);
			break;
		case 3:
			LocalDate fecha;
			do { 
				fecha = Utilidades.pedirFecha(sc, "Introduce la nueva fecha de actualizacion" );
				if (LocalDate.parse(aeropuerto.get("ultimaActualizacion").asText()).isAfter(fecha)) {
					fecha = null;
					System.err.println("La fecha de actualizacion debe ser posterior a la previa");
				}
			}while (fecha == null);
			((ObjectNode)aeropuerto).put("ultimaActualizacion", String.valueOf(fecha));
			System.out.println("Fecha de la ultima actualizacion cambiada a " + fecha);
			break;
			
		}
		volcarJSON();
	}
	
	public JsonNode buscarTipoEquipaje (String tipo) {
		JsonNode reglasEquipaje = aeropuerto.get("equipaje").get("reglas");
		JsonNode regla = null;
		for (JsonNode reglaNode : reglasEquipaje) {
			if (reglaNode.get("tipo").asText().equals(tipo)) {
				regla = reglaNode;
			}
		}
		return regla;
	}

	public JsonNode buscarFranja (String tipo) {
		JsonNode franjas = aeropuerto.get("embarque").get("franjas");
		JsonNode franja = null;
		for (JsonNode franjaNode : franjas) {
			if (franjaNode.get("tipo").asText().equals(tipo)) {
				franja = franjaNode;
			}
		}
		return franja;
	}
}
