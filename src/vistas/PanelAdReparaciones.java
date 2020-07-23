/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.toedter.calendar.JTextFieldDateEditor;
import controladores.ControladorPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class PanelAdReparaciones extends JPanelCustom {

    private DefaultTableModel modelo;
    private int numPlanilla = 0;  //Permanece en 0 siempre y cuando no se venga desde la vista Nueva Planilla
    private ControladorPrincipal controlador;
    
    public PanelAdReparaciones() {
        iniciarCosasEnComun();
        modelo = new DefaultTableModel();
        initComponents();
    }

    PanelAdReparaciones(int numPlanilla) {
        
        this.numPlanilla = numPlanilla; //Se utilizará a la hora de guardar la planilla y cargar los datos
        initComponents();
        iniciarCosasEnComun();
        inicializarLabelData(numPlanilla);
    }

    private void iniciarCosasEnComun(){
        this.controlador = ControladorPrincipal.getInstancia();
        JTextFieldDateEditor editor = (JTextFieldDateEditor) this.jDateChooserFinalizado.getDateEditor();
        editor.setEditable(false);
        this.jRadioButtonReparacion.setSelected(true); //En principio se tratará de una reparación
        this.jComboBoxPeriodo.setEnabled(false); //Se activará si se selecciona mantención
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupTipo = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldImporte = new javax.swing.JTextField();
        jTextFieldDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jRadioButtonReparacion = new javax.swing.JRadioButton();
        jRadioButtonMantenimiento = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jCheckBoxNotificar = new javax.swing.JCheckBox();
        jCheckBoxCompletada = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jButtonCancelarRep = new javax.swing.JButton();
        jButtonAgregarRep = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelNumPlanilla = new javax.swing.JLabel();
        jLabelMarcaModelo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelPatente = new javax.swing.JLabel();
        jComboBoxPeriodo = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jDateChooserFinalizado = new com.toedter.calendar.JDateChooser();
        jButtonBorrar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Planilla:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setText("Vehículo:");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel3.setText("Información:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel4.setText("Descripción:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setText("Importe:");

        jTextFieldImporte.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldDescripcion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel6.setText("Tipo Reparación:");

        buttonGroupTipo.add(jRadioButtonReparacion);
        jRadioButtonReparacion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonReparacion.setText("Reparación");

        buttonGroupTipo.add(jRadioButtonMantenimiento);
        jRadioButtonMantenimiento.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonMantenimiento.setText("Mantenimiento");
        jRadioButtonMantenimiento.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonMantenimientoStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel7.setText("*Fecha finalizado:");

        jCheckBoxNotificar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxNotificar.setText("Notificar");

        jCheckBoxCompletada.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCompletada.setText("Completada");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel8.setText("*Aviso de Mantención o de Reparación incompleta:");

        jButtonCancelarRep.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCancelarRep.setText("Descartar");
        jButtonCancelarRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarRepActionPerformed(evt);
            }
        });

        jButtonAgregarRep.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAgregarRep.setText("Agregar Reparación");
        jButtonAgregarRep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAgregarRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarRepActionPerformed(evt);
            }
        });

        jLabelNumPlanilla.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelNumPlanilla.setText("000000");

        jLabelMarcaModelo.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelMarcaModelo.setText("Marca-Modelo");

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setText("Patente:");

        jLabelPatente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelPatente.setText("AB1234CD");

        jComboBoxPeriodo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Días");

        jDateChooserFinalizado.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jDateChooserFinalizado.setMinimumSize(new java.awt.Dimension(155, 27));

        jButtonBorrar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonBorrar.setText("Borrar Fecha");
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("Periodo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDescripcion))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(53, 53, 53)
                                        .addComponent(jTextFieldImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooserFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBorrar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxNotificar)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxCompletada))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBoxPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel9))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jRadioButtonReparacion)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jRadioButtonMantenimiento)))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(406, 406, 406)
                        .addComponent(jButtonCancelarRep)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAgregarRep)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabelNumPlanilla)
                .addGap(107, 107, 107)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabelMarcaModelo)
                .addGap(71, 71, 71)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jLabelPatente)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabelNumPlanilla)
                    .addComponent(jLabel2)
                    .addComponent(jLabelMarcaModelo)
                    .addComponent(jLabel11)
                    .addComponent(jLabelPatente))
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jCheckBoxNotificar)
                    .addComponent(jCheckBoxCompletada))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButtonReparacion)
                    .addComponent(jRadioButtonMantenimiento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jDateChooserFinalizado, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBorrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelarRep)
                    .addComponent(jButtonAgregarRep))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarRepActionPerformed
        //Se debe consultar si proviene esta vista desde una planilla o desde "modificar reparación" --> 
        JLabel label = new JLabelAriel("Con la reparación completada debe ingresar una fecha de finalizada");
        if(!this.jCheckBoxCompletada.isSelected()) //No está seleccionado--> Rep. no complet. --> no hay problema con la fecha de finalizada
            if(this.numPlanilla == 0) //Estamos modificando una reparación
                actualizarReparacion();
            else //Estamos agregando una reparación a una planilla
                agregarReparacion(this.numPlanilla);
        else{ //La reparación está marcada como completada --> se deberá corroborar si está seteada la fecha de finalizada
            if(this.jDateChooserFinalizado.getDate() == null)
                JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.WARNING_MESSAGE);
            else{ //
                if(this.numPlanilla == 0)
                    actualizarReparacion();
                else
                    agregarReparacion(this.numPlanilla);
            }
        }
    }//GEN-LAST:event_jButtonAgregarRepActionPerformed

    private void jButtonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarActionPerformed
        // Elimina la fecha del jDateChooser
        this.jDateChooserFinalizado.cleanup();
        this.jDateChooserFinalizado.setDate(null);
    }//GEN-LAST:event_jButtonBorrarActionPerformed

    private void jRadioButtonMantenimientoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonMantenimientoStateChanged
        //Como se trata de una mantención entonces debo establecer un periodo - Activo dicha casilla automaticamente
        if(this.jRadioButtonMantenimiento.isSelected())
            this.jComboBoxPeriodo.setEnabled(true);
        else
            this.jComboBoxPeriodo.setEnabled(false); //Creo que no es necesaria esta línea pero por las dudas la dejamos
    }//GEN-LAST:event_jRadioButtonMantenimientoStateChanged

    private void jButtonCancelarRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarRepActionPerformed
        this.controlador.cerrarPanelSeleccionado();
    }//GEN-LAST:event_jButtonCancelarRepActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupTipo;
    private javax.swing.JButton jButtonAgregarRep;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JButton jButtonCancelarRep;
    private javax.swing.JCheckBox jCheckBoxCompletada;
    private javax.swing.JCheckBox jCheckBoxNotificar;
    private javax.swing.JComboBox<String> jComboBoxPeriodo;
    private com.toedter.calendar.JDateChooser jDateChooserFinalizado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelMarcaModelo;
    private javax.swing.JLabel jLabelNumPlanilla;
    private javax.swing.JLabel jLabelPatente;
    private javax.swing.JRadioButton jRadioButtonMantenimiento;
    private javax.swing.JRadioButton jRadioButtonReparacion;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextFieldDescripcion;
    private javax.swing.JTextField jTextFieldImporte;
    // End of variables declaration//GEN-END:variables

    private void actualizarReparacion() {
        
    }

    private void agregarReparacion(int numPlanilla) {
        int siguienteIdRep = obtenerUltimoId()+1;
        String query = "INSERT INTO reparacion VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String tipo_rep;
        if (this.jTextFieldImporte.getText().equals("")) {
            JLabel label = new JLabelAriel("Debe ingresar un importe ");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            return; //Para que no se cierre
        } else {
            try {
                PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement(query);
                this.controlador.obtenerConexion().setAutoCommit(false);
                ps.setInt(1, siguienteIdRep);
                ps.setString(2, this.jTextFieldDescripcion.getText()); //Si es nulo nose que pasa 
                ps.setInt(3, Integer.valueOf(this.jTextFieldImporte.getText())); //El importe
                ps.setBoolean(4, this.jCheckBoxCompletada.isSelected()); //Si está completada --> true
                if (this.jRadioButtonMantenimiento.isSelected()) {
                    tipo_rep = "mantenimiento";
                } else {
                    tipo_rep = "reparacion";
                }
                ps.setObject(5, tipo_rep, java.sql.Types.OTHER); //reparacion o mantenimiento - Es un ENUM
                ps.setInt(6, numPlanilla);
                if (this.jDateChooserFinalizado.getDate() != null) {
                    ps.setDate(7, new java.sql.Date(this.jDateChooserFinalizado.getDate().getTime()));
                } else {
                    ps.setDate(7, null);
                }
                ps.setInt(8, Integer.parseInt((String) this.jComboBoxPeriodo.getSelectedItem()));
                int resultado = ps.executeUpdate();
                this.controlador.obtenerConexion().commit(); //Todo bien si pasa esta línea - falta hacerle el catch al commit()
                JLabel labelOk = new JLabelAriel(" Reparacion agregada con exito ");
                JOptionPane.showMessageDialog(null, labelOk, " TODO BIEN ", JOptionPane.YES_NO_OPTION);
            } catch (SQLException ex) {
                JLabel label = new JLabelAriel("Error al guardar Reparación " + ex.getMessage());
                JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            try {
                this.controlador.obtenerConexion().rollback(); 
//Algo fallo entonces hay que volver las 2 operaciones para atrás sino dsps no se puede hacer nada
            } catch (SQLException ex1) {
                JLabel label = new JLabelAriel(ex1.getMessage());
                JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }
        this.controlador.cerrarPanelSeleccionado();
    }

    private void inicializarLabelData(int numPlanilla) {
        //Método para setear los label con los datos de la planilla a modificar
        this.jLabelNumPlanilla.setText(""+numPlanilla);
        cargarDatos(numPlanilla);
    }
    
    private void cargarDatos(int numPlanilla){
        String marca = null, modelo = null, patente = null;
        String consulta = "SELECT v.marca, v.modelo, v.patente from vehiculo as v inner join planilla as p "
                + "ON v.idvehiculo = p.idvehiculo where p.idplanilla = '"+numPlanilla+"' ";
        int i = 0;
        
        Connection co = this.controlador.obtenerConexion();
        try {
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                marca = rs.getString(1);
                modelo = rs.getString(2);
                patente = rs.getString(3);
                this.jLabelMarcaModelo.setText(marca+" "+modelo);
                this.jLabelPatente.setText(patente); 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage());
        }
        while(i < 360){ //El 365 se agregará aparte
            i+= 15;
            this.jComboBoxPeriodo.addItem(""+i); //Empiezo por 15 días
        }
        this.jComboBoxPeriodo.addItem(""+365);
    }

    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        //Ver ejemplo en "Nueva Planilla"
        return true;
    }

    @Override
    public void onFocus() {
        //Todavía nose para que usar el onFocus acá
    }

    private int obtenerUltimoId() {
        int idRep = 0;
        String consulta = "SELECT MAX(idreparacion) FROM reparacion";
        Statement st;
        try {
            st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                idRep = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PanelAdReparaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idRep;
    }
}
