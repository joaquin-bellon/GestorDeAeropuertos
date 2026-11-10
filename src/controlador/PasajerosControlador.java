package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;

import vista.PasajerosPanel;

public class PasajerosControlador implements ActionListener {
	private PrincipalControlador principalControlador;
	private PasajerosPanel pasajerosPanel;

	public PasajerosControlador(PrincipalControlador principalControlador) {
		super();
		this.principalControlador = principalControlador;
		this.pasajerosPanel = principalControlador.getFrame().getPasajerosPanel();
		
		
		implementarListeners();
	}

	private void implementarListeners() {
		pasajerosPanel.getBtnAniadir().addActionListener(this);
		pasajerosPanel.getBtnModificar().addActionListener(this);
		pasajerosPanel.getBtnEliminar().addActionListener(this);
		pasajerosPanel.getBtnLimpiar().addActionListener(this);
		pasajerosPanel.getBtnGuardar().addActionListener(this);
		pasajerosPanel.getBtnCancelar().addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pasajerosPanel.getBtnAniadir()) {
			aniadir();
		} else if (e.getSource() == pasajerosPanel.getBtnModificar()) {
			modificar();
		} else if (e.getSource() == pasajerosPanel.getBtnEliminar()) {
			eliminar();
		} else if (e.getSource() == pasajerosPanel.getBtnLimpiar()) {
			limpiar();
		} else if (e.getSource() == pasajerosPanel.getBtnGuardar()) {
			guardar();
		} else if (e.getSource() == pasajerosPanel.getBtnCancelar()) {
			cancelar();
		}
	}

	private void aniadir() {
		pasajerosPanel.modoEdicion(true);
	}

	private void modificar() {
		pasajerosPanel.modoEdicion(true);
	}

	private void eliminar() {

	}

	private void limpiar() {

	}

	private void guardar() {
		pasajerosPanel.modoEdicion(false);
	}

	private void cancelar() {
		pasajerosPanel.modoEdicion(false);
	}

}
