package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.Amostra;
import br.com.sdum.model.Usuario;

@Named
public interface AmostraDAO extends DAO<Amostra>{
	
	public List<Amostra> listaAmostraByArea(Long idArea);
	
	public void gerarGradeAmostral(Long codigoArea, int pixelX, int pixelY, Long idAmostra);
	
	public List<Amostra> listaAmostraByAreaByProjetoByUsuarioAll(Usuario usuario);
	
        public List<Amostra> listaAmostraByProjeto(Long id);
        
	public Amostra buscaPorIdAmostra(Long id);
        
       public Amostra buscaAmostraByCodigo(Long codigo);
	
}