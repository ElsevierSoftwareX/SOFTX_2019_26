package br.com.sdum.dao;

import java.util.List;

import br.com.sdum.model.Area;

public interface AreaDAO extends DAO<Area>{
			
	public List<Area> listaAreaByProjeto(Long idProjeto);
        
        public List<Area> listaAreaByUsuario(Long idUsuario);
        
        public Area buscaAreaByCodigo(Long codigo);

}
