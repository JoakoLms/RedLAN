package main.dao.secuencialsql;

import main.dao.EquipoDAO;
import main.excepciones.EquipoExistenteException;
import main.excepciones.EquipoInexistenteException;
import main.modelo.Equipo;

import java.sql.*;
import java.util.*;

public class EquipoSqlDAO implements EquipoDAO {

    private Connection getConnection() throws Exception {
        // Ajusta la ruta si tu ConexionDB está en otro paquete
        return ConexionDB.getConexion();
    }

    @Override
    public void insertar(Equipo equipo) throws EquipoExistenteException {
        try (Connection con = getConnection()) {
            // Verifica si ya existe
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM equipo WHERE codigo = ?");
            check.setString(1, equipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                throw new EquipoExistenteException("El equipo ya existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "INSERT INTO equipo (codigo, descripcion, marca, modelo, tipo_equipo, ubicacion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            st.setString(1, equipo.getCodigo());
            st.setString(2, equipo.getDescripcion());
            st.setString(3, equipo.getMarca());
            st.setString(4, equipo.getModelo());
            st.setString(5, equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getCodigo() : null);
            st.setString(6, equipo.getUbicacion() != null ? equipo.getUbicacion().getCodigo() : null);
            st.setBoolean(7, equipo.getEstado());
            st.executeUpdate();
            st.close();
            // Aquí deberías insertar también los puertos y direcciones IP en sus tablas relacionadas
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizar(Equipo equipo, Equipo equipoModificado) throws EquipoInexistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM equipo WHERE codigo = ?");
            check.setString(1, equipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new EquipoInexistenteException("El equipo no existe");
            }
            rs.close();
            check.close();

            PreparedStatement st = con.prepareStatement(
                "UPDATE equipo SET descripcion=?, marca=?, modelo=?, tipo_equipo=?, ubicacion=?, estado=? WHERE codigo=?"
            );
            st.setString(1, equipoModificado.getDescripcion());
            st.setString(2, equipoModificado.getMarca());
            st.setString(3, equipoModificado.getModelo());
            st.setString(4, equipoModificado.getTipoEquipo() != null ? equipoModificado.getTipoEquipo().getCodigo() : null);
            st.setString(5, equipoModificado.getUbicacion() != null ? equipoModificado.getUbicacion().getCodigo() : null);
            st.setBoolean(6, equipoModificado.getEstado());
            st.setString(7, equipo.getCodigo());
            st.executeUpdate();
            st.close();
            // Actualiza también puertos y direcciones IP si corresponde
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrar(Equipo equipo) throws EquipoInexistenteException {
        try (Connection con = getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT 1 FROM equipo WHERE codigo = ?");
            check.setString(1, equipo.getCodigo());
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new EquipoInexistenteException("El equipo no existe");
            }
            rs.close();
            check.close();

            // Borra primero las relaciones dependientes (puertos, IPs, etc.) si existen
            // Por ejemplo: con.prepareStatement("DELETE FROM equipo_puerto WHERE equipo_codigo=?").executeUpdate();

            PreparedStatement st = con.prepareStatement("DELETE FROM equipo WHERE codigo=?");
            st.setString(1, equipo.getCodigo());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar equipo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TreeMap<String, Equipo> buscarTodos() {
        TreeMap<String, Equipo> equipos = new TreeMap<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM equipo");
            while (rs.next()) {
                // Aquí deberías obtener los objetos TipoEquipo, Ubicacion y puertos desde sus DAOs usando los códigos
                Equipo eq = new Equipo(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    null, // TipoEquipo (deberías buscarlo por código)
                    null, // Ubicacion (deberías buscarlo por código)
                    rs.getBoolean("estado")
                );
                // Cargar puertos y direcciones IP si tienes tablas relacionadas
                equipos.put(eq.getCodigo(), eq);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar equipos: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return equipos;
    }
}