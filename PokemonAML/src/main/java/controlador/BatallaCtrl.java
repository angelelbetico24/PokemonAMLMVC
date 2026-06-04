/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.List;
import javax.swing.JOptionPane;
import modelo.*;
import vista.BatallaVista;
import vista.MenuPrincipal;

/**
 *
 * @author angel
 */
public class BatallaCtrl {

    BatallaVista vista;
    MenuPrincipal menu;
    PokemonModelo modelo;
    Batalla batalla;

    public BatallaCtrl(BatallaVista vista, MenuPrincipal menu) {
        this.vista = vista;
        this.menu = menu;
        this.modelo = new PokemonModelo();
        this.batalla = new Batalla();

        vista.jButtonIniciar.addActionListener(e -> iniciarBatalla());
        vista.jButtonVolver.addActionListener(e -> volver());

        cargarCombosPokemons();
    }

    public void cargarCombosPokemons() {
        // Obtenemos todos los pokemons de la BD
        List<PokemonIndividual> pokemons = modelo.leer();

        vista.jComboBoxPokemon1.removeAllItems();
        vista.jComboBoxPokemon2.removeAllItems();

        for (int i = 0; i < pokemons.size(); i++) {
            PokemonIndividual pok = pokemons.get(i);
            // Mostramos el mote o el nombre de la especie si no tiene mote
            String nombre = (pok.getMote() != null && !pok.getMote().isEmpty())
                    ? pok.getMote()
                    : pok.getPokedex().getNombre();
            vista.jComboBoxPokemon1.addItem(nombre);
            vista.jComboBoxPokemon2.addItem(nombre);
            // Guardamos el indice para recuperar el pokemon completo luego
            vista.jComboBoxPokemon1.putClientProperty("id_" + i, i);
            vista.jComboBoxPokemon2.putClientProperty("id_" + i, i);
        }

        // Guardamos la lista completa para usarla en la batalla
        vista.jComboBoxPokemon1.putClientProperty("lista", pokemons);
        vista.jComboBoxPokemon2.putClientProperty("lista", pokemons);

        // Cargamos los movimientos del pokemon seleccionado por defecto
        cargarMovimientos(pokemons);
    }

    public void cargarMovimientos(List<PokemonIndividual> pokemons) {
        if (pokemons.isEmpty()) {
            return;
        }

        // Cargamos movimientos del pokemon 1
        PokemonIndividual pok1 = pokemons.get(0);
        cargarMovimientosPokemon(pok1,
                vista.jComboBoxMov1Pok1, vista.jComboBoxMov2Pok1,
                vista.jComboBoxMov3Pok1, vista.jComboBoxMov4Pok1);

        // Cargamos movimientos del pokemon 2
        PokemonIndividual pok2 = pokemons.get(pokemons.size() > 1 ? 1 : 0);
        cargarMovimientosPokemon(pok2,
                vista.jComboBoxMov1Pok2, vista.jComboBoxMov2Pok2,
                vista.jComboBoxMov3Pok2, vista.jComboBoxMov4Pok2);
    }

    private void cargarMovimientosPokemon(PokemonIndividual pok,
            javax.swing.JComboBox<String> mov1, javax.swing.JComboBox<String> mov2,
            javax.swing.JComboBox<String> mov3, javax.swing.JComboBox<String> mov4) {
        // Limpiamos los combos
        mov1.removeAllItems();
        mov2.removeAllItems();
        mov3.removeAllItems();
        mov4.removeAllItems();

        List<Movimiento> movimientos = pok.getMovimientos();
        javax.swing.JComboBox<String>[] combos = new javax.swing.JComboBox[]{mov1, mov2, mov3, mov4};

        // Cargamos cada movimiento en su combo correspondiente
        for (int i = 0; i < combos.length; i++) {
            if (i < movimientos.size()) {
                combos[i].addItem(movimientos.get(i).getNombre());
                combos[i].putClientProperty("movimiento", movimientos.get(i));
            } else {
                // Si el pokemon tiene menos de 4 movimientos ponemos "Sin movimiento"
                combos[i].addItem("Sin movimiento");
                combos[i].putClientProperty("movimiento", null);
            }
        }
    }

    public void iniciarBatalla() {
        // Recuperamos los pokemons seleccionados
        List<PokemonIndividual> pokemons = (List<PokemonIndividual>) vista.jComboBoxPokemon1.getClientProperty("lista");

        int idx1 = vista.jComboBoxPokemon1.getSelectedIndex();
        int idx2 = vista.jComboBoxPokemon2.getSelectedIndex();

        if (idx1 == idx2) {
            JOptionPane.showMessageDialog(null, "Selecciona dos pokemons distintos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PokemonIndividual pok1 = pokemons.get(idx1);
        PokemonIndividual pok2 = pokemons.get(idx2);

        // Comprobamos que los pokemons tienen movimientos
        if (pok1.getMovimientos().isEmpty() || pok2.getMovimientos().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los pokemons deben tener al menos un movimiento", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determinamos quien ataca primero segun la velocidad base
        PokemonIndividual primero, segundo;
        if (pok1.getPokedex().getVelocidadBase() >= pok2.getPokedex().getVelocidadBase()) {
            primero = pok1;
            segundo = pok2;
        } else {
            primero = pok2;
            segundo = pok1;
        }

        // Limpiamos el log
        vista.jTextAreaLog.setText("");
        vista.jTextAreaLog.append("¡Empieza la batalla!\n");
        vista.jTextAreaLog.append(primero.getPokedex().getNombre() + " ataca primero por tener más velocidad\n\n");

        // Simulamos la batalla turno a turno hasta que uno se debilite
        int turno = 1;
        while (!primero.estaDebilitado() && !segundo.estaDebilitado()) {
            // Elegimos un movimiento aleatorio de la lista del atacante
            if (turno % 2 != 0) {
                // Turno impar: ataca el primero (mas velocidad)
                Movimiento mov = primero.getMovimientos().get(
                        (int) (Math.random() * primero.getMovimientos().size())
                );
                int dano = batalla.calcularDano(primero, mov, segundo);
                batalla.atacar(primero, mov, segundo);
                vista.jTextAreaLog.append("Turno " + turno + ": " + primero.getPokedex().getNombre()
                        + " usa " + mov.getNombre()
                        + " y hace " + dano + " de daño a "
                        + segundo.getPokedex().getNombre()
                        + " (HP restante: " + segundo.getHpActual() + ")\n");
            } else {
                // Turno par: ataca el segundo
                Movimiento mov = segundo.getMovimientos().get(
                        (int) (Math.random() * segundo.getMovimientos().size())
                );
                int dano = batalla.calcularDano(segundo, mov, primero);
                batalla.atacar(segundo, mov, primero);
                vista.jTextAreaLog.append("Turno " + turno + ": " + segundo.getPokedex().getNombre()
                        + " usa " + mov.getNombre()
                        + " y hace " + dano + " de daño a "
                        + primero.getPokedex().getNombre()
                        + " (HP restante: " + primero.getHpActual() + ")\n");
            }
            turno++;
        }

        // Mostramos el ganador
        String ganador = primero.estaDebilitado()
                ? segundo.getPokedex().getNombre()
                : primero.getPokedex().getNombre();
        vista.jTextAreaLog.append("\n¡" + ganador + " ha ganado la batalla!\n");
    }

    public void volver() {
        vista.setVisible(false);
        menu.setVisible(true);
    }

}
