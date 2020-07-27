/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import controladores.ControladorPrincipal;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.JLabelAriel;

/**
 *
 * @author Ariel
 */
public class PanelAdPagos extends JPanelCustom {

    
    private DefaultTableModel modelo;
    private ControladorPrincipal controlador;
    
    public PanelAdPagos() {
        modelo = new DefaultTableModel(){       
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        initComponents();
        this.jTablePlanillas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        modelo.setColumnIdentifiers(getColumnas());
        this.controlador = ControladorPrincipal.getInstancia();
        cargarTablaClientes("");
    }

    private String[] getColumnas(){
        String columna[] = new String[]{"Nombre Cliente", "Apellido", "N° Planilla", "¿Impaga?", "IMPORTE"};
        
        return columna;
    }
    
    private void cargarTablaClientes(String filtroNombre){
        //Método para cargar planillas en la tabla
        //Diseñar para aplicar filtro de nombre de cliente
        String consulta;
        if(filtroNombre.equals("")){
            consulta = "SELECT per.nombre, per.apellido, p.idplanilla, p.pagado FROM persona AS per "
                    + "INNER JOIN cliente AS c ON per.idpersona = c.idpersona INNER JOIN planilla AS p ON p.idcliente = c.idcliente";
        }else{
            consulta = "SELECT per.nombre, per.apellido, p.idplanilla, p.pagado FROM persona AS per "
                    + "INNER JOIN cliente AS c ON per.idpersona = c.idpersona INNER JOIN planilla AS p ON p.idcliente = c.idcliente "
                    + "WHERE per.nombre LIKE '"+filtroNombre+"'";
        }
        cargarDatosTablaClientes(consulta);
    }   
    
    private void cargarDatosTablaClientes(String consulta){
        //Método para cargar datos en la tabla una vez que la consulta está lista
        String[] registro = new String[5];
        Connection co = this.controlador.obtenerConexion();
        try {
            co.setAutoCommit(false);
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                registro[0] = rs.getString(1); //nombre cliente
                registro[1] = rs.getString(2); //apellido
                registro[2] = ""+rs.getInt(3); //Numero planilla
                if(rs.getBoolean(4)) //Si es verdadero la planilla esta pagada
                    registro[3] = "NO";
                else
                    registro[3] ="SI";
                registro[4] = ""+cargarImporteReparaciones(rs.getInt(3), co); //Acá tendría que cargar el importe total de las reparaciones.
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
                //Esto no importaría porque es una consulta de solo lectura
            }
        } 
    }
    
    private long cargarImporteReparaciones(int nPlanilla, Connection co) throws SQLException{
        //Obtiene la suma de los importes de las reparaciones asignadas a la planilla y la retorna
        long suma = 0;
        String sql = "SELECT SUM(r.IMPORTE) FROM reparacion AS r WHERE r.idplanilla = '"+nPlanilla+"' ";
        Statement st = co.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            suma+= rs.getLong(1);  //El while solo se ejecuta una vez
        }
        return suma;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePlanillas = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Clientes con Planillas en el sistema");

        jTablePlanillas.setModel(modelo);
        jScrollPane1.setViewportView(jTablePlanillas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public boolean sePuedeCerrar() {
        //Teoricamente no habría problemas para cerrar en cualquier momento esta pestaña
        return true;  //El controlador se encarga de cerrar la pestaña actual
    }

    @Override
    public void onFocus() {
        //Cosas a cargar cuando esta vista toma el foco. Ver que conviene
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePlanillas;
    // End of variables declaration//GEN-END:variables
}
