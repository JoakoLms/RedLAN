package main.dao.secuencialsql;

import main.dao.TipoCableDAO;
import main.excepciones.ArchivoExistenteException;
import main.excepciones.ArchivoInexisteException;
import main.modelo.TipoCable;

import java.sql.*;
import java.util.*;

public class TipoCableSqlDAO implements TipoCableDAO {

    private Connection getConnection() throws Exception {
        return ConexionDB.getConexion();
    }

    @Override
    public void insertar(TipoCable tipoCable) throws ArchivoExistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_cable WHERE codigo = ?");
            check.setString(1, tipoCable.getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new ArchivoExistenteException("El tipo de cable ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO tipo_cable (codigo, descripcion, velocidad) VALUES (?, ?, ?)"
            );
            st.setString(1, tipoCable.getCodigo());
            st.setString(2, tipoCable.getDescripcion());
            st.setInt(3, tipoCable.getVelocidad());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar tipo de cable: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(TipoCable tipoCable) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_cable WHERE codigo = ?");
            check.setString(1, tipoCable.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de cable no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "UPDATE tipo_cable SET descripcion=?, velocidad=? WHERE codigo=?"
            );
            st.setString(1, tipoCable.getDescripcion());
            st.setInt(2, tipoCable.getVelocidad());
            st.setString(3, tipoCable.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tipo de cable: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(TipoCable tipoCable) throws ArchivoInexisteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM tipo_cable WHERE codigo = ?");
            check.setString(1, tipoCable.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ArchivoInexisteException("El tipo de cable no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement("DELETE FROM tipo_cable WHERE codigo=?");
            st.setString(1, tipoCable.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar tipo de cable: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeMap<String, TipoCable> buscarTodos() {
        TreeMap<String, TipoCable> tipos = new TreeMap<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tipo_cable");
            while (rs.next()) {
                tipos.put(rs.getString("codigo"),
                    new TipoCable(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getInt("velocidad")
                    )
                );
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipos de cable: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tipos;
    }
}