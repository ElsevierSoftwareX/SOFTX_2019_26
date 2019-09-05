package br.com.sdum.dao;

import javax.inject.Named;

import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;


@Named
public interface UnidadeMedidaDAO extends DAO<UnidadeMedida>{
	
	public UnidadeMedida buscaUnidadeByCodigo(Long codigo);
        
         public UnidadeMedida buscaUnidadeByCodigoEUsuario(Long codigoUnidade, Usuario usuario);
}