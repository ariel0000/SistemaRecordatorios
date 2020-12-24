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
        //Cancelar la notificación de EstadoCC ¿cancelaría la notificación para la planilla impaga más vieja o para todas?
    }

    @Override
    public void verNotificacion() {
        // Abriría la vista "Ver Planillas" del cliente para que aparezcan las que tiene adeudadas.
        //El id de esta clase es el del cliente (idcliente)
    }

    public int getDias() {
        return dias;
    }
    
    
}
