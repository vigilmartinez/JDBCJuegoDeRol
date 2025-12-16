package es.ipartek.rolgame.servicios;

import es.ipartek.rolgame.dao.BatallasDAO;
import es.ipartek.rolgame.dao.ClaseDAO;
import es.ipartek.rolgame.dao.CrudDAO;
import es.ipartek.rolgame.dao.PersonajeDAO;
import es.ipartek.rolgame.datasource.Database;
import es.ipartek.rolgame.modelo.Batalla;
import es.ipartek.rolgame.modelo.Clase;
import es.ipartek.rolgame.modelo.Personaje;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Servicio {

    public void crearPersonaje( Personaje personaje ) throws SQLException {
        try ( Connection con = Database.getConexion()) {
            CrudDAO<Personaje>dao = new PersonajeDAO(con);
            dao.insertar(personaje);
        }
    }

    public List<Clase> listarClases() throws SQLException {
        try (Connection con = Database.getConexion()) {
            CrudDAO<Clase>dao = new ClaseDAO(con);
            return dao.listar();
        }
    }

    public List<Personaje> listarPersonajes() throws SQLException {
        try ( Connection con = Database.getConexion()) {
            CrudDAO<Personaje>dao = new PersonajeDAO(con);
            return dao.listar();
        }
    }

    public Personaje obtenerPersonaje( long id ) throws SQLException {
        try ( Connection con = Database.getConexion()) {
            CrudDAO<Personaje>dao = new PersonajeDAO(con);
            return dao.obtener(id);
        }
    }


    public List<Batalla> listarBatallas() throws SQLException {
        List<Batalla> batallas = new ArrayList<>();
        try ( Connection con = Database.getConexion()) {
            CrudDAO<Batalla> daoBatallas = new BatallasDAO(con);
            batallas = daoBatallas.listar();
        }
        return batallas;
    }

    public Batalla combatePersonaje(Personaje atacante, Personaje defensor, Personaje vencedor) throws SQLException {
        Batalla batalla = null;
        try (Connection con = Database.getConexion()) {

            CrudDAO<Personaje> daoPersonajes = new PersonajeDAO(con);
            CrudDAO<Batalla> daoBatallas = new BatallasDAO(con);
            try {
                // Deshabilita auto-commit
                con.setAutoCommit(false);
                batalla = new Batalla();
                batalla.setAtacante(atacante);
                batalla.setDefensor(defensor);
                batalla.setVencedor(vencedor);
                batalla.setFecha(LocalDateTime.now());
                String resumen = "";

                daoPersonajes.actualizar(atacante);
                daoPersonajes.actualizar(defensor);

                if (atacante.getVida() == 0)
                    resumen = resumen.concat( atacante.getNombre() + " Murio." );
                if (defensor.getVida() == 0)
                    resumen = resumen.concat(defensor.getNombre() + " Murio.");

                batalla.setResumen(resumen);
                daoBatallas.insertar(batalla);
                con.commit();                   // Confirma transacción
            } catch ( SQLException ex ) {
                con.rollback();                 // Deshace transacción
                throw ex;
            }
        }
        return batalla;
    }
}