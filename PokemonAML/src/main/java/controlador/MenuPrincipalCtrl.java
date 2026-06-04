package controlador;

import vista.*;

/**
 *
 * @author angel
 */
public class MenuPrincipalCtrl {
     MenuPrincipal vista;

    public MenuPrincipalCtrl(MenuPrincipal vista) {
        this.vista = vista;

        vista.jButtonEntrenadores.addActionListener(e -> abrirEntrenadores());
        vista.jButtonPokedex.addActionListener(e -> abrirPokedex());
        vista.jButtonPokemons.addActionListener(e -> abrirPokemon());
        vista.jButtonBatalla.addActionListener(e -> abrirBatalla());
    }

    private void abrirEntrenadores() {
        vista.setVisible(false);
        EntrenadorVista v = new EntrenadorVista();
        new EntrenadorCtrl(v,vista);
        v.setVisible(true);
    }

    private void abrirPokedex() {
        vista.setVisible(false);
        PokedexVista v = new PokedexVista();
        new PokedexCtrl(v,vista);
        v.setVisible(true);
    }

    private void abrirPokemon() {
        vista.setVisible(false);
        PokemonVista v = new PokemonVista();
        new PokemonCtrl(v,vista);
        v.setVisible(true);
    }

    private void abrirBatalla() {
        vista.setVisible(false);
        BatallaVista v = new BatallaVista();
        new BatallaCtrl(v,vista);
        v.setVisible(true);
    }
}
