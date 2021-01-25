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
import vistas.PanelReparaciones;

/**
 *
 * @author Ariel
 */
public class Mantenimiento extends Notificador {
 
    private String marca, modelo;
    public Mantenimiento(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido, String marca, String modelo){
        super(id, prioridad, tipo, descripcion, nombre, apellido);  
        this.marca = marca;
        this.modelo = modelo;
    }
    
    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de un mantenimiento (Service)
        try {
            PreparedStatement ps = super.getControlador().obtenerConexion().prepareStatement("UPDATE reparacion SET notificar = '"+valor+"'"
                    + " WHERE idreparacion = '"+super.getId()+"' ");
            ps.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al intentar cambiar atributo Notificar del Mantenimiento: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);   
        }
        super.getControlador().getPanelPrincipal().onFocus();  //Recargo al vista
    }

    @Override
    public void verNotificacion() {
        //Creo que tendría que abrir la vista de "Ver Reparaciones" con el vehículo seleccionado filtrado por mantenciones
        String[] options = new String[] {"Ver Service", "Dejar de Notificar"};
        String descripAcortada = this.getDescripcion();
        if(descripAcortada.length() > 30)
            descripAcortada = descripAcortada.substring(0, 30).concat("...");
        JLabelAriel label1 = new JLabelAriel("Descripción: "+descripAcortada+"  Vh: "+this.getMarca()+this.getModelo());
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (response) {
            case 0:
                try {
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
                }   break;
            case 1:
                JLabelAriel label = new JLabelAriel("¿Está seguro de dejar de notificar: "+super.getDescripcion());
                Notificar(false);  //Cancelo la notificación del mantenimiento
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
