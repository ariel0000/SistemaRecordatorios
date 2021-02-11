/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controladores.ControladorPrincipal;
import javafx.scene.paint.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import vistas.PanelPrincipal;

/**
 *
 * @author Ariel
 */
public class ColorearFilas extends DefaultTableCellRenderer {

    private ControladorPrincipal controlador = ControladorPrincipal.getInstancia();
    private PanelPrincipal p1;
    private Color color;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean Selected, boolean hasFocus, int row, int col) {
        //Renderizador para tablas, que va a servir para marcar celdas con un dato importante a tener en Cuenta
        super.getTableCellRendererComponent(table, value, Selected, hasFocus, row, col);
        //if()
        color = obtenerColorDeFila(row);
        java.awt.Color awtColor = new java.awt.Color((float) color.getRed(),
                                                    (float) color.getGreen(),
                                                    (float) color.getBlue(),
                                                    (float) color.getOpacity());
        
        setBackground(awtColor);
        table.setSelectionForeground(java.awt.Color.blue);
        return this;
    }
    
    private Color obtenerColorDeFila(int row){
        this.p1 = this.controlador.getPanelPrincipal();
        return p1.ColorDeNotificiacion(row);
    }
    
}
