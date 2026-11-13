package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import comparadores.ComparadorIncidencias;
import gestores.EmbarquesGestor;
import gestores.EquipajesGestor;
import gestores.IncidenciasGestor;
import gestores.PasajerosGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Embarque;
import modelo.Equipaje;
import modelo.Incidencia;
import modelo.Pasajero;
import modelo.Reserva;
import modelo.Vuelo;
import vista.IncidenciasPanel;

public class IncidenciasControlador implements ActionListener, ListSelectionListener, KeyListener {
	private PrincipalControlador principalControlador;
	private IncidenciasPanel incidenciasPanel;
	private String rolUsuario;

	public IncidenciasControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.incidenciasPanel = principalControlador.getFrame().getIncidenciasPanel();
		this.rolUsuario = principalControlador.getUsuario().getRol();

		implementarListeners();
		aplicarPermisos();

		cargarTabla(IncidenciasGestor.getInstance().getIncidencias());
		desactivarFormulario();
		habilitarBotonesSeleccion(null);
	}

	private boolean esAdmin() {
		return rolUsuario.equals("ADMIN");
	}

	private void aplicarPermisos() {
		if (!esAdmin()) {
			incidenciasPanel.remove(incidenciasPanel.getBtnModificar());
			incidenciasPanel.remove(incidenciasPanel.getBtnEliminar());
			incidenciasPanel.getPanelFormulario().remove(incidenciasPanel.getBtnCerrarIncidencia());
			incidenciasPanel.getBtnLimpiar().setLocation(172, 594);
			incidenciasPanel.getBtnVerRelacionados().setLocation(20, 398);
		}
	}

	private void implementarListeners() {
		incidenciasPanel.getBtnAniadir().addActionListener(this);
		incidenciasPanel.getBtnModificar().addActionListener(this);
		incidenciasPanel.getBtnEliminar().addActionListener(this);
		incidenciasPanel.getBtnLimpiar().addActionListener(this);
		incidenciasPanel.getBtnGuardar().addActionListener(this);
		incidenciasPanel.getBtnCancelar().addActionListener(this);
		incidenciasPanel.getBtnNoEliminar().addActionListener(this);
		incidenciasPanel.getBtnSiEliminar().addActionListener(this);
		incidenciasPanel.getBtnCerrarIncidencia().addActionListener(this);
		incidenciasPanel.getBtnVerRelacionados().addActionListener(this);
		incidenciasPanel.getBtnVolver().addActionListener(this);
		incidenciasPanel.getComboBoxFiltro().addActionListener(this);
		incidenciasPanel.getComboBoxOrdenar().addActionListener(this);

		incidenciasPanel.getTabla().getSelectionModel().addListSelectionListener(this);

		incidenciasPanel.getTextFieldBuscar().addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == incidenciasPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == incidenciasPanel.getBtnModificar()) {
			if (!esAdmin()) {
				incidenciasPanel.mostrarError("Solo un administrador puede modificar.");
				return;
			}
			modificar();
		} else if (e.getSource() == incidenciasPanel.getBtnEliminar()) {
			if (!esAdmin()) {
				incidenciasPanel.mostrarError("Solo un administrador puede eliminar.");
				return;
			}
			if (incidenciasPanel.getTabla().getSelectedRow() == -1) {
				incidenciasPanel.mostrarError("Selecciona la incidencia a eliminar.");
			}
			else {
				incidenciasPanel.modoConfirmarEliminar(true);
			}
		} else if (e.getSource() == incidenciasPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == incidenciasPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == incidenciasPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == incidenciasPanel.getBtnNoEliminar()) {
			incidenciasPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == incidenciasPanel.getBtnSiEliminar()) {
			eliminar();
			incidenciasPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == incidenciasPanel.getBtnCerrarIncidencia()) {
			if (!esAdmin()) {
				incidenciasPanel.mostrarError("Solo un administrador puede cerrarla.");
				return;
			}
			cerrarIncidencia();
		} else if (e.getSource() == incidenciasPanel.getBtnVerRelacionados()) {
			verRelacionados();
		} else if (e.getSource() == incidenciasPanel.getBtnVolver()) {
			incidenciasPanel.mostrarRelacionados(false);
		} else if (e.getSource() == incidenciasPanel.getComboBoxFiltro()) {
			filtrar();
		} else if (e.getSource() == incidenciasPanel.getComboBoxOrdenar()) {
			filtrar();
		}
	}

	private void aniadir() {
		vaciarFormulario();
		cambiarEditarFormulario(true);
		incidenciasPanel.getTextFieldCodigo().setText(IncidenciasGestor.getInstance().crearNuevoIdentificador());
		incidenciasPanel.getTextFieldFecha().setText(String.valueOf(LocalDate.now()));
		incidenciasPanel.getTextFieldEstado().setText("ABIERTA");
		incidenciasPanel.modoEdicion(true);
	}

	private void modificar() {
		cambiarEditarFormulario(true);
		habilitarBotonesSeleccion(null);
		incidenciasPanel.modoEdicion(true);
	}

	private void eliminar() {
		String codigo = codigoSeleccionado();
		String resultado = IncidenciasGestor.getInstance().eliminarIncidencia(codigo, rolUsuario);
		if (resultado.equals("")) {
			recargarPanel();
			incidenciasPanel.mostrarExito("Se ha eliminado la incidencia " + codigo + ".");
		}
		else {
			incidenciasPanel.mostrarError(resultado);
		}
	}

	private void cerrarIncidencia() {
		String codigo = codigoSeleccionado();
		String resultado = IncidenciasGestor.getInstance().cerrarIncidencia(codigo, rolUsuario);
		if (resultado.equals("")) {
			recargarPanel();
			seleccionarEnTabla(codigo);
			incidenciasPanel.mostrarExito("Se ha cerrado la incidencia " + codigo + ".");
		}
		else {
			incidenciasPanel.mostrarError(resultado);
		}
	}

	private void guardar() {
		String comprobacion = comprobarFormulario();
		if (!comprobacion.equals("")) {
			incidenciasPanel.mostrarError(comprobacion);
			return;
		}

		String codigo = incidenciasPanel.getTextFieldCodigo().getText().trim();
		String fecha = incidenciasPanel.getTextFieldFecha().getText().trim();
		String vuelo = incidenciasPanel.getTextFieldVuelo().getText().trim().toUpperCase();
		String tipo = incidenciasPanel.getComboBoxTipo().getSelectedItem().toString();
		int duracion = Integer.parseInt(incidenciasPanel.getTextFieldDuracion().getText().trim());
		String descripcion = incidenciasPanel.getTextFieldDescripcion().getText().trim();
		String prioridad = incidenciasPanel.getComboBoxPrioridad().getSelectedItem().toString();

		Incidencia incidencia = IncidenciasGestor.getInstance().buscarIncidenciaPorCodigo(codigo);
		String mensaje;
		if (incidencia == null) {
			IncidenciasGestor.getInstance().insertarIncidencia(fecha, codigo, vuelo, tipo, duracion, descripcion, prioridad);
			mensaje = "Incidencia " + codigo + " creada correctamente.";
		} else {
			String resultado = IncidenciasGestor.getInstance().modificarIncidencia(incidencia, fecha, vuelo, tipo, duracion, descripcion, prioridad, rolUsuario);
			if (!resultado.equals("")) {
				incidenciasPanel.mostrarError(resultado);
				return;
			}
			mensaje = "Incidencia " + codigo + " modificada correctamente.";
		}

		recargarPanel();
		incidenciasPanel.modoEdicion(false);
		seleccionarEnTabla(codigo);
		incidenciasPanel.mostrarExito(mensaje);
	}

	private String comprobarFormulario() {
		String fecha = incidenciasPanel.getTextFieldFecha().getText().trim();
		String vuelo = incidenciasPanel.getTextFieldVuelo().getText().trim().toUpperCase();
		String duracionTexto = incidenciasPanel.getTextFieldDuracion().getText().trim();
		String descripcion = incidenciasPanel.getTextFieldDescripcion().getText().trim();

		if (fecha.isBlank() || vuelo.isBlank() || duracionTexto.isBlank() || descripcion.isBlank()) {
			return "Rellena todos los campos.";
		}

		try {
			LocalDate.parse(fecha);
		} catch (DateTimeParseException e) {
			return "La fecha debe tener el formato aaaa-mm-dd.";
		}

		if (VuelosGestor.getInstance().buscarPorCodigo(vuelo) == null) {
			return "No existe el vuelo " + vuelo + ".";
		}

		int duracion;
		try {
			duracion = Integer.parseInt(duracionTexto);
		} catch (NumberFormatException e) {
			return "La duracion debe ser un numero entero.";
		}
		if (duracion < 0) {
			return "La duracion no puede ser negativa.";
		}

		if (descripcion.contains("|")) {
			return "La descripcion no puede contener |";
		}

		return "";
	}

	private void verRelacionados() {
		Incidencia incidencia = IncidenciasGestor.getInstance().buscarIncidenciaPorCodigo(codigoSeleccionado());
		Vuelo vuelo = VuelosGestor.getInstance().buscarPorCodigo(incidencia.getVuelo());
		ArrayList<Reserva> reservas = ReservasGestor.getInstance().buscarResevasPorVuelo(incidencia.getVuelo());

		incidenciasPanel.getLblTituloRelacionados().setText("Relacionados con " + incidencia.getCodigo());
		incidenciasPanel.getModeloReservas().setRowCount(0);
		incidenciasPanel.getModeloPasajeros().setRowCount(0);
		incidenciasPanel.getModeloEquipajes().setRowCount(0);
		incidenciasPanel.getModeloEmbarques().setRowCount(0);

		ArrayList<Pasajero> pasajerosAniadidos = new ArrayList<Pasajero>();
		int confirmadas = 0;
		for (Reserva reserva : reservas) {
			incidenciasPanel.getModeloReservas().addRow(new Object[] {reserva.getIdReserva(), reserva.getPasajero(), reserva.getAsiento(), reserva.getEstado()});
			if (reserva.getEstado().equals("CONFIRMADA")) {
				confirmadas++;
			}

			Pasajero pasajero = PasajerosGestor.getInstance().buscarPorCodPasajero(reserva.getPasajero());
			if (pasajero != null && !pasajerosAniadidos.contains(pasajero)) {
				pasajerosAniadidos.add(pasajero);
				incidenciasPanel.getModeloPasajeros().addRow(new Object[] {pasajero.getCodPasajero(), pasajero.getNombre(), pasajero.getApellidos()});
			}

			for (Equipaje equipaje : EquipajesGestor.getInstance().buscarEquipajesPorReserva(reserva.getIdReserva())) {
				incidenciasPanel.getModeloEquipajes().addRow(new Object[] {equipaje.getCodEquipaje(), equipaje.getCodReserva(), equipaje.getPeso(), equipaje.getEstado()});
			}

			Embarque embarque = EmbarquesGestor.getInstance().buscarPorReserva(reserva.getIdReserva());
			if (embarque != null) {
				incidenciasPanel.getModeloEmbarques().addRow(new Object[] {embarque.getCodEmbarque(), embarque.getCodReserva(), embarque.getEstado()});
			}
		}

		incidenciasPanel.getModeloVuelo().setRowCount(0);
		if (vuelo != null) {
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Código", vuelo.getCodigo()});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Aerolínea", vuelo.getAerolinea()});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Ruta", vuelo.getOrigen() + " - " + vuelo.getDestino()});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Salida", vuelo.getSalida().toString().replace("T", " ")});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Terminal", vuelo.getTerminal()});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Puerta", vuelo.getPuerta()});
			incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Estado", vuelo.getEstado()});
		}
		incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Reservas", reservas.size() + " (" + confirmadas + " confirmadas)"});
		incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Pasajeros", pasajerosAniadidos.size()});
		incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Equipajes", incidenciasPanel.getModeloEquipajes().getRowCount()});
		incidenciasPanel.getModeloVuelo().addRow(new Object[] {"Embarques", incidenciasPanel.getModeloEmbarques().getRowCount()});

		incidenciasPanel.mostrarRelacionados(true);
	}

	private String codigoSeleccionado() {
		return String.valueOf(incidenciasPanel.getTabla().getValueAt(incidenciasPanel.getTabla().getSelectedRow(), 0));
	}

	private void cancelar() {
		incidenciasPanel.modoEdicion(false);
		cambiarEditarFormulario(false);
		vaciarFormulario();
	}

	public void recargarPanel() {
		incidenciasPanel.getTextFieldBuscar().setText("");
		cargarTabla(IncidenciasGestor.getInstance().getIncidencias());
		desactivarFormulario();
		vaciarFormulario();
		incidenciasPanel.getLblAdvertencia().setText("");
	}

	private void seleccionarEnTabla(String codigo) {
		for (int i = 0; i < incidenciasPanel.getModeloTabla().getRowCount(); i++) {
			if (incidenciasPanel.getModeloTabla().getValueAt(i, 0).equals(codigo)) {
				incidenciasPanel.getTabla().setRowSelectionInterval(i, i);
				incidenciasPanel.getTabla().scrollRectToVisible(incidenciasPanel.getTabla().getCellRect(i, 0, true));
			}
		}
	}

	private void habilitarBotonesSeleccion(Incidencia incidencia) {
		boolean seleccion = incidencia != null;
		incidenciasPanel.habilitarBtnEliminarYModificar(seleccion);
		incidenciasPanel.getBtnVerRelacionados().setEnabled(seleccion);
		incidenciasPanel.getBtnCerrarIncidencia().setEnabled(seleccion && incidencia.getEstado().equals("ABIERTA"));
	}

	private void desactivarFormulario() {
		incidenciasPanel.getTextFieldCodigo().setEditable(false);
		incidenciasPanel.getTextFieldEstado().setEditable(false);
		cambiarEditarFormulario(false);
	}

	private void cargarTabla(ArrayList<Incidencia> listaIncidencias) {
		ArrayList<Incidencia> lista = devolverListaOrdenada(listaIncidencias);
		incidenciasPanel.getModeloTabla().setRowCount(0);
		for (Incidencia incidencia : lista) {
			incidenciasPanel.getModeloTabla().addRow(new Object[] {incidencia.getCodigo(), incidencia.getFecha(), incidencia.getVuelo(), incidencia.getTipo(), incidencia.getPrioridad(), incidencia.getEstado()});
		}
	}

	private ArrayList<Incidencia> devolverListaOrdenada(ArrayList<Incidencia> listaIncidencias) {
		ArrayList<Incidencia> lista = new ArrayList<Incidencia>(listaIncidencias);
		Collections.sort(lista, new ComparadorIncidencias(String.valueOf(incidenciasPanel.getComboBoxOrdenar().getSelectedItem())));
		return lista;
	}

	private void vaciarFormulario() {
		incidenciasPanel.getTextFieldCodigo().setText("");
		incidenciasPanel.getTextFieldFecha().setText("");
		incidenciasPanel.getTextFieldVuelo().setText("");
		incidenciasPanel.getComboBoxTipo().setSelectedIndex(0);
		incidenciasPanel.getTextFieldDuracion().setText("");
		incidenciasPanel.getTextFieldDescripcion().setText("");
		incidenciasPanel.getComboBoxPrioridad().setSelectedIndex(0);
		incidenciasPanel.getTextFieldEstado().setText("");
		incidenciasPanel.getTabla().clearSelection();
		incidenciasPanel.mostrarRelacionados(false);
		habilitarBotonesSeleccion(null);
	}

	private void rellenarFormulario(Incidencia incidencia) {
		incidenciasPanel.getTextFieldCodigo().setText(incidencia.getCodigo());
		incidenciasPanel.getTextFieldFecha().setText(incidencia.getFecha());
		incidenciasPanel.getTextFieldVuelo().setText(incidencia.getVuelo());
		incidenciasPanel.getComboBoxTipo().setSelectedItem(incidencia.getTipo());
		incidenciasPanel.getTextFieldDuracion().setText(String.valueOf(incidencia.getDuracion()));
		incidenciasPanel.getTextFieldDescripcion().setText(incidencia.getDescripcion());
		incidenciasPanel.getComboBoxPrioridad().setSelectedItem(incidencia.getPrioridad());
		incidenciasPanel.getTextFieldEstado().setText(incidencia.getEstado());
	}

	private void cambiarEditarFormulario(boolean activo) {
		incidenciasPanel.getTextFieldFecha().setEditable(activo);
		incidenciasPanel.getTextFieldVuelo().setEditable(activo);
		incidenciasPanel.getComboBoxTipo().setEnabled(activo);
		incidenciasPanel.getTextFieldDuracion().setEditable(activo);
		incidenciasPanel.getTextFieldDescripcion().setEditable(activo);
		incidenciasPanel.getComboBoxPrioridad().setEnabled(activo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = incidenciasPanel.getTabla().getSelectedRow();
			Incidencia incidencia = null;
			if (filaInt != -1) {
				incidencia = IncidenciasGestor.getInstance().buscarIncidenciaPorCodigo(
						String.valueOf(incidenciasPanel.getTabla().getValueAt(filaInt, 0)));
				incidenciasPanel.mostrarRelacionados(false);
				rellenarFormulario(incidencia);
			}
			habilitarBotonesSeleccion(incidencia);
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
		String criterio = incidenciasPanel.getComboBoxFiltro().getSelectedItem().toString();
		String texto = incidenciasPanel.getTextFieldBuscar().getText();
		cargarTabla(IncidenciasGestor.getInstance().filtrarIncidencias(criterio, texto));
	}

}
