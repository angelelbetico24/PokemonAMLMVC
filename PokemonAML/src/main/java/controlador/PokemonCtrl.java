package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.MenuPrincipal;
import vista.PokemonVista;

/**
 *
 * @author angel
 */
public class PokemonCtrl {

    PokemonVista vista;
    MenuPrincipal menu;
    PokemonModelo modelo;

    public PokemonCtrl(PokemonVista vista, MenuPrincipal menu) {
        this.vista = vista;
        this.menu = menu;
        this.modelo = new PokemonModelo();
        vista.jButtonActualizar.addActionListener(e -> actualizar());
        vista.jButtonAgregar.addActionListener(e -> agregar());
        vista.jButtonEliminar.addActionListener(e -> eliminar());
        vista.jButtonVolver.addActionListener(e -> volver());

        cargarTabla();
        cargarComboEntrenadores();
        cargarComboPokedex();
        cargarComboMovimientos();
        cargarComboGenero();
    }

    public void actualizar() {
        int fila = vista.jTablePokemons.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un pokemon");
            return;
        }
        try {
            int id = (int) vista.jTablePokemons.getValueAt(fila, 0);
            String mote = vista.jTextFieldMote.getText();
            int nivel = Integer.parseInt(vista.jTextFieldNivel.getText());
            int hpActual = Integer.parseInt(vista.jTextFieldSalud.getText());
            String genero = (String) vista.jComboBoxGenero.getSelectedItem();

            // Recoger entrenador y pokedex seleccionados
            int idEntrenador = (int) vista.jComboBoxEntrenador.getClientProperty("id_" + vista.jComboBoxEntrenador.getSelectedIndex());
            int numPokedex = (int) vista.jComboBoxPokedex.getClientProperty("id_" + vista.jComboBoxPokedex.getSelectedIndex());

            EntrenadorModelo entrModelo = new EntrenadorModelo();
            PokedexModelo pokModelo = new PokedexModelo();
            MovimientoModelo movModelo = new MovimientoModelo();

            Entrenador entrenador = entrModelo.leerPorId(idEntrenador);
            Pokedex pokedex = pokModelo.leerPorId(numPokedex);

            // Recoger movimientos seleccionados
            List<Movimiento> movimientos = new ArrayList<>();
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento1.getClientProperty("id_" + vista.jComboBoxMovimiento1.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento2.getClientProperty("id_" + vista.jComboBoxMovimiento2.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento3.getClientProperty("id_" + vista.jComboBoxMovimiento3.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento4.getClientProperty("id_" + vista.jComboBoxMovimiento4.getSelectedIndex())));

            PokemonIndividual pok = new PokemonIndividual(id, nivel, hpActual, pokedex, entrenador, mote, genero, movimientos);
            if (modelo.actualizar(pok)) {
                JOptionPane.showMessageDialog(null, "Pokemon actualizado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el pokemon", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al recoger datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void agregar() {
        try {
            // Recogemos los datos de los campos de texto
            String mote = vista.jTextFieldMote.getText();
            int nivel = Integer.parseInt(vista.jTextFieldNivel.getText());
            int hpActual = Integer.parseInt(vista.jTextFieldSalud.getText());
            String genero = (String) vista.jComboBoxGenero.getSelectedItem();

            // Recogemos entrenador y pokedex seleccionados usando ClientProperty
            int idEntrenador = (int) vista.jComboBoxEntrenador.getClientProperty("id_" + vista.jComboBoxEntrenador.getSelectedIndex());
            int numPokedex = (int) vista.jComboBoxPokedex.getClientProperty("id_" + vista.jComboBoxPokedex.getSelectedIndex());

            // Creamos los modelos auxiliares para obtener los objetos completos
            EntrenadorModelo entrModelo = new EntrenadorModelo();
            PokedexModelo pokModelo = new PokedexModelo();
            MovimientoModelo movModelo = new MovimientoModelo();

            // Obtenemos los objetos completos de la BD
            Entrenador entrenador = entrModelo.leerPorId(idEntrenador);
            Pokedex pokedex = pokModelo.leerPorId(numPokedex);

            // Recogemos los 4 movimientos seleccionados
            List<Movimiento> movimientos = new ArrayList<>();
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento1.getClientProperty("id_" + vista.jComboBoxMovimiento1.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento2.getClientProperty("id_" + vista.jComboBoxMovimiento2.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento3.getClientProperty("id_" + vista.jComboBoxMovimiento3.getSelectedIndex())));
            movimientos.add(movModelo.leerPorId((int) vista.jComboBoxMovimiento4.getClientProperty("id_" + vista.jComboBoxMovimiento4.getSelectedIndex())));

            // Creamos el pokemon con id 0 porque MySQL lo genera automaticamente
            PokemonIndividual pok = new PokemonIndividual(0, nivel, hpActual, pokedex, entrenador, mote, genero, movimientos);
            if (modelo.crear(pok)) {
                JOptionPane.showMessageDialog(null, "Pokemon agregado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo agregar el pokemon", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al recoger datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        int fila = vista.jTablePokemons.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un pokemon");
            return;
        }
        int id = (int) vista.jTablePokemons.getValueAt(fila, 0);
        if (modelo.eliminar(id)) {
            JOptionPane.showMessageDialog(null, "Pokemon eliminado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "El pokemon no se ha podido eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void volver() {
        this.vista.setVisible(false);
        menu.setVisible(true);
    }

    //METODOS AUXILIARES
    public void cargarTabla() {
        // Obtenemos la lista de pokemons de la BD
        List<PokemonIndividual> pokemons = modelo.leer();
        DefaultTableModel model = (DefaultTableModel) vista.jTablePokemons.getModel();
        // Limpiamos la tabla
        model.setRowCount(0);
        // Añadimos cada pokemon como una fila
        for (PokemonIndividual pok : pokemons) {
            model.addRow(new Object[]{
                pok.getId(),
                pok.getPokedex().getNumPokedex(),
                pok.getEntrenador().getId(),
                pok.getMote(),
                pok.getNivel(),
                pok.getHpActual(),
                pok.getGenero()
            });
        }
    }

    public void cargarComboEntrenadores() {
        // Obtenemos la lista de entrenadores de la BD
        EntrenadorModelo entrModelo = new EntrenadorModelo();
        List<Entrenador> entrenadores = entrModelo.leer();

        // Limpiamos el combo antes de cargar
        vista.jComboBoxEntrenador.removeAllItems();

        // Por cada entrenador añadimos su nombre al combo
        // y guardamos su id como propiedad para recuperarlo luego
        for (int i = 0; i < entrenadores.size(); i++) {
            Entrenador e = entrenadores.get(i);
            vista.jComboBoxEntrenador.addItem(e.getNombre());
            // Guardamos el id asociado al indice del combo
            vista.jComboBoxEntrenador.putClientProperty("id_" + i, e.getId());
        }
    }

    public void cargarComboPokedex() {
        // Obtenemos la lista de especies de la BD
        PokedexModelo pokModelo = new PokedexModelo();
        List<Pokedex> pokedex = pokModelo.leer();

        // Limpiamos el combo antes de cargar
        vista.jComboBoxPokedex.removeAllItems();

        // Por cada especie añadimos su nombre al combo
        // y guardamos su num_pokedex como propiedad
        for (int i = 0; i < pokedex.size(); i++) {
            Pokedex p = pokedex.get(i);
            vista.jComboBoxPokedex.addItem(p.getNombre());
            // Guardamos el num_pokedex asociado al indice del combo
            vista.jComboBoxPokedex.putClientProperty("id_" + i, p.getNumPokedex());
        }
    }

    public void cargarComboMovimientos() {
        // Obtenemos la lista de movimientos de la BD
        MovimientoModelo movModelo = new MovimientoModelo();
        List<Movimiento> movimientos = movModelo.leer();

        // Limpiamos los 4 combos antes de cargar
        vista.jComboBoxMovimiento1.removeAllItems();
        vista.jComboBoxMovimiento2.removeAllItems();
        vista.jComboBoxMovimiento3.removeAllItems();
        vista.jComboBoxMovimiento4.removeAllItems();

        // Por cada movimiento añadimos su nombre a los 4 combos
        // y guardamos su id como propiedad en cada uno
        for (int i = 0; i < movimientos.size(); i++) {
            Movimiento m = movimientos.get(i);
            vista.jComboBoxMovimiento1.addItem(m.getNombre());
            vista.jComboBoxMovimiento2.addItem(m.getNombre());
            vista.jComboBoxMovimiento3.addItem(m.getNombre());
            vista.jComboBoxMovimiento4.addItem(m.getNombre());
            // Guardamos el id asociado al indice en cada combo
            vista.jComboBoxMovimiento1.putClientProperty("id_" + i, m.getId());
            vista.jComboBoxMovimiento2.putClientProperty("id_" + i, m.getId());
            vista.jComboBoxMovimiento3.putClientProperty("id_" + i, m.getId());
            vista.jComboBoxMovimiento4.putClientProperty("id_" + i, m.getId());
            //EL PUT CLIENT PROPERTY FUNCIONA COMO UN HASHMAP DENTRO DE LOS COMPONENTES SWINGS
            //LO USO PARA  GUARDAR EL ID REAL  DE LA BD ASOCIADO A CADA POSICION DE LA COMBOBOX.
        }
    }

    public void cargarComboGenero() {
        // El genero es un valor fijo definido en el SQL como ENUM
        // asi que lo ponemos a mano sin consultar la BD
        vista.jComboBoxGenero.removeAllItems();
        vista.jComboBoxGenero.addItem("Macho");
        vista.jComboBoxGenero.addItem("Hembra");
        vista.jComboBoxGenero.addItem("Sin Género");
    }
}
