package main.servicios;

import java.util.TreeMap;

import main.dao.TipoCableDAO;
import main.dao.secuencial.TipoCableSecuencialDAO;
import main.modelo.TipoCable;

public class TipoCableServiceImpl implements TipoCableService{

	private TipoCableDAO tipoCableDAO;
	
	public TipoCableServiceImpl(){
		tipoCableDAO = new TipoCableSecuencialDAO();
	}
	
	@Override
	public void insertar(TipoCable tipoCable) {
		tipoCableDAO.insertar(tipoCable);				
	}

	@Override
	public void actualizar(TipoCable tipoCable) {
		tipoCableDAO.actualizar(tipoCable);						
	}

	@Override
	public void borrar(TipoCable tipoCable) {
		tipoCableDAO.borrar(tipoCable);
		
	}

	@Override
	public TreeMap<String,TipoCable> buscarTodos() {
		return tipoCableDAO.buscarTodos();
		
	}
}
