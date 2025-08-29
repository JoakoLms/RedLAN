package main.servicios;

import java.util.TreeMap;
import main.modelo.TipoEquipo;

public interface TipoEquipoService {
	void insertar(TipoEquipo tipoEquipo);

	void actualizar(TipoEquipo tipoEquipo);

	void borrar(TipoEquipo tipoEquipo);

	TreeMap<String,TipoEquipo> buscarTodos();
}
