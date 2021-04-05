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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class PanelAdPersonas extends JPanelCustom {

    private ControladorPrincipal controlador;
    private DefaultTableModel modelo;
    
    public PanelAdPersonas() {
        this.modelo = new DefaultTableModel(){       
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };
        initComponents();
        this.jTablePersonas.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
        cargarDatos();
    }

    private void ajustarColumnasDeTabla(){
        //Ajusta los tamaños de las columnas de la tabla
        this.jTablePersonas.getColumnModel().getColumn(0).setMaxWidth(70); //Id
        this.jTablePersonas.getColumnModel().getColumn(0).setMinWidth(40);
        this.jTablePersonas.getColumnModel().getColumn(1).setMinWidth(250);  //Apellido
        this.jTablePersonas.getColumnModel().getColumn(1).setMaxWidth(400);
        this.jTablePersonas.getColumnModel().getColumn(2).setMinWidth(190);  //Nombre
        this.jTablePersonas.getColumnModel().getColumn(3).setMinWidth(95);   //Es chofer
        this.jTablePersonas.getColumnModel().getColumn(3).setMaxWidth(115);
        this.jTablePersonas.getColumnModel().getColumn(4).setMinWidth(95);  //Es cliente
        this.jTablePersonas.getColumnModel().getColumn(4).setMaxWidth(115);
        this.jTablePersonas.getColumnModel().getColumn(5).setMinWidth(130);  //DNI|CUIL
        this.jTablePersonas.getColumnModel().getColumn(5).setMaxWidth(165);  
        this.jTablePersonas.getColumnModel().getColumn(6).setMinWidth(150); //Planillas Impagas?
        this.jTablePersonas.getColumnModel().getColumn(6).setMaxWidth(175); 
        this.jTablePersonas.getColumnModel().getColumn(7).setMinWidth(120);  //Notifica C.C
    //    this.jTablePersonas.removeColumn(this.jTablePersonas.getColumnModel().getColumn(6));  //No se usa más
    }
    
    private void cargarDatos(){
        this.jTablePersonas.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
        this.modelo.setColumnIdentifiers(getColumnas());
        this.ajustarColumnasDeTabla();
        this.controlador = ControladorPrincipal.getInstancia();
        cargarPersonas(armarQuery());
     //   cargarVhBdD();
    }
    
    private String[] getColumnas() { 
        String columna[] = new String[]{"Id", "Apellido", "Nombre", "Es Chofer", "Es Cliente", "DNI/CUIL",  "Planillas Impagas", "¿Notifica C.C?"};
        return columna;
    }
    
    private void cargarPersonas(String consulta) {
        //Cargo las persona a la tabla
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePersonas.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla
        String datos[] = new String[8];  //Tipo, Descripción, Patente
     //   String consulta = "SELECT p.idpersona, p.nombre, p.apellido, c.idchofer, cl.idcliente, cl.cuil, cl.notificar FROM persona AS p "
      //        + "left join chofer AS c on p.idpersona = c.idpersona left join cliente AS cl ON cl.idpersona = p.idpersona ORDER BY p.apellido";
        //Con esta consulta estoy mostrando todas las personas y si son clientes y/o choferes
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                datos[0] = rs.getString(1); //Id de la persona
                datos[1] = rs.getString(3); //Apellido
                datos[2] = rs.getString(2); //Nombre
                datos[3] = String.valueOf((rs.getString(4))!= null); //True si no es igual a "null" - Entonces TRUE si es Chofer 
                datos[4] = String.valueOf(rs.getString(5) != null); // True si es Cliente
                datos[5] = rs.getString(6); //cuil
                datos[6] =  String.valueOf(tienePlanillasImpagas(rs.getInt(1)));
                if(rs.getBoolean(7))
                    datos[7] = "     Sí  ";
                else
                    datos[7] = "     No  ";
                this.modelo.addRow(datos);
                this.jTablePersonas.updateUI();     
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar las personas a la tabla: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean tienePlanillasImpagas(int idPersona){
        //Método que consulta y retorna "true" si la persona (idPersona) tiene alguna planilla impaga
        boolean valor = false;
        String consulta = "SELECT p.idplanilla FROM planilla AS p INNER JOIN cliente AS c ON "
                + "c.idcliente = p.idcliente INNER JOIN persona AS per ON per.idpersona = c.idpersona "
                + "WHERE p.pagado = false AND per.idpersona = "+idPersona+" ";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){ //Si entro acá es porque tiene alguna planilla impaga
                valor = true;
                break; //Salgo porque ya tengo la respuesta y la variable valor seteada. 
                // Hay al menos una planilla impaga para la persona (Cliente) con idPersona
            }               
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al consultar planillas " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return valor;
    }
    
    private boolean tienePlanillasAsociadas(int idper){
        //Método que retorna true si tiene planillas asociadas la persona pasado por parámetro
        boolean valor = false;
        String query = "SELECT p.idplanilla FROM planilla AS p INNER JOIN cliente AS c ON p.idcliente = c.idcliente "
                + "WHERE c.idpersona = '"+idper+"' OR  p.idpersona = '"+idper+"' ";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                valor = true;
            }
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al consultar planillas asociadas a la persona " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);  
        }
        return valor;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePersonas = new javax.swing.JTable();
        jButtonAgregarPer = new javax.swing.JButton();
        jButtonModificarPer = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jCheckBoxApeNom = new javax.swing.JCheckBox();
        jCheckBoxCli = new javax.swing.JCheckBox();
        jCheckBoxPlImpagas = new javax.swing.JCheckBox();
        jTextFieldApeNom = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButtonEliminarPer = new javax.swing.JButton();
        Filtrar = new javax.swing.JButton();

        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTablePersonas.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 16)); // NOI18N
        jTablePersonas.setModel(modelo);
        jTablePersonas.setRowHeight(22);
        jScrollPane1.setViewportView(jTablePersonas);

        jButtonAgregarPer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAgregarPer.setText("Agregar Persona");
        jButtonAgregarPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarPerActionPerformed(evt);
            }
        });

        jButtonModificarPer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonModificarPer.setText("Ver/Modificar");
        jButtonModificarPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarPerActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setText("Filtros:");

        jCheckBoxApeNom.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxApeNom.setText("Nombre o Apellido");

        jCheckBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCli.setText("Es Cliente");

        jCheckBoxPlImpagas.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxPlImpagas.setText("Planillas Impagas");

        jTextFieldApeNom.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextFieldApeNom.setAutoscrolls(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("* Las planillas impagas son las que después de facturada la misma se está esperando que sean saldadas por el Cliente");

        jButtonEliminarPer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonEliminarPer.setText("Eliminar");
        jButtonEliminarPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarPerActionPerformed(evt);
            }
        });

        Filtrar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        Filtrar.setText("Filtrar");
        Filtrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxApeNom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldApeNom, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxCli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxPlImpagas)
                        .addGap(244, 244, 244)
                        .addComponent(Filtrar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAgregarPer)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonModificarPer)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonEliminarPer)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jCheckBoxApeNom)
                    .addComponent(jCheckBoxCli)
                    .addComponent(jCheckBoxPlImpagas)
                    .addComponent(jTextFieldApeNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Filtrar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAgregarPer)
                    .addComponent(jButtonModificarPer)
                    .addComponent(jButtonEliminarPer))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarPerActionPerformed
        //Al presionar el botón agregar persona creo una nueva pestaña con la vista "PanelNuevaPersona" y cambio el panel
        PanelNuevaPersona panelNuevaPer = new PanelNuevaPersona();
        this.controlador.cambiarDePanel(panelNuevaPer, "Nueva Persona");
    }//GEN-LAST:event_jButtonAgregarPerActionPerformed

    private void jButtonModificarPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarPerActionPerformed
        
        int fila = this.jTablePersonas.getSelectedRow();
        if(fila != -1){ //Tengo una persona seleccionada
            PanelNuevaPersona panelNuevaPer = new PanelNuevaPersona(Integer.valueOf((String) this.jTablePersonas.getValueAt(fila, 0)));
            this.controlador.cambiarDePanel(panelNuevaPer, "Modificar Persona");
        }
        else{
            JLabel label = new JLabelAriel("Debe seleccionar una fila de la Tabla ");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_jButtonModificarPerActionPerformed

    private void jButtonEliminarPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarPerActionPerformed
        //debe borrar la persona seleccionada de la Base de datos
        JLabel label = new JLabelAriel("Debe seleccionar una fila de la tabla");
        int filaSelect = this.jTablePersonas.getSelectedRow();
        if(filaSelect != -1)  //Se seleccionó alguna fila
            this.borrarPersona(Integer.parseInt((String) this.jTablePersonas.getValueAt(filaSelect, 0)));
        else
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_jButtonEliminarPerActionPerformed

    private void FiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltrarActionPerformed
        // Filtro de personas
        String query = armarQuery();
        this.cargarPersonas(query);
    }//GEN-LAST:event_FiltrarActionPerformed
    
    private String armarQuery(){
        //Arma un query para consultar dsps
        int contador = 0;
        String consulta = "SELECT p.idpersona, p.nombre, p.apellido, c.idchofer, cl.idcliente, cl.cuil, cl.notificar FROM persona AS p "
              + "left join chofer AS c on p.idpersona = c.idpersona left join cliente AS cl ON cl.idpersona = p.idpersona "; //ORDER BY p.apellido
        
        String nomApe = this.jTextFieldApeNom.getText().toLowerCase();
        
        if(this.jCheckBoxApeNom.isSelected()){
            contador++;
            consulta = consulta.concat(" WHERE lower(p.nombre) like '"+nomApe+"' OR  lower(p.apellido) like '"+nomApe+"' ");
        }
        else if(this.jCheckBoxCli.isSelected()){
            if(contador == 0)
                consulta = consulta.concat(" WHERE p.idpersona in (SELECT c.idpersona FROM cliente as c) ");
            else
                consulta = consulta.concat(" AND p.idpersona in (SELECT c.idpersona FROM cliente as c) ");;
        }
        else if(this.jCheckBoxPlImpagas.isSelected()){
            if(contador == 0)
                consulta = consulta.concat(" WHERE p.idpersona in (SELECT c.idpersona FROM cliente as c INNER JOIN planilla AS p "
                        + "ON p.idcliente = c.idcliente WHERE p.pagado = false) ");
            else
                consulta = consulta.concat(" AND p.idpersona in (SELECT c.idpersona FROM cliente as c INNER JOIN planilla AS p "
                        + "ON p.idcliente = c.idcliente WHERE p.pagado = false) ");
        }
            
        return consulta;
    }
    
    private void borrarPersona(int idPer){
        //borra la persona con el id: idPer
        JLabel label1, label2;
        JLabel label = new JLabelAriel("Persona eliminada del sistema");
        if(!this.tienePlanillasAsociadas(idPer)){  //Si no tiene planillas asociadas
            try {
                PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM persona WHERE idpersona = ? ");
                ps.setInt(1, idPer);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                label2 = new JLabelAriel("Error al borrar Persona: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, label2, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            this.cargarPersonas(armarQuery()); //Recargo al tabla
            }
        else{
            label1 = new JLabelAriel("La persona tiene planillas asociadas");
            JOptionPane.showMessageDialog(null, label1, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Filtrar;
    private javax.swing.JButton jButtonAgregarPer;
    private javax.swing.JButton jButtonEliminarPer;
    private javax.swing.JButton jButtonModificarPer;
    private javax.swing.JCheckBox jCheckBoxApeNom;
    private javax.swing.JCheckBox jCheckBoxCli;
    private javax.swing.JCheckBox jCheckBoxPlImpagas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePersonas;
    private javax.swing.JTextField jTextFieldApeNom;
    // End of variables declaration//GEN-END:variables

    /*
    private void cargarVhBdD(){
        //Carga los vehículos al jComboBox en formato ComboItem(idVh, marca-patente)
        Connection co = this.controlador.obtenerConexion();
        String marca, patente;
        String consulta = "SELECT v.idvehiculo, v.marca, v.patente FROM vehiculo AS v";
        // En este método el ActionItemListener debería haber sido quitado
        try {
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                String idVh = String.valueOf(rs.getInt(1));
                marca = rs.getString(2);
                patente = rs.getString(3);
                ComboItem comboItem = new ComboItem(idVh, marca+" "+patente); //(clave, valor)
                this.jComboBoxVh.addItem(comboItem);
                this.jComboBoxVh.updateUI();    
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            //Acá iría la escritura de un log con fecha si es posible. También iría en otros lugares
        }
    }
    */
    
    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        return true;
    }

    @Override
    public void onFocus() {
        //Hay que recargar la tabla de Personas
        cargarPersonas(armarQuery());
    }
}
