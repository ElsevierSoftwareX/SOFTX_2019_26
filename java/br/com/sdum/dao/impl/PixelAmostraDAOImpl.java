package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.PixelAmostraDAO;
import br.com.sdum.model.PixelAmostra;

@Component
public class PixelAmostraDAOImpl extends DAOImpl<PixelAmostra> implements PixelAmostraDAO {

    private EntityManager entityManager;

    public PixelAmostraDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<PixelAmostra> getClazz() {
        return PixelAmostra.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PixelAmostra> buscaPorIdAmostra(Long id) {

        String sql = "SELECT * FROM sdum.tb_pixelamostra WHERE pix_amocodigo = " + id + ";";

        Query query = entityManager.createNativeQuery(sql, PixelAmostra.class);

        if (query.getResultList().size() >= 1) {
            return query.getResultList();
        } else {
            return null;
        }

    }

    @Override
    public PixelAmostra buscaPixelAmostraByCodigo(Long codigo) {
        String sql = "SELECT s.* FROM sdum.tb_pixelamostra s WHERE s.pix_codigoapi = " + codigo;

        Query query = entityManager.createNativeQuery(sql, PixelAmostra.class);

        if (query.getResultList().size() >= 1) {
            return (PixelAmostra) query.getResultList().get(0);
        } else {
            return null;
        }
    }
}
