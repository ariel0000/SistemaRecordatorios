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
public class Cheque extends Notificador {
    
    private long numeroDeCheque;
    public Cheque(int id, int prioridad, long numeroCheque, String tipo) {
        super(id, prioridad, tipo);
        this.numeroDeCheque = numeroCheque;
    }

    @Override
    public void Notificar(boolean valor) {
        //Desde acá se activa o cancela la notificación de un cheque
    }

    @Override
    public void verNotificacion() {
        //Desde acá se abre la vista correspondiente para poder ver el cheque.
        
    }
   
    private long getNumeroCheque(){
        return this.numeroDeCheque;
    }
}
