package br.com.sdum.dao;

import java.util.List;

import br.com.sdum.model.Projeto;
import br.com.sdum.model.Usuario;

public interface ProjetoDAO extends DAO<Projeto> {
	
	public List<Projeto> listaProjetoByUsuario(Usuario usuario);
	
	public List<Projeto> listaProjetoByUsuarioAll(Usuario usuario);

	// public List<Projeto> listaProjetoByUsuario(String filtro, int pagina, Usuario usuario);
	public Projeto buscaProjetoById(Long id);
}