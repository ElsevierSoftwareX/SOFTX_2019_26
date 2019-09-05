package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.Classificacao;

@Named
public interface ClassificacaoDAO extends DAO<Classificacao>{

	public List<Classificacao> listaProjetoByClassificador(Long idArea, Long idAmostra);
        
        public Classificacao buscaPorCodigo(Long codigo);
        
        public List<Classificacao> buscaPorUsuario(Long id);

}