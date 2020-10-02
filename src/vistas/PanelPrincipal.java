/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import com.toedter.calendar.JTextFieldDateEditor;
import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Cheque;
import modelo.JLabelAriel;
import modelo.Notificador;
/**
 *
 * @author ari_0
 */
public class PanelPrincipal extends JPanelCustom {
    
    private DefaultTableModel tablaNotificaciones;
    private ControladorPrincipal controlador;
    private PanelAdPersonas panelPersonas;
    private PanelRepNueva panelRepNueva;
    private PanelAdVehiculos panelVehiculos;
    private PanelPlanillaNueva panelPlanillaNueva;
    private PanelVerPlanillas panelVerPlanillas;
    private PanelAdPagos panelAdPagos;
    private ArrayList<Notificador> cheques;
    private ArrayList<Notificador> reparaciones;
    private ArrayList<Notificador> planillas;
  //  private ArrayList<Notificador> clientes;  -- Todavía no estoy seguro
    
    public PanelPrincipal() {
        tablaNotificaciones = new DefaultTableModel(null, getColumnas()){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };

        initComponents();
        this.jTableNotificaciones.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.jTableNotificaciones.getColumnModel().getColumn(0).setMinWidth(140);
        this.jTableNotificaciones.getColumnModel().getColumn(0).setMaxWidth(160);
        this.jTableNotificaciones.getColumnModel().getColumn(1).setMinWidth(300);
        this.jTableNotificaciones.getColumnModel().getColumn(2).setMaxWidth(220);
        this.jTableNotificaciones.getColumnModel().getColumn(2).setMinWidth(180);
        controlador = ControladorPrincipal.getInstancia(); //De esta manera uso solo un controlador
        //Creando todos los paneles - al momento de pasarlos al controlador siempre es la misma instancia
        // --> Podemos controlar que no aparezcan más de una vez la misma instancia
        this.panelPersonas = new PanelAdPersonas();
        this.panelRepNueva = new PanelRepNueva();
        this.panelVehiculos = new PanelAdVehiculos();
        this.panelPlanillaNueva = new PanelPlanillaNueva();
        this.panelVerPlanillas = new PanelVerPlanillas(); 
        
        //añadirNotifDePrueba();  //Para poder mostrar dos notificaciones de ejemplo a Leo
        iniciarEnComun();
    }

    
    private String[] getColumnas() {
        String columna[] = new String[]{"Tipo Notif.", "Descripción", "Camión"};
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
    
    private void iniciarEnComun(){
        //Cosas para iniciar en Constructor PanelPrincipal()
        JTextFieldDateEditor editor = (JTextFieldDateEditor) this.jDateChooserFiltro.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) this.jDateChooserFiltro.getDateEditor();
        editor.setEditable(false);
        this.jRadioButtonTodasLasNotif.setSelected(true);
        this.cargarNotificadores();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFiltros = new javax.swing.ButtonGroup();
        jButtonAdCamiones = new javax.swing.JButton();
        jButtonAdPersonas = new javax.swing.JButton();
        jButtonNuevaPlanilla = new javax.swing.JButton();
        jButtonVerReparaciones = new javax.swing.JButton();
        jButtonVerPlanillas = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableNotificaciones = new javax.swing.JTable();
        jButtonAdPagos = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jRadioButtonHoy = new javax.swing.JRadioButton();
        jRadioButtonNose = new javax.swing.JRadioButton();
        jRadioButtonTodasLasNotif = new javax.swing.JRadioButton();
        jDateChooserFiltro = new com.toedter.calendar.JDateChooser();

        setPreferredSize(new java.awt.Dimension(1000, 533));

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

        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTableNotificaciones.setFont(new java.awt.Font("Microsoft YaHei UI Light", 1, 16)); // NOI18N
        jTableNotificaciones.setModel(tablaNotificaciones);
        jScrollPane1.setViewportView(jTableNotificaciones);

        jButtonAdPagos.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jButtonAdPagos.setText("<html><p>Administar</p><p>&nbsp;&nbsp;&nbsp;Pagos</p></html>");
        jButtonAdPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdPagosActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton1.setText("Filtrar");

        buttonGroupFiltros.add(jRadioButtonHoy);
        jRadioButtonHoy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonHoy.setText("Fecha de Hoy");

        buttonGroupFiltros.add(jRadioButtonNose);
        jRadioButtonNose.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonNose.setText("Desde Fecha:");

        buttonGroupFiltros.add(jRadioButtonTodasLasNotif);
        jRadioButtonTodasLasNotif.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonTodasLasNotif.setText("Todas las notificaciones");

        jDateChooserFiltro.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jDateChooserFiltro.setMinimumSize(new java.awt.Dimension(155, 27));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAdPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jButtonAdCamiones, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jButtonNuevaPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jButtonVerReparaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jButtonVerPlanillas, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jButtonAdPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonHoy)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonNose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooserFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(jRadioButtonTodasLasNotif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonAdCamiones)
                        .addComponent(jButton1)
                        .addComponent(jRadioButtonHoy)
                        .addComponent(jRadioButtonNose)
                        .addComponent(jRadioButtonTodasLasNotif)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAdPersonas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonNuevaPlanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonVerReparaciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonVerPlanillas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAdPagos)
                        .addGap(0, 196, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cargarNotificadores(){
        //Este método carga los ArrayList de Notificadores.
        this.cheques = cargarCheques(); //Tanto los comunes como los diferidos
        Arrays.sort(this.cheques.toArray());
        Collections.sort(cheques);
        for (Notificador cheque : this.cheques) {
            System.out.println(cheque.getTipo());  //Funciona - hay que quitarlo o modificarlo
        }
    }
    
    private ArrayList cargarCheques(){
        // Carga los cheques que están listos para cobrar, los que tienen menos días para cobrar tienen más prioridad
        ArrayList<Notificador> chequesComunes, chequesDiferidos, todosLosCheques = new ArrayList<>();
        chequesComunes = obtenerChequeComun();
        chequesDiferidos = obtenerChequesDiferidos();
        chequesDiferidos.addAll(chequesComunes);
        todosLosCheques = chequesDiferidos;
        //Faltaría obtener los cheques con pago diferido
        return todosLosCheques;
    }
    
    private ArrayList<Notificador> obtenerChequeComun(){
        //Cargo los cheques sin fecha de cobro (Cheques comunes)
        LocalDate fechaHoy = LocalDate.now();
        ArrayList<Notificador> chequesComunes = new ArrayList<>();
        LocalDate fechaEmision, fechaCobro;
        int prioridad;
        //Query para cargar los cheques sin fecha de cobro. Estos se pueden cobrar 30 días después de emitidos
            String query = "SELECT k.idcheque, k.fecha_emision, k.numerocheque, p.nombre, p.apellido FROM cheque AS k "
                    + "NATURAL JOIN forma_de_pago AS f INNER JOIN planilla AS pl ON pl.idplanilla = f.idplanilla INNER JOIN cliente AS c "
                    + "ON pl.idcliente = c.idcliente INNER JOIN persona as p ON c.idpersona = p.idpersona WHERE cobrado = false AND "
                    + "fecha_cobro is null ORDER BY k.fecha_emision";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){ //Cargo todos los cheques
                //Además de esta consulta hay que hacer una consulta más por los cheques que tienen fecha de cobro
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaEmision = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //Si lo pasaba directamente explota
                prioridad = (int) DAYS.between(fechaEmision, fechaHoy);
                Cheque cheque = new Cheque(rs.getInt(1), prioridad, rs.getLong(3), "cheque común"); //idcheque, prioridad, numeroDeCheque
                chequesComunes.add(cheque);  //Se cargan primero los que tienen más prioridad
            } 
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al obtener cheques comunes: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
        return chequesComunes;
    }
    
        private ArrayList<Notificador> obtenerChequesDiferidos(){
        //Cargo los cheques sin fecha de cobro (Cheques comunes)
        LocalDate fechaHoy = LocalDate.now();
        ArrayList<Notificador> chequesComunes = new ArrayList<>();
        LocalDate fechaEmision, fechaCobro;
        int prioridad;
        //Query para cargar los cheques sin fecha de cobro. Estos se pueden cobrar 30 días después de emitidos
            String query = "SELECT k.idcheque, k.fecha_cobro, k.numerocheque, p.nombre, p.apellido FROM cheque AS k NATURAL JOIN "
                    + "forma_de_pago AS f INNER JOIN planilla AS pl ON pl.idplanilla = f.idplanilla INNER JOIN cliente AS c ON "
                    + "pl.idcliente = c.idcliente INNER JOIN persona as p ON c.idpersona = p.idpersona WHERE cobrado = false AND "
                    + "fecha_cobro is not null ORDER BY k.fecha_cobro";
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){ //Cargo todos los cheques
                //Además de esta consulta hay que hacer una consulta más por los cheques que tienen fecha de cobro
                Date fecha = new Date(rs.getDate(2).getTime());
                fechaEmision = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                prioridad = (int) DAYS.between(fechaEmision, fechaHoy);
                Cheque cheque = new Cheque(rs.getInt(1), prioridad, rs.getLong(3), "cheque pago diferido"); //idcheque, prioridad, numeroDeCheque
                chequesComunes.add(cheque);  //Se cargan primero los que tienen más prioridad
            } 
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al obtener cheques diferidos: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN!!", JOptionPane.WARNING_MESSAGE);  
        }
        return chequesComunes;
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
        this.panelRepNueva = new PanelRepNueva();
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

    public PanelAdPersonas getPanelPersonas() {
        return panelPersonas;
    }

    public void setPanelPersonas(PanelAdPersonas panelPersonas) {
        this.panelPersonas = panelPersonas;
    }

    public PanelRepNueva getPanelReparaciones() {
        return panelRepNueva;
    }

    public void setPanelReparaciones(PanelRepNueva panelReparaciones) {
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
    private javax.swing.JButton jButtonNuevaPlanilla;
    private javax.swing.JButton jButtonVerPlanillas;
    private javax.swing.JButton jButtonVerReparaciones;
    private com.toedter.calendar.JDateChooser jDateChooserFiltro;
    private javax.swing.JRadioButton jRadioButtonHoy;
    private javax.swing.JRadioButton jRadioButtonNose;
    private javax.swing.JRadioButton jRadioButtonTodasLasNotif;
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
    }
}
