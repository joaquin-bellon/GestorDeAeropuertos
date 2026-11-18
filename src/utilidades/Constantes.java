package utilidades;

public class Constantes {
	public static final String ROL_ADMIN = "ADMIN";
	public static final String ROL_USER = "USER";

	public static final String VUELO_PROGRAMADO = "PROGRAMADO";
	public static final String VUELO_RETRASADO = "RETRASADO";
	public static final String VUELO_CANCELADO = "CANCELADO";
	public static final String VUELO_EMBARCANDO = "EMBARCANDO";
	public static final String VUELO_CERRADO = "CERRADO";
	public static final String[] ESTADOS_VUELO = {VUELO_PROGRAMADO, VUELO_RETRASADO, VUELO_CANCELADO, VUELO_EMBARCANDO, VUELO_CERRADO};
	public static final String AEROPUERTO_ORIGEN = "MAD";
	public static final String ESTADO_OPERATIVA = "OPERATIVA";

	public static final String RESERVA_CONFIRMADA = "CONFIRMADA";
	public static final String RESERVA_CANCELADA = "CANCELADA";
	public static final String MONEDA = "EUR";
	public static final String[] CLASES_RESERVA = {"ECONOMICA", "BUSINESS", "PRIMERA"};
	public static final String[] CANALES_RESERVA = {"WEB", "APP", "TAQUILLA", "AGENCIA"};

	public static final String[] TIPOS_PASAJERO = {"ADULTO", "VIP", "MENOR"};

	public static final String EQUIPAJE_REGISTRADO = "REGISTRADO";
	public static final String EQUIPAJE_PERDIDO = "PERDIDO";
	public static final String[] ESTADOS_EQUIPAJE = {EQUIPAJE_REGISTRADO, "CARGADO", "EN_TRANSITO", "ENTREGADO", EQUIPAJE_PERDIDO};
	public static final String[] TIPOS_EQUIPAJE = {"FACTURADO", "MANO", "ESPECIAL"};
	public static final String[] PRIORIDADES_EQUIPAJE = {"NORMAL", "PRIORITARIO", "FRAGIL"};

	public static final String EMBARQUE_PENDIENTE = "PENDIENTE";
	public static final String EMBARQUE_EMBARCANDO = "EMBARCANDO";
	public static final String EMBARQUE_EMBARCADO = "EMBARCADO";
	public static final String EMBARQUE_NO_EMBARCADO = "NO_EMBARCADO";
	public static final String[] ESTADOS_EMBARQUE = {EMBARQUE_EMBARCANDO, EMBARQUE_EMBARCADO, EMBARQUE_NO_EMBARCADO};

	public static final String INCIDENCIA_ABIERTA = "ABIERTA";
	public static final String INCIDENCIA_CERRADA = "CERRADA";
	public static final String TIPO_INCIDENCIA_RETRASO = "RETRASO";
	public static final String[] TIPOS_INCIDENCIA = {TIPO_INCIDENCIA_RETRASO, "CANCELACION", "CAMBIO_PUERTA", "EQUIPAJE_PERDIDO", "AVERIA"};
	public static final String PRIORIDAD_BAJA = "BAJA";
	public static final String PRIORIDAD_MEDIA = "MEDIA";
	public static final String PRIORIDAD_ALTA = "ALTA";
	public static final String PRIORIDAD_CRITICA = "CRITICA";
	public static final String[] PRIORIDADES_INCIDENCIA = {PRIORIDAD_BAJA, PRIORIDAD_MEDIA, PRIORIDAD_ALTA, PRIORIDAD_CRITICA};
}
