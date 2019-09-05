/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.dao.impl;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.EntidadeDAO;
import br.com.sdum.model.Entidade;
import br.com.sdum.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Gabriela
 */
@Component
public class EntidadeDAOImpl extends DAOImpl<Entidade> implements EntidadeDAO {

    private EntityManager entityManager;

    public EntidadeDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<Entidade> getClazz() {
        return Entidade.class;
    }

    @Override
    public Entidade buscaEntidadeByCodigo(Long codigo) {
        String sql = "SELECT s.* FROM sdum.tb_entidadeclassificadora s WHERE s.ent_codigoapi = " + codigo;
        Query query = entityManager.createNativeQuery(sql, Entidade.class);

        if (query.getResultList().size() >= 1) {
            return (Entidade) query.getResultList().get(0);
        } else {
            return null;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Entidade> listaEntidadeByUsuarioAll(Usuario usuario) {

        String sql = "SELECT m.* FROM sdum.tb_entidadeclassificadora m WHERE m.ent_usucodigo = " + usuario.getId() + ";";

        Query query = entityManager.createNativeQuery(sql, Entidade.class);

        return query.getResultList();
    }
}
