package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Usuario;

@Component
public class AmostraDAOImpl extends DAOImpl<Amostra> implements AmostraDAO {

    private EntityManager entityManager;

    public AmostraDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<Amostra> getClazz() {
        return Amostra.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Amostra> listaAmostraByArea(Long idArea) {

        String sql = "SELECT a.* FROM sdum.tb_amostra a WHERE a.amo_arecodigo = " + idArea + " ORDER BY a.amo_descricao;";

        Query query = entityManager.createNativeQuery(sql, Amostra.class);

        return query.getResultList();
    }

    public void gerarGradeAmostral(Long codigoArea, int pixelX, int pixelY, Long idAmostra) {

        String sql = "SELECT f_gera_grid(" + codigoArea + ", 'sdum.tb_area', " + pixelX + ", " + pixelY + ", 'sdum.tb_pixelamostra', " + idAmostra + ", 'POINT', " + 4326 + ");";

        Query query = entityManager.createNativeQuery(sql);

        query.getResultList();

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Amostra> listaAmostraByAreaByProjetoByUsuarioAll(Usuario usuario) {

        String sql = "SELECT p.* FROM sdum.tb_amostra p WHERE p.amo_usucodigo = " + usuario.getId() + ";";

        Query query = entityManager.createNativeQuery(sql, Amostra.class);

        return query.getResultList();
    }

    @Override
    public List<Amostra> listaAmostraByProjeto(Long id) {

        String sql = "SELECT p.* FROM sdum.tb_amostra p, sdum.tb_area a, sdum.tb_projeto t WHERE p.amo_arecodigo = a.id and a.are_procodigo = t.pro_codigo and t.pro_codigo = " + id + ";";

        Query query = entityManager.createNativeQuery(sql, Amostra.class);

        return query.getResultList();
    }
    
    public Amostra buscaPorIdAmostra(Long id) {

        String sql = "SELECT * FROM sdum.tb_amostra WHERE amo_codigo = " + id + ";";

        Query query = entityManager.createNativeQuery(sql, Amostra.class);

        return (Amostra) query.getResultList().get(0);
    }

    @Override
    public Amostra buscaAmostraByCodigo(Long codigo) {

        String sql = "SELECT s.* FROM sdum.tb_amostra s WHERE s.amo_codigoapi = " + codigo;

        Query query = entityManager.createNativeQuery(sql, Amostra.class);

        if (query.getResultList().size() >= 1) {
            return (Amostra) query.getResultList().get(0);
        } else {
            return null;
        }
    }
}
