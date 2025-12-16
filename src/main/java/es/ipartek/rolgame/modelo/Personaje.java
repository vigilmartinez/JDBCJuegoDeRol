package es.ipartek.rolgame.modelo;

import java.util.Objects;
import java.util.Random;

public class Personaje {

    private static final int GUERRERO = 1;
    private static final int ARQUERO = 2;
    private static final int MAGO = 3;
    private static final int PALADIN = 4;


    private int id;
    private String nombre;
    private Clase clase;
    private int nivel;
    private int experiencia;
    private int vida;

    public Personaje() {}

    public Personaje(String nombre, Clase clase) {
        this.nombre = nombre;
        this.clase = clase;
        this.nivel = 1;
        this.experiencia = 0;
        this.vida = 100;
    }

    // Getters y setters
    // ...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Clase getClase() { return clase; }
    public void setClase(Clase clase) { this.clase = clase; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getExperiencia() { return experiencia; }
    public void setExperiencia(int experiencia) { this.experiencia = experiencia; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public void subirNivel() {
        this.nivel++;
    }

    public void pierdeVida( int cantidad ) {
        this.vida = Math.max((this.vida - cantidad), 0);
    }

    public void subirExperiencia( int experiencia ) {
        int experienciaSubirNivel = 100 + ( this.nivel * 5);
        this.experiencia+= experiencia;
        if ( this.experiencia > experienciaSubirNivel) {
            this.experiencia -= experienciaSubirNivel;
            this.nivel++;
        }
    }

    private float obtenerBonus() {
        return switch( clase.getId()) {
            case GUERRERO -> this.nivel * 3.0f;
            case MAGO -> this.experiencia / 5.0f;
            case ARQUERO -> (float)(Math.random() * 20);
            case PALADIN -> this.vida / 10.0f;
            default -> 0.0f;
        };
    }

    public float calcularAtaque(){
        Random rnd = new Random();
        return (this.getNivel() * 10.0f) + (this.getVida() / 5.0f) + this.obtenerBonus() + rnd.nextInt(20);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Personaje personaje = (Personaje) o;
        return id == personaje.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("#%d %s [%s] - Nivel %d (EXP %d, Vida %d)",
                id, nombre, clase.getNombre(), nivel, experiencia, vida);
    }
}
