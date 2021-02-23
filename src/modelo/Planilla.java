/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import vistas.PanelPlanillaNueva;

/**
 *
 * @author Ariel
 */
public class Planilla extends Notificador {

    public Planilla(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido) {
        super(id, prioridad, tipo, descripcion, nombre, apellido);
    }

    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de una planilla
        try {
            PreparedStatement ps = super.getControlador().obtenerConexion().prepareStatement("UPDATE planilla SET notificar = '" + valor + "'"
                    + " WHERE idplanilla = '" + super.getId() + "' ");
            ps.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al intentar cambiar atributo Notificar de la planilla: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        super.getControlador().getPanelPrincipal().onFocus();  //Actualizo la tabla
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver la Planilla. Será la vista principal?
        String[] options = new String[] {"Ver Planilla", "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel("Planilla de: "+this.getNombre()+" "+this.getApellido());
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                PanelPlanillaNueva planilla = new PanelPlanillaNueva(this.getId());
                super.getControlador().cambiarDePanel(planilla, "Ver/Modificar Planilla");
                break;
            case 1:
                this.Notificar(false);
                break;
            default:
                ;
                break;
        }
    }

    @Override
    public void verNotificacion(int sobrecarga) {
        // Método para que su muestre la notificación emergente al inicio del programa. No permite "Ver Notificación" pero sí "Dejar de notficar"
    }

    @Override
    public boolean esNotificacionInmediata(){
        boolean valor = false;
        // Por ahora no se usan notificaciones inmediatas (al inicio del programa) para las planillas impagas
        return valor;
    }
}
