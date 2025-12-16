package es.ipartek.rolgame.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Batalla {
    private int id;
    private LocalDateTime fecha;
    private String resumen;

    private Personaje atacante;
    private Personaje defensor;
    private Personaje vencedor;

    public Batalla() {}

    public Batalla(int id, Personaje atacante, Personaje defensor, Personaje vencedor, LocalDateTime fecha, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.atacante = atacante;
        this.defensor = defensor;
        this.vencedor = vencedor;
        this.resumen = mensaje;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getResumen() {
        return resumen;
    }

    public Personaje getAtacante() {
        return atacante;
    }

    public void setAtacante(Personaje atacante) {
        this.atacante = atacante;
    }

    public Personaje getDefensor() {
        return defensor;
    }

    public void setDefensor(Personaje defensor) {
        this.defensor = defensor;
    }

    public Personaje getVencedor() {
        return vencedor;
    }

    public void setVencedor(Personaje vencedor) {
        this.vencedor = vencedor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Batalla batalla)) return false;
        return id == batalla.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        return String.format("#%d El ( %s ) Combate %s VS %s Vendedor %s. %s",
                id,
                format.format(this.fecha),
                atacante.getNombre(),
                defensor.getNombre(),
                vencedor.getNombre(),
                resumen );
    }
}
