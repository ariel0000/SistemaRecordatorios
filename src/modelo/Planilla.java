/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Ariel
 */
public class Planilla extends Notificador{

    public Planilla(int id, int prioridad){
        super(id, prioridad);
    }        
    
    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de una planilla
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver la planilla
    }
    
}
