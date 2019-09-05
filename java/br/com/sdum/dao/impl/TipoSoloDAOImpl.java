package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.model.TipoSolo;

@Component
public class TipoSoloDAOImpl extends DAOImpl<TipoSolo> implements TipoSoloDAO {

	private EntityManager entityManager;

	public TipoSoloDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<TipoSolo> getClazz() {
		return TipoSolo.class;
	}
	
	public TipoSolo buscaSoloByCodigo(Long codigoSolo) {
		
		String sql = "SELECT s.* FROM sdum.tb_tiposolo s WHERE s.tip_codigo = " + codigoSolo;

		Query query = entityManager.createNativeQuery(sql, TipoSolo.class);

		if(query.getResultList().size() >= 1 ){
			return (TipoSolo) query.getResultList().get(0);
		}
		else {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoSolo> listaSolosByUsuario(Long idUsuario){
		
		String sql = "SELECT p.* FROM sdum.tb_tiposolo p, sdum.tb_solousuario a WHERE p.id = a.usu_tipcodigo and a.tip_usucodigo = "+ idUsuario+ " ORDER BY p.tip_descricao;";
		
		Query query = entityManager.createNativeQuery(sql, TipoSolo.class);
		
		return query.getResultList();
	}
	
}