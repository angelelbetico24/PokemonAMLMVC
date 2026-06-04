package modelo;

/**
 *
 * @author angelelbetico24
 */
public class Movimiento {
    //Atributos
    Tipo tipo;
    int id, potencia,precisionAtaque,pp;
    String nombre;
    
    //Constructor
    public Movimiento(Tipo tipo, int id, int potencia, int precisionAtaque, int pp, String nombre) {
        this.tipo = tipo;
        this.id = id;
        this.potencia = potencia;
        this.precisionAtaque = precisionAtaque;
        this.pp = pp;
        this.nombre = nombre;
    }
    //Getters y setters
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public int getPrecisionAtaque() {
        return precisionAtaque;
    }

    public void setPrecisionAtaque(int precisionAtaque) {
        this.precisionAtaque = precisionAtaque;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
