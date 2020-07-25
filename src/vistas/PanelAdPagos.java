/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ariel
 */
public class PanelAdPagos extends JPanelCustom {

    
    private DefaultTableModel modelo;
    private ControladorPrincipal controlador;
    
    public PanelAdPagos() {
        modelo = new DefaultTableModel(){       
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        initComponents();
        this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        modelo.setColumnIdentifiers(getColumnas());
        this.controlador = ControladorPrincipal.getInstancia();
    }

    private String[] getColumnas(){
        String columna[] = new String[]{"Nombre Cliente", "Apellido", "N° Planilla", "¿Impaga?"};
        return null;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePlanillas = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Clientes con Planillas en el sistema");

        jTablePlanillas.setModel(modelo);
        jScrollPane1.setViewportView(jTablePlanillas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addGap(138, 138, 138))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public boolean sePuedeCerrar() {
        //Teoricamente no habría problemas para cerrar en cualquier momento esta pestaña
        return true;  //El controlador se encarga de cerrar la pestaña actual
    }

    @Override
    public void onFocus() {
        //Cosas a cargar cuando esta vista toma el foco. Ver que conviene
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePlanillas;
    // End of variables declaration//GEN-END:variables
}
