    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import Atxy2k.CustomTextField.RestrictedTextField;
import controladores.ControladorPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import modelo.ComboItem;
import modelo.JLabelAriel;

/**
 *
 * @author ariel
 */
public class PanelNuevaPersona extends JPanelCustom {

    RestrictedTextField numeroTel;
    ControladorPrincipal controlador;
    private int idPersona = 0; //En 0 significa que estoy agregando un cliente
    private boolean aModificar = false;  //Solo true en la vista a modificar
    //InternalFrameChofer iChofer; falta crear el Panel
    
    public PanelNuevaPersona() {
        initComponents();
        
        inicioEnComun();
    }

    PanelNuevaPersona(int idPersona) {
        //Creación de esta vista para la persona a modificar
        initComponents();
        inicioEnComun();
        this.idPersona = idPersona;
        this.aModificar = true; //Para dsps consultar en los métodos que haga falta saber si la vista es a modificar
        cargarTelefonos();
        cargarDatosModificar();
    }

    private void cargarTelefonos(){
        //Cargo los teléfonos para un cliente a modificar - Con un ComboItem de la forma: (idTelefono, numeroTel)
        this.jComboBoxTelefonos.removeAllItems(); //Por si ya tenía items
        ComboItem cmItem;
        long numTel;
        int idtelefono;
        String tipo_tel;
        String consulta = "SELECT t.idtelefono, t.numero, t.tipo_tel FROM telefono AS t WHERE t.idpersona = "+this.idPersona+" ";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                idtelefono = rs.getInt(1);
                numTel = rs.getLong(2);
                tipo_tel = rs.getString(3);
                cmItem = new ComboItem(""+idtelefono, tipo_tel+" "+numTel);
                this.jComboBoxTelefonos.addItem(cmItem); //Con el idtelefono como llave y tipo_tel + numTel como número
            }
        
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar Teléfonos: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        
    }
    
    private void cargarCamionesChofer(){
        try {
            //Carga en el jComboBox los Vh de los cuáles la persona "this.idPersona" es chofér.
            //Los carga tipo ComboItem de la forma: (idchofer, "apodo", marca, patente) // En principio entrarían esos datos. 
            //No activo las casillas si no encuentro ningún camión manejado por esta persona
            String marca, apodo, modelo, patente;
            int idVh;
            this.jComboBoxVh.removeAllItems();
            ComboItem cmItem;
            Statement st = this.controlador.obtenerConexion().createStatement();
            String consulta = "SELECT v.idvehiculo, c.apodo, v.marca, v.modelo, v.patente FROM chofer AS c INNER JOIN persona AS p " 
                    + "ON p.idpersona = c.idpersona INNER JOIN maneja as m ON m.idchofer = c.idchofer INNER JOIN"
                    + " vehiculo AS v ON v.idvehiculo = m.idvehiculo WHERE c.idpersona = "+this.idPersona+" AND m.fecha_fin is null";
            //Ver insercciones para hacer en la Base de Datos. ------ Leer cuaderno
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                idVh = rs.getInt(1);
                apodo = rs.getString(2);
                marca = rs.getString(3);
                modelo = rs.getString(4);
                patente = rs.getString(5);
                cmItem = new ComboItem(""+idVh, apodo+" maneja: "+marca+" "+modelo+" "+patente);
                this.jComboBoxVh.addItem(cmItem);
            }
            
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar Camiones " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void cargarCamionesDueño(){
        //Cargo en el jComboBox correspondiente los camiones de los cuáles la persona a modificar es dueño.
        try {
            //Carga los Vh tipo ComboItem de la forma: (idcliente, marca + modelo + patente) // En principio entrarían esos datos. 
            //No activo las casillas si no encuentro ningún camión manejado por esta persona
            String marca, apodo, modelo, patente;
            int idVh;
            ComboItem cmItem;
            this.jComboBoxCli.removeAllItems();
            Statement st = this.controlador.obtenerConexion().createStatement();
            String consulta = "SELECT v.idvehiculo, v.marca, v.modelo, v.patente FROM cliente AS c INNER JOIN persona AS p " 
                    + "ON p.idpersona = c.idpersona INNER JOIN vehiculo AS v ON v.idduenio = c.idcliente "
                    + "WHERE c.idpersona = "+this.idPersona+" ";
            //Ver insercciones para hacer en la Base de Datos. ------ Leer cuaderno
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                idVh = rs.getInt(1);
                marca = rs.getString(2);
                modelo = rs.getString(3);
                patente = rs.getString(4);
                cmItem = new ComboItem(""+idVh , marca +" "+ modelo +" "+ patente);
                this.jComboBoxCli.addItem(cmItem);
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar los camiones en el ComboBox:" + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void cargarDatosModificar() {
        //Cargaría los datos (Nombre, Localidad, Observaciones) tomando el atributo "idPersona" 
        cargarDatosPersonaModificar();
        //------PARA CHOFER
        this.cargarCamionesChofer();
        this.jCheckBoxChofer.setSelected(esChofer()); //Se configura según sea chofer (true) o no (false);
        this.activarDesactivarCasillasChofer(esChofer());
        
        //-----PARA DUEÑO
        this.cargarCamionesDueño();
        this.jCheckBoxCliente.setSelected(esDueño());
        this.activarDesactivarCasillasDueño(esDueño());
        
    }
    
    private boolean esChofer(){
        boolean valor = false;
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT c.idchofer from chofer as c natural join persona as p "
                    + "WHERE p.idpersona = '"+this.idPersona+"' ");
            while(rs.next()){
                //Es chofer
                valor = true; //porque se encontró que es chofer y no necesariamente tiene que manejar algún camión
            }
        } catch (SQLException ex) {
            System.out.println("Error en la consulta de si es chofer: "+ex.getMessage());
        }
        return valor;
    }
    
    private boolean esDueño(){
        //Retorna true o false dependiendo si la persona es Cliente o no
        boolean valor = false;
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT c.idcliente FROM cliente AS c NATURAL JOIN persona AS p "
                    + "WHERE p.idpersona = '"+this.idPersona+"'");
            while(rs.next()){
                valor = true; //porque se encontró que es Cliente y no necesariamente tiene que ser dueño de un camión
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al comprobar si la persona es Dueño: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        return valor;
    }
    
    private void cargarDatosPersonaModificar(){
        try {
            //Carga los datos de una persona a modificar en al vista. Exeptuando teléfonos, y datos de cliente o chofer
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.nombre, p.apellido, p.observaciones, p.localidad FROM persona AS p "
                    + "WHERE p.idpersona = '"+this.idPersona+"' ");
            while(rs.next()){ 
                this.jTextFieldNombre.setText(rs.getString(1));
                this.jTextFieldApellido.setText(rs.getString(2));
                this.jTextFieldLocalidad.setText(rs.getString(4));
                this.jTextFieldObs.setText(rs.getString(3));
            }
            
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar datos de la Persona: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        
    }
    
    private void inicioEnComun(){
        //Métodos para iniciar en común siendo la vista para persona a agregar o modificar
        this.controlador = ControladorPrincipal.getInstancia();
        numeroTel = new RestrictedTextField(this.jTextFieldNumero);
        numeroTel.setOnlyNums(true);  //No le permito al número de teléfono otros caracteres distintos de numeros
        desactivarCamposDeChoferYCliente();  //Se desactivan en la creación de esta vista y solo se activan si el usuario lo desea
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameAgregarTel = new javax.swing.JFrame();
        jLabelTitulo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jTextFieldTipoTel = new javax.swing.JTextField();
        jButtonAgregar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jFrameChofer = new javax.swing.JFrame();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldApodo = new javax.swing.JTextField();
        jButtonGuardarChofer = new javax.swing.JButton();
        jButtonCancelarChofer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescripcion = new javax.swing.JTextArea();
        jLabelNombreChofer = new javax.swing.JLabel();
        jLabelApellidoChofer = new javax.swing.JLabel();
        jFrameCliente = new javax.swing.JFrame();
        jLabelNombreCli = new javax.swing.JLabel();
        jLabelApeCli = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldDocCli = new javax.swing.JTextField();
        jCheckBoxAgenteR = new javax.swing.JCheckBox();
        jButtonGuardarCli = new javax.swing.JButton();
        jButtonCancelarCli = new javax.swing.JButton();
        jLabelDescripcion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDescripCli = new javax.swing.JTextArea();
        jCheckBoxNotificar = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jFrameInfo = new javax.swing.JFrame();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelInfoVh = new javax.swing.JLabel();
        jButtonAceptar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jTextFieldApellido = new javax.swing.JTextField();
        jTextFieldLocalidad = new javax.swing.JTextField();
        jTextFieldObs = new javax.swing.JTextField();
        jButtonGuardar = new javax.swing.JButton();
        jButtonCancelarAdPer = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxTelefonos = new javax.swing.JComboBox<>();
        jButtonAgregarTel = new javax.swing.JButton();
        jButtonBorrarTel = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxVh = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxCli = new javax.swing.JComboBox<>();
        jButtonVerVhCh = new javax.swing.JButton();
        jButtonAgregarCli = new javax.swing.JButton();
        jCheckBoxChofer = new javax.swing.JCheckBox();
        jCheckBoxCliente = new javax.swing.JCheckBox();
        jButtonDatosCliente = new javax.swing.JButton();
        jButtonDatosChofer = new javax.swing.JButton();
        jButtonInfo = new javax.swing.JButton();

        jFrameAgregarTel.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrameAgregarTel.setLocationByPlatform(true);
        jFrameAgregarTel.setResizable(false);
        jFrameAgregarTel.setSize(new java.awt.Dimension(505, 345));

        jLabelTitulo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTitulo.setText("Agregar Teléfono");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Número:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Tipo teléfono:");

        jTextFieldNumero.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTextFieldTipoTel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButtonAgregar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jButtonCancelar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Ej: personal, trabajo, fijo,... (OPCIONAL)");

        javax.swing.GroupLayout jFrameAgregarTelLayout = new javax.swing.GroupLayout(jFrameAgregarTel.getContentPane());
        jFrameAgregarTel.getContentPane().setLayout(jFrameAgregarTelLayout);
        jFrameAgregarTelLayout.setHorizontalGroup(
            jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameAgregarTelLayout.createSequentialGroup()
                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameAgregarTelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonAgregar))
                    .addGroup(jFrameAgregarTelLayout.createSequentialGroup()
                        .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrameAgregarTelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldTipoTel)
                                    .addComponent(jTextFieldNumero)))
                            .addGroup(jFrameAgregarTelLayout.createSequentialGroup()
                                .addGap(184, 184, 184)
                                .addComponent(jLabelTitulo)))
                        .addGap(0, 79, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jFrameAgregarTelLayout.setVerticalGroup(
            jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameAgregarTelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldTipoTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(jFrameAgregarTelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAgregar)
                    .addComponent(jButtonCancelar))
                .addContainerGap())
        );

        jFrameChofer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrameChofer.setAlwaysOnTop(true);
        jFrameChofer.setLocationByPlatform(true);
        jFrameChofer.setResizable(false);
        jFrameChofer.setSize(new java.awt.Dimension(717, 327));

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel9.setText("Apodo:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setText("Descripción:");

        jTextFieldApodo.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonGuardarChofer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonGuardarChofer.setText("Guardar");
        jButtonGuardarChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarChoferActionPerformed(evt);
            }
        });

        jButtonCancelarChofer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonCancelarChofer.setText("Cancelar");
        jButtonCancelarChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarChoferActionPerformed(evt);
            }
        });

        jScrollPane1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextAreaDescripcion.setColumns(20);
        jTextAreaDescripcion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescripcion);

        jLabelNombreChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelNombreChofer.setText("jLabel13");

        jLabelApellidoChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelApellidoChofer.setText("jLabel13");

        javax.swing.GroupLayout jFrameChoferLayout = new javax.swing.GroupLayout(jFrameChofer.getContentPane());
        jFrameChofer.getContentPane().setLayout(jFrameChoferLayout);
        jFrameChoferLayout.setHorizontalGroup(
            jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameChoferLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameChoferLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancelarChofer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonGuardarChofer))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameChoferLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrameChoferLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(61, 61, 61)
                        .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrameChoferLayout.createSequentialGroup()
                                .addComponent(jLabelNombreChofer)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelApellidoChofer)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldApodo))))
                .addContainerGap())
        );
        jFrameChoferLayout.setVerticalGroup(
            jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameChoferLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreChofer)
                    .addComponent(jLabelApellidoChofer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldApodo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jFrameChoferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarChofer)
                    .addComponent(jButtonCancelarChofer))
                .addContainerGap())
        );

        jFrameCliente.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrameCliente.setLocationByPlatform(true);
        jFrameCliente.setPreferredSize(new java.awt.Dimension(705, 425));
        jFrameCliente.setSize(new java.awt.Dimension(700, 500));

        jLabelNombreCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelNombreCli.setText("jLabel13");

        jLabelApeCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelApeCli.setText("jLabel14");

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setText("CUIT/CUIL:");

        jTextFieldDocCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jCheckBoxAgenteR.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxAgenteR.setText(" Agente de Retención");

        jButtonGuardarCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonGuardarCli.setText("Guardar");
        jButtonGuardarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarCliActionPerformed(evt);
            }
        });

        jButtonCancelarCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCancelarCli.setText("Cancelar");
        jButtonCancelarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarCliActionPerformed(evt);
            }
        });

        jLabelDescripcion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelDescripcion.setText("Descripción:");

        jTextAreaDescripCli.setColumns(20);
        jTextAreaDescripCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextAreaDescripCli.setRows(5);
        jScrollPane2.setViewportView(jTextAreaDescripCli);

        jCheckBoxNotificar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxNotificar.setText("Notificar");

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setText("Esta casilla sirve para indicar si se notifica el Estado de CC del cliente");

        javax.swing.GroupLayout jFrameClienteLayout = new javax.swing.GroupLayout(jFrameCliente.getContentPane());
        jFrameCliente.getContentPane().setLayout(jFrameClienteLayout);
        jFrameClienteLayout.setHorizontalGroup(
            jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameClienteLayout.createSequentialGroup()
                        .addComponent(jButtonCancelarCli)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGuardarCli))
                    .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jFrameClienteLayout.createSequentialGroup()
                            .addComponent(jLabelNombreCli)
                            .addGap(18, 18, 18)
                            .addComponent(jLabelApeCli))
                        .addGroup(jFrameClienteLayout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(18, 18, 18)
                            .addComponent(jTextFieldDocCli, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jCheckBoxAgenteR)
                    .addGroup(jFrameClienteLayout.createSequentialGroup()
                        .addComponent(jCheckBoxNotificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17))
                    .addGroup(jFrameClienteLayout.createSequentialGroup()
                        .addComponent(jLabelDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jFrameClienteLayout.setVerticalGroup(
            jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreCli)
                    .addComponent(jLabelApeCli))
                .addGap(25, 25, 25)
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldDocCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxAgenteR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxNotificar)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDescripcion)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(jFrameClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelarCli)
                    .addComponent(jButtonGuardarCli))
                .addContainerGap())
        );

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setSize(new java.awt.Dimension(710, 450));

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Esta vista sirve para administrar los siguientes aributos de una persona:");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setText(". Teléfonos de la persona");

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel16.setText(". Si es Chofer y sus datos");

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel18.setText(". Si es Cliente y sus datos");

        jLabelInfoVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelInfoVh.setForeground(new java.awt.Color(204, 0, 51));
        jLabelInfoVh.setText("<html><p>En esta Vista no se pueden quitar ni agregar vehículos de los cuales la persona puede ser el chofer o el dueño. <br>Para realizar estas acciones dirijase a la opci&oacute;n de Administrar Camiones en el Panel Principal</p></html>");

        jButtonAceptar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel19.setText(". Nombre, apellido, localidad y observaciones");

        javax.swing.GroupLayout jFrameInfoLayout = new javax.swing.GroupLayout(jFrameInfo.getContentPane());
        jFrameInfo.getContentPane().setLayout(jFrameInfoLayout);
        jFrameInfoLayout.setHorizontalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAceptar))
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(jLabelInfoVh)
                            .addComponent(jLabel19))
                        .addGap(0, 140, Short.MAX_VALUE)))
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
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addGap(16, 16, 16)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInfoVh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                .addComponent(jButtonAceptar)
                .addContainerGap())
        );

        setAutoscrolls(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(829, 484));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Nombre:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setText("Apellido o Razón Social: ");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel3.setText("Localidad:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel4.setText("Observaciones:");

        jTextFieldNombre.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldApellido.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldLocalidad.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldObs.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonGuardar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jButtonCancelarAdPer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCancelarAdPer.setText("Cancelar");
        jButtonCancelarAdPer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarAdPerActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setText("Teléfonos:");

        jComboBoxTelefonos.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jButtonAgregarTel.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAgregarTel.setText("Agregar Teléfono");
        jButtonAgregarTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarTelActionPerformed(evt);
            }
        });

        jButtonBorrarTel.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonBorrarTel.setText("Borrar");
        jButtonBorrarTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarTelActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setText("Chofer de:");

        jComboBoxVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setText("Dueño de:");

        jComboBoxCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonVerVhCh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonVerVhCh.setText("Ver Camión");
        jButtonVerVhCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerVhChActionPerformed(evt);
            }
        });

        jButtonAgregarCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAgregarCli.setText("Ver Camión");
        jButtonAgregarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarCliActionPerformed(evt);
            }
        });

        jCheckBoxChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxChofer.setText("Es Chofer");
        jCheckBoxChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxChoferActionPerformed(evt);
            }
        });

        jCheckBoxCliente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jCheckBoxCliente.setText("Es Cliente");
        jCheckBoxCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxClienteActionPerformed(evt);
            }
        });

        jButtonDatosCliente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonDatosCliente.setText("Ver Datos");
        jButtonDatosCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatosClienteActionPerformed(evt);
            }
        });

        jButtonDatosChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonDatosChofer.setText("Ver Datos");
        jButtonDatosChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatosChoferActionPerformed(evt);
            }
        });

        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info-icon2.png"))); // NOI18N
        jButtonInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldApellido)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonCancelarAdPer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonGuardar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1))
                                .addGap(121, 121, 121)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNombre)
                                    .addComponent(jTextFieldLocalidad)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldObs))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBoxTelefonos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonAgregarTel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBorrarTel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBoxVh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(1, 1, 1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonVerVhCh)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxChofer)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonDatosChofer)
                                        .addGap(0, 348, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonAgregarCli)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxCliente)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonDatosCliente)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(5, 5, 5))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregarTel)
                    .addComponent(jButtonBorrarTel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBoxVh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVerVhCh)
                    .addComponent(jCheckBoxChofer)
                    .addComponent(jButtonDatosChofer))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jComboBoxCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAgregarCli)
                    .addComponent(jCheckBoxCliente)
                    .addComponent(jButtonDatosCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonGuardar)
                        .addComponent(jButtonCancelarAdPer))
                    .addComponent(jButtonInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarTelActionPerformed
        //Llamar a una vista para agregar el teléfono
        //Acá tendría que saber si es una persona a modificar o agregar
        String nombre = this.jTextFieldNombre.getText();
        if(!nombre.equals("")){  // Si el usuario ya ingreso un nombre se puede continuar agregando un teléfono
            if(this.idPersona == 0){ //Es una vista de "NuevaPersona" por lo que debo guardar la persona antes para dsps agregarle el tel.
                grabarPersonaEnBdD(this.idPersona);
                this.jFrameAgregarTel.setVisible(true);
            }
            else // EL "else" del if de idPersona quiere decir que es una persona a modificar
                this.jFrameAgregarTel.setVisible(true); //Se puede agregar el teléfono tranquilamente
        }
        else{
            JLabel label = new JLabelAriel("Debe ingresar un Nombre");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
            
    }//GEN-LAST:event_jButtonAgregarTelActionPerformed

    private void grabarPersonaEnBdD(int idPer){
        // Método creado para guardar una persona nueva para luego poder agregarle el teléfono. También programarlo para
        // concretar el modificado de una persona cuando se requiera
        // ---ATENCIÓN--- SIEMPRE SE DEBE GUARDAR EL idPersona en la variable correspondiente después de guardado (actualización)
        if(idPer == 0)
            grabarNuevaPersona();
        else
            actualizarPersona();

    }
    
    private long ultimoIdPersona() throws SQLException{
        //Retorna exactamente el id más grande que encuentre en la Base de Datos
        long ultimoId = 0;
        String consulta = "SELECT MAX(idpersona) FROM persona";
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                ultimoId = rs.getLong(1);
            }
        return ultimoId;
    }
    
    private void grabarNuevaPersona(){
        String nombre = this.jTextFieldNombre.getText();
        String apellido = this.jTextFieldApellido.getText();
        String localidad = this.jTextFieldLocalidad.getText();
        String obs = this.jTextFieldObs.getText();
        long idPer;
        Connection co = this.controlador.obtenerConexion();
        try {
            //Graba o Actualiza la Base de Datos dependiendo si la persona es nueva o a modificar
            co.setAutoCommit(false);
            idPer = this.ultimoIdPersona();
            PreparedStatement ps1 = this.controlador.obtenerConexion().prepareStatement("INSERT INTO persona VALUES "
                    + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, (int) (idPer+1));
            ps1.setString(2, nombre);
            if(!apellido.equals("")) // Si no es nulo puedo grabarlo normal
                ps1.setString(3, apellido);
            else
                ps1.setNull(3, java.sql.Types.VARCHAR);
            if(!localidad.equals(""))
                ps1.setString(5, localidad);
            else
                ps1.setNull(5, java.sql.Types.VARCHAR);
            if (!obs.equals("")) {
                ps1.setString(4, obs);
            } else {
                ps1.setNull(4, java.sql.Types.VARCHAR);
            }
            ps1.executeUpdate();
            try (ResultSet generatedKeys = ps1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idPersona = generatedKeys.getInt(1);  //Obtengo el idPersona generado en la inserción
                }
                co.commit(); 
            } catch (SQLException ex1) {
                System.out.println("Error al grabar persona o obtener ultimo id: " + ex1.getMessage());
                try{
                    co.rollback();
                } catch(SQLException ex2){
                    System.out.println("Error al intentar corregir Base de Datos: " + ex1.getMessage());
                }  //No hago bloque "finally" porque siempre falla
            }
            
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar Reparaciones en la Tabla: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void actualizarPersona(){
        //Actualizo la persona con el "idPer" guardado en la variable
        //Algunos atributos como 'es chofer' no se actualizan sino que se trabaja esto desde su propio ActionPerformed
        String nombre = this.jTextFieldNombre.getText();
        String apellido = this.jTextFieldApellido.getText();
        String localidad = this.jTextFieldLocalidad.getText();
        String obs = this.jTextFieldObs.getText();
        try {
            PreparedStatement ps2 = this.controlador.obtenerConexion().prepareStatement("UPDATE persona SET nombre = ?, apellido = ?, "
                    + "observaciones = ?, localidad = ? WHERE idpersona = ? ");
            
            ps2.setString(1, nombre);
            if(!apellido.equals("")) // Si no es nulo puedo grabarlo normal
                ps2.setString(2, apellido);
            else
                ps2.setNull(2, java.sql.Types.VARCHAR);
            if(!localidad.equals(""))
                ps2.setString(3, localidad);
            else
                ps2.setNull(3, java.sql.Types.VARCHAR);
            if (!obs.equals("")) {
                ps2.setString(4, obs);
            } else {
                ps2.setNull(4, java.sql.Types.VARCHAR);
            }   
            ps2.setInt(5, this.idPersona);
            ps2.executeUpdate();
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al actualizar persona: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        this.jFrameAgregarTel.dispose();  //El "dispose" en el "Design" de Netbeans indique que se cambia
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
    //Método para agregar un teléfono al cliente (La persona acá siempre está guardada) y el id en la variable "idPersona"
        
        if(!numeroTel.equals("")){  //tipoTel puede ser nulo en la Base de datos
            if(grabarTelefono(this.idPersona)){  //retorna "true" si grabo bien
                JLabel label = new JLabelAriel("Teléfono grabado");
                JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "MENSAJE", JOptionPane.OK_OPTION);
                this.cargarTelefonos(); //Recargo los teléfonos para que aparezca el nuevo en el JComboBox
                this.jFrameAgregarTel.dispose();
            }
           //Donde iría el "else" igual no hace falta nada
        } else {
            JLabel label = new JLabelAriel("Debe ingresar un número de teléfono");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonCancelarAdPerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarAdPerActionPerformed
        //Cancelo el agregado de la persona: ¿Debería borrarse acá si la persona hubiera sido agregada? - Sí
        //Borrar la persona borra automáticamente los teléfonos que pudieran estar asociados a la misma. Característa "cascade" en BdD
        if(this.idPersona != 0) // Ya esta guardada en la Base de Datos (borrar dependiendo de los siguiente)
            if(this.aModificar) // Es una persona a modificar por lo que no hay que borrarla de la BdD
                this.controlador.cerrarPanelSeleccionado(); // Probar que tal
            else{
                borrarPersona(this.idPersona);  // Es una nueva persona --> hay que borrarla
                this.controlador.cerrarPanelSeleccionado(); //Cierro
            }
        else  //No hay persona agregada
            this.controlador.cerrarPanelSeleccionado();
    }//GEN-LAST:event_jButtonCancelarAdPerActionPerformed

    private void borrarPersona(int idPersona){
        try {
            //Borra una persona por su id
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM persona WHERE idpersona = ?");
            ps.setInt(1, idPersona);
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al borrar persona " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
    }
    
    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        //Guardar la Persona - En realidad ya debería estar guardado? - Fijarse en el atributo "idPersona"
        if(this.jTextFieldNombre.getText().equals(""))
            this.jTextFieldNombre.setText(".");
        if(this.idPersona!= 0){ //Está guardado, toca actualizar
            this.actualizarPersona();
            JLabel label = new JLabelAriel("Datos actualizados");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
            this.controlador.cerrarPanelSeleccionado();
        }
        else{ //No está guardado
            this.grabarNuevaPersona();  //No hay id
            JLabel label = new JLabelAriel("Nueva persona agregada");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
            this.controlador.cerrarPanelSeleccionado();
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed
    
    private void jButtonBorrarTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarTelActionPerformed
        // Borro teléfono seleccionado, tomo la clave del ComboItem que es el "idpersona"
        ComboItem cmItem;
        if(this.jComboBoxTelefonos.getSelectedIndex() != -1){ //hay item seleccionado
            cmItem = (ComboItem) this.jComboBoxTelefonos.getSelectedItem(); //Probar que pasa cuando es null
            borrarTelefono(cmItem.getKey());
            this.cargarTelefonos();
        }
        else{
            JLabel label = new JLabelAriel("Seleccione un teléfono");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ATENCIÓN", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonBorrarTelActionPerformed

    private void jCheckBoxChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxChoferActionPerformed
        // Activo o desactivo las casillas de Chofer según esté seteado el CheckBox
        if (this.jCheckBoxChofer.isSelected()) {  //Tengo que activar los campos referidos a Chofer
            activarDesactivarCasillasChofer(true);
            //Tengo que saber si es una nueva persona (sin idpersona) o es a modificar para cargar, o no, datos en la nueva vista
            if (this.idPersona == 0) { //Nueva persona --> Tengo que abrir la vista (sin nada) pero tengo que tener la persona guardada
                if (!this.jTextFieldNombre.getText().equals("")) {
                    grabarNuevaPersona(); //Solo se graba si está el nombre escrito y si es nuevo persona (if de arriba)
                    this.cargarDatosJFrameChofer();
                    this.jFrameChofer.setVisible(true);
                } else {  //La persona no existe en la Base de datos y en la vista no está el nombre
                    JLabel label = new JLabelAriel("No se puede configurar chofer si la persona no tiene nombre");
                    JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
                    this.jCheckBoxChofer.setSelected(false);
                }
            } else { //Es una persona a modificar a la cuál le activamos la casilla de chofer
                this.cargarDatosJFrameChofer(); //Carga los datos con la variable this.idepersona
                JLabel label = new JLabelAriel("Agregue los datos del chofer, el Apodo es obligatorio");
                JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
                this.jFrameChofer.setVisible(true);
            }

        } else {  //Tengo que desactivar los campos referidos a Chofer
            activarDesactivarCasillasChofer(false); //Estoy desactivando una persona que era chofer pero ahora no lo sería
            //Acá habría que borrar el chofer y los vehículos¿? ¿Y las planillas asociadas?
            if(this.jComboBoxVh.getItemCount() > 0){ // La persona tiene camiones asociados
                JLabel label = new JLabelAriel("No se puede destildar esta opción porque el chofer tiene camiones asociados.");
                JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
                this.jCheckBoxChofer.setSelected(true);
            }
            else{  //La persona no tiene camiones asociados --> Hay que borrar la tupla de la tabla chofer que apunta a la persona
                borrarChofer();
            }
        }
    }//GEN-LAST:event_jCheckBoxChoferActionPerformed

    private void borrarChofer(){
        try {
            //Borro el chofer (tupla) que representa a la persona con id "this.idpersona"
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM chofer WHERE idpersona = ? ");
            ps.setInt(1, this.idPersona);
            ps.executeUpdate();
            JLabel label = new JLabelAriel("La persona ya no es más chofer");
            JOptionPane.showMessageDialog(null, label, "INFO", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al borrar chofer");
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void jCheckBoxClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxClienteActionPerformed
        // Activo o desactivo las casillas de Cliente según esté seteado el CheckBox
        if (this.jCheckBoxCliente.isSelected()) {  //Tengo que activar los campos referidos a Cliente
            activarDesactivarCasillasDueño(true);
            //Tengo que saber si es una nueva persona (sin idPersona) o es a modificar. Para saber si cargar datos en la Vista Cliente
            if (this.idPersona == 0) { //Nueva persona --> Tengo que abrir la vista (sin nada) pero tengo que tener la persona guardada
                if (!this.jTextFieldNombre.getText().equals("")) {
                    grabarNuevaPersona(); //Solo se graba si está el nombre escrito y si es nuevo persona (if de arriba)
                    this.cargarDatosJFrameCliente();
                    this.jFrameCliente.setVisible(true);
                } else {  //La persona no existe en la Base de datos y en la vista no está el nombre
                    JLabel label = new JLabelAriel("No se puede configurar Cliente si la persona no tiene nombre");
                    JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
                    this.jCheckBoxCliente.setSelected(false);
                }
            } else { //Es una persona a modificar a la cuál le activamos la casilla de Cliente
                this.cargarDatosJFrameCliente(); //Carga los datos con la variable this.idPersona
                JLabel label = new JLabelAriel("Agregue los datos del Cliente, el cuil|cuit es obligatorio");
                JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
                this.jFrameCliente.setVisible(true);
            }

        } else {  //Tengo que desactivar los campos referidos a  Cliente
        //Acá habría que borrar el Cliente y los vehículos¿? ¿y las Planillas asociadas?. No se puede borrar un Cliente con Vh asociados
        //La respuesta anterior responde las 2 preguntas            
            if(this.jComboBoxCli.getItemCount() == 0){  //Se puede borrar la persona como Cliente. Entiendo que no interfiere en las planillas
                if(borrarCliente())
                    activarDesactivarCasillasDueño(false); //Estoy desactivando una persona que era cliente pero ahora deja de serlo   
            }
            else{
                JLabel label = new JLabelAriel("No se puede borrar un Cliente con Vehículos asociados.");
                JOptionPane.showMessageDialog(null, label, "INFO", JOptionPane.INFORMATION_MESSAGE);
                this.jCheckBoxCliente.setSelected(true);
            }
        }
    }//GEN-LAST:event_jCheckBoxClienteActionPerformed
    
    private boolean borrarCliente(){
        // Borro el cliente asociado a la persona
        boolean valor = false;
        String query = "DELETE FROM cliente AS c WHERE c.idpersona = '"+this.idPersona+"' ";
        try{
            PreparedStatement st = this.controlador.obtenerConexion().prepareStatement(query);
            st.executeUpdate();
            valor = true;
        }catch(SQLException ex){
            JLabel label = new JLabelAriel("Error al borrar Cliente: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return valor;
    }
    
    private void jButtonGuardarChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarChoferActionPerformed
        //Action Performed de Guardar Chofer. //Comtempla que el mismo ya exista y halla que actualizar los datos
        boolean valor = false;
        String consulta = "SELECT c.apodo from chofer AS c INNER JOIN persona AS p ON c.idpersona = p.idpersona "
                + "WHERE p.idpersona = '"+this.idPersona+"' ";  //Es una consulta para saber si la persona es chofer
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){   //Significa que la persona es chofer
                valor = true;
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al consultar si la persona es chofer");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        if(valor) //Quiere decir que la persona es chofer
            guardarChofer("UPDATE chofer SET apodo = ?, descripcion = ? WHERE idpersona = ?");
        else
            guardarChofer("INSERT INTO chofer VALUES (default, ?, ?, ?)");
    }//GEN-LAST:event_jButtonGuardarChoferActionPerformed

    private void guardarChofer(String consulta){
        if (!this.jTextFieldApodo.getText().equals("")) {  //Action Performed de guardar Chofer | Si el apodo no está vacío -->
            try {
                String apodo, descripcion;
                apodo = this.jTextFieldApodo.getText();
                descripcion = this.jTextAreaDescripcion.getText();
                PreparedStatement pst = this.controlador.obtenerConexion().prepareStatement(consulta);
                pst.setString(1, apodo);
                pst.setString(2, descripcion);
                pst.setInt(3, this.idPersona);
                pst.executeUpdate();
                JLabel label = new JLabelAriel("Datos guardados");
                JOptionPane.showMessageDialog(this.jFrameChofer, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JLabel label = new JLabelAriel("Error al guardar Chofer: "+ex.getMessage());
                JOptionPane.showMessageDialog(this.jFrameChofer, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
            }
            this.jFrameChofer.dispose();
            this.cargarCamionesChofer();  //Para que se actualize el apodo en el JComboBox del Chofer
        } else { //En el lugar del apodo no hay nada y es un dato requerido
            JLabel label = new JLabelAriel("Error al guardar Chofer, el dato APODO es obligatorio");
            JOptionPane.showMessageDialog(this.jFrameChofer, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void jButtonCancelarChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarChoferActionPerformed
        //Action Performed de cancelar Chofer. 
        //Si proviene de Ver Datos este cancelar cierra la vista y pone true en los métodos. En cambio, si viene del ActionPerformed
        // del jCheckBoxChofer --> tenemos que saldrá sin ser Chofer entonces se desactivarán los campos correspondientes
        this.activarDesactivarCasillasChofer(this.esChofer());
        this.jCheckBoxChofer.setSelected(this.esChofer()); //En el VER DATOS también pasa por acá pero sale siendo chofer
        this.jFrameChofer.dispose();
    }//GEN-LAST:event_jButtonCancelarChoferActionPerformed

    private void jButtonDatosChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatosChoferActionPerformed
        //Este Action Performed solo debe permitirse cuando existe el chofer y la persona
        if(this.jCheckBoxChofer.isSelected()){ //Implica que exista la persona y el chofer
            this.jFrameChofer.setVisible(true);
            cargarDatosJFrameChofer();
        }
        else{
            JLabel label = new JLabelAriel("La persona tiene que ser chofer, click en '█ Es Chofer'");
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButtonDatosChoferActionPerformed

    private void jButtonGuardarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarCliActionPerformed
        //Action Performed de guardar datos de Cliente. Copiar formato de métodos de Chofer
        boolean valor = false;
        String consulta = "SELECT c.cuil from cliente AS c INNER JOIN persona AS p ON c.idpersona = p.idpersona "
                + "WHERE p.idpersona = '"+this.idPersona+"' "; //La idea de esta consulta es ver si la persona es Cliente
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){   //Significa que la persona es Cliente
                valor = true;
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al consultar si la persona es cliente");
            JOptionPane.showMessageDialog(this.jFrameCliente, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        if(valor) //Quiere decir que la persona es Cliente
            guardarCliente("UPDATE cliente SET cuil = ?, agente_de_retencion = ?, descripcion = ?, notificar = ? WHERE idpersona = ?");
        else
            guardarCliente("INSERT INTO cliente VALUES (default, ?, ?, ?, ?, ?)");  //el idPersona esta antes de la descripción
    }//GEN-LAST:event_jButtonGuardarCliActionPerformed

    private void guardarCliente(String consulta) {
        //Método para guargar o actualizar Cliente dependiendo del parámetro consulta
        if (!this.jTextFieldDocCli.getText().equals("")) {  //Action Performed de guardar Cliente | Si el cuil no está vacío -->
            try {
                long cuil;
                String descripcion;
                boolean agente_retencion;
                agente_retencion = this.jCheckBoxAgenteR.isSelected();
                cuil = Long.valueOf(this.jTextFieldDocCli.getText());
                descripcion = this.jTextAreaDescripCli.getText();
                PreparedStatement pst = this.controlador.obtenerConexion().prepareStatement(consulta);
                pst.setLong(1, cuil);
                pst.setBoolean(2, agente_retencion);
                if(consulta.contains("UPDATE")){  //Si encontró el subString "UPDATE"
                    pst.setString(3, descripcion);
                    pst.setInt(5, this.idPersona);
                    pst.setBoolean(4, this.jCheckBoxNotificar.isSelected());
                }
                else{
                    pst.setInt(3, this.idPersona);
                    pst.setString(4, descripcion);
                    pst.setBoolean(5, this.jCheckBoxNotificar.isSelected());
                }
                pst.executeUpdate();
                JLabel label = new JLabelAriel("Datos guardados");
                JOptionPane.showMessageDialog(this.jFrameCliente, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JLabel label = new JLabelAriel("Error al guardar Cliente: "+ex.getMessage());
                JOptionPane.showMessageDialog(this.jFrameCliente, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
            }
            this.jFrameCliente.dispose();
            this.cargarCamionesDueño();  //Para que se actualize la info del cliente en el JComboBoxCli del mismo
        } else { //En el lugar del cuil no hay nada y es un dato requerido
            JLabel label = new JLabelAriel("Error al guardar Cliente, el dato CUIL|CUIT es obligatorio");
            JOptionPane.showMessageDialog(this.jFrameCliente, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void jButtonDatosClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatosClienteActionPerformed
       //Este Action Performed solo debe permitirse cuando existe el chofer y la persona
        if(this.jCheckBoxCliente.isSelected()){ //Implica que exista la persona y el chofer
            this.jFrameCliente.setVisible(true);
            cargarDatosJFrameCliente();
        }
        else{
            JLabel label = new JLabelAriel("La persona tiene que ser Cliente, click en 'Es Cliente'");
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonDatosClienteActionPerformed

    private void jButtonCancelarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarCliActionPerformed
        //ActionPerformed de Cancelar de la Vista Cliente. 
        //Si proviene de Ver Datos este cancelar cierra la vista y pone true en los métodos. En cambio, si viene del ActionPerformed
        // del jCheckBoxCliente --> tenemos que saldrá sin ser Cliente entonces se desactivarán los campos correspondientes
        this.activarDesactivarCasillasDueño(this.esDueño());
        this.jCheckBoxCliente.setSelected(this.esDueño()); //En el VER DATOS también pasa por acá pero sale siendo Cliente
        this.jFrameCliente.dispose();
    }//GEN-LAST:event_jButtonCancelarCliActionPerformed

    private void jButtonVerVhChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerVhChActionPerformed
        // Este Action Performed tendría que abrir una nueva pestaña con el panel 'ver/modificar Camión'
        if (this.jComboBoxVh.getSelectedIndex() != -1) {
            String idVh = ((ComboItem) this.jComboBoxVh.getSelectedItem()).getKey();

            NuevoModificarVh mdVh = new NuevoModificarVh(Integer.valueOf(idVh));

            this.controlador.cambiarDePanel(mdVh, "Modificar Vehículo");   //Abre una nueva pestaña para edición del vehículo.
        }
    }//GEN-LAST:event_jButtonVerVhChActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        this.jFrameInfo.dispose();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        // Abre el Frame de JFrameInfo para mostrar info de la vista (Para que sirve para que no sirve).
        this.jFrameInfo.setVisible(true);
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButtonAgregarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarCliActionPerformed
        // Este Action Performed tendría que abrir una nueva pestaña con el panel 'ver/modificar Camión'
        if (this.jComboBoxCli.getSelectedIndex() != -1) {
            String idVh = ((ComboItem) this.jComboBoxCli.getSelectedItem()).getKey();

            NuevoModificarVh mdVh = new NuevoModificarVh(Integer.valueOf(idVh));

            this.controlador.cambiarDePanel(mdVh, "Modificar Vehículo");   //Abre una nueva pestaña para edición del vehículo
        }
    }//GEN-LAST:event_jButtonAgregarCliActionPerformed

    private void cargarDatosJFrameChofer(){
        //Carga los datos de la persona en la sub-vista de Chofer
        Statement st;
        try {
            st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.nombre, p.apellido, c.apodo, c.descripcion FROM persona AS p left join "
                    + "chofer AS c ON c.idpersona = p.idpersona WHERE p.idpersona = '" + this.idPersona + "' ");
            while (rs.next()) {
                this.jLabelNombreChofer.setText(rs.getString(1));
                this.jLabelApellidoChofer.setText(rs.getString(2));
                this.jTextFieldApodo.setText(rs.getString(3));
                this.jTextAreaDescripcion.setText(rs.getString(4));
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar datos de la vista: "+ex.getMessage());
            JOptionPane.showMessageDialog(this.jFrameChofer, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDatosJFrameCliente() {
        //Carga los datos de la persona en la sub-vista de Cliente.
        Statement st;
        try {
            st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.nombre, p.apellido, c.cuil, c.descripcion, c.notificar FROM persona AS p left join "
                    + "cliente AS c ON c.idpersona = p.idpersona WHERE p.idpersona = '" + this.idPersona + "' ");
            while (rs.next()) {
                this.jLabelNombreCli.setText(rs.getString(1));
                this.jLabelApeCli.setText(rs.getString(2));
                this.jTextFieldDocCli.setText(rs.getString(3));
                this.jTextAreaDescripCli.setText(rs.getString(4));
                this.jCheckBoxNotificar.setSelected(rs.getBoolean(5));
            }
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al cargar datos de la vista: " + ex.getMessage());
            JOptionPane.showMessageDialog(this.jFrameCliente, label, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void activarDesactivarCasillasChofer(boolean valor) {
        //Se encarga de activar desactivar las casillas que tienen que ver con chofer 
        this.jComboBoxVh.setEnabled(valor);
        this.jButtonVerVhCh.setEnabled(valor);
    }
    
    private void activarDesactivarCasillasDueño(boolean valor){
        this.jComboBoxCli.setEnabled(valor);
        this.jButtonAgregarCli.setEnabled(valor);
        // --- ¿Donde se agregarían los datos para el cliente?
    }
    
    private void borrarTelefono(String idTel){
        try {
            //Borra un telefono recibiendo el id del mismo
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM telefono WHERE idtelefono = "+idTel+"");
            ps.executeUpdate();
            JLabel label = new JLabelAriel("Teléfono borrado");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al borrar teléfono");
            JOptionPane.showMessageDialog(this.jFrameAgregarTel, label, "ATENCIÓN", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean grabarTelefono(int idPersona){
        boolean valor = false;
        try {
            //Método para grabar el teléfono a una Persona de la Base de Datos. el "tipoTel"
            long numTel = Long.valueOf(this.jTextFieldNumero.getText());
            String tipoTel = this.jTextFieldTipoTel.getText();
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("INSERT into telefono VALUES (default, ?, ?, ?)");
            ps.setLong(1, numTel);  //En la base de datos es un BIGINT
            if(!tipoTel.equals(""))
                ps.setString(2, tipoTel); //Si no es igual a "" (null)
            else
                ps.setNull(2, java.sql.Types.VARCHAR);
            ps.setInt(3, idPersona); //El Fk
            ps.executeUpdate();
                valor = true;
        } catch (SQLException ex) {
            JLabel label = new JLabelAriel("Error al grabar teléfono: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        return valor;
    }
    
    private void desactivarCamposDeChoferYCliente(){
        //Desactiva las casillas de Chofer y Cliente para que solo aparezcan en caso de que el usuario lo requiera
        //---
        //--- Desactivo datos del Cliente
        this.jComboBoxCli.setEnabled(false);
        this.jButtonAgregarCli.setEnabled(false);
        //------------
        //--- Desactivo datos del Chofer
        this.jComboBoxVh.setEnabled(false);
        this.jButtonVerVhCh.setEnabled(false);
        //--------
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonAgregarCli;
    private javax.swing.JButton jButtonAgregarTel;
    private javax.swing.JButton jButtonBorrarTel;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonCancelarAdPer;
    private javax.swing.JButton jButtonCancelarChofer;
    private javax.swing.JButton jButtonCancelarCli;
    private javax.swing.JButton jButtonDatosChofer;
    private javax.swing.JButton jButtonDatosCliente;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonGuardarChofer;
    private javax.swing.JButton jButtonGuardarCli;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonVerVhCh;
    private javax.swing.JCheckBox jCheckBoxAgenteR;
    private javax.swing.JCheckBox jCheckBoxChofer;
    private javax.swing.JCheckBox jCheckBoxCliente;
    private javax.swing.JCheckBox jCheckBoxNotificar;
    private javax.swing.JComboBox<ComboItem> jComboBoxCli;
    private javax.swing.JComboBox<ComboItem> jComboBoxTelefonos;
    private javax.swing.JComboBox<ComboItem> jComboBoxVh;
    private javax.swing.JFrame jFrameAgregarTel;
    private javax.swing.JFrame jFrameChofer;
    private javax.swing.JFrame jFrameCliente;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelApeCli;
    private javax.swing.JLabel jLabelApellidoChofer;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelInfoVh;
    private javax.swing.JLabel jLabelNombreChofer;
    private javax.swing.JLabel jLabelNombreCli;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextAreaDescripCli;
    private javax.swing.JTextArea jTextAreaDescripcion;
    private javax.swing.JTextField jTextFieldApellido;
    private javax.swing.JTextField jTextFieldApodo;
    private javax.swing.JTextField jTextFieldDocCli;
    private javax.swing.JTextField jTextFieldLocalidad;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldNumero;
    private javax.swing.JTextField jTextFieldObs;
    private javax.swing.JTextField jTextFieldTipoTel;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        //Ver ejemplo en "Nueva Planilla"
        // Es posible que haya que consultar
      //  JLabel label = new JLabelAriel("Si realizó algún cambio y no lo guardó se perderán si hace click en 'Sí'.");
      //  int input = JOptionPane.showConfirmDialog(null, label, "ERROR", JOptionPane.INFORMATION_MESSAGE); 
      //  return input == JOptionPane.OK_OPTION;
      return true;
    }

    @Override
    public void onFocus() {
        //Cuando se enfoca en esta vista habiendo salido antes: sirve para actualizar datos (generalmente los visibles)   
        this.cargarDatosPersonaModificar();
        this.cargarCamionesChofer();
        this.cargarCamionesDueño();
        this.cargarTelefonos();
    }
}
