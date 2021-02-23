/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;
import modelo.ComboItem;

/**
 *
 * @author Ariel
 */
public class PanelAdPagos extends JPanelCustom {

    
    private DefaultTableModel modeloCli;
    private ControladorPrincipal controlador;
    
    public PanelAdPagos() {
        modeloCli = new DefaultTableModel(null, this.getColumnasPagos()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false. No se puede editar ninguna celda
                return false;
            }
        };
        initComponents();
        iniciarCosasEnComun();
        this.cargarCli("");  //Cargo los Clientes del JComboBox
        this.jRadioButtonCheque.setSelected(true);  //Por defecto muestro los cheques
        this.jRadioButtonACobrar.setSelected(true);
        this.jTextFieldCC.setEditable(false); 
        String query = this.decidirQuery();
        
        this.cargarTablaPagos(query);
    }
    
    private String[] getColumnasPagos(){
        String columnas[] = new String[]{"ID", "Cobrado", "Monto", "Fecha Emisión", "Fecha Cobro", "Notificar", "Nombre del Cliente"};
        
        return columnas;
    }
    
    public PanelAdPagos(int idCliente){
        modeloCli = new DefaultTableModel(null, this.getColumnasPagos()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false. No se puede editar ninguna celda
                return false;
            }
        };
        initComponents();
        iniciarCosasEnComun();
        this.cargarCli("");  //Cargo los Clientes del JComboBox
        this.jRadioButtonCheque.setSelected(true);  //Por defecto muestro los cheques
        this.jRadioButtonACobrar.setSelected(true);
        this.jTextFieldCC.setEditable(false); 
        this.seleccionarClienteEnComboBox(idCliente);
        this.jCheckBoxCli.setSelected(true);
        String query = this.decidirQuery();
        this.cargarLabelBalance();
        this.cargarTablaPagos(query);
    }
    
    private void iniciarCosasEnComun(){
        //Método que carga datos e info común a todos los constructores de la vista (Por ahora tenemos solo 1)
        this.controlador = ControladorPrincipal.getInstancia();
        this.jTablePagos.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
  //      this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.jTablePagos.getColumnModel().getColumn(0).setMinWidth(80);
        this.jTablePagos.getColumnModel().getColumn(0).setMaxWidth(100);
        this.jTablePagos.getColumnModel().getColumn(1).setMinWidth(80);  //La descripción
        this.jTablePagos.getColumnModel().getColumn(1).setMaxWidth(110);  //La descripción
        this.jTablePagos.getColumnModel().getColumn(2).setMaxWidth(130);
        this.jTablePagos.getColumnModel().getColumn(2).setMinWidth(100);
        this.jTablePagos.getColumnModel().getColumn(5).setMaxWidth(160);
        this.jTablePagos.getColumnModel().getColumn(5).setMinWidth(120);
        this.jTablePagos.getColumnModel().getColumn(6).setMinWidth(350);
  //      this.jTablePagos.getColumnModel().getColumn(6).setMaxWidth(480);
        
    }
    
    private void cargarTablaPagos(String query){
        // Carga la tabla de pagos según query
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePagos.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla

        if(this.jRadioButtonCheque.isSelected())
            cargarTablaConCheques(query);
        else
            cargarTablaConContado(query);  //Carga la tabla con pagos al contado
      
        this.jTablePagos.updateUI();
    }
    
    private void cargarTablaConCheques(String query){
        //Método que carga la tabla con cheques - Debe cambiar las columnas de la misma (la tabla).
        //ID", "Cobrado", "Monto", "Fecha Emisión", "Fecha Cobro", "Notificar", "Nombre del Cliente
        
        Object [] datos = new String[7];
        try {
            //Método para cargar la tabla de pagos cuando se carga la vista o cuando se filtra. Solo elije el query correspondiente
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                datos[0] = ""+rs.getInt(1);
                if(rs.getBoolean(2))  //Cobrado
                    datos[1] = "SI";
                else
                    datos[1] = "NO";
                datos[2] = ""+rs.getLong(3);   //Monto
                datos[3] = ""+rs.getDate(4);  //Fecha Emisión
                if(rs.getDate(5) == null)
                    datos[4] = " --";
                else
                    datos[4] = ""+rs.getDate(5);  //Fecha Cobro;
                if(rs.getBoolean(6))  //Notificar
                    datos[5] = "SI";
                else
                    datos[5] = "NO";
                datos[6] = rs.getString(8)+" "+rs.getString(7);  //Apellido y Nombre
                
                this.modeloCli.addRow(datos);
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar Pagos en la Tabla: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR!", JOptionPane.WARNING_MESSAGE);  
        }
    }
    
    private void cargarTablaConContado(String query){
        //Método que carga la tabla con pagos al contado - Debe cambiar las columnas de la misma(la Tabla).
        //Método que carga la tabla con cheques - Debe cambiar las columnas de la misma (la tabla).
        //ID", "Cobrado", "Monto", "Fecha Emisión", "Fecha Cobro", "Notificar", "Nombre del Cliente
        Object [] datos = new String[7];
        try {
            //Método para cargar la tabla de pagos cuando se carga la vista o cuando se filtra. Solo elije el query correspondiente
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            //ch.id, ch.monto, ch."+fecha+", per.nombre, per.apellido
            while(rs.next()){
                datos[0] = ""+rs.getInt(1); //ID
                datos[1] = "SI";  //Cobrado - Siempre sí para contado
                datos[2] = ""+rs.getLong(2);   //Monto
                datos[3] = " --"; //Fecha Emisión
                datos[4] = ""+rs.getDate(3);  //Fecha Cobro
                datos[5] = "No se usa";
                datos[6] = rs.getString(4)+" "+rs.getString(5);  //Nombre y Apellido
                
                this.modeloCli.addRow(datos);
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar Pagos en la Tabla: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR!", JOptionPane.WARNING_MESSAGE);  
        }
        
    }
    
   /* 
    private void cargarTablaPago(String query){
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
        this.jTablePagos.updateUI();
    }
    */
  
    private int diasPorPlanillasImpagas(int idCliente) throws SQLException {
        //Método que servirá para averiguar la cantidad de días como máximo que tienen las planillas del cliente
        LocalDate fecha_hoy, fecha_salida_vh;
        Date fecha_salida;
        Date fechaHoy = new Date(System.currentTimeMillis());
        fecha_hoy = fechaHoy.toLocalDate();
        int actual;
        int estadoCC = 0; //Podría usarse en base a días desde que una planilla está impaga. 
        String consulta = "SELECT MIN(pl.fecha_de_salida) FROM planilla as pl INNER JOIN cliente AS c ON c.idcliente = pl.idcliente "
                + "WHERE pl.facturado = true AND pl.pagado = false AND c.idcliente = '"+idCliente+"' ";
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
        //Método que retorna el importe total de pagos del cliente. --Cambio 23/02/2021 -Se quito el AND p.facturado = TRUE;
        String sql = "SELECT p.idplanilla FROM planilla as p INNER JOIN cliente AS c ON p.idcliente = c.idcliente "
                + "WHERE c.idcliente = '"+idCliente+"' "; //todas las planillas del Cliente
        long monto = 0;
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                monto += montoPagosPlanilla(rs.getInt(1));  //Paso la planilla del cliente y le calculo el importe que tiene
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
    
    private void cargarPlanilla(int idPago){
        //Método para cargar la planilla correspondiente al pago
        String query = "SELECT p.idplanilla FROM planilla AS p INNER JOIN forma_de_pago AS fdp ON p.idplanilla = fdp.idplanilla " +
            "WHERE fdp.idforma_de_pago = (SELECT idforma_de_pago FROM cheque WHERE cheque.idcheque = '"+idPago+"') " +
            "OR fdp.idforma_de_pago = (SELECT idforma_de_pago FROM contado WHERE contado.idcontado = '"+idPago+"')";
        int idPlanilla = 0;
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idPlanilla = rs.getInt(1);
            }
            PanelPlanillaNueva p1 = new PanelPlanillaNueva(idPlanilla);
            this.controlador.cambiarDePanel(p1, "Ver/Modificar Planilla");
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al cargar la planilla que corresponde al pago: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }   
   /* 
    private void cargarDatosTablaPlanillas(String consulta){
        //Método para cargar datos en la tabla una vez que la consulta está lista
        //Reinició los datos de la tabla.
        DefaultTableModel dtm = (DefaultTableModel) this.jTablePlanillas.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla 
        String[] registro = new String[7];
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
                    registro[2] = "La planilla no fue Facturada";
                registro[3] = ""+cargarImporteReparaciones(rs.getInt(1), co);
                if(rs.getBoolean(4)) //Si es verdadero la planilla esta pagada
                    registro[4] = "SI";
                else
                    registro[4] ="NO";
                registro[5] = ""+this.montoPagosPlanilla(rs.getInt(1)); //Acá calcula el monto de los pagos asignados a la planilla
                if(rs.getBoolean(5)) //Verdadero si tiene al menos un cheque asignado sin cobrar
                    registro[6] = "SI";
                else
                    registro[6] = "NO";
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
    */
 /*   
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
    */
    
    private long montoPorReparaciones(int idCliente) {
        //Obtiene la suma de los importes de las reparaciones asignadas a las planillas (Sean pagas o impagas)
        long suma = 0;
        Connection co = this.controlador.obtenerConexion();
        String sql = "SELECT SUM(r.IMPORTE) FROM reparacion AS r INNER JOIN planilla as p ON p.idplanilla = r.idplanilla INNER JOIN cliente"
                + " AS c ON p.idcliente = c.idcliente WHERE c.idcliente = '"+idCliente+"' ";
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
        jLabel20 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePagos = new javax.swing.JTable();
        jButtonSelectPago = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelBalance = new javax.swing.JLabel();
        jButtonInfo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBoxCli = new javax.swing.JComboBox<>();
        jCheckBoxCli = new javax.swing.JCheckBox();
        jRadioButtonCheque = new javax.swing.JRadioButton();
        jRadioButtonContado = new javax.swing.JRadioButton();
        jRadioButtonACobrar = new javax.swing.JRadioButton();
        jRadioButtonChVencidos = new javax.swing.JRadioButton();
        jTextFieldCli = new javax.swing.JTextField();
        jButtonBuscarCli = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jButtonFiltrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jRadioButtonTodos = new javax.swing.JRadioButton();

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setFocusable(false);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setSize(new java.awt.Dimension(680, 390));
        jFrameInfo.setType(java.awt.Window.Type.POPUP);

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel18.setText("un Cliente y el monto total de pagos de las mismas planillas (todas).");

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel16.setText(". Balance de saldo entre los importes de todas las reparaciones de las planillas de");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setText(". Estado de la Cuenta Corriente de un Cliente. A partir de una planilla facturada");

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Esta vista sirve para ver la siguiente información:");

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

        jLabel20.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel20.setText(". Todos los pagos (cheque y al contado) que un Cliente a realizado");

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel17.setText("e impaga se calculan los días que tiene como impaga.");

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(204, 0, 0));
        jLabel21.setText("Se puede usar el botón de \"Ver Pago\" para dirigirse a la planilla");

        javax.swing.GroupLayout jFrameInfoLayout = new javax.swing.GroupLayout(jFrameInfo.getContentPane());
        jFrameInfo.getContentPane().setLayout(jFrameInfoLayout);
        jFrameInfoLayout.setHorizontalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAceptar)
                        .addContainerGap())
                    .addComponent(jSeparator2)
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addGroup(jFrameInfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel21))))
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        jFrameInfoLayout.setVerticalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addGap(4, 4, 4)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addGap(2, 2, 2)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jButtonAceptar)
                .addContainerGap())
        );

        setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jTablePagos.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 16)); // NOI18N
        jTablePagos.setModel(modeloCli);
        jTablePagos.setRowHeight(22);
        jScrollPane2.setViewportView(jTablePagos);

        jButtonSelectPago.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonSelectPago.setText("Ver Pago");
        jButtonSelectPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectPagoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel3.setText("Estado de Cuenta Corriente:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel5.setText("Balance de CC (Signo negativo si debe):");

        jLabelBalance.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N

        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        jComboBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCli.setText("Cliente:");

        buttonGroup1.add(jRadioButtonCheque);
        jRadioButtonCheque.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonCheque.setText("Cheque");

        buttonGroup1.add(jRadioButtonContado);
        jRadioButtonContado.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonContado.setText("Contado");

        buttonGroup2.add(jRadioButtonACobrar);
        jRadioButtonACobrar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonACobrar.setText("A Cobrar");

        buttonGroup2.add(jRadioButtonChVencidos);
        jRadioButtonChVencidos.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonChVencidos.setText("Vencidos");

        jTextFieldCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonBuscarCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonBuscarCli.setText("Buscar");
        jButtonBuscarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarCliActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButtonFiltrar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonFiltrar.setText("Filtrar");
        jButtonFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFiltrarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 48)); // NOI18N
        jLabel1.setText("↳");

        jTextFieldCC.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        this.jTextFieldCC.setText(" ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("* Ver Pago abre la planilla que contiene el pago seleccionado. Guiarse por el ID de la columna izquierda de esta Tabla");

        buttonGroup2.add(jRadioButtonTodos);
        jRadioButtonTodos.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonTodos.setText("Todos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonSelectPago, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonCheque)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonContado)
                                .addGap(51, 51, 51)
                                .addComponent(jCheckBoxCli))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jRadioButtonTodos))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButtonACobrar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jRadioButtonChVencidos)))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBoxCli, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldCli, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonBuscarCli))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabelBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextFieldCC, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButtonCheque)
                            .addComponent(jRadioButtonContado)
                            .addComponent(jCheckBoxCli)
                            .addComponent(jComboBoxCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscarCli))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jRadioButtonACobrar)
                                    .addComponent(jRadioButtonChVencidos))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonTodos))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldCC, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabelBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonSelectPago)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectPagoActionPerformed
        // ActionPerformed para seleccionar un Cliente. Se deben llenar los campos debajo de la tabla con información y la tabla de planillas
        //Debo cargar las planillas de este cliente y poner información en los jLabel del estado de la cueta (saldo)
        int filaSelect = this.jTablePagos.getSelectedRow();
        int idPago;
        if(filaSelect != -1){
                //Acá deberán consultarse los métodos para calcular el saldo del cliente, activas los labels y cargarlo con la información
                // que se obtenga
                idPago = Integer.valueOf((String)this.jTablePagos.getValueAt(filaSelect, 0));
                this.cargarPlanilla(idPago);
        }
        else{
            JLabelAriel label = new JLabelAriel("Debe seleccionar una fila de la tabla: ");
            JOptionPane.showMessageDialog(null, label, "¡ATENCIÓN!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSelectPagoActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        // Abre el Frame de JFrameInfo para mostrar info de la vista (Para que sirve para que no sirve).
        this.jFrameInfo.setVisible(true);
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        //Cierra el jFrameInfo
        this.jFrameInfo.dispose();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    
    private void jButtonBuscarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarCliActionPerformed
        // ActionPerformed de filtro de marca de Vh -- llama al método para cargar el Vh
        this.jComboBoxCli.removeAllItems();
        this.cargarCli(this.jTextFieldCli.getText());
    }//GEN-LAST:event_jButtonBuscarCliActionPerformed

    private void jButtonFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFiltrarActionPerformed
        // llama al método encargado de decidir el query según los filtros y luego llama a cargar la tabla con ese query
        String query = decidirQuery();
        cargarTablaPagos(query);
        cargarLabelCC();  //Carga el Label de Cuenta corriente 
        cargarLabelBalance(); //Carga el Label de balance de saldo
    }//GEN-LAST:event_jButtonFiltrarActionPerformed

    private String decidirQuery() {
        //Decide que query es el apropiado para cargar la tabla. Le agrega o quita dinámicamente parte de la consulta según corresponda
        String tipo = "contado", query, fecha = "fecha";
        if (this.jRadioButtonCheque.isSelected()) //El WHERE va sí o sí
        {
            tipo = "cheque";
            fecha = "fecha_emision";
            //ID", "Cobrado", "Monto", "Fecha Emisión", "Fecha Cobro", "Notificar", "Nombre del Cliente
            //tipo puede ser contado o cheque - soy un tontinio, acá siempre es cheque - igual queda
            query = "SELECT ch.id" + tipo + ", ch.cobrado, ch.monto, ch."+fecha+", ch.fecha_cobro, ch.notificar, per.nombre, per.apellido "
                    + "FROM forma_de_pago AS fdp INNER JOIN " + tipo + " AS ch ON ch.idforma_de_pago = fdp.idforma_de_pago INNER JOIN "
                    + "planilla AS p ON fdp.idplanilla = p.idplanilla INNER JOIN cliente AS c ON p.idcliente = c.idcliente "
                    + "INNER JOIN persona AS per ON per.idpersona = c.idpersona WHERE fdp.tipo = '" + tipo + "' ";
            if (this.jRadioButtonChVencidos.isSelected()){
                query += " AND (DATE_PART('day', now()::timestamp - ch.fecha_emision::timestamp) > 30 "
                        + "OR DATE_PART('day', now()::timestamp - ch.fecha_cobro::timestamp) > 30) AND ch.cobrado = 'false' ";
            }
            else if(this.jRadioButtonACobrar.isSelected()){  //Query para los cheques a Cobrar que no están vencidos
                query += " AND (DATE_PART('day', now()::timestamp - ch.fecha_emision::timestamp) <= 30 "
                    + "OR DATE_PART('day', now()::timestamp - ch.fecha_cobro::timestamp) <= 30) AND ch.cobrado = 'false' ";
            }
            else  //Todos los cheques - no se agrega nada
                ;
        } else { ////ID", "Cobrado", "Monto", "Fecha Emisión", "Fecha Cobro", "Notificar", "Nombre del Cliente
            query = "SELECT ch.id" + tipo + ", ch.monto, ch."+fecha+", per.nombre, per.apellido FROM forma_de_pago AS fdp "
                    + "INNER JOIN " + tipo + " AS ch ON ch.idforma_de_pago = fdp.idforma_de_pago INNER JOIN planilla AS p ON "
                    + "fdp.idplanilla = p.idplanilla INNER JOIN cliente AS c ON p.idcliente = c.idcliente INNER JOIN persona AS per "
                    + "ON per.idpersona = c.idpersona WHERE (fdp.tipo = '" + tipo + "') ";
        }
        if (this.jCheckBoxCli.isSelected()) {
            ComboItem cIt = (ComboItem) this.jComboBoxCli.getSelectedItem();
            if (!cIt.getKey().equals("0")) {
                query += " AND c.idcliente = '" + cIt.getKey() + "' ";
            }   
        }
        query += " ORDER BY per.apellido ";
        return query;
    }
    
    private void cargarLabelCC(){
        //Cargo los Labels de Datos. 
        int dias;
        ComboItem cIe = (ComboItem) this.jComboBoxCli.getSelectedItem();
        if(!(cIe.getKey().equals("0")) && (!(cIe.getKey().equals("-1")))){
            try {
                dias = this.diasPorPlanillasImpagas(Integer.valueOf(cIe.getKey()));
                if(dias >= 45){
                    this.jTextFieldCC.setForeground(Color.red);
                    this.jTextFieldCC.setText("Estado CC: "+dias+" días con deuda");
                }
                else if(dias >= 30){
                    this.jTextFieldCC.setForeground(Color.orange);
                    this.jTextFieldCC.setText("Estado CC: "+dias+ " días con deuda");
                }
                else{
                    this.jTextFieldCC.setForeground(Color.green);
                    this.jTextFieldCC.setText("Estado CC: "+dias+" días con deuda");
                }
            } catch (SQLException ex) {
                JLabel label = new JLabelAriel("Error al obtener datos de los Labels: "+ex.getMessage());
                JOptionPane.showMessageDialog(null, label, "ERROR!", JOptionPane.WARNING_MESSAGE); 
            }
        }   
    }
    
    private void cargarLabelBalance(){
        //Carga el balance de saldo del cliente seleccionado
        long montoRep, montoPagos;
        ComboItem cIe = (ComboItem) this.jComboBoxCli.getSelectedItem();
        montoRep = this.montoPorReparaciones(Integer.valueOf(cIe.getKey()));
        montoPagos = this.montoPagos(Integer.valueOf(cIe.getKey()));
        long suma = montoPagos - montoRep;
        this.jLabelBalance.setText(""+suma);   
    }
    
    private void cargarCli(String nombre){
        //Método para cargar el cliente según String pasado como parámetro
        //Carga los Vehículos en el jComboBoxVh en formato ComboItem (idVh, marca-modelo-patente)
        String query = "SELECT c.idcliente, p.nombre, p.apellido FROM persona AS p INNER JOIN cliente AS c ON p.idpersona = c.idpersona";
        String nombreApe = nombre.toLowerCase();
        String apellido;
        ComboItem cItem;
        this.jComboBoxCli.addItem(new ComboItem("0", "--Seleccione un Cliente--"));
        int idCli;
        if(!nombre.equals(""))  //Si tengo nombre --> cargo el|los clientes que correspondan según el nombre o apellido
            query = "SELECT c.idcliente, p.nombre, p.apellido FROM persona AS p INNER JOIN cliente AS c ON p.idpersona = c.idpersona"
                    + " WHERE lower(p.nombre) LIKE '"+nombreApe+"%' OR lower(p.apellido) LIKE '"+nombreApe+"%' ";
        Statement st;
        try {
            st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idCli = rs.getInt(1);
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                cItem = new ComboItem(""+idCli, nombre+" "+apellido);
                this.jComboBoxCli.addItem(cItem);
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar el/los clientes "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR!!", JOptionPane.WARNING_MESSAGE); 
        }  
    }
    
    public final void seleccionarClienteEnComboBox(int idCliente){
        //Método que sirve para seleccionar un cliente al cuál ya se le conoce el id. Se puede usar para llamar desde afuera de la clase
        ComboItem cmItem;
        for(int i = 0; i < this.jComboBoxCli.getItemCount(); i++){
            cmItem = this.jComboBoxCli.getItemAt(i);
            if(cmItem.getKey().equals(""+idCliente)){
                this.jComboBoxCli.setSelectedIndex(i);
                break;
            }
        }
    }
    
    @Override
    public boolean sePuedeCerrar() {
        //Teoricamente no habría problemas para cerrar en cualquier momento esta pestaña
        return true;  //El controlador se encarga de cerrar la pestaña actual
    }

    @Override
    public void onFocus() {
        //Cosas a cargar cuando esta vista toma el foco. Ver que conviene:
        String query = this.decidirQuery();
        this.cargarTablaPagos(query);
        this.cargarLabelCC();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonBuscarCli;
    private javax.swing.JButton jButtonFiltrar;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonSelectPago;
    private javax.swing.JCheckBox jCheckBoxCli;
    private javax.swing.JComboBox<ComboItem> jComboBoxCli;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelBalance;
    private javax.swing.JRadioButton jRadioButtonACobrar;
    private javax.swing.JRadioButton jRadioButtonChVencidos;
    private javax.swing.JRadioButton jRadioButtonCheque;
    private javax.swing.JRadioButton jRadioButtonContado;
    private javax.swing.JRadioButton jRadioButtonTodos;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTablePagos;
    private javax.swing.JTextField jTextFieldCC;
    private javax.swing.JTextField jTextFieldCli;
    // End of variables declaration//GEN-END:variables
}
