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
        this.controlador = ControladorPrincipal.getInstancia();
        modeloCli = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false. No se puede editar ninguna celda
                return false;
            }
        };
        initComponents();
        this.jTablePagos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
  //      this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.cargarTablaPagos();
        this.cargarCli("");  //Cargo los Clientes del JComboBox
        this.jRadioButtonCheque.setSelected(true);  //Por defecto muestro los cheques
    }

    private String[] getColumnas(){
        String columna[] = new String[]{"N° Planilla", "Fecha de Entrada", "Fecha Facturado", "Importe", "Pagado", "Monto Pagos", 
            "Cheques por Cobrar"};
        
        return columna;
    }
    
    private String[] getColumnasCliente(){
        String columnas[] = new String[]{"ID", "Tipo", "Monto",  "Notificar", "Nombre Cliente"};
        
        return columnas;
    }
    
    private void cargarTablaPagos(){
        //Método para cargar la tabla de pagos cuando se carga la vista o cuando se filtra. Solo elije el query correspondiente
        String query = "SELECT ";
        
    }
    
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
  
/*    
    private void cargarTablaPlanillas(int idCliente){
        //Método para cargar planillas en la tabla que tiene que llamarse dsps de un ActionPerformed de "Seleccionar Cliente"
        //Diseñar para aplicar filtro de nombre de cliente
        this.jTablePlanillas.getColumnModel().getColumn(0).setMinWidth(90);
        this.jTablePlanillas.getColumnModel().getColumn(0).setMaxWidth(120);
        String consulta;
        consulta = "SELECT p.idplanilla, p.fecha_de_entrada, p.fecha_de_salida, p.pagado, exists (SELECT c.idcheque FROM cheque AS c "
                + "NATURAL JOIN forma_de_pago AS f WHERE p.idplanilla = f.idplanilla AND p.idcliente = '"+idCliente+"' "
                + "AND c.cobrado = false) FROM planilla AS p INNER JOIN cliente as c ON c.idcliente = p.idcliente "
                + "INNER JOIN persona as pe ON pe.idpersona = c.idpersona WHERE c.idcliente = '"+idCliente+"' ";
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
        jLabel20 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePagos = new javax.swing.JTable();
        jButtonSelectCli = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButtonInfo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBoxCli = new javax.swing.JComboBox<>();
        jCheckBoxCli = new javax.swing.JCheckBox();
        jRadioButtonCheque = new javax.swing.JRadioButton();
        jRadioButtonContado = new javax.swing.JRadioButton();
        jRadioButtonTodos = new javax.swing.JRadioButton();
        jRadioButtonChVencidos = new javax.swing.JRadioButton();
        jTextFieldCli = new javax.swing.JTextField();
        jButtonBuscarCli = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setFocusable(false);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setSize(new java.awt.Dimension(620, 350));
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

        jLabel20.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel20.setText(". Que una planilla figure como paga depende de que el usuario lo elija así.");

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
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jFrameInfoLayout.setVerticalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addGap(14, 14, 14)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jButtonAceptar)
                .addContainerGap())
        );

        setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jTablePagos.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jTablePagos.setModel(modeloCli);
        jScrollPane2.setViewportView(jTablePagos);

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

        jComboBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCli.setText("Cliente:");

        buttonGroup1.add(jRadioButtonCheque);
        jRadioButtonCheque.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonCheque.setText("Cheque");

        buttonGroup1.add(jRadioButtonContado);
        jRadioButtonContado.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonContado.setText("Contado");

        jRadioButtonTodos.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jRadioButtonTodos.setText("Todos");

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

        jButton2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton2.setText("Filtrar");

        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 36)); // NOI18N
        jTextField1.setText("↳");

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
                        .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSelectCli))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonCheque)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonContado)
                                .addGap(51, 51, 51)
                                .addComponent(jCheckBoxCli)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxCli, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldCli, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonBuscarCli))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jRadioButtonTodos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonChVencidos)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE)))
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
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonTodos)
                            .addComponent(jRadioButtonChVencidos)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectCliActionPerformed
        // ActionPerformed para seleccionar un Cliente. Se deben llenar los campos debajo de la tabla con información y la tabla de planillas
        //Debo cargar las planillas de este cliente y poner información en los jLabel del estado de la cueta (saldo)
        int filaSelect = this.jTablePagos.getSelectedRow();
        int idCliente;
        if(filaSelect != -1){
                //Acá deberán consultarse los métodos para calcular el saldo del cliente, activas los labels y cargarlo con la información
                // que se obtenga
                idCliente = Integer.valueOf((String)this.jTablePagos.getValueAt(filaSelect, 2));
              //  this.cargarTablaPlanillas(idCliente);
              //  this.cargarEstadoCliente(idCliente);
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

    
    private void jButtonBuscarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarCliActionPerformed
        // ActionPerformed de filtro de marca de Vh -- llama al método para cargar el Vh
        this.jComboBoxCli.removeAllItems();
        this.cargarCli(this.jTextFieldCli.getText());
    }//GEN-LAST:event_jButtonBuscarCliActionPerformed

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
                    + " WHERE lower(p.nombre) LIKE '"+nombreApe+"' OR lower(p.apellido) LIKE '"+nombreApe+"' ";
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonBuscarCli;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonSelectCli;
    private javax.swing.JCheckBox jCheckBoxCli;
    private javax.swing.JComboBox<ComboItem> jComboBoxCli;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton jRadioButtonChVencidos;
    private javax.swing.JRadioButton jRadioButtonCheque;
    private javax.swing.JRadioButton jRadioButtonContado;
    private javax.swing.JRadioButton jRadioButtonTodos;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTablePagos;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldCli;
    // End of variables declaration//GEN-END:variables
}
