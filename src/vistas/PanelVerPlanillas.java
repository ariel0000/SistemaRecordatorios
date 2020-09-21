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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ComboItem;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class PanelVerPlanillas extends JPanelCustom {

    private DefaultTableModel modelo;
    private ControladorPrincipal controlador;
    
    public PanelVerPlanillas() {
        primerosComponentes();
        initComponents();
        cosasParaIniciarEnComun();
        obtenerPlanillas(); //Obtengo todas las planillas sin filtrar ninguna
    }
    
    public PanelVerPlanillas(int idcliente){
        //Constructor para ver las planillas de un cliente específico: Hasta ahora solo se llama desde NuevaPlanilla cuando se quiere
        //crear una nueva planilla pero el cliente tiene deuda --> Quizás se pueda agregar el filtro "impaga"
        primerosComponentes();
        initComponents();
        cosasParaIniciarEnComun();
        seleccionarCliente(idcliente); //Selecciono el cliente para filtrar en el JComboBoxCliente  -- falta implementar
        this.jCheckBoxCliente.setSelected(true);
        filtrar();
    }
    
    private void primerosComponentes(){
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
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
                if (fechaNula != null)
                    Datos[3] = rs.getDate(4);
                else
                    Datos[3] = "Sin fecha"; 
                //Si no seteara acá la fecha ocurre el error de que se toma la fecha equivocada de la iteración anterior para una pl. nula
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
        jComboBoxCliente = new javax.swing.JComboBox<>();
        jCheckBoxCliente = new javax.swing.JCheckBox();
        jComboBoxVh = new javax.swing.JComboBox<>();
        jCheckBoxVh = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePlanillas = new javax.swing.JTable();
        jButtonAdPlanilla = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBoxDesde = new javax.swing.JCheckBox();
        jButtonNuevaPlanilla = new javax.swing.JButton();
        jButtonBorrarPlanilla = new javax.swing.JButton();
        jDateChooserSalida = new com.toedter.calendar.JDateChooser();

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Filtros:");

        jComboBoxCliente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxCliente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCliente.setText("Cliente:");

        jComboBoxVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxVh.setText("Camión:");

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

        jDateChooserSalida.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jDateChooserSalida.setMinimumSize(new java.awt.Dimension(155, 27));

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
                                .addComponent(jCheckBoxCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxCliente, 0, 196, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxVh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxVh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxDesde)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooserSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAdPlanilla)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonNuevaPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBorrarPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBoxCliente)
                    .addComponent(jButton3)
                    .addComponent(jComboBoxCliente)
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBoxVh)
                        .addComponent(jComboBoxVh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBoxDesde))
                    .addComponent(jDateChooserSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
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
            int nPlanilla = (int) this.modelo.getValueAt(filaSeleccionada, 0); //paso el número de planilla
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
                    labelRep = new JLabelAriel("La planilla tiene reparaciones asignadas.");
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
    
    private void filtrar(){
        //Método que filtra y completa la tabla según los filtros aplicados.
        //Consulta general, hay que modificarla para cada fitro
        String consulta = "SELECT p.idplanilla, p.fecha_de_entrada, p.pagado, p.fecha_de_salida, per.nombre, per.apellido, v.marca, "
                + "v.modelo, v.patente, p.descripcion from planilla as p inner join cliente as c on p.idcliente = c.idcliente "
                + "inner join persona as per ON per.idpersona = c.idpersona inner join vehiculo as v on v.idvehiculo = p.idvehiculo";
    }
    
    private void cosasParaIniciarEnComun(){
        this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        modelo.setColumnIdentifiers(getColumnas());
        this.controlador = ControladorPrincipal.getInstancia();
        this.jTablePlanillas.getColumnModel().getColumn(0).setPreferredWidth(2);
        this.jTablePlanillas.getColumnModel().getColumn(1).setPreferredWidth(40);
        this.jTablePlanillas.getColumnModel().getColumn(2).setPreferredWidth(20);
        this.jTablePlanillas.getColumnModel().getColumn(6).setPreferredWidth(500); //La descrición
        cargarClientes(); //Cargo los clientes en el ComboBox
    }
    
    private void cargarClientes(){
        // Cargo todos clientes en el JComboBox
        String query = "SELECT c.idcliente, p.nombre, p.apellido FROM cliente AS c INNER JOIN persona AS p ON p.idpersona = c.idpersona "
                + "ORDER BY p.nombre";
        int idCliente;
        String nombre, apellido;
        ComboItem cm;
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idCliente = rs.getInt(1);
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                cm = new ComboItem(""+idCliente, nombre+" "+apellido);
                this.jComboBoxCliente.addItem(cm);
            }
            
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al cargar Clientes: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        
    }
    
    private void seleccionarCliente(int idcliente){
        // Método que selecciona el cliente especificado como parámetro (Hay que buscar en el 'key' del ComboItem).
        ComboItem cItem;
        for(int i = 0; i < this.jComboBoxCliente.getItemCount(); i++){
            cItem = (ComboItem) this.jComboBoxCliente.getItemAt(i);
            if(cItem.getKey().equals(""+idcliente)){ // Tengo que seleccionar este cliente
                this.jComboBoxCliente.setSelectedIndex(i);
                break;  //Salgo del bucle for
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonAdPlanilla;
    private javax.swing.JButton jButtonBorrarPlanilla;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonNuevaPlanilla;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBoxCliente;
    private javax.swing.JCheckBox jCheckBoxDesde;
    private javax.swing.JCheckBox jCheckBoxVh;
    private javax.swing.JComboBox<ComboItem> jComboBoxCliente;
    private javax.swing.JComboBox<ComboItem> jComboBoxVh;
    private com.toedter.calendar.JDateChooser jDateChooserSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePlanillas;
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
