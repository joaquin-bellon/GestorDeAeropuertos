package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gestores.PasajerosGestor;
import modelo.Pasajero;
import vista.PasajerosPanel;

public class PasajerosControlador implements ActionListener, ListSelectionListener, KeyListener{
	private PrincipalControlador principalControlador;
	private PasajerosPanel pasajerosPanel;

	public PasajerosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.pasajerosPanel = principalControlador.getFrame().getPasajerosPanel();
		
		
		implementarListeners();
		
		cargarTabla(PasajerosGestor.getInstance().getPasajeros());
		desactivarFormulario();
		pasajerosPanel.habilitarBtnEliminarYModificar(false);
	}

	private void implementarListeners() {
		pasajerosPanel.getBtnAniadir().addActionListener(this);
		pasajerosPanel.getBtnModificar().addActionListener(this);
		pasajerosPanel.getBtnEliminar().addActionListener(this);
		pasajerosPanel.getBtnLimpiar().addActionListener(this);
		pasajerosPanel.getBtnGuardar().addActionListener(this);
		pasajerosPanel.getBtnCancelar().addActionListener(this);
		pasajerosPanel.getBtnNoEliminar().addActionListener(this);
		pasajerosPanel.getBtnSiEliminar().addActionListener(this);
		pasajerosPanel.getComboBoxFiltro().addActionListener(this);
		
		pasajerosPanel.getTabla().getSelectionModel().addListSelectionListener(this);
		
		pasajerosPanel.getTextFieldBuscar().addKeyListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pasajerosPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == pasajerosPanel.getBtnModificar()) {
			modificar();
		} else if (e.getSource() == pasajerosPanel.getBtnEliminar()) {
			if (pasajerosPanel.getTabla().getSelectedRow() == -1) {
				pasajerosPanel.mostrarError("Selecciona el pasajero a eliminar.");
			}
			else {
				pasajerosPanel.modoConfirmarEliminar(true);
			}
			
		} else if (e.getSource() == pasajerosPanel.getBtnLimpiar()) {
			vaciarFormulario();
		} else if (e.getSource() == pasajerosPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == pasajerosPanel.getBtnCancelar()) {
			cancelar();
		} else if (e.getSource() == pasajerosPanel.getBtnNoEliminar()) {
			pasajerosPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == pasajerosPanel.getBtnSiEliminar()) {
			eliminar();
			pasajerosPanel.modoConfirmarEliminar(false);
		} else if (e.getSource() == pasajerosPanel.getComboBoxFiltro()) {
			filtrar();
		}
	}

	private void aniadir() {
		activarEditarFormulario(true);
		vaciarFormulario();
		pasajerosPanel.getTextFieldCodigo().setText(PasajerosGestor.getInstance().crearNuevoIdentificador());
		pasajerosPanel.modoEdicion(true);
	}

	private void modificar() {
		activarEditarFormulario(true);
		pasajerosPanel.modoEdicion(true);
		
	}

	private void eliminar() {
		String codPasajero = String.valueOf(pasajerosPanel.getTabla().getValueAt(pasajerosPanel.getTabla().getSelectedRow(), 0));
		boolean eliminado = PasajerosGestor.getInstance().eliminarPasajero(codPasajero);
		if (eliminado) {
			recargarPanel();
			pasajerosPanel.mostrarExito("Se ha eliminado al pasajero " + codPasajero + " y sus relacionados");
			
		}
		else {
			pasajerosPanel.mostrarError("No existe ningun pasajero con el codigo " + codPasajero);
		}
		
	}

	private void guardar() {
		String comprobacion = comprobarFormulario();
		if (!comprobacion.equals("")) {
			pasajerosPanel.mostrarError(comprobacion);
			return;
		}

		String codigo = pasajerosPanel.getTextFieldCodigo().getText().trim();
		String nombre = pasajerosPanel.getTextFieldNombre().getText().trim();
		String apellidos = pasajerosPanel.getTextFieldApellidos().getText().trim();
		String documento = pasajerosPanel.getTextFieldDocumento().getText().trim();
		String pais = pasajerosPanel.getTextFieldPais().getText().trim();
		String correo = pasajerosPanel.getTextFieldCorreo().getText().trim();
		String tipo = pasajerosPanel.getComboBoxTipo().getSelectedItem().toString();

		Pasajero pasajero = PasajerosGestor.getInstance().buscarPorCodPasajero(codigo);
		String mensaje;
		if (pasajero == null) {
			PasajerosGestor.getInstance().insertarPasajero(codigo, nombre, apellidos, documento, pais, correo, tipo);
			mensaje = "Pasajero añadido correctamente.";
		} else {
			PasajerosGestor.getInstance().modificarPasajero(pasajero, codigo, nombre, apellidos, documento, pais, correo, tipo);
			mensaje = "Pasajero modificado correctamente.";
		}

		recargarPanel();
		pasajerosPanel.modoEdicion(false);
		pasajerosPanel.mostrarExito(mensaje);
	}
	
	private String comprobarFormulario() {
		String codigo = pasajerosPanel.getTextFieldCodigo().getText().trim();
		String nombre = pasajerosPanel.getTextFieldNombre().getText().trim();
		String apellidos = pasajerosPanel.getTextFieldApellidos().getText().trim();
		String documento = pasajerosPanel.getTextFieldDocumento().getText().trim();
		String pais = pasajerosPanel.getTextFieldPais().getText().trim();
		String correo = pasajerosPanel.getTextFieldCorreo().getText().trim();

		if (nombre.isBlank() || apellidos.isBlank() || documento.isBlank() || pais.isBlank() || correo.isBlank()) {
			return "Rellena todos los campos.";
		}

		Pasajero pasajeroActual = PasajerosGestor.getInstance().buscarPorCodPasajero(codigo);

		if ((pasajeroActual == null || !documento.equals(pasajeroActual.getDocumentoIdentificativo()))
				&& PasajerosGestor.getInstance().existeDNI(documento)) {
			return "Ese documento ya esta en uso.";
		}

		if ((pasajeroActual == null || !correo.equals(pasajeroActual.getCorreo()))
				&& PasajerosGestor.getInstance().existeCorreo(correo)) {
			return "Ese correo ya esta en uso.";
		}

		return "";
	}
	
	
	private void cancelar() {
		pasajerosPanel.modoEdicion(false);
		vaciarFormulario();
	}
	
	public void recargarPanel() {
		cargarTabla(PasajerosGestor.getInstance().getPasajeros());
		desactivarFormulario();
		vaciarFormulario();
		pasajerosPanel.getLblAdvertencia().setText("");
	}

	private void desactivarFormulario() {
		pasajerosPanel.getTextFieldCodigo().setEditable(false);
		pasajerosPanel.getTextFieldNombre().setEditable(false);
		pasajerosPanel.getTextFieldApellidos().setEditable(false);
		pasajerosPanel.getTextFieldDocumento().setEditable(false);
		pasajerosPanel.getTextFieldPais().setEditable(false);
		pasajerosPanel.getTextFieldCorreo().setEditable(false);
		pasajerosPanel.getComboBoxTipo().setEnabled(false);
		
	}
	
	

	private void cargarTabla(ArrayList<Pasajero> listaPasajeros) {
		pasajerosPanel.getModeloTabla().setRowCount(0);
		for (Pasajero pasajero : listaPasajeros) {
			pasajerosPanel.getModeloTabla().addRow(new Object[] {pasajero.getCodPasajero(), pasajero.getNombre(), pasajero.getApellidos(), pasajero.getTipo()});
		}
	}

	private void vaciarFormulario() {
		pasajerosPanel.getTextFieldCodigo().setText("");
		pasajerosPanel.getTextFieldNombre().setText("");
		pasajerosPanel.getTextFieldApellidos().setText("");
		pasajerosPanel.getTextFieldDocumento().setText("");
		pasajerosPanel.getTextFieldPais().setText("");
		pasajerosPanel.getTextFieldCorreo().setText("");
		pasajerosPanel.getComboBoxTipo().setSelectedIndex(0);
		pasajerosPanel.getTabla().clearSelection();
		pasajerosPanel.habilitarBtnEliminarYModificar(false);
	}
	
	private void rellenarFormulario(Pasajero pasajero) {
		pasajerosPanel.getTextFieldCodigo().setText(pasajero.getCodPasajero());
		pasajerosPanel.getTextFieldNombre().setText(pasajero.getNombre());
		pasajerosPanel.getTextFieldApellidos().setText(pasajero.getApellidos());
		pasajerosPanel.getTextFieldDocumento().setText(pasajero.getDocumentoIdentificativo());
		pasajerosPanel.getTextFieldPais().setText(pasajero.getPais());
		pasajerosPanel.getTextFieldCorreo().setText(pasajero.getCorreo());
		pasajerosPanel.getComboBoxTipo().setSelectedItem(pasajero.getTipo());
	}
	
	private void activarEditarFormulario(boolean activo) {
		pasajerosPanel.getTextFieldNombre().setEditable(activo);
		pasajerosPanel.getTextFieldApellidos().setEditable(activo);
		pasajerosPanel.getTextFieldDocumento().setEditable(activo);
		pasajerosPanel.getTextFieldPais().setEditable(activo);
		pasajerosPanel.getTextFieldCorreo().setEditable(activo);
		pasajerosPanel.getComboBoxTipo().setEnabled(activo);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaInt = pasajerosPanel.getTabla().getSelectedRow();
			boolean seleccion = filaInt != -1;
			if (seleccion) {
				Pasajero pasajero = PasajerosGestor.getInstance().buscarPorCodPasajero(
						String.valueOf(pasajerosPanel.getTabla().getValueAt(filaInt, 0))
						);
				rellenarFormulario(pasajero);
			}
			pasajerosPanel.habilitarBtnEliminarYModificar(seleccion);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		filtrar();
		
	}
	
private void filtrar () {
		String criterio = pasajerosPanel.getComboBoxFiltro().getSelectedItem().toString();
		String texto = pasajerosPanel.getTextFieldBuscar().getText();
		cargarTabla(PasajerosGestor.getInstance().filtrarPasajeros(criterio, texto));
	}

}
