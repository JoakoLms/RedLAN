package main.servicios;

import java.util.List;

import main.modelo.Conexion;

public interface ConexionService {
	void insertar(Conexion conexion);

	void actualizar(Conexion conexion);

	void borrar(Conexion conexion);

	List<Conexion> buscarTodos();

}
