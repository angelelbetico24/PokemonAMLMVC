package modelo;

import java.util.List;

/**
 *
 * @author angelelbetico24
 */
public class PokemonIndividual implements Combatiente{
    //Atributos
    int id,nivel,hpActual;
    Pokedex pokedex;
    Entrenador entrenador;
    String mote,genero;
    List<Movimiento> movimientos;
    
    //Constructor
    public PokemonIndividual(int id, int nivel, int hpActual, Pokedex pokedex, Entrenador entrenador, String mote, String genero, List<Movimiento> movimientos) {
        this.id = id;
        this.nivel = nivel;
        this.hpActual = hpActual;
        this.pokedex = pokedex;
        this.entrenador = entrenador;
        this.mote = mote;
        this.genero = genero;
        this.movimientos = movimientos;
    }
    
    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getHpActual() {
        return hpActual;
    }

    public void setHpActual(int hpActual) {
        this.hpActual = hpActual;
    }

    public Pokedex getPokedex() {
        return pokedex;
    }

    public void setPokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public String getMote() {
        return mote;
    }

    public void setMote(String mote) {
        this.mote = mote;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @Override
    public void recibirDano(int dano) {
        if (this.hpActual - dano < 0) {
            this.hpActual = 0;
        } else {
        this.hpActual -= dano;
    }
    }

    @Override
    public boolean estaDebilitado() {
        return this.hpActual <= 0;
    }
}
