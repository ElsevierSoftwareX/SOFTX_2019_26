package br.com.sdum.dao;

import java.util.List;

public interface DAO<T> {
	
	public List<T> listar();

	public void adicionar(T objeto);

	public void excluir(T objeto);
	
	public void alterar(T objeto);

	public T alterarT(T objeto);
	
	public T buscarPorId(Long id);

	
}