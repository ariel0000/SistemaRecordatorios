/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import modelo.Cheque;
import modelo.Estado;
import modelo.EstadoCC;
import modelo.EstadoCheque;
import modelo.EstadoMantencion;
import modelo.EstadoNotifHoy;
import modelo.EstadoPlanilla;
import modelo.EstadoReparacion;
import modelo.JLabelAriel;
import modelo.Mantenimiento;
import modelo.Notificador;
import modelo.Planilla;
import modelo.Reparacion;

/**
 *
 * @author ari_0
 */
public class PanelPrincipal extends JPanelCustom {

    private DefaultTableModel tablaNotificaciones;
    private ControladorPrincipal controlador;
    private PanelAdPersonas panelPersonas;
    private PanelReparaciones panelRepNueva;
    private PanelAdVehiculos panelVehiculos;
    private PanelPlanillaNueva panelPlanillaNueva;
    private PanelVerPlanillas panelVerPlanillas;
    private PanelAdPagos panelAdPagos;
    private Estado estado;  //Para tener siempre el estado correcto para ejecutar el filtrado de planillas
    private ArrayList<Notificador> notificaciones;
    private ArrayList<Notificador> cheques;
    private ArrayList<Notificador> reparaciones;
    private ArrayList<Notificador> planillasImpagas;
    private ArrayList<Notificador> mantenciones;  //Servis
    private ArrayList<Notificador> estadoCC;  // Estado de Cuenta Corriente
    //  private ArrayList<Notificador> clientes;  -- Todavía no estoy seguro

    public PanelPrincipal() {
        tablaNotificaciones = new DefaultTableModel(null, getColumnas()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        initComponents();
        this.jTableNotificaciones.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
        this.jTableNotificaciones.getColumnModel().getColumn(0).setMinWidth(30);
        this.jTableNotificaciones.getColumnModel().getColumn(0).setMaxWidth(55);
        this.jTableNotificaciones.getColumnModel().getColumn(1).setMinWidth(240);
        this.jTableNotificaciones.getColumnModel().getColumn(1).setMaxWidth(390);
        this.jTableNotificaciones.getColumnModel().getColumn(2).setMaxWidth(900);
        this.jTableNotificaciones.getColumnModel().getColumn(2).setMinWidth(390);
        this.jTableNotificaciones.getColumnModel().getColumn(3).setMinWidth(85);
        this.jTableNotificaciones.getColumnModel().getColumn(3).setMaxWidth(130);
        this.jTableNotificaciones.getColumnModel().getColumn(4).setMinWidth(180);
        this.jTableNotificaciones.getColumnModel().getColumn(4).setMaxWidth(270);
        controlador = ControladorPrincipal.getInstancia(); //De esta manera uso solo un controlador
        //Creando todos los paneles - al momento de pasarlos al controlador siempre es la misma instancia
        // --> Podemos controlar que no aparezcan más de una vez la misma instancia
        this.panelPersonas = new PanelAdPersonas();
        this.panelRepNueva = new PanelReparaciones();
        this.panelVehiculos = new PanelAdVehiculos();
        this.panelPlanillaNueva = new PanelPlanillaNueva();
        this.panelVerPlanillas = new PanelVerPlanillas();

        //añadirNotifDePrueba();  //Para poder mostrar dos notificaciones de ejemplo a Leo
        iniciarEnComun();
    }

    private String[] getColumnas() {
        String columna[] = new String[]{"Id", "Tipo Notif.", "Descripción", "Prioridad", "Cliente"};
        return columna;
    }

    /*
    private void añadirNotifDePrueba(){
        String datos[] = new String[3];  //Tipo, Descripción, Patente
        datos[0] = "Cobro";
        datos[1] = "Cheque 123456789 al portador ya puede ser depositado";
        datos[2] = "Scania 330";
        this.tablaNotificaciones.addRow(datos);
        //--------------------------//
        datos[0] = "Mantenimiento";
        datos[1] = "Cambio Aceite de motor y filtro";
        datos[2] = "Mercedes Benz 1114";
        this.tablaNotificaciones.addRow(datos);
        
        this.jTableNotificaciones.updateUI();
    }
     */
    private void iniciarEnComun() {
        //Cosas para iniciar en Constructor PanelPrincipal()
        this.jRadioButtonNotifHoy.setSelected(true);
        this.estado = new EstadoNotifHoy();  //Por defecto tengo el estado de Notificaciones de Hoy
        this.cargarNotificadores();
        this.controlador.setPanelPrincipal(this);
     //   this.jTableNotificaciones.
        Font fuente = new Font("Sans Serif", Font.PLAIN, 18);  //Dos líneas que sirven para acomodar el tamaño de letra de los botones de
        UIManager.put("OptionPane.buttonFont", fuente);  // los JOptionPane...
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFiltros = new javax.swing.ButtonGroup();
        jFrameInfo = new javax.swing.JFrame();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jButtonAdCamiones = new javax.swing.JButton();
        jButtonAdPersonas = new javax.swing.JButton();
        jButtonNuevaPlanilla = new javax.swing.JButton();
        jButtonVerReparaciones = new javax.swing.JButton();
        jButtonVerPlanillas = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableNotificaciones = new javax.swing.JTable();
        jButtonAdPagos = new javax.swing.JButton();
        jButtonFiltrar = new javax.swing.JButton();
        jRadioButtonCheques = new javax.swing.JRadioButton();
        jRadioButtonReparaciones = new javax.swing.JRadioButton();
        jRadioButtonNotifHoy = new javax.swing.JRadioButton();
        jRadioButtonPlanillas = new javax.swing.JRadioButton();
        jRadioButtonMantencion = new javax.swing.JRadioButton();
        jButtonInfo = new javax.swing.JButton();
        jButtonVerNotif = new javax.swing.JButton();

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setResizable(false);
        jFrameInfo.setSize(new java.awt.Dimension(900, 385));
        jFrameInfo.setType(java.awt.Window.Type.POPUP);

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel24.setText("En esta Vista podemos ver las notificaciones del Sistema. Se incluyen las siguientes:");

        jLabel25.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel25.setText(" * Mantenimiento incompleto: Reparación de tipo mantenimiento marcada como incompleta");

        jLabel26.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel26.setText(" * Planillas impagas. Son las planillas marcadas como facturadas pero que todavían no se pagaron ");

        jLabel27.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel27.setText(" * Reparaciones incompletas: Son las reparaciones creadas que todavía no se marcaron como terminadas  ");

        jLabel28.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel28.setText(" * Cheques por cobrar: Cheques de cualquier tipo que tienen que ser cobrados antes de cierta fecha ");

        jLabel29.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 0, 51));
        jLabel29.setText("     - Ejemplo: Cambio de Aceite y Filtro");

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel30.setText(" * Service por realizar: Mantenimiento de un Camión que debe ser realizado porque se cumplió el periodo.");

        javax.swing.GroupLayout jFrameInfoLayout = new javax.swing.GroupLayout(jFrameInfo.getContentPane());
        jFrameInfo.getContentPane().setLayout(jFrameInfoLayout);
        jFrameInfoLayout.setHorizontalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel25)
                            .addComponent(jLabel28)
                            .addComponent(jLabel30)
                            .addGroup(jFrameInfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel29)))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jFrameInfoLayout.setVerticalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(27, 27, 27))
        );

        setPreferredSize(new java.awt.Dimension(1075, 733));

        jButtonAdCamiones.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonAdCamiones.setText("<html><p>Administar</p><p>Camiones</p></html>");
        jButtonAdCamiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdCamionesActionPerformed(evt);
            }
        });

        jButtonAdPersonas.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonAdPersonas.setText("<html><p>Administrar</p><p>&nbsp;Personas</p></html>");
        jButtonAdPersonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdPersonasActionPerformed(evt);
            }
        });

        jButtonNuevaPlanilla.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonNuevaPlanilla.setText("<html><p>Nueva</p><p>Planilla</p></html>");
        jButtonNuevaPlanilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevaPlanillaActionPerformed(evt);
            }
        });

        jButtonVerReparaciones.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonVerReparaciones.setText("<html><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ver</p><p>Reparaciones</p></html>");
        jButtonVerReparaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerReparacionesActionPerformed(evt);
            }
        });

        jButtonVerPlanillas.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonVerPlanillas.setText("<html><p>&nbsp;&nbsp;&nbsp;&nbsp;Ver</p><p>Planillas</p></html>");
        jButtonVerPlanillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerPlanillasActionPerformed(evt);
            }
        });

        jScrollPane1.setFont(new java.awt.Font("SansSerif", 0, 17)); // NOI18N

        jTableNotificaciones.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 16)); // NOI18N
        jTableNotificaciones.setModel(tablaNotificaciones);
        jTableNotificaciones.setRowHeight(22);
        jScrollPane1.setViewportView(jTableNotificaciones);

        jButtonAdPagos.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonAdPagos.setText("<html><p>Administar</p><p>&nbsp;&nbsp;&nbsp;Pagos</p></html>");
        jButtonAdPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdPagosActionPerformed(evt);
            }
        });

        jButtonFiltrar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonFiltrar.setText("Filtrar");
        jButtonFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFiltrarActionPerformed(evt);
            }
        });

        buttonGroupFiltros.add(jRadioButtonCheques);
        jRadioButtonCheques.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonCheques.setText("Cheques y EstadoCC");
        jRadioButtonCheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonChequesActionPerformed(evt);
            }
        });

        buttonGroupFiltros.add(jRadioButtonReparaciones);
        jRadioButtonReparaciones.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonReparaciones.setText("Reparaciones");
        jRadioButtonReparaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonReparacionesActionPerformed(evt);
            }
        });

        buttonGroupFiltros.add(jRadioButtonNotifHoy);
        jRadioButtonNotifHoy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonNotifHoy.setText("Notif. de Hoy");
        jRadioButtonNotifHoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonNotifHoyActionPerformed(evt);
            }
        });

        buttonGroupFiltros.add(jRadioButtonPlanillas);
        jRadioButtonPlanillas.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonPlanillas.setText("Planillas");
        jRadioButtonPlanillas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPlanillasActionPerformed(evt);
            }
        });

        buttonGroupFiltros.add(jRadioButtonMantencion);
        jRadioButtonMantencion.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonMantencion.setText("Services");
        jRadioButtonMantencion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMantencionActionPerformed(evt);
            }
        });

        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        jButtonVerNotif.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonVerNotif.setText("<html><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ver</p><p>&nbsp;&nbsp;Notificacion</p></html>");
        jButtonVerNotif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerNotifActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButtonAdPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                        .addComponent(jButtonAdCamiones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonNuevaPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonVerReparaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonVerPlanillas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAdPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonVerNotif, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButtonCheques)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonReparaciones)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonPlanillas)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonMantencion)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonNotifHoy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonFiltrar))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonFiltrar)
                        .addComponent(jRadioButtonCheques)
                        .addComponent(jRadioButtonReparaciones)
                        .addComponent(jRadioButtonNotifHoy)
                        .addComponent(jRadioButtonPlanillas)
                        .addComponent(jRadioButtonMantencion))
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAdCamiones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdPersonas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNuevaPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVerReparaciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVerPlanillas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdPagos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVerNotif)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void cargarNotificadores() {
        //Este método carga los ArrayList de Notificadores. (TODOS)
        this.cheques = cargarCheques(); //Tanto los comunes como los diferidos
        
        //... Cargamos las planillas impagas
        this.planillasImpagas = cargarPlanillasImpagas();
        this.estadoCC = cargarEstadoCC();
        this.reparaciones = cargarReparaciones();
        this.mantenciones = cargarMantenciones();
        this.notificaciones = new ArrayList<>(); 
        this.notificaciones.addAll(this.planillasImpagas);
        this.notificaciones.addAll(this.cheques);
        this.notificaciones.addAll(this.reparaciones);
        this.notificaciones.addAll(this.mantenciones);
        this.notificaciones.addAll(this.estadoCC);
        cargarNotificadoresATabla(this.notificaciones);
    }

    public void cargarNotificadores(ArrayList<Notificador> arrai){
        //Sobrecarga en el método. Este es llamado desde los estados para filtrar las notifcaciones y pasar a tabla las que el estado indique
        this.notificaciones = new ArrayList<>(); //Primero borro todas las notificaciones actuales.
        this.notificaciones = arrai;
        cargarNotificadoresATabla(this.notificaciones);
    }
    
    public ArrayList cargarCheques() {
        // Carga los cheques que están listos para cobrar, los que tienen menos días para cobrar tienen más prioridad
        ArrayList<Notificador> chequesComunes, chequesDiferidos, todosLosCheques;
        chequesComunes = obtenerChequeComun();
        chequesDiferidos = obtenerChequesDiferidos();
        chequesDiferidos.addAll(chequesComunes);
        todosLosCheques = chequesDiferidos;
        //Faltaría obtener los cheques con pago diferido
        return todosLosCheques;
    }

    public ArrayList<Notificador> obtenerChequeComun() {
        //Cargo los cheques sin fecha de cobro (Cheques comunes)
        LocalDate fechaHoy = LocalDate.now();
        ArrayList<Notificador> chequesComunes = new ArrayList<>();
        LocalDate fechaEmision;
        String variable = "";
        String nombre, apellido;
        int prioridad;
        //Query para cargar los cheques sin fecha de cobro. Estos se pueden cobrar 30 días después de emitidos
        String query = "SELECT k.idcheque, k.fecha_emision, k.numerocheque, p.nombre, p.apellido, k.monto FROM cheque AS k "
                + "NATURAL JOIN forma_de_pago AS f INNER JOIN planilla AS pl ON pl.idplanilla = f.idplanilla INNER JOIN cliente AS c "
                + "ON pl.idcliente = c.idcliente INNER JOIN persona as p ON c.idpersona = p.idpersona WHERE cobrado = false AND "
                + "fecha_cobro is null AND k.notificar = true ORDER BY k.fecha_emision";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) { //Cargo todos los cheques
                //Además de esta consulta hay que hacer una consulta más por los cheques que tienen fecha de cobro
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaEmision = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //Si lo pasaba directamente explota
                prioridad = (int) DAYS.between(fechaEmision, fechaHoy); //Mas días --> más prioridad
                if(prioridad > 30){
                    prioridad = 10;  //Bajo la prioridad para que aparezcan antes los que se pueden cobrar
                    variable = "Vencido";
                }
                else
                    variable = "a cobrar";
                nombre = rs.getString(4);
                apellido = rs.getString(5);
                Cheque cheque = new Cheque(rs.getInt(1), prioridad, rs.getLong(3), "cheque",
                        "Cheque Pago inmediato "+variable+". monto: "+rs.getLong(6), nombre, apellido); //idcheque, prioridad, numeroDeCheque
                chequesComunes.add(cheque);  //Se cargan primero los que tienen más prioridad
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al obtener cheques comunes: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);
        }
        return chequesComunes;
    }

    public ArrayList<Notificador> obtenerChequesDiferidos() {
        //Cargo los cheques sin fecha de cobro (Cheques comunes)
        LocalDate fechaHoy = LocalDate.now();
        String nombre, apellido;
        ArrayList<Notificador> chequesComunes = new ArrayList<>();
        LocalDate fechaCobro;
        String variable = "";
        int prioridad;
        //Este cheque se puede cobrar dentro de los 30 días posteriores a la fecha de cobro indicada
        String query = "SELECT k.idcheque, k.fecha_cobro, k.numerocheque, p.nombre, p.apellido, k.monto FROM cheque AS k NATURAL JOIN "
                + "forma_de_pago AS f INNER JOIN planilla AS pl ON pl.idplanilla = f.idplanilla INNER JOIN cliente AS c ON "
                + "pl.idcliente = c.idcliente INNER JOIN persona as p ON c.idpersona = p.idpersona WHERE cobrado = false AND "
                + "fecha_cobro is not null AND k.notificar = true ORDER BY k.fecha_cobro";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) { //Cargo todos los cheques
                //Además de esta consulta hay que hacer una consulta más por los cheques que tienen fecha de cobro
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaCobro = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                prioridad = (int) DAYS.between(fechaCobro, fechaHoy);
                if(prioridad > 30){
                    prioridad = 10;  //Bajo la prioridad para que aparezcan antes los que se pueden cobrar
                    variable = "Vencido";
                }
                else
                    variable = "sin cobrar";
                nombre = rs.getString(4);
                apellido = rs.getString(5);
                Cheque cheque = new Cheque(rs.getInt(1), prioridad, rs.getLong(3), "cheque",
                        "Cheque Pago Diferido "+variable+". monto:"+rs.getLong(6), nombre, apellido);
                chequesComunes.add(cheque);  //Se cargan primero los que tienen más prioridad
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al obtener cheques diferidos: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);
        }
        return chequesComunes;
    }

    public ArrayList<Notificador> cargarPlanillasImpagas(){
        //Devuelve un ArrayList<Notificador> con todas las planillas impagas
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaSalida; //Cuando el Vh fue entregado
        String nombre, apellido, descripcion;
        int prioridad = 1;
        ArrayList<Notificador> planillasImpagass = new ArrayList<>();
        String query = "SELECT p.idplanilla, p.fecha_de_salida, p.descripcion, per.nombre, per.apellido FROM planilla AS p INNER JOIN cliente"
                + " AS c ON p.idcliente = c.idcliente INNER JOIN persona AS per ON per.idpersona = c.idpersona WHERE p.notificar = TRUE"
                + " AND p.fecha_de_salida IS NOT NULL AND p.facturado = TRUE AND p.pagado = FALSE";
        
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaSalida = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                prioridad = (int) DAYS.between(fechaSalida, fechaHoy);
                if(prioridad > 30)  
                    prioridad = 30;
                descripcion = rs.getString(3);
                nombre = rs.getString(4);
                apellido = rs.getString(5);
                Planilla planilla = new Planilla(rs.getInt(1), prioridad, "Planilla Impaga", descripcion, nombre, apellido);
                planillasImpagass.add(planilla);
            }
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al obtener planillas impagas: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
        return planillasImpagass;
    }
    
    public ArrayList<Notificador> cargarEstadoCC(){
        // Devuelve un ArrayList con los estados de CC de los clientes que están en Naranja o Rojo
        ArrayList<Notificador> estadoCc = new ArrayList<>();
        LocalDate fechaHoy = LocalDate.now();
        String apellido, nombre, tipo;
        int dias = 0;
        LocalDate fechaSalida;
        String variable = "";
        int prioridad;
        //Este cheque se puede cobrar dentro de los 30 días posteriores a la fecha de cobro indicada
        String query = "SELECT c.idcliente, MIN(p.fecha_de_salida), per.nombre, per.apellido FROM planilla AS p INNER JOIN cliente AS c "
                + "ON p.idcliente = c.idcliente INNER JOIN persona AS per ON per.idpersona = c.idpersona "
                + "WHERE p.pagado = false AND  p.facturado = true AND c.notificar = true GROUP BY c.idcliente, per.nombre, per.apellido";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) { //Cargo todos los cheques
                //Además de esta consulta hay que hacer una consulta más por los cheques que tienen fecha de cobro
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaSalida = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                prioridad = (int) DAYS.between(fechaSalida, fechaHoy);
                if(prioridad >= 45)
                    variable = "Complicado";
                else if(prioridad > 30)
                        variable = "A tener en cuenta";
                else
                    variable = "Normal";
                nombre = rs.getString(3);
                apellido = rs.getString(4);
                EstadoCC estado_cuenta_corriente = new EstadoCC(rs.getInt(1), prioridad, "Estado Cuenta Corriente", "Estado CC: "+variable+".  Días debiendo: ", nombre, apellido, dias);
                estadoCc.add(estado_cuenta_corriente);  //Se cargan primero los que tienen más prioridad
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al obtener cheques diferidos: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);
        }
         return estadoCc;
    }
    
    public ArrayList<Notificador> cargarReparaciones(){
        //Método que devuelve las reparaciones o mantenciones incompletas
        String nombre, apellido, descripcion, incompleto, marca, modelo;
        int prioridad, idRep;
        String tipoRep;
        ArrayList<Notificador> reparacioness = new ArrayList();
        String query = "SELECT r.idreparacion, r.descripcion, per.nombre, per.apellido, r.tipo, v.marca, v.modelo FROM reparacion AS r "
                + "INNER JOIN planilla AS p ON r.idplanilla = p.idplanilla INNER JOIN cliente AS c ON c.idcliente = p.idcliente "
                + "INNER JOIN persona AS per ON per.idpersona = c.idpersona INNER JOIN vehiculo AS v ON v.idvehiculo = p.idvehiculo"
                + " WHERE r.completada = false AND r.notificar = true";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idRep = rs.getInt(1); //el idreparacion
                prioridad = 15;  //La prioridad de la reparación es fija
                descripcion = rs.getString(2);
                nombre = rs.getString(3);
                apellido = rs.getString(4);
                tipoRep = rs.getString(5); 
                marca = rs.getString(6);    //Marca
                modelo = rs.getString(7);  //Modelo
                if("mantenimiento".equals(tipoRep))
                    incompleto = "incompleto";
                else
                    incompleto = "incompleta";
                Reparacion reparacion = new Reparacion(idRep, prioridad, tipoRep+" "+incompleto, descripcion, nombre, apellido, marca, modelo);
                reparacioness.add(reparacion);
            }
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al obtener Reparaciones incompletas: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
        return reparacioness;
    }
    
    public ArrayList<Notificador> cargarMantenciones(){
            //Método que devuelve una lista de todas las mantenciones por realizar. No devuelve las incompletas
        String nombre, apellido, descripcion, modelo, marca;
        LocalDate fechaSalida, fechaHoy = LocalDate.now();
        int prioridad, idRep;
        ArrayList<Notificador> reparacioness = new ArrayList();
        String query = "SELECT r.idreparacion, r.descripcion, per.nombre, per.apellido, v.marca, v.modelo, r.fecha_terminado, r.periodo"
                + " FROM reparacion AS r INNER JOIN planilla AS p ON r.idplanilla = p.idplanilla INNER JOIN cliente AS c ON "
                + "c.idcliente = p.idcliente INNER JOIN persona AS per ON per.idpersona = c.idpersona INNER JOIN vehiculo AS v ON "
                + " v.idvehiculo = p.idvehiculo WHERE r.tipo = 'mantenimiento' AND r.completada = true AND r.notificar = TRUE";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                idRep = rs.getInt(1); //el idreparacion
                prioridad = 15;  //La prioridad de la reparación es 15 para 2 meses 25 para 1 mes
                descripcion = rs.getString(2);
                nombre = rs.getString(3);
                apellido = rs.getString(4);
                marca = rs.getString(5);
                modelo = rs.getString(6);
                Date fecha = new Date(rs.getDate(7).getTime());  //Fecha de mantención finalizada.
                fechaSalida = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int mesesDiferencia = (int) ChronoUnit.MONTHS.between(fechaSalida.withDayOfMonth(1), fechaHoy.withDayOfMonth(1));
                if(mesesDiferencia-rs.getInt(8) >= 0 && (mesesDiferencia-rs.getInt(8) <= 2)){ //En el mismo mes o dos meses atrasado el Serv.
                    // Si la diferencia de meses anterior "coincide" con el periodo agregamos la noti.
                    if(mesesDiferencia-rs.getInt(8) < 2)                                      // 0 a 2
                        prioridad = 25;  //Aumento la prioridad porque es una mantención para realizar pronto (1 mes o menos)
                    Mantenimiento mantenimiento = new Mantenimiento(idRep, prioridad, "Service por realizar", descripcion, 
                        nombre, apellido, marca, modelo);    
                    reparacioness.add(mantenimiento);
                }
            }
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al obtener los Mantenimientos (Servi): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
        return reparacioness;
    }
    
    public void cargarNotificadoresATabla(ArrayList<Notificador> notificadores){
    //Método para cargar las notificaciones ordenadas según su prioridad
        Collections.sort(notificadores, Collections.reverseOrder());
        String pl[] = new String[5];
        for (Notificador notificador : notificadores) {
            pl[0] = "" + notificador.getId();
            pl[1] = "" + notificador.getTipo();
            pl[2] = "" + notificador.getDescripcion();
            pl[3] = "" + notificador.getPrioridad();
            pl[4] = notificador.getNombre() + " " + notificador.getApellido();
            this.tablaNotificaciones.addRow(pl);
        }
    }
    
    private void jButtonNuevaPlanillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevaPlanillaActionPerformed
        this.panelPlanillaNueva = new PanelPlanillaNueva(); //Para que se acomoden los datos
        controlador.cambiarDePanel(this.panelPlanillaNueva, "Nueva Planilla");
    }//GEN-LAST:event_jButtonNuevaPlanillaActionPerformed

    private void jButtonAdCamionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdCamionesActionPerformed
        this.panelVehiculos = new PanelAdVehiculos();
        controlador.cambiarDePanel(this.panelVehiculos, "Administar Vehículos");
    }//GEN-LAST:event_jButtonAdCamionesActionPerformed

    private void jButtonVerReparacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerReparacionesActionPerformed
        this.panelRepNueva = new PanelReparaciones();
        controlador.cambiarDePanel(this.panelRepNueva, "Ver Reparaciones");
    }//GEN-LAST:event_jButtonVerReparacionesActionPerformed

    private void jButtonAdPersonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdPersonasActionPerformed
        this.panelPersonas = new PanelAdPersonas();
        controlador.cambiarDePanel(this.panelPersonas, "Administar Personas");
    }//GEN-LAST:event_jButtonAdPersonasActionPerformed

    private void jButtonVerPlanillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerPlanillasActionPerformed
        this.panelVerPlanillas = new PanelVerPlanillas();
        controlador.cambiarDePanel(this.panelVerPlanillas, "Ver Planillas");
    }//GEN-LAST:event_jButtonVerPlanillasActionPerformed

    private void jButtonAdPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdPagosActionPerformed
        // Action Performed de botón "Administrar Pagos"
        this.panelAdPagos = new PanelAdPagos();
        this.controlador.cambiarDePanel(this.panelAdPagos, "Administrar Pagos");
    }//GEN-LAST:event_jButtonAdPagosActionPerformed

    public void borrarTabla(){
        DefaultTableModel dtm = (DefaultTableModel) this.jTableNotificaciones.getModel();
        dtm.setRowCount(0);
    }
    
    private void jButtonFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFiltrarActionPerformed
        // Action Performed de Filtrar Planillas. Debería cargar las planillas con respecto al jRadioButton Seleccionado
        this.notificaciones = new ArrayList<>();
        this.estado.cargarNotifificaciones(); 
    }//GEN-LAST:event_jButtonFiltrarActionPerformed

    private void jRadioButtonChequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonChequesActionPerformed
        //Action Performed del RadioButton de selección de cheques
        this.estado = new EstadoCheque();
    }//GEN-LAST:event_jRadioButtonChequesActionPerformed

    private void jRadioButtonReparacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonReparacionesActionPerformed
        //Action Performed del RadioButton de selección de Reparaciones
        this.estado = new EstadoReparacion();
    }//GEN-LAST:event_jRadioButtonReparacionesActionPerformed

    private void jRadioButtonPlanillasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPlanillasActionPerformed
        //Action Performed del RadioButton de selección de Planillas
        this.estado = new EstadoPlanilla();
    }//GEN-LAST:event_jRadioButtonPlanillasActionPerformed

    private void jRadioButtonMantencionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMantencionActionPerformed
        //Action Performed del RadioButton de selección de Mantenciones (Servis)
        this.estado = new EstadoMantencion();
    }//GEN-LAST:event_jRadioButtonMantencionActionPerformed

    private void jRadioButtonNotifHoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonNotifHoyActionPerformed
        //Action Performed del RadioButton de selección de Notificaciones de hoy
        this.estado = new EstadoNotifHoy();
    }//GEN-LAST:event_jRadioButtonNotifHoyActionPerformed

    private void jButtonVerNotifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerNotifActionPerformed
        // Action Performed que sirve para llamar a la vista que se encargará de administrar cada notificacion
        int filaSelect = this.jTableNotificaciones.getSelectedRow();
        if (filaSelect != -1) {
            Notificador notif;
            notif = this.notificaciones.get(filaSelect);  //Obtengo la notificacion en base a la indexación del Array equivalente con la fila
            //idNotif = Integer.valueOf((String) this.jTableNotificaciones.getValueAt(filaSelect, 0)); //El id de la notificación
            notif.verNotificacion(); //No importa el tipo de notificación la misma es llamada mediante la clase padre abstracta
        }
        else{
            JLabel label = new JLabelAriel("No hay fila seleccionada ");
            JOptionPane.showMessageDialog(this, label, "¡¡ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
    }//GEN-LAST:event_jButtonVerNotifActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
       //  Cargar el Frame de información
       this.jFrameInfo.setVisible(true);
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.jFrameInfo.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    public PanelAdPersonas getPanelPersonas() {
        return panelPersonas;
    }

    public void setPanelPersonas(PanelAdPersonas panelPersonas) {
        this.panelPersonas = panelPersonas;
    }

    public PanelReparaciones getPanelReparaciones() {
        return panelRepNueva;
    }

    public void setPanelReparaciones(PanelReparaciones panelReparaciones) {
        this.panelRepNueva = panelReparaciones;
    }

    public PanelAdVehiculos getPanelVehiculos() {
        return panelVehiculos;
    }

    public void setPanelVehiculos(PanelAdVehiculos panelVehiculos) {
        this.panelVehiculos = panelVehiculos;
    }

    public PanelPlanillaNueva getPanelPlanillaNueva() {
        return panelPlanillaNueva;
    }

    public void setPanelPlanillaNueva(PanelPlanillaNueva panelPlanillaNueva) {
        this.panelPlanillaNueva = panelPlanillaNueva;
    }

    public PanelVerPlanillas getPanelVerPlanillas() {
        return panelVerPlanillas;
    }

    public void setPanelVerPlanillas(PanelVerPlanillas panelVerPlanillas) {
        this.panelVerPlanillas = panelVerPlanillas;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupFiltros;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdCamiones;
    private javax.swing.JButton jButtonAdPagos;
    private javax.swing.JButton jButtonAdPersonas;
    private javax.swing.JButton jButtonFiltrar;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonNuevaPlanilla;
    private javax.swing.JButton jButtonVerNotif;
    private javax.swing.JButton jButtonVerPlanillas;
    private javax.swing.JButton jButtonVerReparaciones;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JRadioButton jRadioButtonCheques;
    private javax.swing.JRadioButton jRadioButtonMantencion;
    private javax.swing.JRadioButton jRadioButtonNotifHoy;
    private javax.swing.JRadioButton jRadioButtonPlanillas;
    private javax.swing.JRadioButton jRadioButtonReparaciones;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableNotificaciones;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean sePuedeCerrar() {
        JLabel label = new JLabelAriel(" ¿DESEA CERRAR EL PROGRAMA? ");
        int valor = JOptionPane.showConfirmDialog(panelPersonas, label, " ¡ATENCIÓN! ", JOptionPane.YES_NO_OPTION);
        return valor == JOptionPane.YES_OPTION;
    }

    @Override
    public void onFocus() {
        //¿Tendrían que cargarse las notificaciones de la Tabla? - Someday
        this.notificaciones = new ArrayList<>();
        this.estado.cargarNotifificaciones(); 
    }
}
