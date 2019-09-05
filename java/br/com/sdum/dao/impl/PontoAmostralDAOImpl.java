package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.PontoAmostralDAO;
import br.com.sdum.model.PontoAmostral;

@Component
public class PontoAmostralDAOImpl extends DAOImpl<PontoAmostral> implements PontoAmostralDAO {
	
	private EntityManager entityManager;

	public PontoAmostralDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<PontoAmostral> getClazz() {
		return PontoAmostral.class;
	}	
	
	@Override @SuppressWarnings("unchecked")
	public List<PontoAmostral> buscaPorIdPontoAmostral(Long id) {

		String sql = "SELECT * FROM sdum.tb_pontoamostral WHERE pon_gracodigo = "+ id +";";
		
		Query query = entityManager.createNativeQuery(sql, PontoAmostral.class);

		return query.getResultList();	
	}
	
	public PontoAmostral buscaPorIdPontoAmostralSensor(String id, String nomeTabela){
		//String sql = "SELECT * FROM sdum.tb_"+nomeTabela+" WHERE pon_codigo = "+ id +";";
		String sql = "SELECT b.* FROM sdum.tb_"+nomeTabela+" a, sdum.tb_pontoamostral b WHERE a.pon_codigo = "+id+" and a.the_geom = b.the_geom;";
		
		Query query = entityManager.createNativeQuery(sql, PontoAmostral.class);

		return (PontoAmostral) query.getResultList().get(0);
	}
        
}	
	
	
	
	
	


	

	

	
	


		
	