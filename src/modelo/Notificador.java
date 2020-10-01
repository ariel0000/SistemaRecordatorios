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
public abstract class Notificador {
    private int id;
    private int prioridad;
    
    public Notificador(int id, int prioridad){
        this.id = id;
        this.prioridad = prioridad;
    }
    
    public abstract void Notificar(boolean valor);
    //Valor Booleano que determina si el Notificador sigue o no enviando avisos
    
    public abstract void verNotificacion();

    public int getPrioridad() {
        return prioridad;
    }
 
}
