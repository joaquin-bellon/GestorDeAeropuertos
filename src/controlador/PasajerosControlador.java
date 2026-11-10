package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gestores.PasajerosGestor;
import modelo.Pasajero;
import vista.PasajerosPanel;

public class PasajerosControlador implements ActionListener, ListSelectionListener{
	private PrincipalControlador principalControlador;
	private PasajerosPanel pasajerosPanel;

	public PasajerosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.pasajerosPanel = principalControlador.getFrame().getPasajerosPanel();
		
		
		implementarListeners();
		
		cargarTabla();
		desactivarFormulario();
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
		
		pasajerosPanel.getTabla().getSelectionModel().addListSelectionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pasajerosPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == pasajerosPanel.getBtnModificar()) {
			activarEditarFormulario(true);
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
		}
	}

	private void aniadir() {
		activarEditarFormulario(true);
		pasajerosPanel.modoEdicion(true);
	}

	private void modificar() {
		
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
		pasajerosPanel.modoEdicion(false);
	}

	private void cancelar() {
		pasajerosPanel.modoEdicion(false);
	}
	
	private void recargarPanel() {
		cargarTabla();
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

	private void cargarTabla() {
		pasajerosPanel.getModeloTabla().setRowCount(0);
		for (Pasajero pasajero : PasajerosGestor.getInstance().getPasajeros()) {
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
		if (e.getValueIsAdjusting()) {
			return;
		} else {
			Pasajero pasajero = PasajerosGestor.getInstance().buscarPorCodPasajero(
					String.valueOf(pasajerosPanel.getTabla().getValueAt(pasajerosPanel.getTabla().getSelectedRow(), 0))
					);
			rellenarFormulario(pasajero);
		}
		
	}

}
