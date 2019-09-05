package br.com.sdum.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.Usuario;

@Component
public class UsuarioDAOImpl extends DAOImpl<Usuario> implements UsuarioDAO {

    private EntityManager entityManager;

    public UsuarioDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<Usuario> getClazz() {
        return Usuario.class;
    }

    public Usuario verificaCadastro(Usuario usuario) {

        Session session = (Session) entityManager.getDelegate();

        Criteria criteria = session.createCriteria(Usuario.class);
        // criteria.add(Restrictions.eq("login", usuario.getEmail()));
        criteria.add(Restrictions.eq("codigo", usuario.getCodigo()));

        if (criteria.list().size() > 0) {
            return (Usuario) criteria.list().get(0);
        } else {
            return null;
        }

        /*
		 * Query query = entityManager.createNativeQuery(
		 * "SELECT u.* FROM sdum.tb_usuario u WHERE u.usu_codigo = "+
		 * usuario.getCodigo() +";", Usuario.class); List<Usuario> usuarios =
		 * query.getResultList();
		 * 
		 * if(usuarios.size() > 0){ return usuarios.get(0); } else{ return null;
		 * }
         */
    }

    public void refresh(Usuario usuario) {
        entityManager.refresh(usuario);
    }
}
