/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import javax.swing.JPanel;

/**
 *
 * @author ari_0
 */
public abstract class JPanelCustom extends JPanel{
    
    public abstract boolean sePuedeCerrar();
    public abstract void onFocus(); //Permitirá al controlador avisar a la vista que tiene otra vez el foco: para cargar sus datos
}
