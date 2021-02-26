/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.Color;
import javax.swing.JOptionPane;
import vistas.PanelAdPagos;
/**
 *
 * @author Ariel
 */
public class EstadoCC extends Notificador{
    //Aunque EstadoCC no está presente como entidad en la Base de datos se puede representar mediante esta clase
    private int dias;

    public EstadoCC(int id, int prioridad, String tipo, String descripcion, String nombre, String apellido, int dias, Color color){  
    //int id, int prioridad, String tipo, String descripcion, String nombre, String apellido - id del cliente
        super(id, prioridad, tipo, descripcion, nombre, apellido);
        this.dias = dias;  //Días de Impaga la CC.
        super.color = color;
    }
    
    @Override  
    public void Notificar(boolean valor) {
        // Desde acá se activa o se cancela la notificación del EstadoCC
        //Cancelar la notificación de EstadoCC ¿cancelaría la notificación para la planilla impaga más vieja o para todas?. 
        //Necesidad de un atributo notificar en el Cliente.
        JLabelAriel label = new  JLabelAriel("¿Está seguro que no quiere recibir más notificaciones del estado de CC de: "
                +super.getNombre()+" "+super.getApellido());
        if(!valor){  //False si se desactiva la notificación (única forma en la que se usa por ahora en el sistema)
            int opcion = JOptionPane.showConfirmDialog(null, label, "INFO", JOptionPane.WARNING_MESSAGE);
            if(opcion == JOptionPane.OK_OPTION)  //Puse que si, hay que quitar la notificación
                SetearNotif(valor);
            else
                ;  //No quito la notificación porque decidí que no dsps de la pregunta
        }
        else
            SetearNotif(valor); // Estoy seteando para que notifique
        
        super.getControlador().getPanelPrincipal().onFocus();  //Recargo al vista
    }

    private void SetearNotif(boolean valor){
        //Metodo que se encarga solamente de llamar a la base de datos para setear el atributo notificar 
        String query = "UPDATE cliente SET notificar = '"+valor+"' WHERE idcliente = '"+super.getId()+"' ";
        try{
            PreparedStatement pst = super.getControlador().obtenerConexion().prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void verNotificacion() {
        // Abriría la vista "Ver Planillas" del cliente para que aparezcan las que tiene adeudadas.
        //El id de esta clase es el del cliente (idcliente)
        String[] options = new String[] {"Ver Estado de CC del Cliente: "+this.getApellido()+" "+this.getNombre(), "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel(""+this.getNombre()+" "+this.getApellido()+" debe desde hace: "+this.dias+" días");
        PanelAdPagos panelAdPagos = new PanelAdPagos(super.getId());  //Creo la nueva vista
        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch(response){
            case 0:
                super.getControlador().cambiarDePanel(panelAdPagos, "Administrar Pagos");
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
    
    @Override
    public void verNotificacion(int sobrecarga) {
    // Método para que su muestre la notificación emergente al inicio del programa. No permite "Ver Notificación" pero sí "Dejar de notficar"
        String[] options = new String[] {"Aceptar ", "Dejar de Notificar"};
        JLabelAriel label1 = new JLabelAriel("Cambio el Estado de CC de: "+this.getApellido()+" "+this.getNombre());

        int response = JOptionPane.showOptionDialog(null, label1, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch(response){
            case 0:
                // Click en Aceptar, nada que hacer
                break;
            case 1:
                this.Notificar(false);  //Cancelo la notificacion del Cliente
                break;
            default:
                ;
                break;
        }
    }
    
    @Override
    public boolean esNotificacionInmediata(){
        // Sirve para consultar a la notificación si es notificación inmediata (En EstadoCC es notif. inmediata cuando cambia de estado)
        boolean valor = false;
        if(super.getPrioridad() == 30)
            valor = true;
        else if(super.getPrioridad() == 45)
            valor = true;
        return valor;
    }
    
}
