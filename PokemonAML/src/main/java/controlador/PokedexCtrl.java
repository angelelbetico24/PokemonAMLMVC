package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Pokedex;
import modelo.PokedexModelo;
import modelo.Tipo;
import vista.MenuPrincipal;
import vista.PokedexVista;

/**
 *
 * @author angel
 */
public class PokedexCtrl {

    PokedexVista vista;
    MenuPrincipal menu;
    PokedexModelo modelo;

    public PokedexCtrl(PokedexVista vista, MenuPrincipal menu) {
        this.vista = vista;
        this.menu = menu;
        modelo = new PokedexModelo();

        vista.jButtonActualizar.addActionListener(e -> actualizar());
        vista.jButtonAgregar.addActionListener(e -> agregar());
        vista.jButtonEliminar.addActionListener(e -> eliminar());
        vista.jButtonVolver.addActionListener(e -> volver());
        cargarTabla();
    }

    public void agregar() {
        String numPokedexString = vista.jTextFieldNumPok.getText();
        String nombre = vista.jTextFieldNombre.getText();
        String hpBaseString = vista.jTextFieldSalud.getText();
        String ataqueBaseString = vista.jTextFieldAtaque.getText();
        String defensaBaseString = vista.jTextFieldDefensa.getText();
        String velocidadBaseString = vista.jTextFieldVelocidad.getText();

        // Recoger tipos del ComboBox List<Tipo> 
        List<Tipo> tipos = new ArrayList<>();
        tipos = new ArrayList<>();
        int idTipo1 = vista.jComboBoxTipo1.getSelectedIndex() + 1;
        String nombreTipo1 = (String) vista.jComboBoxTipo1.getSelectedItem();
        tipos.add(new Tipo(idTipo1, nombreTipo1));

        // Solo añadir tipo 2 si no es "Ninguno"
        if (vista.jComboBoxTipo2.getSelectedIndex() != 0) {
            int idTipo2 = vista.jComboBoxTipo2.getSelectedIndex();
            String nombreTipo2 = (String) vista.jComboBoxTipo2.getSelectedItem();
            tipos.add(new Tipo(idTipo2, nombreTipo2));
        }
        try {
            int numPokedex = Integer.parseInt(numPokedexString);
            int hpBase = Integer.parseInt(hpBaseString);
            int ataqueBase = Integer.parseInt(ataqueBaseString);
            int defensaBase = Integer.parseInt(defensaBaseString);
            int velocidadBase = Integer.parseInt(velocidadBaseString);
            Pokedex p = new Pokedex(numPokedex, hpBase, ataqueBase, defensaBase, velocidadBase, nombre, tipos);
            if (modelo.crear(p)) {
                JOptionPane.showMessageDialog(null, "Pokemon agregado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Fallo a la hora de agregar el pokemon", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fallo a la hora de recoger datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        cargarTabla();
    }

    public void actualizar() {
        String numPokedexString = vista.jTextFieldNumPok.getText();
        String nombre = vista.jTextFieldNombre.getText();
        String hpBaseString = vista.jTextFieldSalud.getText();
        String ataqueBaseString = vista.jTextFieldAtaque.getText();
        String defensaBaseString = vista.jTextFieldDefensa.getText();
        String velocidadBaseString = vista.jTextFieldVelocidad.getText();

        // Recoger tipos del ComboBox List<Tipo> 
        List<Tipo> tipos = new ArrayList<>();
        tipos = new ArrayList<>();
        int idTipo1 = vista.jComboBoxTipo1.getSelectedIndex() + 1;
        String nombreTipo1 = (String) vista.jComboBoxTipo1.getSelectedItem();
        tipos.add(new Tipo(idTipo1, nombreTipo1));

        // Solo añadir tipo 2 si no es "Ninguno"
        if (vista.jComboBoxTipo2.getSelectedIndex() != 0) {
            int idTipo2 = vista.jComboBoxTipo2.getSelectedIndex();
            String nombreTipo2 = (String) vista.jComboBoxTipo2.getSelectedItem();
            tipos.add(new Tipo(idTipo2, nombreTipo2));
        }
        try {
            int numPokedex = Integer.parseInt(numPokedexString);
            int hpBase = Integer.parseInt(hpBaseString);
            int ataqueBase = Integer.parseInt(ataqueBaseString);
            int defensaBase = Integer.parseInt(defensaBaseString);
            int velocidadBase = Integer.parseInt(velocidadBaseString);
            Pokedex p = new Pokedex(numPokedex, hpBase, ataqueBase, defensaBase, velocidadBase, nombre, tipos);
            if (modelo.actualizar(p)) {
                JOptionPane.showMessageDialog(null, "Pokemon actualizado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Fallo a la hora de actualizar el pokemon", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fallo a la hora de recoger datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        cargarTabla();
    }

    public void eliminar() {
        String numPokeString = vista.jTextFieldNumPok.getText();
        try {
            int numPoke = Integer.parseInt(numPokeString);
            if (modelo.eliminar(numPoke)) {
                JOptionPane.showMessageDialog(null, "Pokemon eliminado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Fallo a la hora de eliminar el pokemon", "Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fallo a la hora de recoger datos", "Error", JOptionPane.ERROR_MESSAGE);

        }
        cargarTabla();
    }

    public void volver() {
        this.vista.setVisible(false);
        menu.setVisible(true);
    }

    public void cargarTabla() {
        List<Pokedex> pokedex = modelo.leer();
        DefaultTableModel model = (DefaultTableModel) vista.jTablePokedex.getModel();
        model.setRowCount(0);

        for (Pokedex pox : pokedex) {
            String tipos = pox.getTipos().stream()
                    .map(t -> t.getNombre())
                    .reduce((t1, t2) -> t1 + " / " + t2)
                    .orElse(""); //TODO ESO ES PARA QUE SALGA ASI: FUEGO/VOLADOR,ETC...

            model.addRow(new Object[]{
                pox.getNumPokedex(),
                pox.getNombre(),
                pox.getHpBase(),
                pox.getAtaqueBase(),
                pox.getDefensaBase(),
                pox.getVelocidadBase(),
                tipos
            });
        }
    }
}
