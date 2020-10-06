/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controladores.ControladorPrincipal;

/**
 *
 * @author Ariel
 */
public abstract class Estado {
    //Interface Estado que va a permitir cambiar el comportamiento de la carga de las notificaciones del panel principal
    private ControladorPrincipal controlador;
    
    public abstract void cargarNotifificaciones();

    protected ControladorPrincipal getControlador() {
        return controlador;
    } 
    
}
