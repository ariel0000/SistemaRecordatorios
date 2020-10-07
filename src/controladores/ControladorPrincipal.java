/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import conexion.DataBase;
import java.sql.Connection;
import vistas.JPanelCustom;
import vistas.Principal;

public class ControladorPrincipal {

    
    private Principal principal;
    private static ControladorPrincipal instancia;

   // public ControladorPrincipal() {} No se usa con el patrón Singleton
    
    public static ControladorPrincipal getInstancia(){
        if(instancia == null)
            instancia = new ControladorPrincipal();
        return instancia;
    }
    
    public void cambiarDePanel(JPanelCustom panel, String titulo){
     // boolean yaExiste = false; //Es para corroborar si una vista ya está desplegada
     //   principal.getJTabbedPaneP().getTabCount();
     /*   for(int i = 0; i < principal.getJTabbedPaneP().getTabCount(); i++){ //.getComponentCount() reemplazado por getTabCount()
            if(this.principal.getJTabbedPaneP().getTabComponentAt(i) == panel){  //Parece no funcionar - Y si funciona no creo que convenga
                yaExiste = true; //No hago break porque este if "dura poco"
            }
        } */
      /*  if(yaExiste)
            this.principal.getJTabbedPaneP().setSelectedComponent(panel);
        else{  */
        this.principal.getJTabbedPaneP().addTab(titulo, panel);
        this.principal.getJTabbedPaneP().setSelectedComponent(panel);
       // }    
    }

    public void focusear(){
        JPanelCustom panel = (JPanelCustom) this.principal.getJTabbedPaneP().getSelectedComponent();  //Obtengo el panel actual
        panel.onFocus();   //Actualizo su info
    }
    
    public void cerrarPanelSeleccionado() {
        JPanelCustom panelPrincipal;
        if(this.principal.getJTabbedPaneP().getTabCount() > 0){
            int i = this.principal.getJTabbedPaneP().getSelectedIndex();
            if(!this.principal.getJTabbedPaneP().getTitleAt(i).equals("Principal")) { //Si no es el panel Principal
            //Solo se borra si no es el panel Principal
                
                // public boolean sePuedeCerrar()
                JPanelCustom seleccionado = (JPanelCustom) this.principal.getJTabbedPaneP().getSelectedComponent();
                if(seleccionado.sePuedeCerrar())  //JPanelCustom tendría que ser extendido por todas las vistas
                    this.principal.getJTabbedPaneP().remove(i); 
                //Tendría que llamar acá al método onFocus del JPanelCustom para que la vista cargue lo necesario por obtener el foco
                seleccionado = (JPanelCustom) this.principal.getJTabbedPaneP().getSelectedComponent();
                i = this.principal.getJTabbedPaneP().getSelectedIndex();
                if(!this.principal.getJTabbedPaneP().getTitleAt(i).equals("Principal")) //Despues de cerrar, me quedó el Principal?
                    seleccionado.onFocus(); //No es el principal asi que hay que avisarle que tiene el "foco"
            }
            else{
                panelPrincipal = (JPanelCustom) this.principal.getJTabbedPaneP().getSelectedComponent();
                if(panelPrincipal.sePuedeCerrar())
                    this.principal.dispose(); //Si el panelPrincipal me deja cerrar cierro el programa
            }
        }
    }

    public Connection obtenerConexion(){
       // this.controladorBdD.abrirConexion();
        DataBase bdd = DataBase.getInstancia();
        Connection conexion = bdd.getConexion();
        return conexion;
    }
    
    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
    
}