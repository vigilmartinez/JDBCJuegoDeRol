package es.ipartek.rolgame.dao;

import es.ipartek.rolgame.modelo.Clase;
import es.ipartek.rolgame.modelo.Personaje;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClaseDAO implements CrudDAO<Clase>{

    private Connection con;

    public ClaseDAO( Connection con ) {
        this.con = con;
    }

    @Override
    public void insertar(Clase obj) throws SQLException {
        throw new SQLException("No implementado");
    }

    @Override
    public List<Clase> listar() throws SQLException {
        List<Clase> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM clases ORDER BY id";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Clase c = new Clase();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public void eliminar(Clase obj) throws SQLException {
        throw new SQLException("No implementado");
    }

    @Override
    public void actualizar(Clase obj) throws SQLException {
        throw new SQLException("No implementado");
    }

    @Override
    public Clase obtener(long id) throws SQLException {
        Clase c = null;
        String sql = "SELECT id, nombre FROM clases WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if ( rs.next()) {
                c = new Clase();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
            }
        }
        return c;
    }
}
