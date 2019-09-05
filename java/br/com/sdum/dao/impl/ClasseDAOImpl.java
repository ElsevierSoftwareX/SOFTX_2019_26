package br.com.sdum.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.ClasseDAO;
import br.com.sdum.model.Classe;

@Component
public class ClasseDAOImpl extends DAOImpl<Classe> implements ClasseDAO  {

	private EntityManager entityManager;
	
	public ClasseDAOImpl(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	@Override
	protected Class<Classe> getClazz() {
		return Classe.class;
	}

        @Override
	public Classe intervaloByValor(Long idClassificador, Double valor){
		
		String sql = "SELECT * FROM sdum.tb_classe WHERE cla_clacodigo = "+ idClassificador +" AND (cla_valormin <= "+ valor +" AND cla_valormax >= "+ valor +");";
		
		Query query = entityManager.createNativeQuery(sql, Classe.class);
		
		if ( query.getResultList().isEmpty() ){
			
			sql = "SELECT * FROM sdum.tb_classe WHERE cla_clacodigo = "+ idClassificador +" AND cla_valormax = (SELECT MAX(cla_valormax) FROM sdum.tb_classe WHERE cla_clacodigo = "+ idClassificador +" )";
			
			query = entityManager.createNativeQuery(sql, Classe.class);
			
			return (Classe) query.getResultList().get(0);
			
		}
		else {
			
			return (Classe) query.getResultList().get(0);
			
		}
	}
}