package main.dao.secuencialsql;

import main.dao.UbicacionDAO;
import main.excepciones.ArchivoExistenteException;
import main.excepciones.ArchivoInexisteException;
import main.modelo.Ubicacion;

import java.sql.*;
import java.util.*;

public class UbicacionSqlDAO implements UbicacionDAO {

    private Connection getConnection() throws Exception {
        return ConexionDB.getConexion();
    }

    @Override
    public void insertar(Ubicacion ubicacion) throws ArchivoExistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM ubicacion WHERE codigo = ?");
            check.setString(1, ubicacion.getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new ArchivoExistenteException("La ubicación ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO ubicacion (codigo, descripcion) VALUES (?, ?)"
            );
            st.setString(1, ubicacion.getCodigo());
            st.setString(2, ubicacion.getDescripcion());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar ubicación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(Ubicacion ubicacion) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM ubicacion WHERE codigo = ?");
            check.setString(1, ubicacion.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("La ubicación no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "UPDATE ubicacion SET descripcion=? WHERE codigo=?"
            );
            st.setString(1, ubicacion.getDescripcion());
            st.setString(2, ubicacion.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ubicación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(Ubicacion ubicacion) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM ubicacion WHERE codigo = ?");
            check.setString(1, ubicacion.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("La ubicación no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement("DELETE FROM ubicacion WHERE codigo=?");
            st.setString(1, ubicacion.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar ubicación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeMap<String, Ubicacion> buscarTodos() {
        TreeMap<String, Ubicacion> ubicaciones = new TreeMap<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ubicacion");
            while (rs.next()) {
                ubicaciones.put(rs.getString("codigo"),
                    new Ubicacion(
                        rs.getString("codigo"),
                        rs.getString("descripcion")
                    )
                );
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ubicaciones: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ubicaciones;
    }
}