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
public class Cheque extends Notificador implements Comparable<Cheque>{

    private long numeroDeCheque;
    public Cheque(int id, int prioridad, long numeroCheque) {
        super(id, prioridad);
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

    @Override
    public int compareTo(Cheque e){  //Este método servirá para ordenar el ArrayList con un simple "cheques.sort()" - ver Vista Principal
        if(this.getPrioridad() < e.getPrioridad())
            return -1;
        if(this.getPrioridad() > e.getPrioridad())
            return 1;
        return 0;
    }
}
