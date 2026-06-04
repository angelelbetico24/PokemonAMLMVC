package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angel
 */
public class EntrenadorModelo extends ConexionBD {

    String sql;

    public List<Entrenador> leer() {
        Connection con = getConexion();
        sql = "SELECT * FROM ENTRENADOR;";
        List<Entrenador> entrenadores = new ArrayList<>();
        try{
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(sql);
            while(res.next()){
                int id = res.getInt(1);
                String nombre = res.getString(2);
                String ciudad = res.getString(3);
                Entrenador e = new Entrenador(id,nombre,ciudad);
                entrenadores.add(e);
            }
            con.close();
            s.close();
            res.close();
            return entrenadores;
        }catch(SQLException error){
            System.err.println(error);
            return null;
        }
    }

    //NOS SIRVE PARA POKEMONMODELO TAMBIEN
    public Entrenador leerPorId(int id){
        sql = "SELECT * FROM ENTRENADOR WHERE id = ?";
        Connection con = getConexion();
        Entrenador entr = null;
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String nombre = rs.getString(2);
                String ciudad = rs.getString(3);
                entr = new Entrenador(id, nombre, ciudad);
            }
            return entr;
        }catch(SQLException e){
            System.err.println(e);
            return null;
        }
    }
    public boolean crear(Entrenador e) {
        Connection con = getConexion();
        sql = "INSERT INTO ENTRENADOR(nombre,ciudad) VALUES(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCiudad());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);
            ps.close();
            con.close();
            return true;
        } catch (SQLException error) {
            System.err.println(error);
            return false;
        }
    }

    public boolean actualizar(Entrenador e) {
        Connection con = getConexion();
        sql = "UPDATE ENTRENADOR SET nombre = ?, ciudad = ? WHERE id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCiudad());
            ps.setInt(3, e.getId());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: "+rows);
            ps.close();
            con.close();
            return true;
        } catch (SQLException error) {
            System.err.println(error);
            return false;
        }
    }

    public boolean eliminar(int id) {
        Connection con = getConexion();
        sql = "DELETE FROM ENTRENADOR WHERE id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: "+rows);
            ps.close();
            con.close();
            return true;
        }catch(SQLException error){
            System.err.println(error);
            return false;
        }
    }
}
