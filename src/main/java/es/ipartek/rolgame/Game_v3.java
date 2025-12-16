package es.ipartek.rolgame;

import es.ipartek.rolgame.helpers.ConsoleHelper;
import es.ipartek.rolgame.modelo.Batalla;
import es.ipartek.rolgame.modelo.Clase;
import es.ipartek.rolgame.modelo.Personaje;
import es.ipartek.rolgame.servicios.Servicio;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game_v3 {

    public static void crearPersonaje() {
        try {
            Servicio servicio = new Servicio();
            List<Clase> clases = servicio.listarClases();
            Clase clase_seleccionada = null;
            do {
                System.out.println("Indica clase del personaje:");
                clases.forEach(System.out::println);
                int id_clase = ConsoleHelper.pedirEntero("Clase ID: ");
                clase_seleccionada = clases.stream().filter(c -> c.getId() == id_clase).findFirst().orElse(null);
                if ( clase_seleccionada == null ) System.out.println("Clase incorrecta. Vuelve a intentarlo.");
            } while (clase_seleccionada == null );
            String nombre = ConsoleHelper.pedirCadena("Nombre del personaje");
            servicio.crearPersonaje(new Personaje(nombre, clase_seleccionada));
        } catch (SQLException e) {
            System.out.println("Error creando personaje: " + e.getMessage());
        }
    }
    public static Personaje seleccionarPersonaje(String mensaje) {
        Personaje personaje = null;
        Servicio servicio = new Servicio();
        try {
            do {
                int id = ConsoleHelper.pedirEntero(mensaje);
                personaje = servicio.obtenerPersonaje(id);
                if (personaje == null) System.out.println("Personaje no existente.");
            } while (personaje == null);
        } catch (SQLException ex) {
            System.out.println("Error obteniendo listado de personajes");
        }
        return personaje;
    }

    public static void listar() {
        try {
            Servicio servicio = new Servicio();
            List<Personaje> personajes = servicio.listarPersonajes();
            if ( !personajes.isEmpty())
                personajes.forEach(System.out::println);
            else
                System.out.println("No hay personajes registrados");
        }
        catch( SQLException ex) {
            System.out.println("Error obteniendo listado de personajes. " + ex.getMessage());
        }
    }

    public static void combatir() {

        Personaje atacante;
        Personaje defensor;
        Personaje vencedor;
        Personaje vencido;

        try {
            Servicio servicio = new Servicio();

            // Seleccion de personajes
            List<Personaje> personajes = servicio.listarPersonajes();
            personajes.forEach(System.out::println);
            atacante = seleccionarPersonaje("Selecciona personaje atacante: ");
            defensor = seleccionarPersonaje("Selecciona personaje atacado: ");
            while( atacante.equals(defensor)) {
                System.out.println("El personaje atacante no puede ser el mismo que el atacado");
                defensor = seleccionarPersonaje("Selecciona personaje atacado: ");
            }

            // Calculo de valores de ataque
            float poderAtacante = atacante.calcularAtaque();
            float poderAtacado = defensor.calcularAtaque();
            int diferencia = (int)Math.abs(poderAtacante - poderAtacado);

            // visualizacion
            System.out.println("COMBATE " + atacante.getNombre() + " vs " + defensor.getNombre());
            System.out.println("ATACANTE: " + poderAtacante + " Puntos.");
            System.out.println("ATACADO: " + poderAtacado + " Puntos.");

            // Obtencion de victorioso y derrotado
            if ( poderAtacante > poderAtacado ) {
                vencedor = atacante;
                vencido = defensor;
            } else {
                vencedor = defensor;
                vencido = atacante;
            }

            // Daño
            vencido.pierdeVida(diferencia);

            // Anuncio vencedor
            int xpGanador  = (10 * vencedor.getNivel());
            vencedor.subirExperiencia(xpGanador);

            // Anuncio vencido
            if ( vencido.getVida() > 0) {
                int xpVencido = (3 * vencido.getNivel());
                vencido.subirExperiencia(xpVencido);
            }


            Batalla batalla = servicio.combatePersonaje(atacante, defensor, vencedor);
            System.out.println("Resumen Batalla: ");
            System.out.println(batalla);
            System.out.println("Estado final de los personajes: ");
            System.out.println(atacante);
            System.out.println(defensor);

        } catch (SQLException e) {
            System.out.println("Error gestionando combate: " + e.getMessage());
        }
    }

    public static void listarBatallas() {
        try {
            Servicio servicio = new Servicio();
            List<Batalla> batallas = servicio.listarBatallas();
            if (!batallas.isEmpty())
                batallas.forEach(System.out::println);
            else
                System.out.println("No hay batallas registradas");
        }
        catch( SQLException ex) {
            System.out.println("Error obteniendo listado de batallas. " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        do {
            System.out.println("\n=== RPG MANAGER ===");
            System.out.println("1. Crear personaje");
            System.out.println("2. Listar personajes");
            System.out.println("3. Combatir");
            System.out.println("4. Listar Batallas");
            System.out.println("0. Salir");
            int op = ConsoleHelper.pedirEntero("Selecciona opcion: ", 0, 4);
            switch (op) {
                case 1: {
                    crearPersonaje();
                }; break;
                case 2: {
                    listar();
                }; break;
                case 3: {
                    combatir();
                }; break;
                case 4: {
                    listarBatallas();
                }; break;
                case 0:
                    System.out.println("Fin de programa");
                    System.exit(0);
            }
        } while (true);
    }
}
