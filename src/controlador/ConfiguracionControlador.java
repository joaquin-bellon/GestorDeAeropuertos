package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import comparadores.ComparadorUsuario;
import gestores.UsuariosGestor;
import modelo.Usuario;
import vista.ConfiguracionPanel;

public class ConfiguracionControlador implements ActionListener, ListSelectionListener {
	private PrincipalControlador principalControlador;
	private ConfiguracionPanel configuracionPanel;
	private String rolUsuario;

	public ConfiguracionControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
		this.configuracionPanel = principalControlador.getFrame().getConfiguracionPanel();
		this.rolUsuario = principalControlador.getUsuario().getRol();

		implementarListeners();
		recargarPanel();
	}

	private void implementarListeners() {
		configuracionPanel.getTablaUsuarios().getSelectionModel().addListSelectionListener(this);
		configuracionPanel.getBtnCambiarRol().addActionListener(this);
		configuracionPanel.getBtnSiConfirmar().addActionListener(this);
		configuracionPanel.getBtnNoConfirmar().addActionListener(this);
	}

	public void recargarPanel() {
		configuracionPanel.modoConfirmar(false, "");
		configuracionPanel.getTablaUsuarios().clearSelection();
		configuracionPanel.getModeloUsuarios().setRowCount(0);
		ArrayList<Usuario> usuariosOrdenados = new ArrayList<Usuario>(UsuariosGestor.getInstance().getUsuarios());
		Collections.sort(usuariosOrdenados,new ComparadorUsuario());
		for (Usuario usuario : usuariosOrdenados) {
			configuracionPanel.getModeloUsuarios().addRow(new Object[] {usuario.getUsuario(), usuario.getRol()});
		}
		vaciarUsuarioSeleccionado();
	}

	private void vaciarUsuarioSeleccionado() {
		configuracionPanel.getLblValorUsuario().setText("-");
		configuracionPanel.getLblValorRol().setText("-");
		configuracionPanel.getBtnCambiarRol().setEnabled(false);
		configuracionPanel.getBtnCambiarRol().setText("Cambiar rol");
	}

	private String obtenerUsuarioSeleccionado() {
		int fila = configuracionPanel.getTablaUsuarios().getSelectedRow();
		if (fila == -1) {
			return null;
		}
		return (String) configuracionPanel.getModeloUsuarios().getValueAt(fila, 0);
	}

	private void seleccionarEnTabla(String nombre) {
		for (int i = 0; i < configuracionPanel.getModeloUsuarios().getRowCount(); i++) {
			if (configuracionPanel.getModeloUsuarios().getValueAt(i, 0).equals(nombre)) {
				configuracionPanel.getTablaUsuarios().setRowSelectionInterval(i, i);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			String nombre = obtenerUsuarioSeleccionado();
			if (nombre == null) {
				vaciarUsuarioSeleccionado();
			}
			else {
				Usuario usuario = UsuariosGestor.getInstance().buscarUsuarioPorNombre(nombre);
				configuracionPanel.getLblValorUsuario().setText(usuario.getUsuario());
				configuracionPanel.getLblValorRol().setText(usuario.getRol());
				configuracionPanel.getBtnCambiarRol().setEnabled(!nombre.equals(principalControlador.getUsuario().getUsuario()));
				if (usuario.isAdmin()) {
					configuracionPanel.getBtnCambiarRol().setText("Hacer USER");
				}
				else {
					configuracionPanel.getBtnCambiarRol().setText("Hacer ADMIN");
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == configuracionPanel.getBtnCambiarRol()) {
			String nombre = obtenerUsuarioSeleccionado();
			configuracionPanel.modoConfirmar(true, "¿" + configuracionPanel.getBtnCambiarRol().getText() + " a " + nombre + "?");
		}
		else if (e.getSource() == configuracionPanel.getBtnSiConfirmar()) {
			cambiarRol();
		}
		else if (e.getSource() == configuracionPanel.getBtnNoConfirmar()) {
			configuracionPanel.modoConfirmar(false, "");
		}
	}

	private void cambiarRol() {
		String nombre = obtenerUsuarioSeleccionado();
		String nuevoRol;
		String error;
		if (UsuariosGestor.getInstance().buscarUsuarioPorNombre(nombre).isAdmin()) {
			nuevoRol = "USER";
			error = UsuariosGestor.getInstance().quitarAdmin(nombre, principalControlador.getUsuario().getUsuario(), rolUsuario);
		}
		else {
			nuevoRol = "ADMIN";
			error = UsuariosGestor.getInstance().hacerAdmin(nombre, rolUsuario);
		}
		if (error.isEmpty()) {
			recargarPanel();
			seleccionarEnTabla(nombre);
			configuracionPanel.mostrarExito(nombre + " ahora es " + nuevoRol + ".");
		}
		else {
			configuracionPanel.modoConfirmar(false, "");
			configuracionPanel.mostrarError(error);
		}
	}

	public PrincipalControlador getPrincipalControlador() {
		return principalControlador;
	}

	public void setPrincipalControlador(PrincipalControlador principalControlador) {
		this.principalControlador = principalControlador;
	}

	public ConfiguracionPanel getConfiguracionPanel() {
		return configuracionPanel;
	}

	public void setConfiguracionPanel(ConfiguracionPanel configuracionPanel) {
		this.configuracionPanel = configuracionPanel;
	}

	public String getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}
}
