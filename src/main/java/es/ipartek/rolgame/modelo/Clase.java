package es.ipartek.rolgame.modelo;

public class Clase {

    private int id;
    private String nombre;

    public Clase() {

    }

    public Clase(String nombre) {
        this.nombre = nombre;
    }

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

    @Override
    public String toString() {
        return String.format("#%d %s ",
                id, nombre);
    }
}
