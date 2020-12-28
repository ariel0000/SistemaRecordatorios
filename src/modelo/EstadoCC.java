/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import vistas.PanelVerPlanillas;

/**
 *
 * @author Ariel
 */
public class EstadoCC extends Notificador{
    //Aunque EstadoCC no está presente como entidad en la Base de datos se puede representar mediante esta clase
    private int dias;
    public EstadoCC(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido, int dias){  
    //int id, int prioridad, String tipo, String descripcion, String nombre, String apellido - id del cliente
        super(id, prioridad, tipo, descripcion, nombre, apellido);
        this.dias = dias;  //Días de Impaga la CC.
    }
    
    @Override  
    public void Notificar(boolean valor) {
        // Desde acá se activa o se cancela la notificación del EstadoCC
        //Cancelar la notificación de EstadoCC ¿cancelaría la notificación para la planilla impaga más vieja o para todas?. 
        //Necesidad de un atributo notificar en el Cliente.
        String query = "UPDATE cliente SET notificar = '"+valor+"' WHERE idcliente = '"+super.getId()+"' ";
        try{
            PreparedStatement pst = super.getControlador().obtenerConexion().prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        super.getControlador().getPanelPrincipal().onFocus();  //Recargo al vista
    }

    @Override
    public void verNotificacion() {
        // Abriría la vista "Ver Planillas" del cliente para que aparezcan las que tiene adeudadas.
        //El id de esta clase es el del cliente (idcliente)
        String[] options = new String[] {"Ver Planillas", "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel("Cuenta Corriente del Cliente: "+this.getNombre()+" "+this.getApellido());
        PanelVerPlanillas panelVerPlanillas = new PanelVerPlanillas(super.getId());  //Creo la nueva vista
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch(response){
            case 0:
                super.getControlador().cambiarDePanel(panelVerPlanillas, "Ver Planillas");
                break;
            case 1:
                this.Notificar(false);  //Cancelo la notificacion del Cliente
                break;
            default:
                ;
                break;
        }
    }

    public int getDias() {
        return dias;
    }
    
    
}
