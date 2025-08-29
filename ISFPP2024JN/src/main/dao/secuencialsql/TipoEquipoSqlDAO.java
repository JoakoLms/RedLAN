package main.dao.secuencialsql;

import main.dao.TipoEquipoDAO;
import main.excepciones.ArchivoExistenteException;
import main.excepciones.ArchivoInexisteException;
import main.modelo.TipoEquipo;

import java.sql.*;
import java.util.*;

public class TipoEquipoSqlDAO implements TipoEquipoDAO {

    private Connection getConnection() throws Exception {
        return ConexionDB.getConexion();
    }

    @Override
    public void insertar(TipoEquipo tipoEquipo) throws ArchivoExistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_equipo WHERE codigo = ?");
            check.setString(1, tipoEquipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new ArchivoExistenteException("El tipo de equipo ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO tipo_equipo (codigo, descripcion) VALUES (?, ?)"
            );
            st.setString(1, tipoEquipo.getCodigo());
            st.setString(2, tipoEquipo.getDescripcion());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar tipo de equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(TipoEquipo tipoEquipo) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_equipo WHERE codigo = ?");
            check.setString(1, tipoEquipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de equipo no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "UPDATE tipo_equipo SET descripcion=? WHERE codigo=?"
            );
            st.setString(1, tipoEquipo.getDescripcion());
            st.setString(2, tipoEquipo.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tipo de equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(TipoEquipo tipoEquipo) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_equipo WHERE codigo = ?");
            check.setString(1, tipoEquipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de equipo no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement("DELETE FROM tipo_equipo WHERE codigo=?");
            st.setString(1, tipoEquipo.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar tipo de equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeMap<String, TipoEquipo> buscarTodos() {
        TreeMap<String, TipoEquipo> tipos = new TreeMap<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tipo_equipo");
            while (rs.next()) {
                tipos.put(rs.getString("codigo"),
                    new TipoEquipo(
                        rs.getString("codigo"),
                        rs.getString("descripcion")
                    )
                );
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipos de equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tipos;
    }
}