package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.PixelAmostra;

@Named
public interface PixelAmostraDAO extends DAO<PixelAmostra>{
	
	public List<PixelAmostra> buscaPorIdAmostra(Long id);
        
        public PixelAmostra buscaPixelAmostraByCodigo(Long codigo);
	
}