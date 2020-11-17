/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import vistas.PanelReparaciones;

/**
 *
 * @author Ariel
 */
public class Mantenimiento extends Notificador {
 
    public Mantenimiento(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido){
        super(id, prioridad, tipo, descripcion, nombre, apellido);  
    }
    
    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de un cheque
    }

    @Override
    public void verNotificacion() {
        try {
            //Creo que tendría que abrir la vista de "Ver Reparaciones" con el vehículo seleccionado filtrado por mantenciones
            PanelReparaciones panelRep;
            int idVh = 0;
            String query = "SELECT vh.idvehiculo FROM vehiculo AS vh INNER JOIN planilla AS p ON vh.idvehiculo = p.idvehiculo INNER JOIN "
                    + "reparacion AS r ON r.idplanilla = p.idplanilla WHERE r.idreparacion = '"+super.getId()+"' ";
            Statement st = super.getControlador().obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idVh = rs.getInt(1);
            }
            if(idVh != 0){
                panelRep = new PanelReparaciones(super.getId(), idVh, "mantenimiento");
                super.getControlador().cambiarDePanel(panelRep, "Ver Mantenimientos");
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al mostrar panel de Reparaciones " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE); 
        }
        
        
        
    }
}
