package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.GradeAmostral;
	
@Named
public interface GradeAmostralDAO extends DAO<GradeAmostral> {
	
	public List<GradeAmostral> listaGradeAmostralByArea(Long idArea);
	
	public void gerarGradeAmostral(Long codigoArea, float pixelX, float pixelY, Long pcodgrade);
        
        public GradeAmostral buscaGradeAmostralByCodigo(Long codigo);
        
}