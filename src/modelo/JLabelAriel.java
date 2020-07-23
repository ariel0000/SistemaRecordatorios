/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author Ariel
 */
public class JLabelAriel extends JLabel{
    
    Font fuente = new Font("Tahoma", Font.PLAIN, 18);
    
    public JLabelAriel(String mensaje){
        super(mensaje);
        super.setFont(fuente);
    }

    public String getMensaje(){
        return super.getText();
    }

    public void cambiarFuente(Font font){
        super.setFont(font);
    }
    
}
