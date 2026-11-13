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

import comparadores.ComparadorReservas;
import gestores.EmbarquesGestor;
import gestores.EquipajesGestor;
import gestores.PasajerosGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Embarque;
import modelo.Equipaje;
import modelo.Reserva;
import modelo.Vuelo;
import vista.ReservasPanel;

public class ReservasControlador implements ActionListener, ListSelectionListener, KeyListener {
	private PrincipalControlador principalControlador;
	private ReservasPanel reservasPanel;

	public ReservasControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.reservasPanel = principalControlador.getFrame().getReservasPanel();

		implementarListeners();

		cargarTabla(ReservasGestor.getInstance().getReservas());
		desactivarFormulario();
		reservasPanel.habilitarBtnEliminarYModificar(false);
	}

	private void implementarListeners() {
		reservasPanel.getBtnAniadir().addActionListener(this);
		reservasPanel.getBtnModificar().addActionListener(this);
		reservasPanel.getBtnEliminar().addActionListener(this);
		reservasPanel.getBtnLimpiar().addActionListener(this);
		reservasPanel.getBtnGuardar().addActionListener(this);
		reservasPanel.getBtnCancelar().addActionListener(this);
		reservasPanel.getBtnNoEliminar().addActionListener(this);
		reservasPanel.getBtnSiEliminar().addActionListener(this);
		reservasPanel.getComboBoxFiltro().addActionListener(this);
		reservasPanel.getComboBoxOrdenar().addActionListener(this);

		reservasPanel.getTabla().getSelectionModel().addListSelectionListener(this);

		reservasPanel.getTextFieldBuscar().addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reservasPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == reservasPanel.getBtnModificar()) {
			modificar();
		} else if (e.getSource() == reservasPanel.getBtnEliminar()) {
			if (reservasPanel.getTabla().getSelectedRow() == -1) {
				reservasPanel.mostrarError("Selecciona la reserva a cancelar.");
			}
			else {
				reservasPanel.modoConfirmarEliminar(true);
			}
		} else if (e.getSource() == reservasPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == reservasPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == reservasPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == reservasPanel.getBtnNoEliminar()) {
			reservasPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == reservasPanel.getBtnSiEliminar()) {
			cancelarReserva();
			reservasPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == reservasPanel.getComboBoxFiltro()) {
			filtrar();
		} else if (e.getSource() == reservasPanel.getComboBoxOrdenar()) {
			filtrar();
		}
	}

	private void aniadir() {
		vaciarFormulario();
		cambiarEditarFormulario(true, true);
		reservasPanel.getTextFieldCodigo().setText(ReservasGestor.getInstance().crearNuevoIdentificador());
		reservasPanel.getTextFieldFecha().setText(String.valueOf(LocalDate.now()));
		reservasPanel.getTextFieldEstado().setText("CONFIRMADA");
		reservasPanel.modoEdicion(true);
	}

	private void modificar() {
		cambiarEditarFormulario(true, false);
		reservasPanel.modoEdicion(true);
	}

	private void cancelarReserva() {
		String codReserva = String.valueOf(reservasPanel.getTabla().getValueAt(reservasPanel.getTabla().getSelectedRow(), 0));
		String resultado = ReservasGestor.getInstance().cancelarReserva(codReserva);
		if (resultado.equals("")) {
			recargarPanel();
			mostrarAsociados(codReserva);
			reservasPanel.mostrarExito("Se ha cancelado la reserva " + codReserva + ".");
		}
		else {
			reservasPanel.mostrarError(resultado);
		}
	}

	private void mostrarAsociados(String codReserva) {
		reservasPanel.getLblTituloAsociados().setText("Reserva " + codReserva + " cancelada");

		reservasPanel.getModeloEquipajes().setRowCount(0);
		for (Equipaje equipaje : EquipajesGestor.getInstance().buscarEquipajesPorReserva(codReserva)) {
			reservasPanel.getModeloEquipajes().addRow(new Object[] {equipaje.getCodEquipaje(), equipaje.getPeso(), equipaje.getTipo(), equipaje.getEstado()});
		}

		Embarque embarque = EmbarquesGestor.getInstance().buscarPorReserva(codReserva);
		if (embarque == null) {
			reservasPanel.getLblEmbarqueCodigo().setText("Sin embarque asociado");
			reservasPanel.getLblEmbarqueFecha().setText("");
			reservasPanel.getLblEmbarquePuerta().setText("");
			reservasPanel.getLblEmbarqueEstado().setText("");
		}
		else {
			reservasPanel.getLblEmbarqueCodigo().setText("Código: " + embarque.getCodEmbarque());
			reservasPanel.getLblEmbarqueFecha().setText("Fecha y hora: " + embarque.getFechaHora().replace("T", " "));
			reservasPanel.getLblEmbarquePuerta().setText("Puerta: " + embarque.getPuerta());
			reservasPanel.getLblEmbarqueEstado().setText("Estado: " + embarque.getEstado());
		}

		reservasPanel.mostrarAsociados(true);
	}

	private void guardar() {
		String comprobacion = comprobarFormulario();
		if (!comprobacion.equals("")) {
			reservasPanel.mostrarError(comprobacion);
			return;
		}

		String codigo = reservasPanel.getTextFieldCodigo().getText().trim();
		String pasajero = reservasPanel.getTextFieldPasajero().getText().trim().toUpperCase();
		String vuelo = reservasPanel.getTextFieldVuelo().getText().trim().toUpperCase();
		String asiento = reservasPanel.getTextFieldAsiento().getText().trim().toUpperCase();
		String clase = reservasPanel.getComboBoxClase().getSelectedItem().toString();
		double precio = Double.parseDouble(reservasPanel.getTextFieldPrecio().getText().trim().replace(",", "."));
		String fecha = reservasPanel.getTextFieldFecha().getText().trim();
		String canal = reservasPanel.getComboBoxCanal().getSelectedItem().toString();

		Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(codigo);
		String mensaje;
		if (reserva == null) {
			ReservasGestor.getInstance().insertarReserva(codigo, pasajero, vuelo, asiento, clase, precio, fecha, canal);
			mensaje = "Reserva añadida correctamente.";
		} else {
			ReservasGestor.getInstance().modificarReserva(reserva, asiento, precio);
			mensaje = "Reserva modificada correctamente.";
		}

		recargarPanel();
		reservasPanel.modoEdicion(false);
		reservasPanel.mostrarExito(mensaje);
	}

	private String comprobarFormulario() {
		String codigo = reservasPanel.getTextFieldCodigo().getText().trim();
		String pasajero = reservasPanel.getTextFieldPasajero().getText().trim().toUpperCase();
		String codVuelo = reservasPanel.getTextFieldVuelo().getText().trim().toUpperCase();
		String asiento = reservasPanel.getTextFieldAsiento().getText().trim().toUpperCase();
		String clase = reservasPanel.getComboBoxClase().getSelectedItem().toString();
		String precioTexto = reservasPanel.getTextFieldPrecio().getText().trim().replace(",", ".");
		String fechaTexto = reservasPanel.getTextFieldFecha().getText().trim();

		if (pasajero.isBlank() || codVuelo.isBlank() || asiento.isBlank() || precioTexto.isBlank() || fechaTexto.isBlank()) {
			return "Rellena todos los campos.";
		}

		if (!PasajerosGestor.getInstance().existeCodPasajero(pasajero)) {
			return "No existe el pasajero " + pasajero + ".";
		}

		Vuelo vuelo = VuelosGestor.getInstance().buscarPorCodigo(codVuelo);
		if (vuelo == null) {
			return "No existe el vuelo " + codVuelo + ".";
		}
		if (vuelo.getEstado().equals("CANCELADO")) {
			return "El vuelo " + codVuelo + " esta cancelado.";
		}

		if (!ReservasGestor.getInstance().obtenerClasesDelVuelo(vuelo).contains(clase)) {
			return "El avion de ese vuelo no tiene clase " + clase + ".";
		}

		Reserva reservaActual = ReservasGestor.getInstance().buscarPorCodigo(codigo);
		if ((reservaActual == null || !asiento.equals(reservaActual.getAsiento()))
				&& ReservasGestor.getInstance().comprobarAsiento(codVuelo, asiento)) {
			return "El asiento " + asiento + " ya esta ocupado.";
		}

		double precio;
		try {
			precio = Double.parseDouble(precioTexto);
		} catch (NumberFormatException e) {
			return "El precio debe ser un numero.";
		}
		if (precio < 100) {
			return "Introduce un precio realista (minimo 100).";
		}

		LocalDate fecha;
		try {
			fecha = LocalDate.parse(fechaTexto);
		} catch (DateTimeParseException e) {
			return "La fecha debe tener el formato aaaa-mm-dd.";
		}
		if (fecha.isAfter(vuelo.getSalida().toLocalDate())) {
			return "La fecha no puede ser posterior a la salida.";
		}

		return "";
	}

	private void cancelar() {
		reservasPanel.modoEdicion(false);
		cambiarEditarFormulario(false, false);
		vaciarFormulario();
	}

	public void recargarPanel() {
		reservasPanel.getTextFieldBuscar().setText("");
		cargarTabla(ReservasGestor.getInstance().getReservas());
		desactivarFormulario();
		vaciarFormulario();
		reservasPanel.getLblAdvertencia().setText("");
	}

	private void desactivarFormulario() {
		reservasPanel.getTextFieldCodigo().setEditable(false);
		reservasPanel.getTextFieldEstado().setEditable(false);
		cambiarEditarFormulario(false, false);
	}

	private void cargarTabla(ArrayList<Reserva> listaReservas) {
		ArrayList<Reserva> lista = devolverListaOrdenada(listaReservas);
		reservasPanel.getModeloTabla().setRowCount(0);
		for (Reserva reserva : lista) {
			reservasPanel.getModeloTabla().addRow(new Object[] {reserva.getIdReserva(), reserva.getPasajero(), reserva.getVuelo(), reserva.getAsiento(), reserva.getClase(), reserva.getEstado()});
		}
	}

	private ArrayList<Reserva> devolverListaOrdenada(ArrayList<Reserva> listaReservas) {
		ArrayList<Reserva> lista = new ArrayList<Reserva>(listaReservas);
		Collections.sort(lista, new ComparadorReservas(String.valueOf(reservasPanel.getComboBoxOrdenar().getSelectedItem())));
		return lista;
	}

	private void vaciarFormulario() {
		reservasPanel.getTextFieldCodigo().setText("");
		reservasPanel.getTextFieldPasajero().setText("");
		reservasPanel.getTextFieldVuelo().setText("");
		reservasPanel.getTextFieldAsiento().setText("");
		reservasPanel.getComboBoxClase().setSelectedIndex(0);
		reservasPanel.getTextFieldPrecio().setText("");
		reservasPanel.getTextFieldFecha().setText("");
		reservasPanel.getComboBoxCanal().setSelectedIndex(0);
		reservasPanel.getTextFieldEstado().setText("");
		reservasPanel.getTabla().clearSelection();
		reservasPanel.habilitarBtnEliminarYModificar(false);
		reservasPanel.mostrarAsociados(false);
	}

	private void rellenarFormulario(Reserva reserva) {
		reservasPanel.getTextFieldCodigo().setText(reserva.getIdReserva());
		reservasPanel.getTextFieldPasajero().setText(reserva.getPasajero());
		reservasPanel.getTextFieldVuelo().setText(reserva.getVuelo());
		reservasPanel.getTextFieldAsiento().setText(reserva.getAsiento());
		reservasPanel.getComboBoxClase().setSelectedItem(reserva.getClase());
		reservasPanel.getTextFieldPrecio().setText(String.valueOf(reserva.getPrecio()));
		reservasPanel.getTextFieldFecha().setText(reserva.getFecha());
		reservasPanel.getComboBoxCanal().setSelectedItem(reserva.getCanal());
		reservasPanel.getTextFieldEstado().setText(reserva.getEstado());
	}

	private void cambiarEditarFormulario(boolean activo, boolean aniadiendo) {
		reservasPanel.getTextFieldPasajero().setEditable(activo && aniadiendo);
		reservasPanel.getTextFieldVuelo().setEditable(activo && aniadiendo);
		reservasPanel.getTextFieldAsiento().setEditable(activo);
		reservasPanel.getComboBoxClase().setEnabled(activo && aniadiendo);
		reservasPanel.getTextFieldPrecio().setEditable(activo);
		reservasPanel.getTextFieldFecha().setEditable(activo && aniadiendo);
		reservasPanel.getComboBoxCanal().setEnabled(activo && aniadiendo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = reservasPanel.getTabla().getSelectedRow();
			boolean seleccion = filaInt != -1;
			if (seleccion) {
				Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(
						String.valueOf(reservasPanel.getTabla().getValueAt(filaInt, 0)));
				reservasPanel.mostrarAsociados(false);
				rellenarFormulario(reserva);
			}
			reservasPanel.habilitarBtnEliminarYModificar(seleccion);
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
		String criterio = reservasPanel.getComboBoxFiltro().getSelectedItem().toString();
		String texto = reservasPanel.getTextFieldBuscar().getText();
		cargarTabla(ReservasGestor.getInstance().filtrarReservas(criterio, texto));
	}

}
