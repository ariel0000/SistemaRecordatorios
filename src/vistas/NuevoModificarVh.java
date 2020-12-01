/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ComboItem;
import modelo.JLabelAriel;

/**
 *
 * @author ari_0
 */
public class NuevoModificarVh extends JPanelCustom {

    DefaultTableModel modelo, choferesModel;
    String[] listaMarcas;
    ControladorPrincipal controlador;
    private int idVh = 0; //Será diferente de 0 solo si es Vh a modificar
    private int idCliente = 0;
    
    public NuevoModificarVh() {
        //Constructor para un vehículo nuevo
        
        choferesModel = new DefaultTableModel(null, getColumnas()){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };
        initComponents();
        inicializarCamposEnComun();
        cargarDatos();
        habilitarCasillas(false); //las que no se usan para un nuevo vehículo
        //Las advertencias de clientes y choferes deben esconderse para no confundir.
        this.jLabelAtencionCh.setVisible(false);
        this.jLabelAtencionCli.setVisible(false);
    }
    
    public NuevoModificarVh(int idVh) {
        //Constructor para un vehículo a modificar
        
        choferesModel = new DefaultTableModel(null, getColumnas()){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
            return false;
            } 
        };
        initComponents();
        this.idVh = idVh;
        inicializarCamposEnComun();
        this.jButtonGuardar.setText("Actualizar");
        this.jTextFieldPatente.setEnabled(false); //No se puede volver a modificar la patente
        cargarDatos(idVh); //Paso el id del vehículo para que se carguen los datos correctos
        this.cargarChoferesActuales(idVh);
    }

    private String[] getColumnas() {
        //Histórico sirve para indicar si el chofer maneja actualmente o tiene fecha de fin
        String columna[] = new String[]{"Nombre", "Apellido", "Apodo", "Histórico", "idmaneja"};
        
        return columna;
    }

    private void cargarDatos(){
        //Carga los datos a la vista según lo necesario para un Vehículo nuevo
        cargarClientes("SELECT c.cuil, p.nombre, p.apellido, c.idcliente FROM cliente AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre");  //cargar el JComboBox de clientes
       
        
        cargarChoferes("SELECT c.apodo, p.nombre, p.apellido, c.idchofer FROM chofer AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre ");  //Carga el JComboBox de choferes para agregarlos"
      //  cargarChoferesActuales(); No se usa en Vehículo nuevo
    }
    
    private void cargarDatos(int idVh){
    //Carga los datos a la vista para un vehículo a modificar
        cargarClientes("SELECT c.cuil, p.nombre, p.apellido, c.idcliente FROM cliente AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre"); //Cargo todos los clientes, dsps tendré que seleccionar el que va (JComboBoxCli es de tipo ComboItem)
        String query = "SELECT v.modelo, v.marca, v.patente, v.modeloanio, c.idcliente FROM vehiculo AS v INNER JOIN cliente as c "
                + "ON v.idduenio = c.idcliente WHERE v.idvehiculo = '"+idVh+"' ";
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                this.jTextFieldModelo.setText(rs.getString(1));
             //   this.seleccionarMarca(rs.getString(2));
                this.jComboBoxMarcasVh.setSelectedItem(rs.getString(2)); //Capaz que explota - O selecciona bien la marca
                this.jTextFieldPatente.setText(rs.getString(3));
                this.jComboBoxAnioModelo.setSelectedItem(rs.getString(4)); //Capaz que explota
                this.idCliente = rs.getInt(5);  //Cargo el id del Cliente en la variable, solo se puede modificar si llamo al método específico
                this.seleccionarCliente(rs.getInt(5)); //Los clientes ya están cargados-paso idcliente para mostar sus datos en el JLabelCli
            }
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ATENCIÓN", JOptionPane.WARNING_MESSAGE);   
        }
        cargarChoferesActuales(idVh); //Carga los choferes del vehículo en la tabla
        cargarChoferes("SELECT c.apodo, p.nombre, p.apellido, c.idchofer FROM chofer AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre"); //Carga el jComboBox de choferes
    }
    
    private void habilitarCasillas(boolean valor){
        //Método para habilitar/deshabilitar las casillas innecesarias para un vehículo nuevo. Necesario una vez que se guarda ese Vh nuevo.
        this.jTableChoferes.setEnabled(valor);
        this.jLabelChofer.setEnabled(valor);
        this.jButtonAgregarCh.setEnabled(valor);
        this.jButtonQuitarCh.setEnabled(valor);
        this.jLabelCh.setEnabled(valor);
        this.jComboBoxChofer.setEnabled(valor);
        this.jButtonBorrarCh.setEnabled(valor);
    }
    
    private void cargarMarcas(){
        //Método para cargar marcas para los vehículos nuevos
        this.jComboBoxMarcasVh.setModel(new DefaultComboBoxModel(leerArchivoMarcas()));
    }
    
    private String[] leerArchivoMarcas  () {
        //Método que genera un String compuesto con cada una de las marcas de los camiones
        String [] marcas = null;
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            String ruta = new File("MarcasMionca.txt").getAbsolutePath();
            archivo = new File(ruta); //Probamos así
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            String linea, marcasJuntas = "";
            while ((linea = br.readLine()) != null) {
                marcasJuntas = marcasJuntas + linea + ";"; //Quedaría un ";" en la última línea
            }
            marcasJuntas = marcasJuntas.substring(0, marcasJuntas.length()-1); //Quito el último caracter ";"
            marcas = marcasJuntas.split(";"); //Separa las marcas por medio del ";"
        } catch (IOException e) {
            System.out.println("Error en 'leer archivo marcas': "+e.getMessage());
        } finally {
            // En el finally cerramos el fichero, para asegurarnos que se cierra tanto si todo va bien como si salta una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
                JLabelAriel label = new JLabelAriel("Error al leer archivo de marcas "+e2.getMessage());
                JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        }
        return marcas;
    }
    
 /*   private void seleccionarMarca(String marca){
    //Método para cargar la marca del Vh a modificar --> Seleccionaría la marca correcta en el jComboBox de Marcas
        for(int i = 0; i < this.jComboBoxMarcasVh.getItemCount(); i++){
            if(this.jComboBoxMarcasVh.getItemAt(i) )
        }
    } */
    
    private void jComboBoxClienteItemStateChanged(java.awt.event.ItemEvent evt){
        //ActionPerformed creado por mi para administar el cambio de un cliente o la selección de uno nuevo desde el jComboBoxCliente
        //debería Actualizar el cliente y ponerlo en el JLabelCli
        if(this.jComboBoxCliente.getSelectedIndex() != -1 && this.jComboBoxCliente.getSelectedIndex() != 0){
            ComboItem cIt = (ComboItem) this.jComboBoxCliente.getSelectedItem();
            this.idCliente = Integer.valueOf(cIt.getKey()); //obtengo el idcli
            //Si el Vh es nuevo el cliente se guardará al momento de clickear guardar - mientras lo muestro como seleccionado
            this.seleccionarCliente(this.idCliente);
            
            
        }
    }
    
    private void seleccionarCliente(int idcliente){
        // Método que sirve para indicar en el JLabelCli el cliente que es dueño del vehículo
        String queryCli = "SELECT p.nombre, p.apellido, c.cuil FROM persona AS p INNER JOIN cliente AS c ON p.idpersona = c.idpersona "
                + "WHERE c.idcliente = '"+idcliente+"' ";
        String nombre, apellido;
        long cuil;
        try{
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(queryCli);
            while(rs.next()){
                nombre = rs.getString(1); //Nombre
                apellido = rs.getString(2); //Apellido
                cuil = rs.getLong(3);  //cuil
                this.jLabelCli.setText(nombre+" "+apellido+". Cuil: "+cuil);
            }
            
        }catch(SQLException ex){
            JLabelAriel label = new JLabelAriel("Error al cargar datos del Cliente "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cargarClientes(String query){
        //Método para cargar los Clientes en el ComboBox - Para vista: "Nuevo Vh" --> los carga a todos
        this.jComboBoxCliente.removeAllItems();
        String apellido, nombre;
        long cuil;
        ComboItem cmItem;
        cmItem = new ComboItem("0" ,"--Desde acá puede seleccionar el Dueño del Camión--");
        this.jComboBoxCliente.addItem(cmItem);
        
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                cuil = rs.getLong(1);
                cmItem = new ComboItem(""+rs.getInt(4), nombre+" "+apellido+" cuit/cuil: "+cuil); 
                                        //el idPersona y el nombre, apellido y apodo
                this.jComboBoxCliente.addItem(cmItem);
            }
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar los choferes "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        } 
    }
    
    private void cargarChoferes(String query){
        //Carga el JComboBox de choferes para agregarlos 
        // Borro las filas actuales para asegurarme de no cargarlas dos veces
        String apellido, nombre;
        String apodo;
        ComboItem cmItem;
        cmItem = new ComboItem("0", "--En este lugar se puede seleccionar un chofer--");
        this.jComboBoxChofer.addItem(cmItem);
        try {
            Statement st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                nombre = rs.getString(2);
                apellido = rs.getString(3);
                apodo = rs.getString(1);
                cmItem = new ComboItem(""+rs.getInt(4), nombre+" "+apellido+" con Apodo: "+apodo); 
                                        //el idPersona y el nombre, apellido y Apodo
                this.jComboBoxChofer.addItem(cmItem);
            }
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al cargar los clientes "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cargarChoferesActuales(int idVh){
        //Cargar la tabla de choferes con los choferes actuales del vehículo
        DefaultTableModel dtm = (DefaultTableModel) this.jTableChoferes.getModel();
        dtm.setRowCount(0);  //Magicamente anduvo y sirve para eliminar las filas de la tabla
        dtm = new DefaultTableModel(null, this.getColumnas()); //Las 5 columnas con idmaneja
        this.choferesModel = dtm;
        this.jTableChoferes.setModel(this.choferesModel); //Otra vez seteo el modelo (Por las dudas)
        
        Object[] registro;
        registro = new String[5];
        String query = "SELECT m.idmaneja, p.nombre, p.apellido, c.apodo, m.fecha_fin FROM chofer AS c NATURAL JOIN persona AS p "
                + "NATURAL JOIN maneja AS m NATURAL JOIN vehiculo AS vh WHERE vh.idvehiculo = '"+idVh+"' "
                + "ORDER BY p.nombre";
        Statement st;
        try {
            st = this.controlador.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                registro[0] = rs.getString(2); //nombre
                registro[1] = rs.getString(3);  //apellido
                registro[2] = rs.getString(4);  //apodo
                if(rs.getDate(5) != null) //existe fecha de fin
                    registro[3] = "Sí: "+rs.getDate(5);
                else
                    registro[3] = "No";
                registro[4] = ""+rs.getInt(1); //el idmaneja al final - no hay columnas suficientes (4) para mostrar este dato "escondido"
                this.choferesModel.addRow(registro); //Se agrega al final el idmaneja. Columna número: 4 contando desde 0;
            }
        } catch (SQLException ex) { 
            JLabelAriel label = new JLabelAriel("Error al cargar los clientes "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);   
        }
        this.jTableChoferes.removeColumn(this.jTableChoferes.getColumnModel().getColumn(4)); //Borro la última columna de la vista
        //El método de arriba no borra el dato en la posición 4 del modelo. Solo afecta a la vista y sirve para esconder el idmaneja
        this.jTableChoferes.updateUI();
    }
    
    private void inicializarCamposEnComun(){
        //Los campos en común para iniciar, sea vehículo nuevo o a modificar
     //   jComboBoxMarcasVh.setModel(new javax.swing.DefaultComboBoxModel(leerArchivoMarcas())); utilizo la línea de abajo
        cargarMarcas(); //Cargo todas las marcas posibles
        this.jTableChoferes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        this.controlador = ControladorPrincipal.getInstancia();
        this.cargarJComboBoxAnioModelo();
        agregarItemStateChange();  //Agrega un evento de ItemStateChange al JComboBox de Clientes
    }
    
    private void cargarJComboBoxAnioModelo() {
        //Carga los años del 1950 al año actual para elegir de que año es el vehículo
        Date fecha = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Argentina/BuenosAires"));
        cal.setTime(fecha);
        int year = cal.get(Calendar.YEAR); //El año actual
        for (int i = 1950; year >= i; i++) {
            this.jComboBoxAnioModelo.addItem("" + i); //Inserto todos los años hasta el actual
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        jLabel21 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldModelo = new javax.swing.JTextField();
        jTextFieldPatente = new javax.swing.JTextField();
        jLabelChofer = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxCliente = new javax.swing.JComboBox<>();
        jTextFieldFPersona = new javax.swing.JTextField();
        jTextFieldFNombre = new javax.swing.JTextField();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelCh = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableChoferes = new javax.swing.JTable();
        jButtonFiltrarCli = new javax.swing.JButton();
        jComboBoxMarcasVh = new javax.swing.JComboBox<>();
        jButtonAgregarCh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxAnioModelo = new javax.swing.JComboBox<>();
        jComboBoxChofer = new javax.swing.JComboBox<>();
        jButtonFiltrarChofer = new javax.swing.JButton();
        jButtonQuitarCh = new javax.swing.JButton();
        jLabelAtencionCh = new javax.swing.JLabel();
        jLabelAtencionCli = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButtonSalir = new javax.swing.JButton();
        jButtonBorrarCh = new javax.swing.JButton();
        jLabelCli = new javax.swing.JLabel();
        jButtonInfo = new javax.swing.JButton();

        jFrameInfo.setAlwaysOnTop(true);
        jFrameInfo.setFocusable(false);
        jFrameInfo.setLocationByPlatform(true);
        jFrameInfo.setSize(new java.awt.Dimension(745, 435));
        jFrameInfo.setType(java.awt.Window.Type.POPUP);

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel18.setText(" . Dueño");

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel16.setText(" . Año de Fabricación");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setText(" . Marca y modelo ");

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Esta vista sirve para administrar siguiente información referida a un Vehículo");

        jButtonAceptar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 0, 0));
        jLabel19.setText(" . Cuando se agrega un vehículo nuevo solo se puede agregar chofer después");

        jLabel20.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel20.setText(" . Choferes del mismo. Si el Vh es nuevo primero se debe guardar para agregar Chofer");
        jLabel20.setMaximumSize(new java.awt.Dimension(730, 24));
        jLabel20.setMinimumSize(new java.awt.Dimension(730, 24));
        jLabel20.setPreferredSize(new java.awt.Dimension(725, 24));

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(204, 0, 0));
        jLabel21.setText(" de hacer click en \"Guardar\"");

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
                    .addGroup(jFrameInfoLayout.createSequentialGroup()
                        .addGroup(jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jFrameInfoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel21))
                            .addComponent(jLabel18))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jSeparator2)
        );
        jFrameInfoLayout.setVerticalGroup(
            jFrameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addGap(28, 28, 28)
                .addComponent(jButtonAceptar)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Marca:");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setText("Modelo:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setText("Patente:");

        jTextFieldModelo.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldPatente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabelChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelChofer.setText("Choferes del Vehículo:");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel7.setText("Dueño:");

        jComboBoxCliente.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jTextFieldFPersona.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jTextFieldFPersona.setToolTipText("nombre o apodo");

        jTextFieldFNombre.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jButtonCancelar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGuardar.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jLabelCh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabelCh.setText("Chofer:");

        jTableChoferes.setFont(new java.awt.Font("SansSerif", 0, 17)); // NOI18N
        jTableChoferes.setModel(choferesModel);
        jScrollPane2.setViewportView(jTableChoferes);

        jButtonFiltrarCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonFiltrarCli.setText("Filtrar");
        jButtonFiltrarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFiltrarCliActionPerformed(evt);
            }
        });

        jComboBoxMarcasVh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jComboBoxMarcasVh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Otra Marca" }));
        jComboBoxMarcasVh.setName(""); // NOI18N

        jButtonAgregarCh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonAgregarCh.setText("Agregar");
        jButtonAgregarCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarChActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel3.setText("Año Modelo:");

        jComboBoxAnioModelo.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jComboBoxChofer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButtonFiltrarChofer.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonFiltrarChofer.setText("Filtrar");
        jButtonFiltrarChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFiltrarChoferActionPerformed(evt);
            }
        });

        jButtonQuitarCh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonQuitarCh.setText("Desmarcar Chofér");
        jButtonQuitarCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarChActionPerformed(evt);
            }
        });

        jLabelAtencionCh.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelAtencionCh.setForeground(new java.awt.Color(255, 0, 51));
        jLabelAtencionCh.setText("<html>\nLa persona tiene que estar anotada como chofer<br> para que aparezca en este listado\n<html>");

        jLabelAtencionCli.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabelAtencionCli.setForeground(new java.awt.Color(255, 0, 51));
        jLabelAtencionCli.setText("<html>\nLa persona tiene que estar anotada como cliente <br>\npara que aparezca en este listado\n<html>");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel4.setText("Filtrar por Nombre o Apellido:");

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setText("Filtrar personas por nombre o apodo:");

        jButtonSalir.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jButtonBorrarCh.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButtonBorrarCh.setText("Borrar Chofér");
        jButtonBorrarCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarChActionPerformed(evt);
            }
        });

        jLabelCli.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxMarcasVh, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelAtencionCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(74, 74, 74)))))
                        .addGap(111, 111, 111)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxAnioModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldPatente, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelCh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelAtencionCh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxChofer, 0, 417, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonAgregarCh)
                                        .addGap(201, 201, 201))
                                    .addComponent(jLabel11)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldFPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonFiltrarChofer)))
                                .addGap(49, 49, 49))
                            .addComponent(jLabelChofer)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonQuitarCh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonBorrarCh))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE))
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSalir))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonFiltrarCli)
                                    .addComponent(jTextFieldFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxAnioModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPatente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jComboBoxMarcasVh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFiltrarCli)
                    .addComponent(jLabelAtencionCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelCli, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCh)
                    .addComponent(jComboBoxChofer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregarCh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelAtencionCh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFiltrarChofer))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelChofer)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonQuitarCh)
                    .addComponent(jButtonBorrarCh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonSalir)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jComboBoxMarcasVh.getAccessibleContext().setAccessibleName("");
        jComboBoxMarcasVh.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // Debería salir del sistema preguntando, en caso de que no esté guardado en el sistema, si se desea seguir
        if(this.idVh == 0){ //Es un vehículo nuevo y no se guardo nada aún
            JLabelAriel label = new JLabelAriel("Los cambios realizados no se guardarán, ¿Desea Salir?");
            if(JOptionPane.showConfirmDialog(null, label, "ATENCIÓN!", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION)//Se Aceptó
                this.controlador.cerrarPanelSeleccionado(); //Cierro este panel - No hay Vh guardado
            else //No se acepta cerrar --> Se sigue editando
                ; //Nada que hacer
        }else{ //El idVh ya está editado --> El vehículo ya se guardó o es Vh a modificar
            this.controlador.cerrarPanelSeleccionado();
        }
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        //Se cancela la edición --> No se pregunta si se debe guardar algo
        this.controlador.cerrarPanelSeleccionado();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // Verifica si están todos los datos y guarda el Vh -- Si es a modificar el idVh tiene que ser != de 0
        if(guardarVh()){ //Este método comprueba si están los campos necesarios y actualiza o guarda según corresponda
            this.habilitarCasillas(true); //Si venía de actualizar no sería necesario
            this.jButtonGuardar.setText("Actualizar");  //Cambio el texto de este botón porque el vehículo ya se guardo.
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonAgregarChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarChActionPerformed
        try {
            //Método para agregar chofer al vehículo, si es nuevo se habilita esta operación una vez guardado el vehículo
            //Cuando se agrega un chofer puede que sea un chofer que ya esté asociado al vh, en caso de vh a modificar.
            //Datos de la tabla de choferes: p.nombre, p.apellido, c.apodo, t.numero, t.tipo_tel
            //Lo lógico sería agregar el nuevo chofer y dsps volver a cargar los datos de la tabla con el método correspondiente
            ComboItem cItm= (ComboItem) this.jComboBoxChofer.getSelectedItem();
            if(!cItm.getKey().equals("0")){
                String idChofer = ((ComboItem)this.jComboBoxChofer.getSelectedItem()).getKey();
                PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("INSERT INTO maneja values(default, ?, ?, ?, ?)");
                //idmaneja, idvh, idchofer, fecha_inicio, fecha_fin
                ps.setInt(1, idVh);
                ps.setInt(2, Integer.valueOf(idChofer));
                ps.setDate(3, new java.sql.Date(new Date().getTime()));
                ps.setDate(4, null); //Nunca se carga la fecha de fin
                ps.executeUpdate();
                JLabelAriel label3 = new JLabelAriel("Chofer agregado: ");
                this.cargarChoferesActuales(idVh);
                JOptionPane.showMessageDialog(null, label3, "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE); 
            }
            else{
                JLabelAriel label4 = new JLabelAriel("No se seleccionó ningún Chofer");
                JOptionPane.showMessageDialog(null, label4, "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE); 
            }
        } catch (SQLException ex) {
            JLabelAriel label3 = new JLabelAriel("Error al agregar chofer: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label3, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }     
        
    }//GEN-LAST:event_jButtonAgregarChActionPerformed

    private void jButtonQuitarChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarChActionPerformed
        // Pone fecha de fin al chofer que maneja este vehículo
        int filaSelect = this.jTableChoferes.getSelectedRow();
        if(filaSelect != -1){
            eliminarChofer(this.jTableChoferes.getValueAt(filaSelect, 0), this.jTableChoferes.getValueAt(filaSelect, 1)); //Nombre y apellido
        }
        else{
            JLabelAriel label3 = new JLabelAriel("No hay chofer seleccionado");
            JOptionPane.showMessageDialog(null, label3, "¡Atención!", JOptionPane.WARNING_MESSAGE); 
        }
        
        //--Dsps de eliminar el chofer hay que actualizar la tabla
        
    }//GEN-LAST:event_jButtonQuitarChActionPerformed

    private void jButtonBorrarChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarChActionPerformed
        // Nuevo botón de borrar chofer para eliminar, en caso de que se pueda, un chofer completamente
        int filaSelect = this.jTableChoferes.getSelectedRow();
        if(filaSelect != -1){
            String idmaneja = (String) this.choferesModel.getValueAt(filaSelect, 4); 
            //el idmaneja que no se muestra en la vista pero está en el modelo
           // String apellido = this.jTableChoferes.getValueAt(filaSelect, 1).toString();
            borrarChofer(idmaneja);
        }
        else{
            JLabelAriel label3 = new JLabelAriel("No hay chofer seleccionado");
            JOptionPane.showMessageDialog(null, label3, "¡Atención!", JOptionPane.WARNING_MESSAGE); 
        }
    }//GEN-LAST:event_jButtonBorrarChActionPerformed

    private void jButtonInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInfoActionPerformed
        // Abre el Frame de JFrameInfo para mostrar info de la vista (Para que sirve para que no sirve).
        this.jFrameInfo.setVisible(true);
    }//GEN-LAST:event_jButtonInfoActionPerformed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        //Cierra el jFrameInfo
        this.jFrameInfo.dispose();
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jButtonFiltrarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFiltrarCliActionPerformed
        //..AP de Filtrar Clientes - tendría que crear el query y enviarlo al método de cargar clientes para cargar lo correspondiente
        String query = "SELECT c.cuil, p.nombre, p.apellido, c.idcliente FROM cliente AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre";
        if(!this.jTextFieldFNombre.getText().equals("")){
            query = "SELECT c.cuil, p.nombre, p.apellido, c.idcliente FROM cliente AS c INNER JOIN persona AS p ON "
                    + "p.idpersona = c.idpersona WHERE lower(p.nombre) LIKE '"+(this.jTextFieldFNombre.getText()).toLowerCase()+"' "
                    + "OR lower(p.apellido) LIKE '"+(this.jTextFieldFNombre.getText().toLowerCase())+"'  ORDER BY p.nombre";    
        }
        
        this.cargarClientes(query);
    }//GEN-LAST:event_jButtonFiltrarCliActionPerformed

    private void jButtonFiltrarChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFiltrarChoferActionPerformed
        // Filtro choferes. En el JComboBoxChoferes hay Combo Items de la forma: //el idPersona y el nombre, apellido y Apodo
        this.jComboBoxChofer.removeAllItems();
        String filtroCh = this.jTextFieldFPersona.getText().toLowerCase();
        String query = "SELECT c.apodo, p.nombre, p.apellido, c.idchofer FROM chofer AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona WHERE lower(p.nombre) LIKE '%"+filtroCh+"%'  OR lower(apodo) LIKE '%"+filtroCh+"%' ORDER BY p.nombre";
        this.cargarChoferes(query);
        
    }//GEN-LAST:event_jButtonFiltrarChoferActionPerformed

    private void borrarChofer(String idmaneja){
      
        //Borra el chofer del Vh de la entidad maneja. Al chofer se lo busca por nombre y apellido en una subconsulta
        PreparedStatement ps;
        try {
         /*   ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM maneja AS m "
                + "WHERE m.idvehiculo = '"+this.idVh+"' AND m.idchofer IN ( SELECT c.idchofer FROM chofer AS c NATURAL JOIN persona AS p "
                + "WHERE p.nombre = '"+nombre+"' AND p.apellido = '"+apellido+"') "); */
            ps = this.controlador.obtenerConexion().prepareStatement("DELETE FROM maneja WHERE idmaneja = '"+idmaneja+"' ");
            ps.executeUpdate();
            JLabelAriel label3 = new JLabelAriel("Chofer eliminado");
            JOptionPane.showMessageDialog(null, label3, "INFO", JOptionPane.INFORMATION_MESSAGE); 
        }catch (SQLException ex) {
            JLabelAriel label3 = new JLabelAriel("Error al borrar chofér: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label3, "¡ERROR!", JOptionPane.WARNING_MESSAGE); 
        }
        this.cargarChoferesActuales(idVh); //Recarga la tabla de choferes
    }
    
    private boolean guardarVh(){
        //Guarda el nuevo Vh y devuelve "true" en caso de éxito. O lo actualiza en caso de que sea para actualizar
        boolean valor = false;
        JLabelAriel label3 = null;
        //Guardar el nuevo Vehículo --> Debe verificar que estén los datos necesarios
        //La Marca, el modeloAnio y el Cliente siempre tienen algún dato

        if(!(this.jTextFieldPatente.getText().equals("") || this.jTextFieldModelo.getText().equals(""))){ //Si no están vacíos
            if(this.idVh == 0) //Me pregunto si es un Vehículo nuevo    
                if(this.idCliente != 0)  //Si elegí Dueño puedo guardar, sino tendré que dar aviso
                    valor = guardarNuevoVh(this.idCliente); //Lo que debería hacer siendo un vehículo nuevo
                else{
                    label3 = new JLabelAriel("Debe elegir un dueño para el Vehículo");  //Carga el JLabel para dar aviso
                    JOptionPane.showMessageDialog(null, label3, "ATENCIÓN", JOptionPane.WARNING_MESSAGE); 
                }
            else
                valor = actualizarVh(); //Lo que debería hacer siendo un Vehículo a modificar
        }
        else{
            label3 = new JLabelAriel("Verifique si los campos de PATENTE y MODELO tienen datos");
            JOptionPane.showMessageDialog(null, label3, "ATENCIÓN", JOptionPane.WARNING_MESSAGE); 
        }
        
        return valor;
    }
    
    private boolean actualizarVh(){
        //Actualiza el Vh --> Se verificó que estén todos los datos en el método que llama a este
        boolean valor = false;
        String marca =  this.jComboBoxMarcasVh.getSelectedItem().toString(); //Si no anda así castearlo a String
        String model = this.jTextFieldModelo.getText();
      //  String patente = this.jTextFieldPatente.getText();
        int modeloAnio = Integer.valueOf(this.jComboBoxAnioModelo.getSelectedItem().toString());
      //  ComboItem cmItem = (ComboItem) (this.jComboBoxCliente.getSelectedItem());  
        Connection conexion = this.controlador.obtenerConexion();
        try {
            //Guarda un nuevo Vh
            conexion.setAutoCommit(false);
            PreparedStatement ps = conexion.prepareStatement("UPDATE vehiculo SET modelo = ?, marca = ?, "
                    + "modeloanio = ?, idduenio = ? WHERE idvehiculo = '"+idVh+"'");
            ps.setString(1, model);
            ps.setString(2, marca);
            ps.setInt(3, modeloAnio);
            ps.setInt(4, this.idCliente);
            ps.executeUpdate();
            
            conexion.commit();
            valor = true;

            JLabelAriel label = new JLabelAriel("Vehículo actualizado con éxito");
            JOptionPane.showMessageDialog(null, label, "INFO", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al actualizar vehículo: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
            try{
                conexion.rollback();
            }
            catch(SQLException sq){
                JLabelAriel label2 = new JLabelAriel("Error al volver atrás actualización de Vehículo, posibles datos inconsistentes: "
                        +sq.getMessage());
                JOptionPane.showMessageDialog(null, label2, "ERROR", JOptionPane.ERROR_MESSAGE);
                JLabelAriel label3 = new JLabelAriel("Verifique si existe el vehículo en el Sistema y proceda a borrarlo ");
                JOptionPane.showMessageDialog(null, label3, "ATENCIÓN", JOptionPane.WARNING_MESSAGE); 
            }
        }
        return valor;
    }
    
    private boolean guardarNuevoVh(int idCli){
        // Método para guardar un vehículo nuevo
        //Tendría que capturar el idVh para poder agregar los choferes dsps
        boolean valor = false;
        String marca =  this.jComboBoxMarcasVh.getSelectedItem().toString(); //Si no anda así castearlo a String
        String model = this.jTextFieldModelo.getText();
        String patente = this.jTextFieldPatente.getText();
        int modeloAnio = Integer.valueOf(this.jComboBoxAnioModelo.getSelectedItem().toString());
        int idvh = 0;
        Connection conexion = this.controlador.obtenerConexion();
        try {
            //Guarda un nuevo Vh
            conexion.setAutoCommit(false);
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO vehiculo "
                    + "VALUES(default, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS); //modelo, marca, patente, modeloanio, duenio
            ps.setString(1, model);
            ps.setString(2, marca);
            ps.setString(3, patente.toUpperCase());
            ps.setInt(4, modeloAnio);
            ps.setInt(5, idCli);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                idvh = generatedKeys.getInt(1); //id del vehículo - después del commit lo pasamos a la variable idVh
            }
            conexion.commit();
            valor = true;
            this.idVh = idvh;
            JLabelAriel label = new JLabelAriel("Vehículo guardado con éxito");
            JOptionPane.showMessageDialog(null, label, "INFO", JOptionPane.INFORMATION_MESSAGE);
            this.jButtonGuardar.setText("Actualizar");
            
        } catch (SQLException ex) {
            JLabelAriel label = new JLabelAriel("Error al guardar vehículo: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
            try{
                conexion.rollback();
            }
            catch(SQLException sq){
                JLabelAriel label2 = new JLabelAriel("Error al volver atrás guardado de Vehículo, posibles datos inconsistentes: "
                        +sq.getMessage());
                JOptionPane.showMessageDialog(null, label2, "ERROR", JOptionPane.ERROR_MESSAGE);
                JLabelAriel label3 = new JLabelAriel("Verifique si aparece el vehículo en el sistema y en lo posible borrelo: ");
                JOptionPane.showMessageDialog(null, label3, "ATENCIÓN", JOptionPane.WARNING_MESSAGE); 
            }
        }  
        return valor;
    }

    private void eliminarChofer(Object nm, Object ape){
        //No elimina sino que pone fecha de fin al chofer - pasando a ser chofer "histórico"
        String nombre = (String) nm;
        String apellido = (String) ape;
        try{
            PreparedStatement ps = this.controlador.obtenerConexion().prepareStatement("UPDATE maneja SET fecha_fin = ? "
                    + "WHERE idchofer IN (SELECT c.idchofer FROM persona AS p INNER JOIN chofer as c ON p.idpersona = c.idpersona"
                    + " WHERE p.nombre = '"+nombre+"' AND p.apellido = '"+apellido+"' )");
            ps.setDate(1, new java.sql.Date(new Date().getTime()));
            ps.executeUpdate();
            JLabelAriel label3 = new JLabelAriel("Chofer marcado como histórico: ");
            JOptionPane.showMessageDialog(null, label3, "ATENCIÓN", JOptionPane.INFORMATION_MESSAGE); 
        }catch(SQLException ex){
            JLabelAriel label3 = new JLabelAriel("Error en la modificación: "+ex.getMessage());
            JOptionPane.showMessageDialog(null, label3, "ERROR", JOptionPane.WARNING_MESSAGE); 
        }
        this.cargarChoferesActuales(idVh);
    }
    
    private void agregarItemStateChange(){
        ItemListener cli = new ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxClienteItemStateChanged(evt);
            }
        };

        this.jComboBoxCliente.addItemListener(cli);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonAgregarCh;
    private javax.swing.JButton jButtonBorrarCh;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonFiltrarChofer;
    private javax.swing.JButton jButtonFiltrarCli;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonQuitarCh;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox<String> jComboBoxAnioModelo;
    private javax.swing.JComboBox<ComboItem> jComboBoxChofer;
    private javax.swing.JComboBox<ComboItem> jComboBoxCliente;
    private javax.swing.JComboBox<String> jComboBoxMarcasVh;
    private javax.swing.JFrame jFrameInfo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelAtencionCh;
    private javax.swing.JLabel jLabelAtencionCli;
    private javax.swing.JLabel jLabelCh;
    private javax.swing.JLabel jLabelChofer;
    private javax.swing.JLabel jLabelCli;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableChoferes;
    private javax.swing.JTextField jTextFieldFNombre;
    private javax.swing.JTextField jTextFieldFPersona;
    private javax.swing.JTextField jTextFieldModelo;
    private javax.swing.JTextField jTextFieldPatente;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean sePuedeCerrar() {
        // falta completar los requerimientos para saber si se puede cerrar la vista y devolver true
        return true;
    }

    @Override
    public void onFocus() {
        //Tendría que recargar los jComboBox y nada más
        this.jComboBoxChofer.removeAllItems();
        this.jComboBoxCliente.removeAllItems();
        cargarClientes("SELECT c.cuil, p.nombre, p.apellido, c.idcliente FROM cliente AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre");
        cargarChoferes("SELECT c.apodo, p.nombre, p.apellido, c.idchofer FROM chofer AS c INNER JOIN persona AS p ON "
                + "p.idpersona = c.idpersona ORDER BY p.nombre ");
        if(this.idVh != 0)
            this.cargarChoferesActuales(this.idVh);
    }
}
