package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.model.Classificacao;

@Component
public class ClassificacaoDAOImpl extends DAOImpl<Classificacao> implements ClassificacaoDAO {

    private EntityManager entityManager;

    public ClassificacaoDAOImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected Class<Classificacao> getClazz() {
        return Classificacao.class;
    }

    @SuppressWarnings("unchecked")
    public List<Classificacao> listaProjetoByClassificador(Long idArea, Long idAmostra) {

        //String sql = "SELECT c.* FROM sdum.classificador c, sdum.area a WHERE c.atributo_id = "+ idAmostra +" AND a.id = "+ idArea +" AND a.solo_id = c.solo_id;";
        String sql = "SELECT c.* FROM sdum.tb_classificacao c, sdum.tb_area a, sdum.tb_amostra b WHERE a.id = " + idArea + " AND a.are_tipsolocodigo = c.cla_tipcodigo AND b.amo_codigo = " + idAmostra + " AND b.amo_atrcodigo = c.cla_atrcodigo;";

        Query query = entityManager.createNativeQuery(sql, Classificacao.class);

        return query.getResultList();
    }

    public Classificacao buscaPorCodigo(Long codigo) {

        String sql = "SELECT * FROM sdum.tb_classificacao WHERE cla_codigoapi = " + codigo + ";";

        Query query = entityManager.createNativeQuery(sql, Classificacao.class);

        if (query.getResultList().size() >= 1) {
            return (Classificacao) query.getResultList().get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public List<Classificacao> buscaPorUsuario(Long id) {

        String sql = "SELECT * FROM sdum.tb_classificacao WHERE cla_usucodigo = " + id + ";";

        Query query = entityManager.createNativeQuery(sql, Classificacao.class);

        return query.getResultList();
    }


}
