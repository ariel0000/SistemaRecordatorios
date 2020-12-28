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
    private String marca, modelo;
    public Reparacion(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido, String marca, String modelo){
        super(id, prioridad, tipo, descripcion, nombre, apellido);
        this.marca = marca;
        this.modelo = modelo;
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
        String descripAcortada = this.getDescripcion();
        String[] options = new String[] {"Ver Planilla", "Dejar de Notificar"};
        if(this.getDescripcion().length() > 40)
            descripAcortada = this.getDescripcion().substring(0, 40).concat("...");
        JLabelAriel label1 = new JLabelAriel("Reparación: "+descripAcortada+".  Vh: "+this.getMarca()+" "+this.getModelo());//No más de 40 chars
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
}
