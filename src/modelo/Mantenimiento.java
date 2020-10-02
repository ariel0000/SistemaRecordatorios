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
public class Mantenimiento extends Notificador{
 
    public Mantenimiento(int id, int prioridad, String tipo){
        super(id, prioridad, tipo);  
    }
    
    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de un cheque
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver el cheque
    }
}
