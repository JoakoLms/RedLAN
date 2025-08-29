package main.servicios;

import java.util.TreeMap;
import main.modelo.TipoCable;

public interface TipoCableService {
	void insertar(TipoCable tipoCable);

	void actualizar(TipoCable tipoCable);

	void borrar(TipoCable tipoCable);

	TreeMap<String,TipoCable> buscarTodos();
}
