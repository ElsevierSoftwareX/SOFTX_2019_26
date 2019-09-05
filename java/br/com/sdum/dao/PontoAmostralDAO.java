package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.PontoAmostral;

@Named
public interface PontoAmostralDAO extends DAO<PontoAmostral>{
	
	public List<PontoAmostral> buscaPorIdPontoAmostral(Long id);
	
	public PontoAmostral buscaPorIdPontoAmostralSensor(String id, String nomeTabela);

}