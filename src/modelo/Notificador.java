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
public abstract class Notificador implements Comparable<Notificador>{
    private int id;
    private int prioridad;
    private String tipo;
    
    public Notificador(int id, int prioridad, String tipo){
        this.id = id;
        this.prioridad = prioridad;
        this.tipo = tipo;
    }
    
    public abstract void Notificar(boolean valor);
    //Valor Booleano que determina si el Notificador sigue o no enviando avisos
    
    public abstract void verNotificacion();

    public int getPrioridad() {
        return prioridad;
    }
    
    public String getTipo(){
        return this.tipo;
    }
 
    @Override
    public int compareTo(Notificador n){
        if(this.getPrioridad() < n.getPrioridad())
            return -1;
        if(this.getPrioridad() > n.getPrioridad())
            return 1;
        return 0;
    }
}
