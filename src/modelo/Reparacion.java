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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import vistas.PanelPlanillaNueva;

/**
 *
 * @author Ariel
 */
public class Reparacion extends Notificador{

    public Reparacion(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido){
        super(id, prioridad, tipo, descripcion, nombre, apellido); 
    }

    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de una Reparación
        try {
            //Desde acá se activa o cancela la notificación de un cheque
            PreparedStatement ps = super.getControlador().obtenerConexion().prepareStatement("UPDATE reparacion SET notificar = '"+valor+"'"
                    + " WHERE idreparacion = '"+super.getId()+"' ");
            ps.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al intentar cambiar atributo Notificar de la Reparación: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);   
        }
        super.getControlador().getPanelPrincipal().onFocus();  //Recargo al vista
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver la Reparación.
        int idPlanilla = 0;
        String[] options = new String[] {"Ver Planilla", "Dejar de Notificar la Reparacion"};
        JLabelAriel label1 = new JLabelAriel("Vea la planilla de la reparación o cancelar aviso de esta notificación");
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        
        // JOptionPane.showMessageDialog(null, label1, "INFO", JOptionPane.INFORMATION_MESSAGE);
        switch (response) {
            case 0:
                String query = "SELECT p.idplanilla FROM planilla AS p INNER JOIN reparacion AS r ON r.idplanilla = p.idplanilla "
                        + "WHERE r.idreparacion = '" + super.getId() + "' ";
                try {
                    Statement st = super.getControlador().obtenerConexion().createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while(rs.next())
                        idPlanilla = rs.getInt(1);   //idPlanilla
                    PanelPlanillaNueva panelNPl = new PanelPlanillaNueva(idPlanilla);
                    super.getControlador().cambiarDePanel(panelNPl, "Ver/Modificar Planilla");
                } catch (SQLException ex) {
                    JLabel label = new JLabelAriel("Error al consultar planilla de la reparación. "+ex.getMessage());
                    JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
                }       break;
            case 1:
                this.Notificar(false);  //Cancelo la notificación
                break;
            default:
                ;
                break;
        }
    }
}
