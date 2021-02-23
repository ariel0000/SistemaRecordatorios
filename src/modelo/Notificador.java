/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controladores.ControladorPrincipal;
import java.awt.Color;

/**
 *
 * @author Ariel
 */
public abstract class Notificador implements Comparable<Notificador>{
    private int id;
    private int prioridad;
    private String tipo, nombre, apellido, descripcion;
    private static ControladorPrincipal controlador;
    protected Color color = new Color(220, 235, 210, 250);
    
    public Notificador(){

    }
    
    public Notificador(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido){
        this.id = id;
        this.prioridad = prioridad;
        this.tipo = tipo;
        this.nombre = nombre;
        this.apellido = apellido; //Serían nombre y apellido del cliente
        this.descripcion = descripcion;
        Notificador.controlador = ControladorPrincipal.getInstancia();
    }
    
    public abstract void Notificar(boolean valor);
    //Valor Booleano que determina si el Notificador sigue o no enviando avisos
    
    public abstract void verNotificacion();
    
    public abstract void verNotificacion(int sobrecarga);  //Usaremos este método para las notificaciones inmediatas

    public abstract boolean esNotificacionInmediata();
    
    public int getPrioridad() {
        return prioridad;
    }
    
    public String getTipo(){
        return this.tipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDescripcion() {
        return descripcion;
    }
 
    @Override
    public int compareTo(Notificador n){
        if(this.getPrioridad() < n.getPrioridad())
            return -1;
        if(this.getPrioridad() > n.getPrioridad())
            return 1;
        return 0;
    }

    public ControladorPrincipal getControlador() {
        return controlador;
    }

    public void setControlador(ControladorPrincipal controlador) {
        Notificador.controlador = controlador;
    }
    
    public Color getColor(){
        return this.color;
    }

}
