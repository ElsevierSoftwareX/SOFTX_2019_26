package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.TipoSolo;

@Named
public interface TipoSoloDAO extends DAO<TipoSolo>{

	public TipoSolo buscaSoloByCodigo(Long codigo);
	
	public List<TipoSolo> listaSolosByUsuario(Long id);

}