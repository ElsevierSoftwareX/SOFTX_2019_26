package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.model.Projeto;
import br.com.sdum.model.Usuario;

@Component
public class ProjetoDAOImpl extends DAOImpl<Projeto> implements ProjetoDAO {

	private EntityManager entityManager;
	
	public ProjetoDAOImpl(EntityManager em) {
		super(em);
		this.entityManager = em;
	}

	@Override
	protected Class<Projeto> getClazz() {
		return Projeto.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> listaProjetoByUsuario(Usuario usuario){
		
		String sql = "SELECT p.* FROM sdum.tb_projeto p WHERE p.pro_usucodigo = "+ usuario.getId() +";";
		
		/*
		 String sql = " SELECT p.* "
						+ " FROM sdum.projeto p "
							+ " WHERE p.usuario_id = "+ usuario.getId() 
								+ " AND (UPPER(p.nome) LIKE UPPER('%"+filtro+"%')) "
										+ " ORDER BY p.nome "
											+ " LIMIT 10 OFFSET("+pagina+" - 1) * 10;";
		*/
		
		Query query = entityManager.createNativeQuery(sql, Projeto.class);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> listaProjetoByUsuarioAll(Usuario usuario){
		
		String sql = "SELECT p.* FROM sdum.tb_projeto p WHERE p.pro_usucodigo = "+ usuario.getId() +";";
		
		Query query = entityManager.createNativeQuery(sql, Projeto.class);
		
		return query.getResultList();
	}
        
        public Projeto buscaProjetoById(Long id) {
		
		String sql = "SELECT s.* FROM sdum.tb_projeto s WHERE s.pro_codigo = " + id;
             
		Query query = entityManager.createNativeQuery(sql, Projeto.class);

		if(query.getResultList().size() >= 1 ){
			return (Projeto) query.getResultList().get(0);
		}
		else {
			return null;
		}
	}
}