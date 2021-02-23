/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Color;
import javax.swing.JOptionPane;
import vistas.PanelPlanillaNueva;
/**
 *
 * @author Ariel
 */
public class Cheque extends Notificador {
    
    private long numeroDeCheque;
    
    public Cheque(int id, int prioridad, long numeroCheque, String tipo, String descripcion, String nombre, String apellido) {
        super(id, prioridad, tipo, descripcion, nombre, apellido);
        this.numeroDeCheque = numeroCheque;
    }
    
    public Cheque(int id, int prioridad, long numeroCheque, String tipo, String descripcion, String nombre, String apellido, Color color) {
        super(id, prioridad, tipo, descripcion, nombre, apellido);
        super.color = color;
        this.numeroDeCheque = numeroCheque;
    }

    @Override
    public void Notificar(boolean valor) {
        try {
            //Desde acá se activa o cancela la notificación de un cheque
            PreparedStatement ps = super.getControlador().obtenerConexion().prepareStatement("UPDATE cheque SET notificar = '"+valor+"'"
                    + " WHERE idcheque = '"+super.getId()+"' ");
            ps.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al intentar cambiar atributo Notificar del cheque: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);   
        }
        super.getControlador().getPanelPrincipal().onFocus();  //Recargo al vista
    }

    @Override
    public void verNotificacion() {
        int idPlanilla = 0;
        PanelPlanillaNueva planilla;
        String[] options = new String[] {"Ver Planilla", "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel("Cheque N°: "+this.getNumeroCheque()+"  Pertenece a: "+this.getNombre()+" "+this.getApellido());
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                try {
                    //Desde acá se abre la vista correspondiente para poder ver el cheque. Será la vista principal?
                    String query = "SELECT p.idplanilla FROM planilla AS p INNER JOIN forma_de_pago AS fdp ON fdp.idplanilla = p.idplanilla INNER JOIN"
                            + " cheque AS c ON c.idforma_de_pago = fdp.idforma_de_pago WHERE c.idcheque = '" + this.getId() + "' ";
                    Statement st = super.getControlador().obtenerConexion().createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) { //En teoría no tendría que haber más de una iteración
                        idPlanilla = rs.getInt(1);
                        planilla = new PanelPlanillaNueva(idPlanilla);
                        super.getControlador().cambiarDePanel(planilla, "Ver/Modificar Planilla");
                    }
                } catch (SQLException ex) {
                    JLabelAriel label = new JLabelAriel("Error al intentar ver la planilla asociada al cheque: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
                }   break;
            case 1:
                Notificar(false); //Cancelo la notificación
                break;
            default:
                ;  //Se cerró la vista - nada que hacer
                break;
        }
    }
   
    private long getNumeroCheque(){
        return this.numeroDeCheque;
    }

    @Override
    public void verNotificacion(int sobrecarga) {
        // Método para que su muestre la notificación emergente al inicio del programa. No permite "Ver Notificación" pero sí "Dejar de notficar"
        String[] options = new String[] {"Aceptar", "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel("Cheque A Cobrar N°: "+this.getNumeroCheque()+" | De: "+this.getNombre()+" "+this.getApellido());
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                break;  //Apreté aceptar
            case 1:
                Notificar(false); //Cancelo la notificación
                break;
            default:
                ;  //Se cerró la vista - nada que hacer
                break;
        } 
    }
    
    @Override
    public boolean esNotificacionInmediata(){
        boolean valor = false;
        if(super.getPrioridad() == 30){
            valor = true;  //Es para notificación inmediata
        }
        return valor;
    }
}
