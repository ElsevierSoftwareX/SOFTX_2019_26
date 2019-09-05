package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.model.Area;


@Component
public class AreaDAOImpl extends DAOImpl<Area> implements AreaDAO {
	
	private EntityManager entityManager;

	public AreaDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<Area> getClazz() {
		return Area.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> listaAreaByProjeto(Long idProjeto) {
		String sql = " SELECT a.* "
				+ " FROM sdum.tb_area a "
					+ " WHERE a.are_procodigo = "+ idProjeto 
								+ " ORDER BY a.are_descricao;";

		Query query = entityManager.createNativeQuery(sql, Area.class);
		
		return query.getResultList();
	}
        
        @SuppressWarnings("unchecked")
	@Override
	public List<Area> listaAreaByUsuario(Long idUsuario) {
		String sql = " SELECT a.* "
				+ " FROM sdum.tb_area a "
					+ " WHERE a.are_usucodigo = "+ idUsuario 
								+ " ORDER BY a.are_descricao;";

		Query query = entityManager.createNativeQuery(sql, Area.class);
		
		return query.getResultList();
	}
        
        @Override
        public Area buscaAreaByCodigo(Long codigo) {
		
		String sql = "SELECT s.* FROM sdum.tb_area s WHERE s.are_codigo = " + codigo;
               // String sql = "SELECT s.* FROM sdum.tb_area s WHERE s.id = " + codigo;

		Query query = entityManager.createNativeQuery(sql, Area.class);

		if(query.getResultList().size() >= 1 ){
			return (Area) query.getResultList().get(0);
		}
		else {
			return null;
		}
	}
	
}	
	
	
	
	
	
	
	


	

	

	
	


		
	