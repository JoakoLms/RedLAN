package main.dao.secuencialsql;

import main.dao.ConexionDAO;
import main.excepciones.ArchivoExistenteException;
import main.excepciones.ArchivoInexisteException;
import main.modelo.Conexion;
import main.modelo.Equipo;
import main.modelo.TipoCable;
import main.modelo.TipoPuerto;

import java.sql.*;
import java.util.*;

public class ConexionSqlDao implements ConexionDAO {

    private Connection getConnection() throws Exception {
        return ConexionDB.getConexion();
    }

    // Debes tener instancias de los DAOs SQL para obtener los objetos completos
    private EquipoSqlDAO equipoDAO = new EquipoSqlDAO();
    private TipoPuertoSqlDAO tipoPuertoDAO = new TipoPuertoSqlDAO();
    private TipoCableSqlDAO tipoCableDAO = new TipoCableSqlDAO();

    @Override
    public void insertar(Conexion conexion) throws ArchivoExistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement(
                "SELECT 1 FROM conexion WHERE equipo1=? AND tipo_puerto1=? AND equipo2=? AND tipo_puerto2=? AND tipo_cable=?"
            );
            check.setString(1, conexion.getEquipo1().getCodigo());
            check.setString(2, conexion.getTipoPuerto1().getCodigo());
            check.setString(3, conexion.getEquipo2().getCodigo());
            check.setString(4, conexion.getTipoPuerto2().getCodigo());
            check.setString(5, conexion.getTipoCable().getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new ArchivoExistenteException("La conexión ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO conexion (equipo1, tipo_puerto1, equipo2, tipo_puerto2, tipo_cable) VALUES (?, ?, ?, ?, ?)"
            );
            st.setString(1, conexion.getEquipo1().getCodigo());
            st.setString(2, conexion.getTipoPuerto1().getCodigo());
            st.setString(3, conexion.getEquipo2().getCodigo());
            st.setString(4, conexion.getTipoPuerto2().getCodigo());
            st.setString(5, conexion.getTipoCable().getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar conexión: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(Conexion conexion) throws ArchivoInexisteException {
        // Para actualizar, necesitas una clave primaria o identificador único en la tabla 'conexion'
        // Aquí se asume que los 5 campos forman la clave, pero lo ideal es tener un campo id
        throw new UnsupportedOperationException("Actualizar conexión requiere un identificador único.");
    }

    @Override
    public void borrar(Conexion conexion) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement(
                "SELECT 1 FROM conexion WHERE equipo1=? AND tipo_puerto1=? AND equipo2=? AND tipo_puerto2=? AND tipo_cable=?"
            );
            check.setString(1, conexion.getEquipo1().getCodigo());
            check.setString(2, conexion.getTipoPuerto1().getCodigo());
            check.setString(3, conexion.getEquipo2().getCodigo());
            check.setString(4, conexion.getTipoPuerto2().getCodigo());
            check.setString(5, conexion.getTipoCable().getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("La conexión no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "DELETE FROM conexion WHERE equipo1=? AND tipo_puerto1=? AND equipo2=? AND tipo_puerto2=? AND tipo_cable=?"
            );
            st.setString(1, conexion.getEquipo1().getCodigo());
            st.setString(2, conexion.getTipoPuerto1().getCodigo());
            st.setString(3, conexion.getEquipo2().getCodigo());
            st.setString(4, conexion.getTipoPuerto2().getCodigo());
            st.setString(5, conexion.getTipoCable().getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar conexión: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Conexion> buscarTodos() {
        List<Conexion> conexiones = new ArrayList<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM conexion");
            Map<String, Equipo> equipos = equipoDAO.buscarTodos();
            Map<String, TipoPuerto> puertos = tipoPuertoDAO.buscarTodos();
            Map<String, TipoCable> cables = tipoCableDAO.buscarTodos();

            while (rs.next()) {
                Equipo e1 = equipos.get(rs.getString("equipo1"));
                TipoPuerto tp1 = puertos.get(rs.getString("tipo_puerto1"));
                Equipo e2 = equipos.get(rs.getString("equipo2"));
                TipoPuerto tp2 = puertos.get(rs.getString("tipo_puerto2"));
                TipoCable tc = cables.get(rs.getString("tipo_cable"));
                if (e1 != null && tp1 != null && e2 != null && tp2 != null && tc != null) {
                    conexiones.add(new Conexion(e1, tp1, e2, tp2, tc));
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar conexiones: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conexiones;
    }
}