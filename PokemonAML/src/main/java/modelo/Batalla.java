package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author angel
 */
public class Batalla {

    Map<String, Map<String, Double>> efectividad = new HashMap<>();

    public Batalla() {
        /*
        ESTA LOGICA ES PARA LOS TIPOS DE POKEMONS, ALGUNOS SON MAS EFECTIVOS CONTRA OTROS
        ENTONCES LO MAS FACIL Y LEGIBLE PARA SABER CUANTO DE EFECTIVO ES ES PONIENDOLO EN UN HASHMAP
        FUEGO ES EFECTIVO CONTRA PLANTA, SU MULTIPLICADOR DE DAÑO SERIA X2 SEGUN MI LOGICA
        <3
         */

        Map<String, Double> fuego = new HashMap<>();
        fuego.put("Planta", 2.0);
        fuego.put("Agua", 0.5);
        fuego.put("Fuego", 0.5);
        efectividad.put("Fuego", fuego);

        Map<String, Double> agua = new HashMap<>();
        agua.put("Fuego", 2.0);
        agua.put("Planta", 0.5);
        agua.put("Agua", 0.5);
        efectividad.put("Agua", agua);

        Map<String, Double> electrico = new HashMap<>();
        electrico.put("Volador", 2.0);
        electrico.put("Agua", 2.0);
        electrico.put("Planta", 0.5);
        electrico.put("Eléctrico", 0.5);
        efectividad.put("Eléctrico", electrico);

        Map<String, Double> volador = new HashMap<>();
        volador.put("Planta", 2.0);
        volador.put("Eléctrico", 0.5);
        efectividad.put("Volador", volador);

        Map<String, Double> planta = new HashMap<>();
        planta.put("Agua", 2.0);
        planta.put("Fuego", 0.5);
        planta.put("Planta", 0.5);
        efectividad.put("Planta", planta);
    }

    public int calcularDano(PokemonIndividual pokAt, Movimiento mov, PokemonIndividual pokDef) {
        double multiplicador = 1.0;
        String tipoAtaque = mov.getTipo().getNombre();
        for (Tipo t : pokDef.getPokedex().getTipos()) {
            multiplicador *= efectividad.getOrDefault(tipoAtaque, new HashMap<>()).getOrDefault(t.getNombre(), 1.0);
        }
        // Formula simplificada basada en la real de Pokemon
        // Divide entre 50 para que el daño sea proporcional al HP
        double danoBase = ((2.0 * pokAt.getNivel() / 5 + 2) * mov.getPotencia() * pokAt.getPokedex().getAtaqueBase() / pokDef.getPokedex().getDefensaBase()) / 50;
        double dano = danoBase * multiplicador;
        return (int) dano;
    }

    public boolean atacar(PokemonIndividual pokAt, Movimiento mov, PokemonIndividual pokDef) {
        int dano = calcularDano(pokAt, mov, pokDef);
        pokDef.recibirDano(dano);
        return pokDef.estaDebilitado();
    }
}
