/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Ariel
 */
public abstract class Notificador {
    private int id;
    private Date fecha;
    
    public Notificador(int id){
        this.id = id;
    }
    
    public abstract void Notificar(boolean valor);
    //Valor Booleano que determina si la
    
    public abstract void verNotificacion();
    
}
