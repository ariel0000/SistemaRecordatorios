/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

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
        //Creo que tendría que abrir la vista de "Ver Reparaciones" con el vehículo seleccionado
        PanelReparaciones panelRep;
        String query = "SELECT vh.idvehiculo FROM vehiculo AS vh INNER JOIN planilla AS p ON vh.idvehiculo = p.idvehiculo INNER JOIN "
                + "reparacion AS r ON r.idplanilla = p.idplanilla WHERE r.idrepracion = '"+super.getId()+"' ";
        
    }
}
