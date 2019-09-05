package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.model.GradeAmostral;

@Component
public class GradeAmostralDAOImpl extends DAOImpl<GradeAmostral> implements GradeAmostralDAO {

    private EntityManager entityManager;

    public GradeAmostralDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<GradeAmostral> getClazz() {
        return GradeAmostral.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GradeAmostral> listaGradeAmostralByArea(Long idArea) {

        String sql = "SELECT a.* FROM sdum.tb_gradeamostral a WHERE a.gra_arecodigo = " + idArea + " and a.gra_flagsensor is not true ORDER BY a.gra_descricao;";

        Query query = entityManager.createNativeQuery(sql, GradeAmostral.class);

        return query.getResultList();
    }

    public void gerarGradeAmostral(Long codigoArea, float pixelX, float pixelY, Long pcodgrade) {
        System.out.println("------------------------------SELECT: " + codigoArea + ", 'sdum.tb_area', " + pixelX + ", " + pixelY + ", 'sdum.tb_pontoamostral'," + pcodgrade + ", 'POINT', " + 4326 + ");");
        String sql = "SELECT f_gera_grid(" + codigoArea + ", 'sdum.tb_area', " + pixelX + ", " + pixelY + ", 'sdum.tb_pontoamostral'," + pcodgrade + ", 'POINT', " + 4326 + ");";

        Query query = entityManager.createNativeQuery(sql);

        query.getResultList();

    }

    public GradeAmostral buscaGradeAmostralByCodigo(Long codigo) {

        String sql = "SELECT s.* FROM sdum.tb_gradeamostral s WHERE s.gra_codigoapi = " + codigo;
        Query query = entityManager.createNativeQuery(sql, GradeAmostral.class);

        if (query.getResultList().size() >= 1) {
            return (GradeAmostral) query.getResultList().get(0);
        } else {
            return null;
        }
    }

}
