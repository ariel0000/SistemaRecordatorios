/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de un cheque
    }

    @Override
    public void verNotificacion() {
        int idPlanilla = 0;
        PanelPlanillaNueva planilla;
        JLabelAriel label1 = new JLabelAriel("Se muestra la planilla que contiene el cheque seleccionado");
        JOptionPane.showMessageDialog(null, label1, "INFO", JOptionPane.INFORMATION_MESSAGE); 
        try {
            //Desde acá se abre la vista correspondiente para poder ver el cheque. Será la vista principal?
            String query = "SELECT p.idplanilla FROM planilla AS p INNER JOIN forma_de_pago AS fdp ON fdp.idplanilla = p.idplanilla INNER JOIN"
                    + " cheque AS c ON c.idforma_de_pago = fdp.idforma_de_pago WHERE c.idcheque = '"+this.getId()+"' ";
            Statement st = super.getControlador().obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){ //En teoría no tendría que haber más de una iteración
                idPlanilla = rs.getInt(1);
                planilla = new PanelPlanillaNueva(idPlanilla);
                super.getControlador().cambiarDePanel(planilla, "Ver/Modificar Planilla");
            }
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al intentar ver la planilla asociada al cheque: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);  
        }
    }
   
    private long getNumeroCheque(){
        return this.numeroDeCheque;
    }
}
