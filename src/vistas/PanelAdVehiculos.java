/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ComboItem;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class PanelAdVehiculos extends JPanelCustom {

    private DefaultTableModel modeloVh;
    private DefaultTableModel modeloPlanilla;
    private final ControladorPrincipal controlador;
    public PanelAdVehiculos() {
        modeloVh = new DefaultTableModel(null, getColumnas()){       
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };
        modeloPlanilla = new DefaultTableModel(null, getColumnasPlanilla()){       
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };
        initComponents();
        this.jTableVh.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.jTablePlanilla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        controlador = ControladorPrincipal.getInstancia(); //La única instancia de este controlador
        cargarTablas();
        cargarClientesCombo();
    }


    private String[] getColumnasPlanilla() {
        String columnas[] = new String[] {"Numero Planilla", "Fecha Entrada", "Fecha Salida", "Pagado"};
        return columnas;
    }
    
    private String[] getColumnasReparaciones(){
        String columnas[] = new String[] {"Numero Planilla", "Fecha Entrada", "Descripción"};
        return columnas;
    }
    private String[] getColumnas() {
        String columna[] = new String[]{"Patente", "Marca", "Modelo", "Dueño"};
        return columna;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jCheckBoxCli = new javax.swing.JCheckBox();
        jComboBoxCli = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVh = new javax.swing.JTable();
        jButtonNuevoVh = new javax.swing.JButton();
        jButtonBorrarVh = new javax.swing.JButton();
        jButtonModificarVh = new javax.swing.JButton();
        jButtonVerRep = new javax.swing.JButton();
        jButtonVerPlanillas = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePlanilla = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(32767, 520));
        setMinimumSize(new java.awt.Dimension(875, 500));
        setRequestFocusEnabled(false);

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Filtro:");

        jCheckBox1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox1.setText("Patente:");

        jCheckBox2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBox2.setText("Marca:");

        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCli.setText("Cliente:");

        jComboBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTableVh.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 16)); // NOI18N
        jTableVh.setModel(modeloVh);
        jScrollPane1.setViewportView(jTableVh);

        jButtonNuevoVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonNuevoVh.setText("Agregar Nuevo");
        jButtonNuevoVh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoVhActionPerformed(evt);
            }
        });

        jButtonBorrarVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonBorrarVh.setText("Borrar");
        jButtonBorrarVh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarVhActionPerformed(evt);
            }
        });

        jButtonModificarVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonModificarVh.setText("Modificar");
        jButtonModificarVh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarVhActionPerformed(evt);
            }
        });

        jButtonVerRep.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonVerRep.setText("Ver Reparaciones");
        jButtonVerRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerRepActionPerformed(evt);
            }
        });

        jButtonVerPlanillas.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonVerPlanillas.setText("Ver Planillas asociadas");
        jButtonVerPlanillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerPlanillasActionPerformed(evt);
            }
        });

        jTablePlanilla.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 16)); // NOI18N
        jTablePlanilla.setModel(this.modeloPlanilla);
        jScrollPane2.setViewportView(jTablePlanilla);

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setText("Planillas o Reparaciones:");

        jButton6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton6.setText("Ver planilla");

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton1.setText("Filtrar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jCheckBoxCli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonNuevoVh)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBorrarVh)
                                .addGap(12, 12, 12)
                                .addComponent(jButtonModificarVh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonVerRep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonVerPlanillas))))
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBox1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxCli)
                    .addComponent(jComboBoxCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNuevoVh)
                    .addComponent(jButtonBorrarVh)
                    .addComponent(jButtonModificarVh)
                    .addComponent(jButtonVerRep)
                    .addComponent(jButtonVerPlanillas))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNuevoVhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoVhActionPerformed
        NuevoModificarVh nuevoVh = new NuevoModificarVh();
        this.controlador.cambiarDePanel(nuevoVh, "Nuevo Camión");
    }//GEN-LAST:event_jButtonNuevoVhActionPerformed

    private void jButtonVerRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerRepActionPerformed
        //Las columnas de reparaciones (getColumnasReparaciones) tienen una menos que para las planillas
        //ACtion Performed para ver las reparaciones asociadas a cierto vehículo
        int filaSelect = this.jTableVh.getSelectedRow();
        if(filaSelect != -1){  //Hay fila seleccionada
            //Debo cargar la tabla "planilla" con  las reparaciones correspondientes
            //Las columnas de la tabla son 3 en el caso de las reparaciones (4 para las planillas)
            cargarTablaReparaciones(filaSelect);
        }else{
            //No hay fila seleccionada
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonVerRepActionPerformed

    private void jButtonVerPlanillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerPlanillasActionPerformed
        //ACtion Performed para ver las planillas asociadas a cierto vehículo
        int filaSelect = this.jTableVh.getSelectedRow();
        if(filaSelect != -1){  //Hay fila seleccionada
            //Debo cargar la tabla "planilla" con  las reparaciones correspondientes
            //Las columnas de la tabla son 3 en el caso de las reparaciones (4 para las planillas)
            cargarTablaPlanillas(filaSelect);
        }else{
            //No hay fila seleccionada
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonVerPlanillasActionPerformed

    private void jButtonBorrarVhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarVhActionPerformed
        //Action Performed del botón de borrar Vehículo 
        int filaSelect = this.jTableVh.getSelectedRow();
        if(filaSelect != -1){ try {
            //Se seleccionó una fila
            //No se debería poder borrar si el vehículo pertenece a una planilla
            String patente = (String) this.jTableVh.getValueAt(filaSelect, 0); //Selecciono la patente
            PreparedStatement pst = this.controlador.obtenerConexion().prepareStatement("DELETE FROM vehiculo where patente = '"+patente+"'");
            int executeUpdate = pst.executeUpdate();
            JLabelAriel label = new JLabelAriel("Vehículo borrado correctamente");
            JOptionPane.showMessageDialog(null, label, "INFO", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
                JLabelAriel label = new JLabelAriel("Error al intentar borrar vehículo: "+ex.getMessage());
                JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            } 
        }else{ //No hay fila seleccionada
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla");
            JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.WARNING_MESSAGE);
        }
        this.cargarTablas();
    }//GEN-LAST:event_jButtonBorrarVhActionPerformed

    private void jButtonModificarVhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarVhActionPerformed
        //Toma el vehículo seleccionado y lo pasa en el constructor de "NuevoModificarVh"
        int filaSelect = this.jTableVh.getSelectedRow();
        if(filaSelect == -1){
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de una tabla: ");
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.WARNING_MESSAGE);  
        }
        else{
            int idVh = modificarVh(filaSelect);
            if(idVh != 0){     //Encontró el vehículo  
                NuevoModificarVh mdVh = new NuevoModificarVh(idVh);
                this.controlador.cambiarDePanel(mdVh, "Modificar Vehículo");   //Abre una nueva pestaña para edición del vehículo.
            }
        }
    }//GEN-LAST:event_jButtonModificarVhActionPerformed

    private void cargarTablaReparaciones(int filaSelect){
        //Método para cargar la tabla reparaciones según el vehículo seleccionado
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePlanilla.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla
        String Datos[] = new String[3];
        String patente = (String) this.jTableVh.getValueAt(filaSelect, 0);
        
        this.modeloPlanilla.setColumnIdentifiers(this.getColumnasReparaciones());
        this.jTablePlanilla.getColumnModel().getColumn(2).setPreferredWidth(650);
        try {
            //Carga la tabla "de abajo" con las reparaciones que le corresponden al vehículos seleccionado
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.idplanilla, p.fecha_de_entrada, r.descripcion  FROM"
                    + " planilla AS p INNER JOIN reparacion AS r ON p.idplanilla = r.idplanilla "
                    + " INNER JOIN vehiculo AS v ON v.idvehiculo = p.idvehiculo "
                    + "WHERE v.patente = '"+patente+"' ");
            while(rs.next()){
                Datos[0] = ""+rs.getInt(1); //El idplanilla
                Datos[1] = ""+rs.getDate(2);
                Datos[2] = ""+rs.getString(3);
                this.modeloPlanilla.addRow(Datos);
            }
            this.jTablePlanilla.updateUI();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar las reparaciones en la tabla: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }   
    }
    
    private void cargarTablaPlanillas(int filaSelect){
        //Cargo la tabla de planillas borrando lo que puede estar de antes en la tabla de planillas
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePlanilla.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla
        String Datos[] = new String[4];
        String patente = (String) this.jTableVh.getValueAt(filaSelect, 0);
        this.modeloPlanilla.setColumnIdentifiers(this.getColumnasPlanilla());
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.idplanilla, p.fecha_de_entrada, p.fecha_de_salida,"
                    + "p.pagado FROM planilla AS p INNER JOIN vehiculo as v ON v.idvehiculo = p.idvehiculo"
                    + " WHERE v.patente = '"+patente+"' ");
            while(rs.next()){
                Datos[0] = String.valueOf(rs.getInt(1)); //Numero de planilla
                Datos[1] = rs.getDate(2).toString(); //fecha_de_entrada
                if(rs.getDate(3) != null)
                    Datos[2] = ""+rs.getDate(3); //Fecha de salida - puede ser nula
                else
                    Datos[2] = "Sin Fecha";
                Datos[3] = String.valueOf(rs.getBoolean(4)); //Pagado
                this.modeloPlanilla.addRow(Datos);
            }
            this.jTablePlanilla.updateUI();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar las planillas a la tabla "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        } 
    }
    
    private void cargarTablas(){
        //Carga los datos que van en las tablas. la segunda tabla se carga solo en Acciones sobre
        //botones de la primera.
        
        //Borro los datos que pueda haber en la tabla
        DefaultTableModel dtm = (DefaultTableModel) this.jTableVh.getModel();
        dtm.setRowCount(0);  //Magicamente funciona y sirve para eliminar las filas de la tabla 
        String queryVh = "SELECT v.patente, v.marca, v.modelo, p.nombre, p.apellido FROM "
                + "vehiculo AS v inner join cliente AS c ON v.idduenio = c.idcliente INNER JOIN "
                + "persona AS p ON p.idpersona = c.idpersona ORDER BY p.nombre";
        cargarVh(queryVh);
    }

    private void cargarVh(String query){
        //Método que carga los vehículos 
        //Header de Columnas: "Patente", "Marca", "Modelo", "Dueño"
        String patente, marca, modelo, nombreApellido;
        String Datos[] = new String[5];
        try { 
            //Carga los vehículos en la tabla correspondiente según el query pasado por parámetro
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                patente = rs.getString(1);
                marca = rs.getString(2);
                modelo = rs.getString(3);
                nombreApellido = rs.getString(4);
                nombreApellido = nombreApellido + " " + rs.getString(5);
                Datos[0] = patente;
                Datos[1] = marca;
                Datos[2] = modelo;
                Datos[3] = nombreApellido;
                this.modeloVh.addRow(Datos);
            }
            this.jTableVh.updateUI();
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("No se pudo realizar la carga de vehículos: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private int modificarVh(int filaSelect){
    //Abre la vista de NuevoModificarVh con el id del Vh a modificar
        String patente;
        int idVh = 0;
        patente = (String) this.jTableVh.getValueAt(filaSelect, 0);
        String sql = "SELECT idvehiculo FROM vehiculo WHERE patente = '"+patente+"' ";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                idVh = rs.getInt(1);
            }
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("No se pudo realizar la carga de vehículos: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return idVh;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButtonBorrarVh;
    private javax.swing.JButton jButtonModificarVh;
    private javax.swing.JButton jButtonNuevoVh;
    private javax.swing.JButton jButtonVerPlanillas;
    private javax.swing.JButton jButtonVerRep;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBoxCli;
    private javax.swing.JComboBox<ComboItem> jComboBoxCli;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePlanilla;
    private javax.swing.JTable jTableVh;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        //Ver ejemplo en "Nueva Planilla"
        return true;
    }

    @Override
    public void onFocus() {
        //Cuando esta vista toma el Foco
        //---
        this.cargarTablas();
        
    }

    private void cargarClientesCombo() {
        String nombre, apellido;
        int idCliente;
        try {
            //Carga los clientes en el jComboBoxCli para poder brindar la posibilidad de filtrar por clientes
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.nombre, p.apellido, c.idcliente FROM persona as p inner join cliente as c"
                    + " ON p.idpersona = c.idpersona ORDER BY p.nombre");
            while(rs.next()){
                nombre = rs.getString(1);
                apellido = rs.getString(2);
                idCliente = rs.getInt(3);
                ComboItem cm = new ComboItem(""+idCliente, nombre+" "+apellido);
                this.jComboBoxCli.addItem(cm);
            }
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar ComboBox de Clientes: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
