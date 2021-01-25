/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ari_0
 */
public class DataBase {
    private final String usuario = "postgres";
    private final String contraseña = "postgres";  //para usuario leo - contraseña: h24e89{Q3.  //Se puede usar "postgres" sino
    private final String url = "jdbc:postgresql://127.0.0.1:5432/bddLeo";
    Connection conn;
    private static DataBase instancia;
    
    private DataBase(){}
    
    public static DataBase getInstancia(){
        if (instancia == null)
            DataBase.instancia = new DataBase();
        return DataBase.instancia;
    }
    
    public void conexion(){
        try{
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(url, usuario, contraseña);            
        }catch(Exception e){
            System.out.println(" Error "+e.getMessage());
        }
    }
    
    public Connection getConexion(){
        if (this.conn == null){
            conexion();
            return this.conn;
        }
        return this.conn;
   }
    
    public void cerrarConexion(){
        try {
            if(conn != null){
                this.conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error de desconexión: "+ex.getMessage());
        }
    }
    
    public void abrirConexion(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, usuario, contraseña);            
        }catch(Exception e){
            System.out.println(" Error "+e.getMessage());
        }
    }
}
