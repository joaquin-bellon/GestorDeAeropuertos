package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gestores.EquipajesGestor;
import gestores.ReservasGestor;
import modelo.Equipaje;
import modelo.Reserva;
import vista.EquipajesPanel;

public class EquipajesControlador implements ActionListener, ListSelectionListener, KeyListener {
	private PrincipalControlador principalControlador;
	private EquipajesPanel equipajesPanel;

	public EquipajesControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.equipajesPanel = principalControlador.getFrame().getEquipajesPanel();

		implementarListeners();

		cargarTabla(EquipajesGestor.getInstance().getEquipajes());
		desactivarFormulario();
		equipajesPanel.habilitarBtnEliminarYModificar(false);
	}

	private void implementarListeners() {
		equipajesPanel.getBtnAniadir().addActionListener(this);
		equipajesPanel.getBtnModificar().addActionListener(this);
		equipajesPanel.getBtnEliminar().addActionListener(this);
		equipajesPanel.getBtnLimpiar().addActionListener(this);
		equipajesPanel.getBtnGuardar().addActionListener(this);
		equipajesPanel.getBtnCancelar().addActionListener(this);
		equipajesPanel.getBtnNoEliminar().addActionListener(this);
		equipajesPanel.getBtnSiEliminar().addActionListener(this);
		equipajesPanel.getComboBoxFiltro().addActionListener(this);
		equipajesPanel.getComboBoxTipo().addActionListener(this);

		equipajesPanel.getTabla().getSelectionModel().addListSelectionListener(this);

		equipajesPanel.getTextFieldBuscar().addKeyListener(this);
		equipajesPanel.getTextFieldReserva().addKeyListener(this);
		equipajesPanel.getTextFieldPeso().addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == equipajesPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == equipajesPanel.getBtnModificar()) {
			modificar();
		} else if (e.getSource() == equipajesPanel.getBtnEliminar()) {
			if (equipajesPanel.getTabla().getSelectedRow() == -1) {
				equipajesPanel.mostrarError("Selecciona el equipaje a dar de baja.");
			}
			else {
				equipajesPanel.modoConfirmarEliminar(true);
			}
		} else if (e.getSource() == equipajesPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == equipajesPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == equipajesPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == equipajesPanel.getBtnNoEliminar()) {
			equipajesPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == equipajesPanel.getBtnSiEliminar()) {
			eliminar();
			equipajesPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == equipajesPanel.getComboBoxFiltro()) {
			filtrar();
		} else if (e.getSource() == equipajesPanel.getComboBoxTipo()) {
			actualizarAvisoPeso();
		}
	}

	private void aniadir() {
		vaciarFormulario();
		cambiarEditarFormulario(true, true);
		equipajesPanel.getTextFieldCodigo().setText(EquipajesGestor.getInstance().crearIdentificador());
		equipajesPanel.getComboBoxEstado().setSelectedItem("REGISTRADO");
		equipajesPanel.modoEdicion(true);
	}

	private void modificar() {
		cambiarEditarFormulario(true, false);
		equipajesPanel.modoEdicion(true);
	}

	private void eliminar() {
		String codEquipaje = String.valueOf(equipajesPanel.getTabla().getValueAt(equipajesPanel.getTabla().getSelectedRow(), 0));
		boolean eliminado = EquipajesGestor.getInstance().eliminarEquipaje(codEquipaje);
		if (eliminado) {
			recargarPanel();
			equipajesPanel.mostrarExito("Se ha dado de baja el equipaje " + codEquipaje + ".");
		}
		else {
			equipajesPanel.mostrarError("No existe ningun equipaje con el codigo " + codEquipaje);
		}
	}

	private void guardar() {
		String comprobacion = comprobarFormulario();
		if (!comprobacion.equals("")) {
			equipajesPanel.mostrarError(comprobacion);
			return;
		}

		String codigo = equipajesPanel.getTextFieldCodigo().getText().trim();
		String codReserva = equipajesPanel.getTextFieldReserva().getText().trim().toUpperCase();
		double peso = leerPeso();
		String tipo = equipajesPanel.getComboBoxTipo().getSelectedItem().toString();
		String estado = equipajesPanel.getComboBoxEstado().getSelectedItem().toString();
		String prioridad = equipajesPanel.getComboBoxPrioridad().getSelectedItem().toString();

		Equipaje equipaje = EquipajesGestor.getInstance().buscarPorCodigo(codigo);
		String mensaje;
		if (equipaje == null) {
			EquipajesGestor.getInstance().insertarEquipaje(codigo, codReserva, peso, tipo, estado, prioridad);
			mensaje = "Equipaje " + codigo + " registrado correctamente.";
		} else {
			EquipajesGestor.getInstance().modificarEquipaje(equipaje, peso, tipo, estado, prioridad);
			mensaje = "Equipaje " + codigo + " modificado correctamente.";
		}

		recargarPanel();
		equipajesPanel.modoEdicion(false);
		seleccionarEnTabla(codigo);
		equipajesPanel.mostrarExito(mensaje);
	}

	private String comprobarFormulario() {
		String codReserva = equipajesPanel.getTextFieldReserva().getText().trim().toUpperCase();
		String pesoTexto = equipajesPanel.getTextFieldPeso().getText().trim();

		if (codReserva.isBlank() || pesoTexto.isBlank()) {
			return "Rellena todos los campos.";
		}

		Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(codReserva);
		if (reserva == null) {
			return "No existe la reserva " + codReserva + ".";
		}
		boolean aniadiendo = EquipajesGestor.getInstance().buscarPorCodigo(equipajesPanel.getTextFieldCodigo().getText().trim()) == null;
		if (aniadiendo && reserva.getEstado().equals("CANCELADA")) {
			return "La reserva " + codReserva + " esta cancelada.";
		}

		double peso = leerPeso();
		if (peso == -1) {
			return "El peso debe ser un numero.";
		}
		if (peso <= 0) {
			return "El peso tiene que ser mayor que 0.";
		}

		return "";
	}

	private double leerPeso() {
		try {
			return Double.parseDouble(equipajesPanel.getTextFieldPeso().getText().trim().replace(",", "."));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void actualizarAvisoPeso() {
		double peso = leerPeso();
		if (peso <= 0) {
			equipajesPanel.mostrarAvisoPeso(false);
			return;
		}

		String tipo = equipajesPanel.getComboBoxTipo().getSelectedItem().toString();
		double pesoMaximo = EquipajesGestor.getInstance().obtenerPesoMaximo(tipo);
		if (peso > pesoMaximo) {
			double diferencia = (int)((peso - pesoMaximo) * 100) / 100.0;
			equipajesPanel.getLblAvisoPesoRegistrado().setText("Peso registrado: " + peso + " kg");
			equipajesPanel.getLblAvisoPesoLimite().setText("Limite para " + tipo + ": " + pesoMaximo + " kg");
			equipajesPanel.getLblAvisoPesoDiferencia().setText("Diferencia: " + diferencia + " kg de mas");
			equipajesPanel.mostrarAvisoPeso(true);
		}
		else {
			equipajesPanel.mostrarAvisoPeso(false);
		}
	}

	private void actualizarDatosReserva() {
		String codReserva = equipajesPanel.getTextFieldReserva().getText().trim().toUpperCase();
		Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(codReserva);
		if (reserva == null) {
			equipajesPanel.getTextFieldPasajero().setText("");
			equipajesPanel.getTextFieldVuelo().setText("");
		}
		else {
			equipajesPanel.getTextFieldPasajero().setText(reserva.getPasajero());
			equipajesPanel.getTextFieldVuelo().setText(reserva.getVuelo());
		}
	}

	private void cancelar() {
		equipajesPanel.modoEdicion(false);
		cambiarEditarFormulario(false, false);
		vaciarFormulario();
	}

	public void recargarPanel() {
		equipajesPanel.getTextFieldBuscar().setText("");
		cargarTabla(EquipajesGestor.getInstance().getEquipajes());
		desactivarFormulario();
		vaciarFormulario();
		equipajesPanel.getLblAdvertencia().setText("");
	}

	private void seleccionarEnTabla(String codigo) {
		for (int i = 0; i < equipajesPanel.getModeloTabla().getRowCount(); i++) {
			if (equipajesPanel.getModeloTabla().getValueAt(i, 0).equals(codigo)) {
				equipajesPanel.getTabla().setRowSelectionInterval(i, i);
				equipajesPanel.getTabla().scrollRectToVisible(equipajesPanel.getTabla().getCellRect(i, 0, true));
			}
		}
	}

	private void desactivarFormulario() {
		equipajesPanel.getTextFieldCodigo().setEditable(false);
		equipajesPanel.getTextFieldPasajero().setEditable(false);
		equipajesPanel.getTextFieldVuelo().setEditable(false);
		cambiarEditarFormulario(false, false);
	}

	private void cargarTabla(ArrayList<Equipaje> listaEquipajes) {
		equipajesPanel.getModeloTabla().setRowCount(0);
		for (Equipaje equipaje : listaEquipajes) {
			Reserva reserva = ReservasGestor.getInstance().buscarPorCodigo(equipaje.getCodReserva());
			String pasajero = "";
			String vuelo = "";
			if (reserva != null) {
				pasajero = reserva.getPasajero();
				vuelo = reserva.getVuelo();
			}
			equipajesPanel.getModeloTabla().addRow(new Object[] {equipaje.getCodEquipaje(), equipaje.getCodReserva(), pasajero, vuelo, equipaje.getPeso(), equipaje.getTipo()});
		}
	}

	private void vaciarFormulario() {
		equipajesPanel.getTextFieldCodigo().setText("");
		equipajesPanel.getTextFieldReserva().setText("");
		equipajesPanel.getTextFieldPasajero().setText("");
		equipajesPanel.getTextFieldVuelo().setText("");
		equipajesPanel.getTextFieldPeso().setText("");
		equipajesPanel.getComboBoxTipo().setSelectedIndex(0);
		equipajesPanel.getComboBoxEstado().setSelectedIndex(0);
		equipajesPanel.getComboBoxPrioridad().setSelectedIndex(0);
		equipajesPanel.getTabla().clearSelection();
		equipajesPanel.habilitarBtnEliminarYModificar(false);
		equipajesPanel.mostrarAvisoPeso(false);
	}

	private void rellenarFormulario(Equipaje equipaje) {
		equipajesPanel.getTextFieldCodigo().setText(equipaje.getCodEquipaje());
		equipajesPanel.getTextFieldReserva().setText(equipaje.getCodReserva());
		actualizarDatosReserva();
		equipajesPanel.getTextFieldPeso().setText(String.valueOf(equipaje.getPeso()));
		equipajesPanel.getComboBoxTipo().setSelectedItem(equipaje.getTipo());
		equipajesPanel.getComboBoxEstado().setSelectedItem(equipaje.getEstado());
		equipajesPanel.getComboBoxPrioridad().setSelectedItem(equipaje.getPrioridad());
		actualizarAvisoPeso();
	}

	private void cambiarEditarFormulario(boolean activo, boolean aniadiendo) {
		equipajesPanel.getTextFieldReserva().setEditable(activo && aniadiendo);
		equipajesPanel.getTextFieldPeso().setEditable(activo);
		equipajesPanel.getComboBoxTipo().setEnabled(activo);
		equipajesPanel.getComboBoxEstado().setEnabled(activo && !aniadiendo);
		equipajesPanel.getComboBoxPrioridad().setEnabled(activo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = equipajesPanel.getTabla().getSelectedRow();
			boolean seleccion = filaInt != -1;
			if (seleccion) {
				Equipaje equipaje = EquipajesGestor.getInstance().buscarPorCodigo(
						String.valueOf(equipajesPanel.getTabla().getValueAt(filaInt, 0)));
				rellenarFormulario(equipaje);
			}
			equipajesPanel.habilitarBtnEliminarYModificar(seleccion);
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
		if (e.getSource() == equipajesPanel.getTextFieldBuscar()) {
			filtrar();
		} else if (e.getSource() == equipajesPanel.getTextFieldReserva()) {
			actualizarDatosReserva();
		} else if (e.getSource() == equipajesPanel.getTextFieldPeso()) {
			actualizarAvisoPeso();
		}
	}

	private void filtrar() {
		String criterio = equipajesPanel.getComboBoxFiltro().getSelectedItem().toString();
		String texto = equipajesPanel.getTextFieldBuscar().getText();
		cargarTabla(EquipajesGestor.getInstance().filtrarEquipajes(criterio, texto));
	}

}
