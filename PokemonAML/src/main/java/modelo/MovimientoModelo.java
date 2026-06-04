/*
ESTA CLASE ES SOLO LECTURA, NO SE ESCRIBE NADA
*/
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angel
 */
public class MovimientoModelo extends ConexionBD{
    String sql;
    
    public List<Movimiento> leer(){
        Connection con = getConexion();
        sql = "SELECT * FROM movimiento JOIN tipo ON movimiento.id_tipo = tipo.id";//aqui no voy a crear otro metodo como en PokemonModelo, es un join sencillo y no merece la pena crear otro metodo
        List<Movimiento> movimientos = new ArrayList<>();
        try{
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(sql);
            while(res.next()){
                //TABLA MOVIMIENTO
                int id = res.getInt(1);
                String nombre = res.getString(2);
                int idTipo = res.getInt(3);
                int potencia = res.getInt(4);
                int precisionAtq = res.getInt(5);
                int pp = res.getInt(6);
                //TABLA TIPO
                String nombreTipo = res.getString(8);
                Tipo tipo = new Tipo(idTipo, nombreTipo);
                Movimiento mov = new Movimiento(tipo, id, potencia, precisionAtq, pp, nombre);
                movimientos.add(mov);
            }
            s.close();
            res.close();
            con.close();
            return movimientos;
        }catch(SQLException e){
            System.err.println(e);
            return null;
        }
    }
    
    public Movimiento leerPorId(int id){
        Connection con = getConexion();
        Movimiento mov = null;
        Tipo tipo = null;
        sql = "SELECT * FROM movimiento JOIN tipo ON movimiento.id_tipo = tipo.id WHERE movimiento.id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String nombre = rs.getString(2);
                int idTipo = rs.getInt(3);
                int potencia = rs.getInt(4);
                int precisionAtq = rs.getInt(5);
                int pp = rs.getInt(6);
                //TABLA TIPO
                String nombreTipo = rs.getString(8);
                tipo = new Tipo(idTipo, nombreTipo);
                mov = new Movimiento(tipo, id, potencia, precisionAtq, pp, nombre);
            }
            con.close();
            ps.close();
            rs.close();
            return mov;
        }catch(SQLException e){
            System.err.println(e);
            return mov;
        }
    }
}
