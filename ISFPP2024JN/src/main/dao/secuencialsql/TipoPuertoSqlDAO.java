package main.dao.secuencialsql;

import main.dao.TipoPuertoDAO;
import main.excepciones.ArchivoExistenteException;
import main.excepciones.ArchivoInexisteException;
import main.modelo.TipoPuerto;

import java.sql.*;
import java.util.*;

public class TipoPuertoSqlDAO implements TipoPuertoDAO {

    private Connection getConnection() throws Exception {
        return ConexionDB.getConexion();
    }

    @Override
    public void insertar(TipoPuerto tipoPuerto) throws ArchivoExistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_puerto WHERE codigo = ?");
            check.setString(1, tipoPuerto.getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new ArchivoExistenteException("El tipo de puerto ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO tipo_puerto (codigo, descripcion, velocidad) VALUES (?, ?, ?)"
            );
            st.setString(1, tipoPuerto.getCodigo());
            st.setString(2, tipoPuerto.getDescripcion());
            st.setInt(3, tipoPuerto.getVelocidad());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar tipo de puerto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(TipoPuerto tipoPuerto) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_puerto WHERE codigo = ?");
            check.setString(1, tipoPuerto.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de puerto no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "UPDATE tipo_puerto SET descripcion=?, velocidad=? WHERE codigo=?"
            );
            st.setString(1, tipoPuerto.getDescripcion());
            st.setInt(2, tipoPuerto.getVelocidad());
            st.setString(3, tipoPuerto.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tipo de puerto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(TipoPuerto tipoPuerto) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_puerto WHERE codigo = ?");
            check.setString(1, tipoPuerto.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de puerto no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement("DELETE FROM tipo_puerto WHERE codigo=?");
            st.setString(1, tipoPuerto.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar tipo de puerto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeMap<String, TipoPuerto> buscarTodos() {
        TreeMap<String, TipoPuerto> tipos = new TreeMap<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tipo_puerto");
            while (rs.next()) {
                tipos.put(rs.getString("codigo"),
                    new TipoPuerto(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getInt("velocidad")
                    )
                );
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipos de puerto: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tipos;
    }
}