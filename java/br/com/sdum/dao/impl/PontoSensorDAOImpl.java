package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.PontoSensorlDAO;
import br.com.sdum.model.PontoSensor;

@Component
public class PontoSensorDAOImpl extends DAOImpl<PontoSensor> implements PontoSensorlDAO {
	
	private EntityManager entityManager;

	public PontoSensorDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<PontoSensor> getClazz() {
		return PontoSensor.class;
	}	
	
	@Override @SuppressWarnings("unchecked")
	public List<PontoSensor> buscaPorIdPontoSensor(Long id) {

		String sql = "SELECT * FROM sdum.tb_pontosensor WHERE pon_codigo = "+ id +";";
		
		Query query = entityManager.createNativeQuery(sql, PontoSensor.class);

		return query.getResultList();	
	}
}	
	
	
	
	
	


	

	

	
	


		
	