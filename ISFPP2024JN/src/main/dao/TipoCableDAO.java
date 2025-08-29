package main.dao;

import java.util.TreeMap;

import main.modelo.TipoCable;

public interface TipoCableDAO {
	void insertar(TipoCable tipoCable);

	void actualizar(TipoCable tipoCable);

	void borrar(TipoCable tipoCable);

	TreeMap<String,TipoCable> buscarTodos();

}
