/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import vistas.PanelPlanillaNueva;

/**
 *
 * @author Ariel
 */
public class Planilla extends Notificador {

    public Planilla(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido){
        super(id, prioridad, tipo, descripcion, nombre, apellido); 
    }        
    
    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de una planilla
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver el cheque. Será la vista principal?
        PanelPlanillaNueva planilla = new PanelPlanillaNueva(this.getId());
        super.getControlador().cambiarDePanel(planilla, "Ver/Modificar Planilla");

    }
    
}
