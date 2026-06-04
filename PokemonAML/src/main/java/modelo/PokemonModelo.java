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
public class PokemonModelo extends ConexionBD {

    String sql;

    public List<PokemonIndividual> leer() {
        sql = "SELECT * FROM pokemon_individual "
                + "JOIN pokedex ON pokemon_individual.num_pokedex = pokedex.num_pokedex "
                + "JOIN entrenador ON pokemon_individual.id_entrenador = entrenador.id "
                + "JOIN pokemon_movimiento ON pokemon_individual.id = pokemon_movimiento.id_pokemon "
                + "JOIN movimiento ON pokemon_movimiento.id_movimiento = movimiento.id "
                + "JOIN tipo ON movimiento.id_tipo = tipo.id";
        Connection con = getConexion();
        List<PokemonIndividual> listaPokemons = new ArrayList<>();
        Map<Integer, PokemonIndividual> mapa = new LinkedHashMap<>();
        try {
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery(sql);
            while (res.next()) {
                //TABLA POKEMONINDIVIDUAL
                int id = res.getInt(1);
                int numPokedex = res.getInt(2);
                int idEntrenador = res.getInt(3);
                String mote = res.getString(4);
                int nivel = res.getInt(5);
                int hpActual = res.getInt(6);
                String genero = res.getString(7);

                //TABLA POKEDEX
                PokedexModelo pokMo = new PokedexModelo();
                Pokedex pok = pokMo.leerPorId(numPokedex);

                //TABLA ENTRENADOR
                EntrenadorModelo entrMo = new EntrenadorModelo();
                Entrenador e = entrMo.leerPorId(idEntrenador);

                //TABLA MOVIMIENTO
                int idMovimiento = res.getInt(18);

                MovimientoModelo movMo = new MovimientoModelo();
                Movimiento mov = movMo.leerPorId(idMovimiento);

                if (mapa.containsKey(id)) {
                    mapa.get(id).getMovimientos().add(mov);
                } else {
                    List<Movimiento> movs = new ArrayList<>();
                    movs.add(mov);
                    PokemonIndividual pokInd = new PokemonIndividual(id, nivel, hpActual, pok, e, mote, genero, movs);
                    mapa.put(id, pokInd);
                }
            }
            con.close();
            s.close();
            res.close();
            return new ArrayList<>(mapa.values());
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public boolean crear(PokemonIndividual pok) throws SQLException {
        Connection con = getConexion();
        int idPokemon = 0;
        sql = "INSERT INTO pokemon_individual(num_pokedex, id_entrenador, mote, nivel, hp_actual, genero) VALUES(?,?,?,?,?,?)";
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pok.getPokedex().getNumPokedex());
            ps.setInt(2, pok.getEntrenador().getId());
            ps.setString(3, pok.getMote());
            ps.setInt(4, pok.getNivel());
            ps.setInt(5, pok.getHpActual());
            ps.setString(6, pok.getGenero());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);
            ResultSet ids = ps.getGeneratedKeys();
            while (ids.next()) {
                idPokemon = ids.getInt(1);
            }
            sql = "INSERT INTO pokemon_movimiento(id_pokemon, id_movimiento) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            for (Movimiento m : pok.getMovimientos()) {
                ps.setInt(1, idPokemon);
                ps.setInt(2, m.getId());
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

    public boolean actualizar(PokemonIndividual pok) throws SQLException {
        Connection con = getConexion();
        sql = "UPDATE pokemon_individual SET id_entrenador = ?, mote = ?, nivel = ?, hp_actual = ?, genero = ? WHERE id = ?";
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pok.getEntrenador().getId());
            ps.setString(2, pok.getMote());
            ps.setInt(3, pok.getNivel());
            ps.setInt(4, pok.getHpActual());
            ps.setString(5, pok.getGenero());
            ps.setInt(6, pok.getId());
            int rows = ps.executeUpdate();
            System.out.println("Numero de filas afectadas: " + rows);
            //BORRAR MOVIMIENTOS ACTUALES (MAS FACIL=
            sql = "DELETE FROM pokemon_movimiento WHERE id_pokemon = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, pok.getId());
            ps.executeUpdate();

            //INSERTAR NUEVOS MOVIMIENTOS
            sql = "INSERT INTO pokemon_movimiento(id_pokemon, id_movimiento) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            for (Movimiento movs : pok.getMovimientos()) {
                ps.setInt(1, pok.getId());
                ps.setInt(2, movs.getId());
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

    public boolean eliminar(int id) {
        sql = "DELETE FROM pokemon_individual WHERE id = ?";
        Connection con = getConexion();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
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
}
