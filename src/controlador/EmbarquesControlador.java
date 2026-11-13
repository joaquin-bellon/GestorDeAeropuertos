package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import comparadores.ComparadorEmbarques;
import gestores.EmbarquesGestor;
import gestores.ReservasGestor;
import gestores.VuelosGestor;
import modelo.Embarque;
import modelo.Reserva;
import modelo.Vuelo;
import vista.EmbarquesPanel;

public class EmbarquesControlador implements ActionListener, ListSelectionListener, KeyListener {
	private PrincipalControlador principalControlador;
	private EmbarquesPanel embarquesPanel;

	public EmbarquesControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.embarquesPanel = principalControlador.getFrame().getEmbarquesPanel();

		implementarListeners();

		cargarTabla(EmbarquesGestor.getInstance().getEmbarques());
		desactivarFormulario();
		embarquesPanel.habilitarBtnEliminarYModificar(false);
	}

	private void implementarListeners() {
		embarquesPanel.getBtnAniadir().addActionListener(this);
		embarquesPanel.getBtnModificar().addActionListener(this);
		embarquesPanel.getBtnEliminar().addActionListener(this);
		embarquesPanel.getBtnLimpiar().addActionListener(this);
		embarquesPanel.getBtnGuardar().addActionListener(this);
		embarquesPanel.getBtnCancelar().addActionListener(this);
		embarquesPanel.getBtnNoEliminar().addActionListener(this);
		embarquesPanel.getBtnSiEliminar().addActionListener(this);
		embarquesPanel.getComboBoxFiltro().addActionListener(this);
		embarquesPanel.getComboBoxOrdenar().addActionListener(this);

		embarquesPanel.getTabla().getSelectionModel().addListSelectionListener(this);

		embarquesPanel.getTextFieldBuscar().addKeyListener(this);
		embarquesPanel.getTextFieldReserva().addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == embarquesPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == embarquesPanel.getBtnModificar()) {
			modificar();
		} else if (e.getSource() == embarquesPanel.getBtnEliminar()) {
			if (embarquesPanel.getTabla().getSelectedRow() == -1) {
				embarquesPanel.mostrarError("Selecciona el embarque a eliminar.");
			}
			else {
				embarquesPanel.modoConfirmarEliminar(true);
			}
		} else if (e.getSource() == embarquesPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == embarquesPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == embarquesPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == embarquesPanel.getBtnNoEliminar()) {
			embarquesPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == embarquesPanel.getBtnSiEliminar()) {
			eliminar();
			embarquesPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == embarquesPanel.getComboBoxFiltro()) {
			filtrar();
		} else if (e.getSource() == embarquesPanel.getComboBoxOrdenar()) {
			filtrar();
		}
	}

	private void aniadir() {
		vaciarFormulario();
		cambiarEditarFormulario(true, true);
		embarquesPanel.getTextFieldCodigo().setText(EmbarquesGestor.getInstance().crearIdentificador());
		embarquesPanel.modoEdicion(true);
	}

	private void modificar() {
		cambiarEditarFormulario(true, false);
		embarquesPanel.modoEdicion(true);
	}

	private void eliminar() {
		String codEmbarque = String.valueOf(embarquesPanel.getTabla().getValueAt(embarquesPanel.getTabla().getSelectedRow(), 0));
		boolean eliminado = EmbarquesGestor.getInstance().eliminarEmbarque(codEmbarque);
		if (eliminado) {
			recargarPanel();
			embarquesPanel.mostrarExito("Se ha eliminado el embarque " + codEmbarque + ".");
		}
		else {
			embarquesPanel.mostrarError("No existe ningun embarque con el codigo " + codEmbarque);
		}
	}

	private void guardar() {
		String comprobacion = comprobarFormulario();
		if (!comprobacion.equals("")) {
			embarquesPanel.mostrarError(comprobacion);
			return;
		}

		String codigo = embarquesPanel.getTextFieldCodigo().getText().trim();
		String codReserva = embarquesPanel.getTextFieldReserva().getText().trim().toUpperCase();
		LocalDateTime fechaHora = leerFechaHora();
		String puerta = embarquesPanel.getTextFieldPuerta().getText().trim().toUpperCase();
		String estado = embarquesPanel.getComboBoxEstado().getSelectedItem().toString();
		String observaciones = embarquesPanel.getTextFieldObservaciones().getText().trim();
		if (observaciones.isEmpty()) {
			observaciones = "Sin incidencias";
		}

		String aviso = "";
		Embarque embarque = EmbarquesGestor.getInstance().buscarPorCodigo(codigo);
		if (embarque == null) {
			Embarque existente = EmbarquesGestor.getInstance().buscarPorReserva(codReserva);
			if (existente == null) {
				EmbarquesGestor.getInstance().insertarEmbarque(codigo, codReserva, fechaHora.toString(), puerta, estado, observaciones);
			}
			else {
				EmbarquesGestor.getInstance().modificarEmbarque(existente, fechaHora.toString(), puerta, estado, observaciones);
				codigo = existente.getCodEmbarque();
				aviso = codReserva + " ya tenia el embarque " + codigo + ": actualizado.";
			}
		} else {
			EmbarquesGestor.getInstance().modificarEmbarque(embarque, fechaHora.toString(), puerta, estado, observaciones);
		}

		if (aviso.equals("")) {
			aviso = EmbarquesGestor.getInstance().comprobarHorarioEmbarque(obtenerVuelo(codReserva), fechaHora);
		}

		recargarPanel();
		embarquesPanel.modoEdicion(false);
		seleccionarEnTabla(codigo);
		if (aviso.equals("")) {
			embarquesPanel.mostrarExito("Embarque " + codigo + " guardado correctamente.");
		}
		else {
			embarquesPanel.mostrarError(aviso);
		}
	}

	private String comprobarFormulario() {
		String codReserva = embarquesPanel.getTextFieldReserva().getText().trim().toUpperCase();
		String fechaHoraTexto = embarquesPanel.getTextFieldFechaHora().getText().trim();
		String puerta = embarquesPanel.getTextFieldPuerta().getText().trim().toUpperCase();
		String estado = embarquesPanel.getComboBoxEstado().getSelectedItem().toString();

		if (codReserva.isBlank() || fechaHoraTexto.isBlank() || puerta.isBlank()) {
			return "Rellena todos los campos.";
		}

		Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(codReserva);
		if (reserva == null) {
			return "No existe la reserva " + codReserva + ".";
		}
		Vuelo vuelo = obtenerVuelo(codReserva);
		if (vuelo == null) {
			return "No existe el vuelo de la reserva " + codReserva + ".";
		}

		if (leerFechaHora() == null) {
			return "La fecha debe ser aaaa-mm-dd hh:mm.";
		}

		if (!puerta.equals(vuelo.getPuerta())) {
			return "La puerta del vuelo " + vuelo.getCodigo() + " es " + vuelo.getPuerta() + ".";
		}

		if (!estado.equals("NO_EMBARCADO")) {
			if (reserva.getEstado().equals("CANCELADA")) {
				return "No se puede embarcar: reserva cancelada.";
			}
			if (vuelo.getEstado().equals("CANCELADO")) {
				return "No se puede embarcar: vuelo cancelado.";
			}
		}

		return "";
	}

	private LocalDateTime leerFechaHora() {
		try {
			return LocalDateTime.parse(embarquesPanel.getTextFieldFechaHora().getText().trim().replace(" ", "T"));
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private Vuelo obtenerVuelo(String codReserva) {
		Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(codReserva);
		if (reserva == null) {
			return null;
		}
		return VuelosGestor.getInstance().buscarPorCodigo(reserva.getVuelo());
	}

	private void rellenarPuertaDelVuelo() {
		Vuelo vuelo = obtenerVuelo(embarquesPanel.getTextFieldReserva().getText().trim().toUpperCase());
		if (vuelo == null) {
			embarquesPanel.getTextFieldPuerta().setText("");
		}
		else {
			embarquesPanel.getTextFieldPuerta().setText(vuelo.getPuerta());
		}
	}

	private void cancelar() {
		embarquesPanel.modoEdicion(false);
		cambiarEditarFormulario(false, false);
		vaciarFormulario();
	}

	public void recargarPanel() {
		embarquesPanel.getTextFieldBuscar().setText("");
		cargarTabla(EmbarquesGestor.getInstance().getEmbarques());
		desactivarFormulario();
		vaciarFormulario();
		embarquesPanel.getLblAdvertencia().setText("");
	}

	private void seleccionarEnTabla(String codigo) {
		for (int i = 0; i < embarquesPanel.getModeloTabla().getRowCount(); i++) {
			if (embarquesPanel.getModeloTabla().getValueAt(i, 0).equals(codigo)) {
				embarquesPanel.getTabla().setRowSelectionInterval(i, i);
				embarquesPanel.getTabla().scrollRectToVisible(embarquesPanel.getTabla().getCellRect(i, 0, true));
			}
		}
	}

	private void desactivarFormulario() {
		embarquesPanel.getTextFieldCodigo().setEditable(false);
		cambiarEditarFormulario(false, false);
	}

	private void cargarTabla(ArrayList<Embarque> listaEmbarques) {
		ArrayList<Embarque> lista = devolverListaOrdenada(listaEmbarques);
		embarquesPanel.getModeloTabla().setRowCount(0);
		for (Embarque embarque : lista) {
			embarquesPanel.getModeloTabla().addRow(new Object[] {embarque.getCodEmbarque(), embarque.getCodReserva(), embarque.getFechaHora().replace("T", " "), embarque.getPuerta(), embarque.getEstado()});
		}
	}

	private ArrayList<Embarque> devolverListaOrdenada(ArrayList<Embarque> listaEmbarques) {
		ArrayList<Embarque> lista = new ArrayList<Embarque>(listaEmbarques);
		Collections.sort(lista, new ComparadorEmbarques(String.valueOf(embarquesPanel.getComboBoxOrdenar().getSelectedItem())));
		return lista;
	}

	private void vaciarFormulario() {
		embarquesPanel.getTextFieldCodigo().setText("");
		embarquesPanel.getTextFieldReserva().setText("");
		embarquesPanel.getTextFieldFechaHora().setText("");
		embarquesPanel.getTextFieldPuerta().setText("");
		embarquesPanel.getComboBoxEstado().setSelectedIndex(0);
		embarquesPanel.getTextFieldObservaciones().setText("");
		embarquesPanel.getTabla().clearSelection();
		embarquesPanel.habilitarBtnEliminarYModificar(false);
	}

	private void rellenarFormulario(Embarque embarque) {
		embarquesPanel.getTextFieldCodigo().setText(embarque.getCodEmbarque());
		embarquesPanel.getTextFieldReserva().setText(embarque.getCodReserva());
		embarquesPanel.getTextFieldFechaHora().setText(embarque.getFechaHora().replace("T", " "));
		embarquesPanel.getTextFieldPuerta().setText(embarque.getPuerta());
		embarquesPanel.getComboBoxEstado().setSelectedItem(embarque.getEstado());
		embarquesPanel.getTextFieldObservaciones().setText(embarque.getObservaciones());
	}

	private void cambiarEditarFormulario(boolean activo, boolean aniadiendo) {
		embarquesPanel.getTextFieldReserva().setEditable(activo && aniadiendo);
		embarquesPanel.getTextFieldFechaHora().setEditable(activo);
		embarquesPanel.getTextFieldPuerta().setEditable(activo);
		embarquesPanel.getComboBoxEstado().setEnabled(activo);
		embarquesPanel.getTextFieldObservaciones().setEditable(activo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = embarquesPanel.getTabla().getSelectedRow();
			boolean seleccion = filaInt != -1;
			if (seleccion) {
				Embarque embarque = EmbarquesGestor.getInstance().buscarPorCodigo(
						String.valueOf(embarquesPanel.getTabla().getValueAt(filaInt, 0)));
				rellenarFormulario(embarque);
			}
			embarquesPanel.habilitarBtnEliminarYModificar(seleccion);
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
		if (e.getSource() == embarquesPanel.getTextFieldBuscar()) {
			filtrar();
		} else if (e.getSource() == embarquesPanel.getTextFieldReserva()) {
			rellenarPuertaDelVuelo();
		}
	}

	private void filtrar() {
		String texto = embarquesPanel.getTextFieldBuscar().getText().trim();
		if (texto.isEmpty()) {
			cargarTabla(EmbarquesGestor.getInstance().getEmbarques());
			return;
		}
		try {
			LocalDate fecha = LocalDate.parse(texto);
			cargarTabla(EmbarquesGestor.getInstance().filtrarPorFecha(fecha));
		} catch (DateTimeParseException e) {
		}
	}

}
