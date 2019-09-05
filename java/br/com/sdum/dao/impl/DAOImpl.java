package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import br.com.sdum.dao.DAO;

public abstract class DAOImpl<T> implements DAO<T> {

    private EntityManager em;

    public DAOImpl(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("rawtypes")
    protected abstract Class getClazz();

    public EntityManager getEm() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public List<T> listar() {

        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(getClazz());
        query.select(query.from(getClazz()));

        List<T> lista = em.createQuery(query).getResultList();

        return lista;
    }

    public void adicionar(T objeto) {
        em.getTransaction().begin();
        em.persist(objeto);
        em.getTransaction().commit();
    }

    public void excluir(T objeto) {
        em.getTransaction().begin();
        em.remove(em.merge(objeto));
        em.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public T buscarPorId(Long id) {
        return (T) em.find(getClazz(), id);
    }

    public void alterar(T objeto) {
        em.getTransaction().begin();
        em.merge(objeto);
        em.getTransaction().commit();
    }

    public T alterarT(T objeto) {
        em.getTransaction().begin();
        objeto = em.merge(objeto);
        em.getTransaction().commit();

        return objeto;
    }
    
}
