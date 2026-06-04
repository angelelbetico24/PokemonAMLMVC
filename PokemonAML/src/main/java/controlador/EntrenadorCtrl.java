package controlador;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Entrenador;
import modelo.EntrenadorModelo;
import vista.EntrenadorVista;
import vista.MenuPrincipal;

/**
 *
 * @author angel
 */
public class EntrenadorCtrl {
    MenuPrincipal menuPrin;
    EntrenadorVista vista;
    EntrenadorModelo modelo;
    
    
    public EntrenadorCtrl(EntrenadorVista vista, MenuPrincipal menuPrin) {
        this.vista = vista;
        this.menuPrin = menuPrin;
        modelo = new EntrenadorModelo();
        vista.jButtonAgregar.addActionListener(e -> agregar());
        vista.jButtonActualizar.addActionListener(e -> actualizar());
        vista.jButtonEliminar.addActionListener(e -> eliminar());
        vista.jButtonVolver.addActionListener(e -> volver());
        cargarTabla();
    }

    public void agregar() {
        String nombre = vista.jTextFieldNombre.getText();
        String ciudad = vista.jTextFieldCiudad.getText();
        Entrenador e = new Entrenador(0, nombre, ciudad);
        if (modelo.crear(e)) {
            JOptionPane.showMessageDialog(null, "Entrenador agregado correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Fallo a la hora de agregar el entrenador", "Error", JOptionPane.ERROR_MESSAGE);
        }
        cargarTabla();
    }

    public void actualizar() {
        int fila = vista.jTableEntrenadores.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un entrenador");
            return;
        }
        
        String nombre = vista.jTextFieldNombre.getText();
        String ciudad = vista.jTextFieldCiudad.getText();
        int id = (int) vista.jTableEntrenadores.getValueAt(fila, 0);
        Entrenador e = new Entrenador(id, nombre, ciudad);
        if (modelo.actualizar(e)) {
            JOptionPane.showMessageDialog(null, "Entrenador actualizado correctamente","Informacion",JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        }else{
            JOptionPane.showMessageDialog(null, "El entrenador no se ha podido actualizar","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar() {
        int fila = vista.jTableEntrenadores.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona un entrenador");
            return; 
        }
        int id = (int) vista.jTableEntrenadores.getValueAt(fila, 0);
        if (modelo.eliminar(id)) {
            JOptionPane.showMessageDialog(null, "Entrenador eliminado","Informacion",JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        }else{
            JOptionPane.showMessageDialog(null, "El entrenador no se ha podido eliminar","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void volver(){
        this.vista.setVisible(false);
        menuPrin.setVisible(true);
    }
    //Metodo auxiliar
    public void cargarTabla() {
        List<Entrenador> entrenadores = modelo.leer();

        DefaultTableModel model
                = (DefaultTableModel) vista.jTableEntrenadores.getModel();

        // Vaciar la tabla
        model.setRowCount(0);

        // Añadir los datos de la BD
        for (Entrenador entrenador : entrenadores) {
            model.addRow(new Object[]{
                entrenador.getId(),
                entrenador.getNombre(),
                entrenador.getCiudad()
            });
        }
    }
}
