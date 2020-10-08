/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Ariel
 */
public class EstadoReparacion extends Estado{

    @Override
    public void cargarNotifificaciones() {
        super.getControlador().getPanelPrincipal().borrarTabla();
        ArrayList reparaciones = super.getControlador().getPanelPrincipal().cargarReparaciones(); //
        super.getControlador().getPanelPrincipal().cargarNotificadoresATabla(reparaciones);        
    }
    
}
