package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author angel
 */
public class PokedexModelo extends ConexionBD {

    String sql;

    public List<Pokedex> leer() {
        Connection con = getConexion();
        sql = "SELECT * FROM pokedex JOIN pokedex_tipo ON pokedex.num_pokedex = pokedex_tipo.num_pokedex JOIN tipo ON pokedex_tipo.id_tipo = tipo.id";
        Map<Integer, Pokedex> mapa = new LinkedHashMap<>();//lo vamos a hacer por si sale el mismo pokemon duplicado dos veces por culpa de que tenga distinto tipo
        try {
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(sql);
            while (res.next()) {
                //Datos de la tabla pokedex
                int numPokedex = res.getInt(1);
                String nombre = res.getString(2);
                int hpBase = res.getInt(3);
                int ataqueBase = res.getInt(4);
                int defensaBase = res.getInt(5);
                int velocidadBase = res.getInt(6);
                //Columnas 7 y 8 son de pokedex_tipo (num_pokedex e id_tipo), no las necesitamos directamente a tabla tipo
                int idTipo = res.getInt(9);
                String nombreTipo = res.getString(10);
                //COMPROBAR SI EL NUMPOKEDEX YA EXISTE EN EL MAPA
                if (mapa.containsKey(numPokedex)) {
                    mapa.get(numPokedex).getTipos().add(new Tipo(idTipo, nombreTipo));
                } else {
                    List<Tipo> tipos = new ArrayList<>();
                    tipos.add(new Tipo(idTipo, nombreTipo));
                    Pokedex pox = new Pokedex(numPokedex, hpBase, ataqueBase, defensaBase, velocidadBase, nombre, tipos);
                    mapa.put(numPokedex, pox);
                }

            }
            con.close();
            s.close();
            res.close();
            return new ArrayList<>(mapa.values());
        } catch (SQLException error) {
            System.err.println(error);
            return null;
        }
    }

    //ESTE METODO NOS SIRVE PARA POKEMONMODELO
    public Pokedex leerPorId(int id) {
        Connection con = getConexion();
        sql = "SELECT * FROM pokedex JOIN pokedex_tipo ON pokedex.num_pokedex = pokedex_tipo.num_pokedex JOIN tipo ON pokedex_tipo.id_tipo = tipo.id WHERE pokedex.num_pokedex = ?";
        Pokedex pox = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            List<Tipo> tipos = new ArrayList<>();//PARA AÑADIR LOS TIPOS
            while (res.next()) {
                int numPokedex = res.getInt(1);
                String nombre = res.getString(2);
                int hpBase = res.getInt(3);
                int ataqueBase = res.getInt(4);
                int defensaBase = res.getInt(5);
                int velocidadBase = res.getInt(6);
                int idTipo = res.getInt(9);
                String nombreTipo = res.getString(10);
                if (pox == null) {
                    tipos.add(new Tipo(idTipo, nombreTipo));
                    pox = new Pokedex(numPokedex, hpBase, ataqueBase, defensaBase, velocidadBase, nombre, tipos);
                } else {
                    pox.getTipos().add(new Tipo(idTipo, nombreTipo));
                }
            }
            return pox;
        } catch (SQLException error) {
            System.err.println(error);
            return null;
        }
    }

    public boolean crear(Pokedex pok) throws SQLException {
        Connection con = getConexion();
        sql = "INSERT INTO POKEDEX(num_pokedex,nombre,hp_base,ataque_base,defensa_base,velocidad_base) VALUES(?,?,?,?,?,?)";
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pok.getNumPokedex());
            ps.setString(2, pok.getNombre());
            ps.setInt(3, pok.getHpBase());
            ps.setInt(4, pok.getAtaqueBase());
            ps.setInt(5, pok.getDefensaBase());
            ps.setInt(6, pok.getVelocidadBase());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);

            // insercion en pokedex_tipo
            sql = "INSERT INTO POKEDEX_TIPO(num_pokedex,id_tipo) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            for (Tipo t : pok.getTipos()) {
                ps.setInt(1, pok.getNumPokedex());
                ps.setInt(2, t.getId());
                ps.executeUpdate();
            }
            con.commit();
            ps.close();
            con.close();
            return true;
        } catch (SQLException error) {
            con.rollback();
            System.err.println(error);
            return false;
        }
    }

    public boolean eliminar(int numPokedex) {
        Connection con = getConexion();
        sql = "DELETE FROM POKEDEX WHERE num_pokedex = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, numPokedex);
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);

            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean actualizar(Pokedex pox) throws SQLException {
        Connection con = getConexion();
        sql = "UPDATE POKEDEX SET nombre = ?, hp_base = ?, ataque_base = ?, defensa_base = ?, velocidad_base = ? WHERE num_pokedex = ?";
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pox.getNombre());
            ps.setInt(2, pox.getHpBase());
            ps.setInt(3, pox.getAtaqueBase());
            ps.setInt(4, pox.getDefensaBase());
            ps.setInt(5, pox.getVelocidadBase());
            ps.setInt(6, pox.getNumPokedex());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);

            // BORRAR LOS TIPOS ACTUALES para evitar duplicados
            sql = "DELETE FROM POKEDEX_TIPO WHERE num_pokedex = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, pox.getNumPokedex());
            ps.executeUpdate();

            // INSERTAR LOS NUEVOS TIPOS
            sql = "INSERT INTO POKEDEX_TIPO(num_pokedex, id_tipo) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            for (Tipo tipo : pox.getTipos()) {
                ps.setInt(1, pox.getNumPokedex());
                ps.setInt(2, tipo.getId());
                ps.executeUpdate();
            }
            con.commit();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            con.rollback();
            System.err.println(e);
            return false;
        }
    }
}
