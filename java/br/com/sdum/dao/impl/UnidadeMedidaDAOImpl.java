package br.com.sdum.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;

@Component
public class UnidadeMedidaDAOImpl extends DAOImpl<UnidadeMedida> implements UnidadeMedidaDAO {

	private EntityManager entityManager;

	public UnidadeMedidaDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<UnidadeMedida> getClazz() {
		return UnidadeMedida.class;
	}
	
	
        @Override
	public UnidadeMedida buscaUnidadeByCodigo(Long codigoUnidade){
		//String sql = "SELECT s.* FROM sdum.tb_unidademedida s WHERE s.uni_codigo = " + codigoUnidade;
                String sql = "SELECT s.* FROM sdum.tb_unidademedida s WHERE s.codigo = " + codigoUnidade;
		Query query = entityManager.createNativeQuery(sql, UnidadeMedida.class);

		if(query.getResultList().size() >= 1 ){
			return (UnidadeMedida) query.getResultList().get(0);
		}
		else {
			return null;
		}
	}
        
        public UnidadeMedida buscaUnidadeByCodigoEUsuario(Long codigoUnidade, Usuario usuario){
		//String sql = "SELECT s.* FROM sdum.tb_unidademedida s WHERE s.uni_codigo = " + codigoUnidade;
                String sql = "SELECT s.* FROM sdum.tb_unidademedida s WHERE s.codigo = " + codigoUnidade+ " and s.uni_usucodigo = "+usuario.getId();
		Query query = entityManager.createNativeQuery(sql, UnidadeMedida.class);

		if(query.getResultList().size() >= 1 ){
			return (UnidadeMedida) query.getResultList().get(0);
		}
		else {
			return null;
		}
	}
}