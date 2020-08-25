/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;

/**
 *
 * @author Ariel
 */
public class PanelAdPagos extends JPanelCustom {

    
    private DefaultTableModel modelo, modeloCli;
    private ControladorPrincipal controlador;
    
    public PanelAdPagos() {
        this.controlador = ControladorPrincipal.getInstancia();
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        modeloCli = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false. No se puede editar ninguna celda
                return false;
            }
        };

        initComponents();
        this.jTableClientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        modelo.setColumnIdentifiers(getColumnas());
        modeloCli.setColumnIdentifiers(getColumnasCliente()); 
        this.cargarTablaClientes("");
    }

    private String[] getColumnas(){
        String columna[] = new String[]{"N° Planilla", "Fecha de Entrada", "Fecha de Salida", "Importe", "Pagado"};
        
        return columna;
    }
    
    private String[] getColumnasCliente(){
        String columnas[] = new String[]{"Nombre", "Apellido", "id_cliente", "Estado de C.C"};
        
        return columnas;
    }
    
    private void cargarTablaClientes(String nombreApellido){
        //Método para cargar la tabla de clientes cuando se carga la vista o cuando se filtra por nombre. Solo elije el query correspondiente
        String query;
        if (nombreApellido.equals("")) {
            query = "SELECT per.nombre, per.apellido, c.idcliente, exists(select p.idplanilla FROM planilla as p WHERE " +
                "p.idcliente = c.idcliente AND p.entregado = true AND p.pagado = false) FROM persona AS per NATURAL JOIN cliente AS c"
                    + " ORDER BY per.nombre";
        }
 // Lo de arriba consulta Nombre y Apellido de algún cliente y además si tiene alguna planilla impaga
        else {
            query = "SELECT per.nombre, per.apellido, c.idcliente, exists(select p.idplanilla from planilla as p WHERE p.idcliente = c.idcliente AND "
                    + "p.entregado = true AND p.pagado = false) FROM persona AS per NATURAL JOIN cliente AS c "
                    + "WHERE CONCAT(per.nombre, per.apellido) LIKE '%"+nombreApellido+"%' ORDER BY per.nombre"; 
        }
        this.cargarTablaCli(query);
    }
    
    private void cargarTablaCli(String query){
        // Método que carga en la tabla el/los Clientes desde la Base de datos.
        int días;
        String datos[] = new String[4];  //Nombre, Apellido, ¿Pl. Impagas?, Estado de C.C
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = ""+rs.getInt(3); //el idcliente
                días = this.diasPorPlanillasImpagas(rs.getInt(3)); //Cantidad de dias que la planilla está impaga
                    if(rs.getBoolean(4)) //Si es verdadero tiene una o más planillas impagas
                        datos[3] = "impagas con hasta "+días+ " días"; //Estado de C.C
                    else
                        datos[3] = "no tiene planillas impagas";
                
                this.modeloCli.addRow(datos);
            }
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al cargar tabla de Clientes: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        this.jTableClientes.updateUI();
    }
  
    private int diasPorPlanillasImpagas(int idCliente) throws SQLException {
        //Método que servirá para avriguar la cantidad de días como máximo que tienen las planillas del cliente
        LocalDate fecha_hoy, fecha_salida_vh;
        Date fecha_salida;
        Date fechaHoy = new Date(System.currentTimeMillis());
        fecha_hoy = fechaHoy.toLocalDate();
        int actual;
        int estadoCC = 0; //Podría usarse en base a días desde que una planilla está impaga. 
        String consulta = "SELECT MIN(pl.fecha_de_salida) FROM planilla as pl INNER JOIN cliente AS c ON c.idcliente = pl.idcliente "
                + "WHERE pl.entregado = true AND pl.pagado = false AND c.idcliente = '"+idCliente+"' ";
        Connection co = this.controlador.obtenerConexion();

        Statement st = co.createStatement();
        ResultSet rs = st.executeQuery(consulta);
        while (rs.next()) {
            fecha_salida = rs.getDate(1);
            if (fecha_salida != null) {
                fecha_salida_vh = fecha_salida.toLocalDate();
                actual = (int) ChronoUnit.DAYS.between(fecha_salida_vh, fecha_hoy);
                if (actual > estadoCC) {
                    estadoCC = actual;
                }
            }
           // else{...}  //La fecha es nula, se retorna el valor por defecto 0  
        }
        return estadoCC;
    }

    LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        //Convierto una fecha Date en LocalDateTime
        return new java.sql.Timestamp(dateToConvert.getTime()).toLocalDateTime();
    }
    
    private long montoPagos(int idCliente){
        //Método que retorna el importe total de pagos del cliente
        String sql = "SELECT SUM(r.importe) FROM reparacion AS r INNER JOIN planilla as p ON p.idplanilla = r.idplanilla "
                + "INNER JOIN cliente AS c ON p.idcliente = c.idcliente WHERE c.idcliente = '"+idCliente+"' ";
        long monto = 0;
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                monto += rs.getLong(1);  //En realidad la suma la hace el query, este while solo se ejecuta una vez
            }
            
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al consultar montos de Pagos: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return monto;
    }
    
    private Long montoPagosPlanilla(int idPlanilla){
        //Método que calcula cuanto dinero se recibió - Los cheques no cobrados se suman como pagos igualmente.
        //
       // long montoRep = this.montoPorReparaciones();
        long monto = 0; 
        //Esta consulta Retorna dos valores para los montos de pago de cheques y contado 
        String consulta = "Select sum(ch.monto) from cheque as ch inner join forma_de_pago as fdp on fdp.idforma_de_pago = ch.idforma_de_pago "
                + "inner join planilla as p on p.idplanilla = fdp.idplanilla where p.idplanilla= '"+idPlanilla+"' "
                + "UNION"
                + " select sum(c.monto) from contado as c inner join forma_de_pago as fdp on fdp.idforma_de_pago = c.idforma_de_pago "
                + "inner join planilla as p on p.idplanilla = fdp.idplanilla where p.idplanilla = '"+idPlanilla+"'";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                monto += rs.getLong(1); //Hay que probar si anda - Entiendo que primero toma el monto de cheque y en el siguiente el de contado
            }
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel(" Error al cargar pagos: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return monto;
    }
            
    private void cargarTablaPlanillas(int idCliente){
        //Método para cargar planillas en la tabla que tiene que llamarse dsps de un ActionPerformed de "Seleccionar Cliente"
        //Diseñar para aplicar filtro de nombre de cliente
        String consulta;
        consulta = "SELECT p.idplanilla, p.fecha_de_entrada, p.fecha_de_salida, p.pagado FROM planilla AS p"
                + " INNER JOIN cliente as c ON c.idcliente = p.idcliente INNER JOIN persona as pe ON pe.idpersona = c.idpersona"
                + " WHERE c.idcliente = '"+idCliente+"' ";
        cargarDatosTablaPlanillas(consulta);
    }   
    
    private void cargarEstadoCliente(int idCliente){
        //Metodo que carga en JLabel4 y JLabel6 información del estado de la cuenta del Cliente
        this.jLabel6.setText(""+(this.montoPagos(idCliente)-this.montoPorReparaciones(idCliente))); 
        //Diferencia entre importes y pagos del Cli.            
    }
    
    private void cargarDatosTablaPlanillas(String consulta){
        //Método para cargar datos en la tabla una vez que la consulta está lista
        //Reinició los datos de la tabla.
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePlanillas.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla 
        String[] registro = new String[6];
        Connection co = this.controlador.obtenerConexion();
        try {
            co.setAutoCommit(false);
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                registro[0] = ""+rs.getInt(1); //idplanilla
                registro[1] = ""+rs.getDate(2); //fecha de entrada de Vh
                registro[2] = ""+rs.getDate(3); //fecha de salida del Vh
                if(rs.getDate(3) == null)
                    registro[2] = "El Vehículo no fue entregado";
                registro[3] = ""+cargarImporteReparaciones(rs.getInt(1), co);
                if(rs.getBoolean(4)) //Si es verdadero la planilla esta pagada
                    registro[4] = "SI";
                else
                    registro[4] ="NO";
                registro[5] = ""+this.montoPagosPlanilla(rs.getInt(1)); //Acá calcula el monto de los pagos asignados a la planilla
                co.commit();
                this.modelo.addRow(registro);
                this.jTablePlanillas.updateUI();
            }
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar tabla de Clientes: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
            try {
                co.rollback();
            } catch (SQLException ex1) {
                System.out.println("Error en el rollback después del error");
            }
        } 
    }
    
    private long cargarImporteReparaciones(int nPlanilla, Connection co) throws SQLException{
        //Obtiene la suma de los importes de las reparaciones asignadas a una planilla y la retorna
        long suma = 0;
        String sql = "SELECT SUM(r.IMPORTE) FROM reparacion AS r WHERE r.idplanilla = '"+nPlanilla+"' ";
        Statement st = co.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            suma+= rs.getLong(1);  //El while solo se ejecuta una vez
        }
        return suma;
    }
    
    private long montoPorReparaciones(int idCliente) {
        //Obtiene la suma de los importes de las reparaciones asignadas a todas las planillas de un Cliente
        long suma = 0;
        Connection co = this.controlador.obtenerConexion();
        String sql = "SELECT SUM(r.IMPORTE) FROM reparacion AS r INNER JOIN planilla as p ON p.idplanilla = r.idplanilla "
                + "WHERE p.idplanilla = '"+idCliente+"' ";
        Statement st;
        try {
            st = co.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            suma+= rs.getLong(1);  //El while solo se ejecuta una vez
        }
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al consultar monto por reparaciones: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        return suma;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameInfo = new javax.swing.JFrame();
        jLabel18 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButtonAceptar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePlanillas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButtonSelectCli = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButtonInfo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonVerPlanilla = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setFocusable(false);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setPreferredSize(new java.awt.Dimension(635, 350));
        jFrameInfo.setSize(new java.awt.Dimension(500, 350));
        jFrameInfo.setType(java.awt.Window.Type.POPUP);

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel18.setText(". El detalle de cada planilla de un Cliente seleccionado.");

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel16.setText(". Que Clientes adeudan o tienen planillas impagas.");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setText(". Estado de la Cuenta Corriente de un Cliente.");

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Esta vista es solo para ver la siguiente información:");

        jButtonAceptar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 0, 0));
        jLabel19.setText(". Los pagos (contado y cheques), solo pueden editarse desde la Planilla");

        javax.swing.GroupLayout jFrameInfoLayout = new javax.swing.GroupLayout(jFrameInfo.getContentPane());
        jFrameInfo.getContentPane().setLayout(jFrameInfoLayout);
        jFrameInfoLayout.setHorizontalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAceptar))
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jFrameInfoLayout.setVerticalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(jButtonAceptar)
                .addContainerGap())
        );

        setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Planillas del Cliente:");

        jTablePlanillas.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jTablePlanillas.setModel(this.modelo);
        jScrollPane1.setViewportView(jTablePlanillas);

        jTableClientes.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jTableClientes.setModel(modeloCli);
        jScrollPane2.setViewportView(jTableClientes);

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setText("Clientes con Planillas en el sistema:");

        jButtonSelectCli.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonSelectCli.setText("Seleccionar Cliente");
        jButtonSelectCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectCliActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel3.setText("Estado de Cuenta Corriente:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel4.setText("jLabel4");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel5.setText("Balance de saldo. (con signo negativo si debe):");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel6.setText("jLabel6");

        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        jButtonVerPlanilla.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonVerPlanilla.setText("Ver Planilla");
        jButtonVerPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerPlanillaActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jButton1.setText("Ver todos los pagos del Cliente");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel7.setText("Este botón sirve para abrir una nueva pestaña que muestra en detalle los cheques y pagos al contado del Cliente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel4))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel6)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addComponent(jButtonSelectCli))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonVerPlanilla))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSelectCli)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)))
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVerPlanilla)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectCliActionPerformed
        // ActionPerformed para seleccionar un Cliente. Se deben llenar los campos debajo de la tabla con información y la tabla de planillas
        //Debo cargar las planillas de este cliente y poner información en los jLabel del estado de la cueta (saldo)
        int filaSelect = this.jTableClientes.getSelectedRow();
        int idCliente;
        if(filaSelect != -1){
                //Acá deberán consultarse los métodos para calcular el saldo del cliente, activas los labels y cargarlo con la información
                // que se obtenga
                idCliente = Integer.valueOf((String)this.jTableClientes.getValueAt(filaSelect, 2));
                this.cargarTablaPlanillas(idCliente);
                this.cargarEstadoCliente(idCliente);
        }
        else{
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla: ");
            JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSelectCliActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        // Abre el Frame de JFrameInfo para mostrar info de la vista (Para que sirve para que no sirve).
        this.jFrameInfo.setVisible(true);
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        //Cierra el jFrameInfo
        this.jFrameInfo.dispose();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jButtonVerPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerPlanillaActionPerformed
        // Abre la vista de planillas con la planilla seleccionada.
        PanelPlanillaNueva p1;
        int filaSelect = this.jTablePlanillas.getSelectedRow();
        if(filaSelect != -1){ //Hay fila seleccionada --> Debo tomar el N° de Planilla y crear una Nueva Planilla con ese número
            int nPlanilla = Integer.valueOf(this.modelo.getValueAt(filaSelect, 0)+""); //paso el número de planilla
            p1 = new PanelPlanillaNueva(nPlanilla); //Acá va el número de planilla
            this.controlador.cambiarDePanel(p1, "Ver/Modificar Planilla");
        }
        else{
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla: ");
            JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonVerPlanillaActionPerformed

    @Override
    public boolean sePuedeCerrar() {
        //Teoricamente no habría problemas para cerrar en cualquier momento esta pestaña
        return true;  //El controlador se encarga de cerrar la pestaña actual
    }

    @Override
    public void onFocus() {
        //Cosas a cargar cuando esta vista toma el foco. Ver que conviene:
        // .Debería cargar de nuevo las dos tablas
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonSelectCli;
    private javax.swing.JButton jButtonVerPlanilla;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTablePlanillas;
    // End of variables declaration//GEN-END:variables
}
