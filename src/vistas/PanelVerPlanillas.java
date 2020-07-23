/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class PanelVerPlanillas extends JPanelCustom {

    private DefaultTableModel modelo;
    private ControladorPrincipal controlador;
    
    public PanelVerPlanillas() {
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
        obtenerPlanillas();
    }
    
    
    private String[] getColumnas() { 
        String columna[] = new String[]{"N°", "Fecha Entrada", "Impaga", "Fecha Salida", "   Cliente   ", "   Vehículo   ", "    Descripcion    "};
        return columna;
    }

    private void obtenerPlanillas() {
        //Método que carga la tabla de planillas.

        Object Datos[] = new Object[7];
        String nombre, marca;
        Boolean pagado;
        Connection co = this.controlador.obtenerConexion();
        String consulta = "SELECT p.idplanilla, p.fecha_de_entrada, p.pagado, p.fecha_de_salida, per.nombre, per.apellido, v.marca, "
                + "v.modelo, v.patente, p.descripcion from planilla as p inner join cliente as c on p.idcliente = c.idcliente inner join persona as per "
                + "ON per.idpersona = c.idpersona inner join vehiculo as v on v.idvehiculo = p.idvehiculo";
        try {
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                Datos[0] = rs.getInt(1); //Numero de planilla
                Datos[1] = rs.getDate(2);
                pagado = rs.getBoolean(3);
                if (pagado) //Verdadero si pago
                {
                    Datos[2] = "No"; //No está impaga la planilla
                } else {
                    Datos[2] = "Sí";
                }
                Object fechaNula = rs.getDate(4); //Puede ser nula
                if (fechaNula != null) {
                    Datos[3] = rs.getDate(4);
                }
                nombre = rs.getString(5);
                String nombreYapellido = nombre.concat(" " + rs.getString(6)); //Agrego el apellido
                Datos[4] = nombreYapellido;
                marca = rs.getString(7);
                String marcaModelo = marca.concat(" " + rs.getString(8));
                String marcaModeloYpatente = marcaModelo.concat(" " + rs.getString(9));
                Datos[5] = marcaModeloYpatente;
                Datos[6] = rs.getString(10);
                //-----------
                this.modelo.addRow(Datos);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + ex.getMessage());
        }
        this.jTablePlanillas.updateUI();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jCheckBox3 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePlanillas = new javax.swing.JTable();
        jButtonAdPlanilla = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBoxDesde = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jButtonNuevaPlanilla = new javax.swing.JButton();
        jButtonBorrarPlanilla = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Filtros:");

        jComboBox1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox1.setText("Cliente:");

        jCheckBox2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox2.setText("Camión:");

        jComboBox2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox3.setText("Patente:");

        jTablePlanillas.setModel(modelo);
        jScrollPane1.setViewportView(jTablePlanillas);

        jButtonAdPlanilla.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAdPlanilla.setText("Ver/Modificar Planilla");
        jButtonAdPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdPlanillaActionPerformed(evt);
            }
        });

        jButtonCancelar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCancelar.setText("Cerrar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton3.setText("Filtrar");

        jCheckBox4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox4.setText("Impagas");

        jCheckBoxDesde.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxDesde.setText("Desde:");

        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextField1.setText("jCalendar");

        jButtonNuevaPlanilla.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonNuevaPlanilla.setText("Nueva Planilla");
        jButtonNuevaPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevaPlanillaActionPerformed(evt);
            }
        });

        jButtonBorrarPlanilla.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonBorrarPlanilla.setText("Borrar Planilla");
        jButtonBorrarPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarPlanillaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, 0, 133, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxDesde)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAdPlanilla)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonNuevaPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBorrarPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3)
                    .addComponent(jButton3)
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxDesde)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdPlanilla)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonNuevaPlanilla)
                    .addComponent(jButtonBorrarPlanilla))
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAdPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdPlanillaActionPerformed
        //Habría que pasarle el numero de planilla para que pueda ser identificada
        PanelPlanillaNueva p1;
        int filaSeleccionada;
        filaSeleccionada = this.jTablePlanillas.getSelectedRow();
        if(filaSeleccionada != -1){
            int nPlanilla = (int) this.modelo.getValueAt(filaSeleccionada, 0);
            p1 = new PanelPlanillaNueva(nPlanilla); //Acá va el número de planilla
            this.controlador.cambiarDePanel(p1, "Ver/Modificar Planilla");
        }
        else{
            JOptionPane.showMessageDialog(null, "Error: debe seleccionar una planilla ");
        }
    }//GEN-LAST:event_jButtonAdPlanillaActionPerformed

    private void jButtonNuevaPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevaPlanillaActionPerformed
        PanelPlanillaNueva p = new PanelPlanillaNueva();
        this.controlador.cambiarDePanel(p, "Nueva Planilla");
    }//GEN-LAST:event_jButtonNuevaPlanillaActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // Llamar al metodo cerrarPanelSeleccionado
        this.controlador.cerrarPanelSeleccionado();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonBorrarPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarPlanillaActionPerformed
    // Action Performed para borrar la planilla seleccionada. Si la planilla tiene reparaciones asignadas debe preguntar si desea borrarlas
        int filaSelect = this.jTablePlanillas.getSelectedRow();
        JLabelAriel labelRep = new JLabelAriel(""), labelPagos = new JLabelAriel("");
        if(filaSelect != -1){ //Hay fila seleccionada
            int nPlanilla = (int) this.modelo.getValueAt(filaSelect, 0);
            boolean repAsig = this.tieneReparacionesAsignadas(nPlanilla);
            boolean pagAsig = this.pagosAsignados(nPlanilla);
            if(!repAsig && !pagAsig) //Si no tiene reparaciones asignadas ni pagos asignados puede borrar
                borrarPlanilla(nPlanilla);
            else{
                if(repAsig)  //Si tiene reparaciones asignadas
                    labelRep = new JLabelAriel("La planilla tiene reparaciones asignadas");
                if(pagAsig)  //Si tiene pagos asignados
                    labelPagos = new JLabelAriel("La planilla tiene pagos asginados");
                JLabelAriel label2 = new JLabelAriel(labelRep.getMensaje()+" "+labelPagos.getMensaje());
                JOptionPane.showMessageDialog(null, label2, "¡ATENCIÓN!", JOptionPane.WARNING_MESSAGE);
                JLabelAriel label = new JLabelAriel("¿ Desea borrar la planilla  ?");
                int confirmDialog = JOptionPane.showConfirmDialog(null, label);
                if(confirmDialog == JOptionPane.OK_OPTION)
                    borrarPlanilla(nPlanilla);
            }
        }  
        else{
            JLabelAriel label = new JLabelAriel("No hay fila seleccionada");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonBorrarPlanillaActionPerformed

    private boolean tieneReparacionesAsignadas(int nPlanilla){
        //Compueba si una planilla tiene reparaciones asignadas
        boolean valor = false;
        int cantidad = 0; //servirá para ver la cantidad de reparaciones que tiene asignada la planilla
        String query = "Select count(*) from reparacion as r WHERE r.idplanilla = '"+nPlanilla+"' ";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                cantidad = rs.getInt(1);
            }
            if(cantidad > 0)  //Tiene reparaciones asignadas
                valor = true;
            
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Problema al consultar las reparaciones de una Planilla: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return valor;
    }

    private void borrarPlanilla(int numPlanilla){
        //Método para borrar la planilla seleccionada
        try {
            //Método que borra la planilla seleccionada y sus reparaciones. La reparación está configurada asi: ON DELETE CASCADE
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM planilla WHERE idplanilla = '"+numPlanilla+"' ");
            ps.executeUpdate();
            JLabelAriel label = new JLabelAriel("Planilla borrada");
            JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.INFORMATION_MESSAGE);
            recargarPlanillas();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("No se pudo eliminar: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private boolean pagosAsignados(int nPlanilla){
        //Método que consulta si la planilla tiene pagos asginados
        boolean valor = false;
        String consulta = "SELECT count(*) FROM forma_de_pago AS f WHERE f.idplanilla = '"+nPlanilla+"' ";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                if(rs.getInt(1) > 0)
                    valor = true;
            }
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al consultar pagos de las planilla, método pagosAsignados: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return valor;
    }
    
    private void recargarPlanillas(){
        //Recargar filas de la planilla
        //----
        // Elimino las filas actuales
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePlanillas.getModel();
        dtm.setRowCount(0);
        //-----
        //Cargo las filas de las planillas existentes
        this.obtenerPlanillas();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonAdPlanilla;
    private javax.swing.JButton jButtonBorrarPlanilla;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonNuevaPlanilla;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBoxDesde;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePlanillas;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        //Ver ejemplo en "Nueva Planilla"
        return true;
    }

    @Override
    public void onFocus() {
        //Recarga los datos cuando esta vista toma el foco
        //La tabla de planillas tendría que recargarse
        this.recargarPlanillas();
    }
}
