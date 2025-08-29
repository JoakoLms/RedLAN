package main.dao;

import java.util.TreeMap;

import main.modelo.Ubicacion;

public interface UbicacionDAO {
	void insertar(Ubicacion ubicacion);

	void actualizar(Ubicacion ubicacion);

	void borrar(Ubicacion ubicacion);

	TreeMap<String, Ubicacion> buscarTodos();

}
