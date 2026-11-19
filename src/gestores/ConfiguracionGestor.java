package gestores;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
