package br.com.sdum.dao.impl;

import javax.persistence.EntityManager;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.Usuario;
import java.util.List;
import javax.persistence.Query;

@Component
public class AtributoDAOImpl extends DAOImpl<Atributo> implements AtributoDAO {

    @SuppressWarnings("unused")
    private EntityManager entityManager;

    public AtributoDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<Atributo> getClazz() {
        return Atributo.class;
    }

    public Atributo buscaPorCodigo(Long codigo) {

        String sql = "SELECT * FROM sdum.tb_atributo WHERE atr_codigo = " + codigo + ";";

        Query query = entityManager.createNativeQuery(sql, Atributo.class);

        if (query.getResultList().size() >= 1) {
            return (Atributo) query.getResultList().get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Atributo> listaAtributoByUsuarioAll(Usuario usuario) {

        String sql = "SELECT m.* FROM sdum.tb_atributo m, sdum.tb_unidademedida u WHERE m.atr_pt_unicodigo = u.uni_codigo and u.uni_usucodigo= " + usuario.getId() + ";";

        Query query = entityManager.createNativeQuery(sql, Atributo.class);

        return query.getResultList();
    }
    
    public Atributo buscaPorCodigoEUsuario(Long codigo, Usuario usuario) {

        String sql = "SELECT a.* FROM sdum.tb_atributo a, sdum.tb_unidademedida b WHERE a.atr_codigo = " + codigo + " and a.atr_pt_unicodigo = b.uni_codigo and b.uni_usucodigo= " + usuario.getId() + ";";

        Query query = entityManager.createNativeQuery(sql, Atributo.class);

        if (query.getResultList().size() >= 1) {
            return (Atributo) query.getResultList().get(0);
        } else {
            return null;
        }
    }
}
