package modelo;

/**
 *
 * @author angelelbetico24
 */
public class Tipo {
    //Atributos
    int id;
    String nombre;
    
    //Constructor
    public Tipo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
