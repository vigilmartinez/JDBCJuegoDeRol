package es.ipartek.rolgame.dao;

import es.ipartek.rolgame.datasource.Database;
import es.ipartek.rolgame.modelo.Clase;
import es.ipartek.rolgame.modelo.Personaje;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonajeDAO implements CrudDAO<Personaje> {

    private Connection con;

    public PersonajeDAO( Connection con ) {
        this.con = con;
    }

    @Override
    public void insertar(Personaje p) throws SQLException {
        String sql = "INSERT INTO personajes (nombre, id_clase, nivel, experiencia, vida) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getClase().getId());
            ps.setInt(3, p.getNivel());
            ps.setInt(4, p.getExperiencia());
            ps.setInt(5, p.getVida());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId(rs.getInt(1));
        }
    }

    @Override
    public List<Personaje> listar() throws SQLException {
        List<Personaje> lista = new ArrayList<>();
        CrudDAO<Clase> daoClases = new ClaseDAO(con);
        String sql = "SELECT id, nombre, id_clase, nivel, experiencia, vida FROM personajes WHERE vida > 0 ORDER BY id";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Personaje p = new Personaje();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                Clase c = daoClases.obtener(rs.getInt("id_clase"));
                p.setClase(c);
                p.setNivel(rs.getInt("nivel"));
                p.setExperiencia(rs.getInt("experiencia"));
                p.setVida(rs.getInt("vida"));
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public void eliminar(Personaje obj) throws SQLException {
        String sql = "DELETE FROM personajes WHERE id = ?";
        try ( PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, obj.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Personaje obj) throws SQLException {
        String sql = "UPDATE personajes SET nombre = ?, id_clase = ?, nivel = ?, experiencia = ?, vida = ? WHERE id = ?";
        try ( PreparedStatement ps = con.prepareStatement(sql) ) {
            ps.setString(1, obj.getNombre());
            ps.setInt(2, obj.getClase().getId());
            ps.setInt(3, obj.getNivel());
            ps.setInt(4, obj.getExperiencia());
            ps.setInt(5, obj.getVida());
            ps.setInt(6, obj.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Personaje obtener(long id) throws SQLException {
        Personaje p = null;
        String sql = "SELECT id, nombre, id_clase, nivel, experiencia, vida FROM personajes WHERE id = ?";
        CrudDAO<Clase> daoClases = new ClaseDAO(con);
        try (PreparedStatement st = con.prepareStatement(sql)) {
             st.setLong(1, id);
             ResultSet rs = st.executeQuery();
             if ( rs.next()) {
                 p = new Personaje();
                 p.setId(rs.getInt("id"));
                 p.setNombre(rs.getString("nombre"));
                 Clase c = daoClases.obtener(rs.getInt("id_clase"));
                 p.setClase(c);
                 p.setNivel(rs.getInt("nivel"));
                 p.setExperiencia(rs.getInt("experiencia"));
                 p.setVida(rs.getInt("vida"));
             }
        }
        return p;
    }
}
