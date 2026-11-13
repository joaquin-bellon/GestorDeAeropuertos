package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.w3c.dom.Element;

import gestores.IncidenciasGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Incidencia;
import modelo.Reserva;
import modelo.Vuelo;
import vista.VuelosPanel;

public class VuelosControlador implements ActionListener, ListSelectionListener, KeyListener {
	private PrincipalControlador principalControlador;
	private VuelosPanel vuelosPanel;
	private String rolUsuario;

	public VuelosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.vuelosPanel = principalControlador.getFrame().getVuelosPanel();
		this.rolUsuario = principalControlador.getUsuario().getRol();

		implementarListeners();
		aplicarPermisos();

		cargarTabla(VuelosGestor.getInstance().getVuelos());
		desactivarFormulario();
		habilitarBotonesSeleccion(false);
	}

	private boolean esAdmin() {
		return rolUsuario.equals("ADMIN");
	}

	private void aplicarPermisos() {
		if (!esAdmin()) {
			vuelosPanel.remove(vuelosPanel.getBtnAniadir());
			vuelosPanel.remove(vuelosPanel.getBtnModificar());
			vuelosPanel.remove(vuelosPanel.getBtnFicha());
			vuelosPanel.getBtnLimpiar().setLocation(20, 594);
		}
	}

	private void implementarListeners() {
		vuelosPanel.getBtnAniadir().addActionListener(this);
		vuelosPanel.getBtnModificar().addActionListener(this);
		vuelosPanel.getBtnLimpiar().addActionListener(this);
		vuelosPanel.getBtnGuardar().addActionListener(this);
		vuelosPanel.getBtnCancelar().addActionListener(this);
		vuelosPanel.getBtnFicha().addActionListener(this);
		vuelosPanel.getBtnVolver().addActionListener(this);
		vuelosPanel.getComboBoxFiltro().addActionListener(this);

		vuelosPanel.getTabla().getSelectionModel().addListSelectionListener(this);

		vuelosPanel.getTextFieldBuscar().addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vuelosPanel.getBtnAniadir()) {
			if (!esAdmin()) {
				vuelosPanel.mostrarError("Solo un administrador puede añadir.");
				return;
			}
			aniadir();
		} else if (e.getSource() == vuelosPanel.getBtnModificar()) {
			if (!esAdmin()) {
				vuelosPanel.mostrarError("Solo un administrador puede modificar.");
				return;
			}
			modificar();
		} else if (e.getSource() == vuelosPanel.getBtnFicha()) {
			if (!esAdmin()) {
				vuelosPanel.mostrarError("Solo un administrador puede ver la ficha.");
				return;
			}
			verFicha();
		} else if (e.getSource() == vuelosPanel.getBtnVolver()) {
			vuelosPanel.mostrarFicha(false);
		} else if (e.getSource() == vuelosPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == vuelosPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == vuelosPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == vuelosPanel.getComboBoxFiltro()) {
			filtrar();
		}
	}

	private void aniadir() {
		vaciarFormulario();
		cambiarEditarFormulario(true, true);
		vuelosPanel.getTextFieldOrigen().setText("MAD");
		vuelosPanel.getComboBoxEstado().setSelectedItem("PROGRAMADO");
		vuelosPanel.modoEdicion(true);
	}

	private void modificar() {
		cambiarEditarFormulario(true, false);
		habilitarBotonesSeleccion(false);
		vuelosPanel.modoEdicion(true);
	}

	private void guardar() {
		String codigo = vuelosPanel.getTextFieldCodigo().getText().trim().toUpperCase();
		Vuelo vuelo = VuelosGestor.getInstance().buscarPorCodigo(codigo);
		boolean aniadiendo = vuelosPanel.getTextFieldCodigo().isEditable();

		String comprobacion = comprobarFormulario(aniadiendo);
		if (!comprobacion.equals("")) {
			vuelosPanel.mostrarError(comprobacion);
			return;
		}

		String aerolinea = vuelosPanel.getTextFieldAerolinea().getText().trim().toUpperCase();
		String avion = vuelosPanel.getTextFieldAvion().getText().trim().toUpperCase();
		String destino = vuelosPanel.getTextFieldDestino().getText().trim().toUpperCase();
		String terminal = vuelosPanel.getTextFieldTerminal().getText().trim().toUpperCase();
		String puerta = vuelosPanel.getTextFieldPuerta().getText().trim().toUpperCase();
		LocalDateTime salida = leerFechaHora(vuelosPanel.getTextFieldSalida().getText());
		LocalDateTime llegada = leerFechaHora(vuelosPanel.getTextFieldLlegada().getText());
		String estado = vuelosPanel.getComboBoxEstado().getSelectedItem().toString();

		String resultado;
		String mensaje;
		if (aniadiendo) {
			resultado = VuelosGestor.getInstance().insertarVuelo(codigo, aerolinea, avion, terminal, puerta, destino, salida, llegada, rolUsuario);
			mensaje = "Vuelo " + codigo + " añadido correctamente.";
		} else {
			resultado = VuelosGestor.getInstance().modificarVuelo(vuelo, terminal, puerta, salida, llegada, estado, rolUsuario);
			mensaje = "Vuelo " + codigo + " modificado correctamente.";
		}
		if (!resultado.equals("")) {
			vuelosPanel.mostrarError(resultado);
			return;
		}

		recargarPanel();
		vuelosPanel.modoEdicion(false);
		seleccionarEnTabla(codigo);
		vuelosPanel.mostrarExito(mensaje);
	}

	private String comprobarFormulario(boolean aniadiendo) {
		VuelosGestor gestor = VuelosGestor.getInstance();
		String codigo = vuelosPanel.getTextFieldCodigo().getText().trim().toUpperCase();
		String aerolinea = vuelosPanel.getTextFieldAerolinea().getText().trim().toUpperCase();
		String avion = vuelosPanel.getTextFieldAvion().getText().trim().toUpperCase();
		String destino = vuelosPanel.getTextFieldDestino().getText().trim().toUpperCase();
		String terminal = vuelosPanel.getTextFieldTerminal().getText().trim().toUpperCase();
		String puerta = vuelosPanel.getTextFieldPuerta().getText().trim().toUpperCase();
		String salidaTexto = vuelosPanel.getTextFieldSalida().getText().trim();
		String llegadaTexto = vuelosPanel.getTextFieldLlegada().getText().trim();

		if (codigo.isBlank() || aerolinea.isBlank() || avion.isBlank() || destino.isBlank() || terminal.isBlank()
				|| puerta.isBlank() || salidaTexto.isBlank() || llegadaTexto.isBlank()) {
			return "Rellena todos los campos.";
		}

		if (aniadiendo) {
			if (gestor.buscarEn("Aerolineas", "Aerolinea", "codigo", aerolinea) == null) {
				return "No existe la aerolinea " + aerolinea + ".";
			}
			if (!codigo.matches(aerolinea + "\\d{4}")) {
				return "El codigo debe ser " + aerolinea + " y 4 cifras.";
			}
			if (gestor.buscarPorCodigo(codigo) != null) {
				return "Ya existe el vuelo " + codigo + ".";
			}
			Element elementoAvion = gestor.buscarEn("Aviones", "Avion", "matricula", avion);
			if (elementoAvion == null) {
				return "No existe el avion " + avion + ".";
			}
			if (!elementoAvion.getAttribute("aerolinea").equals(aerolinea)) {
				return "El avion " + avion + " no es de " + aerolinea + ".";
			}
			if (destino.equals("MAD")) {
				return "El destino no puede ser el origen.";
			}
			if (gestor.buscarEn("Destinos", "Destino", "codigo", destino) == null) {
				return "No existe el destino " + destino + ".";
			}
		}

		Element elementoTerminal = gestor.buscarEn("Terminales", "Terminal", "codigo", terminal);
		if (elementoTerminal == null || !elementoTerminal.getAttribute("estado").equals("OPERATIVA")) {
			return "Terminal no valida o no operativa.";
		}
		Element elementoPuerta = gestor.buscarEn("Puertas", "Puerta", "codigo", puerta);
		if (elementoPuerta == null) {
			return "No existe la puerta " + puerta + ".";
		}
		if (!elementoPuerta.getAttribute("terminal").equals(terminal)) {
			return "La puerta " + puerta + " no es de la " + terminal + ".";
		}
		if (!elementoPuerta.getAttribute("estado").equals("OPERATIVA")) {
			return "La puerta " + puerta + " no esta operativa.";
		}

		LocalDateTime salida = leerFechaHora(salidaTexto);
		LocalDateTime llegada = leerFechaHora(llegadaTexto);
		if (salida == null || llegada == null) {
			return "Las fechas deben ser aaaa-mm-dd hh:mm.";
		}
		if (!llegada.isAfter(salida)) {
			return "La llegada debe ser posterior a la salida.";
		}

		return "";
	}

	private LocalDateTime leerFechaHora(String texto) {
		try {
			return LocalDateTime.parse(texto.trim().replace(" ", "T"));
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private void verFicha() {
		Vuelo vuelo = VuelosGestor.getInstance().buscarPorCodigo(codigoSeleccionado());
		int plazas = VuelosGestor.getInstance().recogerTamanioDelAvionDeUnVuelo(vuelo.getAvion());
		ArrayList<Reserva> confirmadas = ReservasGestor.getInstance().buscarResevaConfirmadasPorVuelo(vuelo.getCodigo());
		ArrayList<Incidencia> incidencias = IncidenciasGestor.getInstance().buscarIncidenciaPorVuelo(vuelo.getCodigo());
		int porcentaje = (int) Math.round(confirmadas.size() * 100.0 / plazas);

		vuelosPanel.getLblTituloFicha().setText("Ficha del vuelo " + vuelo.getCodigo());
		vuelosPanel.getLblOcupacion().setText("Ocupacion: " + confirmadas.size() + " de " + plazas + " plazas");
		vuelosPanel.getProgressBarOcupacion().setValue(Math.min(porcentaje, 100));
		vuelosPanel.getProgressBarOcupacion().setString(porcentaje + " %");

		int abiertas = 0;
		vuelosPanel.getModeloIncidencias().setRowCount(0);
		for (Incidencia incidencia : incidencias) {
			vuelosPanel.getModeloIncidencias().addRow(new Object[] {incidencia.getCodigo(), incidencia.getTipo(), incidencia.getPrioridad(), incidencia.getEstado()});
			if (incidencia.getEstado().equals("ABIERTA")) {
				abiertas++;
			}
		}

		vuelosPanel.getModeloReservas().setRowCount(0);
		for (Reserva reserva : confirmadas) {
			vuelosPanel.getModeloReservas().addRow(new Object[] {reserva.getIdReserva(), reserva.getPasajero(), reserva.getAsiento(), reserva.getClase()});
		}

		String sobreventa = "No";
		if (confirmadas.size() > plazas) {
			sobreventa = "Si, " + (confirmadas.size() - plazas) + " reservas de mas";
		}

		vuelosPanel.getModeloResumen().setRowCount(0);
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Ruta", vuelo.getOrigen() + " - " + vuelo.getDestino()});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Salida", vuelo.getSalida().toString().replace("T", " ")});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Llegada", vuelo.getLlegada().toString().replace("T", " ")});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Avion", vuelo.getAvion()});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Terminal y puerta", vuelo.getTerminal() + " - " + vuelo.getPuerta()});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Estado", vuelo.getEstado()});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Plazas", plazas});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Reservas confirmadas", confirmadas.size()});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Sobreventa", sobreventa});
		vuelosPanel.getModeloResumen().addRow(new Object[] {"Incidencias", incidencias.size() + " (" + abiertas + " abiertas)"});

		vuelosPanel.mostrarFicha(true);
	}

	private String codigoSeleccionado() {
		return String.valueOf(vuelosPanel.getTabla().getValueAt(vuelosPanel.getTabla().getSelectedRow(), 0));
	}

	private void cancelar() {
		vuelosPanel.modoEdicion(false);
		cambiarEditarFormulario(false, false);
		vaciarFormulario();
	}

	public void recargarPanel() {
		vuelosPanel.getTextFieldBuscar().setText("");
		cargarTabla(VuelosGestor.getInstance().getVuelos());
		desactivarFormulario();
		vaciarFormulario();
		vuelosPanel.getLblAdvertencia().setText("");
	}

	private void seleccionarEnTabla(String codigo) {
		for (int i = 0; i < vuelosPanel.getModeloTabla().getRowCount(); i++) {
			if (vuelosPanel.getModeloTabla().getValueAt(i, 0).equals(codigo)) {
				vuelosPanel.getTabla().changeSelection(i, 0, false, false);
			}
		}
	}

	private void habilitarBotonesSeleccion(boolean seleccion) {
		vuelosPanel.habilitarBtnEliminarYModificar(seleccion);
		vuelosPanel.getBtnFicha().setEnabled(seleccion);
	}

	private void desactivarFormulario() {
		vuelosPanel.getTextFieldOrigen().setEditable(false);
		cambiarEditarFormulario(false, false);
	}

	private void cargarTabla(ArrayList<Vuelo> listaVuelos) {
		vuelosPanel.getModeloTabla().setRowCount(0);
		for (Vuelo vuelo : listaVuelos) {
			vuelosPanel.getModeloTabla().addRow(new Object[] {vuelo.getCodigo(), vuelo.getOrigen(), vuelo.getDestino(), vuelo.getSalida().toString().replace("T", " "), vuelo.getAvion(), vuelo.getEstado()});
		}
	}

	private void vaciarFormulario() {
		vuelosPanel.getTextFieldCodigo().setText("");
		vuelosPanel.getTextFieldAerolinea().setText("");
		vuelosPanel.getTextFieldAvion().setText("");
		vuelosPanel.getTextFieldOrigen().setText("");
		vuelosPanel.getTextFieldDestino().setText("");
		vuelosPanel.getTextFieldSalida().setText("");
		vuelosPanel.getTextFieldLlegada().setText("");
		vuelosPanel.getTextFieldTerminal().setText("");
		vuelosPanel.getTextFieldPuerta().setText("");
		vuelosPanel.getComboBoxEstado().setSelectedIndex(0);
		vuelosPanel.getTabla().clearSelection();
		vuelosPanel.mostrarFicha(false);
		habilitarBotonesSeleccion(false);
	}

	private void rellenarFormulario(Vuelo vuelo) {
		vuelosPanel.getTextFieldCodigo().setText(vuelo.getCodigo());
		vuelosPanel.getTextFieldAerolinea().setText(vuelo.getAerolinea());
		vuelosPanel.getTextFieldAvion().setText(vuelo.getAvion());
		vuelosPanel.getTextFieldOrigen().setText(vuelo.getOrigen());
		vuelosPanel.getTextFieldDestino().setText(vuelo.getDestino());
		vuelosPanel.getTextFieldSalida().setText(vuelo.getSalida().toString().replace("T", " "));
		vuelosPanel.getTextFieldLlegada().setText(vuelo.getLlegada().toString().replace("T", " "));
		vuelosPanel.getTextFieldTerminal().setText(vuelo.getTerminal());
		vuelosPanel.getTextFieldPuerta().setText(vuelo.getPuerta());
		vuelosPanel.getComboBoxEstado().setSelectedItem(vuelo.getEstado());
	}

	private void cambiarEditarFormulario(boolean activo, boolean aniadiendo) {
		vuelosPanel.getTextFieldCodigo().setEditable(activo && aniadiendo);
		vuelosPanel.getTextFieldAerolinea().setEditable(activo && aniadiendo);
		vuelosPanel.getTextFieldAvion().setEditable(activo && aniadiendo);
		vuelosPanel.getTextFieldDestino().setEditable(activo && aniadiendo);
		vuelosPanel.getTextFieldSalida().setEditable(activo);
		vuelosPanel.getTextFieldLlegada().setEditable(activo);
		vuelosPanel.getTextFieldTerminal().setEditable(activo);
		vuelosPanel.getTextFieldPuerta().setEditable(activo);
		vuelosPanel.getComboBoxEstado().setEnabled(activo && !aniadiendo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = vuelosPanel.getTabla().getSelectedRow();
			boolean seleccion = filaInt != -1;
			if (seleccion) {
				Vuelo vuelo = VuelosGestor.getInstance().buscarPorCodigo(
						String.valueOf(vuelosPanel.getTabla().getValueAt(filaInt, 0)));
				vuelosPanel.mostrarFicha(false);
				rellenarFormulario(vuelo);
			}
			habilitarBotonesSeleccion(seleccion);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		filtrar();
	}

	private void filtrar() {
		String criterio = vuelosPanel.getComboBoxFiltro().getSelectedItem().toString();
		String texto = vuelosPanel.getTextFieldBuscar().getText();
		cargarTabla(VuelosGestor.getInstance().filtrarVuelos(criterio, texto));
	}

}
