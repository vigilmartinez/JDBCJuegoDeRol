package es.ipartek.rolgame.dao;

import es.ipartek.rolgame.modelo.Batalla;
import es.ipartek.rolgame.modelo.Personaje;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatallasDAO implements CrudDAO<Batalla> {

    private Connection con;

    public BatallasDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void insertar(Batalla obj) throws SQLException {
        String sql = "INSERT INTO batallas( id_atacante, id_defensor, id_vencedor, fecha, resumen ) value (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, obj.getAtacante().getId());
            ps.setInt(2, obj.getDefensor().getId());
            ps.setInt(3, obj.getVencedor().getId());
            ps.setTimestamp(4, Timestamp.valueOf(obj.getFecha()));
            ps.setString(5, obj.getResumen());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) obj.setId(rs.getInt(1));
        }
    }

    @Override
    public List<Batalla> listar() throws SQLException {
        List<Batalla> lista = new ArrayList<>();
        CrudDAO<Personaje> daoPersonajes = new PersonajeDAO(con);
        String sql = "SELECT id, id_atacante, id_defensor, id_vencedor, fecha, resumen FROM batallas ORDER BY id";
        try (Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Batalla b = new Batalla();
                b.setId(rs.getInt("id"));
                Personaje atacante = daoPersonajes.obtener(rs.getInt("id_atacante"));
                Personaje defensor = daoPersonajes.obtener(rs.getInt("id_defensor"));
                Personaje vencedor = daoPersonajes.obtener(rs.getInt("id_vencedor"));
                b.setAtacante(atacante);
                b.setDefensor(defensor);
                b.setVencedor(vencedor);
                b.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                b.setResumen(rs.getString("resumen"));
                lista.add(b);
            }
        }
        return lista;
    }

    @Override
    public void eliminar(Batalla obj) throws SQLException {
        throw new SQLException("Not implemented");
    }

    @Override
    public void actualizar(Batalla obj) throws SQLException {
        throw new SQLException("Not implemented");
    }

    @Override
    public Batalla obtener(long id) throws SQLException {
        Batalla b = null;
        CrudDAO<Personaje> daoPersonajes = new PersonajeDAO(con);
        String sql = "SELECT id, id_atacante, id_defensor, id_vencedor, fecha, resumen FROM batallas WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                b = new Batalla();
                b.setId(rs.getInt("id"));
                Personaje atacante = daoPersonajes.obtener(rs.getInt("id_atacante"));
                Personaje defensor = daoPersonajes.obtener(rs.getInt("id_defensor"));
                Personaje vencedor = daoPersonajes.obtener(rs.getInt("id_vencedor"));
                b.setAtacante(atacante);
                b.setDefensor(defensor);
                b.setVencedor(vencedor);
                b.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                b.setResumen(rs.getString("resumen"));
            }
        }
        return b;
    }
}
