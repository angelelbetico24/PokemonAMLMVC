package modelo;

import java.sql.*;

/**
 *
 * @author angel
 */
public class ConexionBD {
    private final String bd = "pokemon";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost:3306/"+bd;
    private Connection con = null;
    
    public Connection getConexion(){
        try{
            con = DriverManager.getConnection(this.url, this.user, this.password);
        }catch(SQLException e){
            System.err.println(e);
        }
        return con;
    }
}
