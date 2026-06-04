package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelelbetico24
 */
public class Pokedex {
    //Atributos
    int numPokedex,hpBase,ataqueBase,defensaBase,velocidadBase;
    String nombre;
    List<Tipo> tipos;

    //Constructor
    public Pokedex(int numPokedex, int hpBase, int ataqueBase, int defensaBase, int velocidadBase, String nombre, List<Tipo> tipos) {
        this.numPokedex = numPokedex;
        this.hpBase = hpBase;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.velocidadBase = velocidadBase;
        this.nombre = nombre;
        this.tipos = tipos;
    }
    
    //Getters y setters
    public int getNumPokedex() {
        return numPokedex;
    }

    public void setNumPokedex(int numPokedex) {
        this.numPokedex = numPokedex;
    }

    public int getHpBase() {
        return hpBase;
    }

    public void setHpBase(int hpBase) {
        this.hpBase = hpBase;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public void setAtaqueBase(int ataqueBase) {
        this.ataqueBase = ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public void setDefensaBase(int defensaBase) {
        this.defensaBase = defensaBase;
    }

    public int getVelocidadBase() {
        return velocidadBase;
    }

    public void setVelocidadBase(int velocidadBase) {
        this.velocidadBase = velocidadBase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public void setTipos(List<Tipo> tipos) {
        this.tipos = tipos;
    }
    
}
