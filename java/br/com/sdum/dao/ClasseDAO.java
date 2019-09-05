package br.com.sdum.dao;

import javax.inject.Named;

import br.com.sdum.model.Classe;



@Named
public interface ClasseDAO extends DAO<Classe>{
	
	public Classe intervaloByValor(Long idClassificador, Double valor);
	
}