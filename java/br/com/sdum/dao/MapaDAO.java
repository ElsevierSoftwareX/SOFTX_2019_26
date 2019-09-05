package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.Classificacao;
import br.com.sdum.model.Mapa;
import br.com.sdum.model.Usuario;

@Named
public interface MapaDAO extends DAO<Mapa>{
	
	public void gerarMapaIntepolacao(String nome, Long area, Long amostra, String interpolador, double pixelX, double pixelY, double expoente, int vizinho, double raio, Usuario usuario);
	
	public void gerarPixelsInterpolacaoR(Mapa mapa, String pontos, String pixvalor);
        
        public Mapa buscarUltimoMapaIntepolacao(Long id, Usuario usuario);
	
	public List<Mapa> listaMapaByUsuarioAll(Usuario usuario);
        
        public List<Mapa> listaMapaByArea(Long idArea);
        
        public List<Mapa> listaMapaByProjeto(Long idProjeto);
        
        public Mapa buscaPorCodigo(Long codigo);
        
        public String ConversaoGrausUTMArea(Long idArea);
        
        public List<String> ConversaoGrausUTMPixelAmostra(Long idAmostra);
        
        public String ConversaoUTMGrausPixelMapa(String x, String y);
	
         public List<String> CentroidPixelMapa(Long idmapa);
}