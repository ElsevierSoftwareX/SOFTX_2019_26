package br.com.sdum.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.dao.SelecaoSensorDAO;
import br.com.sdum.model.SelecaoSensor;

@Component
public class SelecaoSensorDAOImpl extends DAOImpl<SelecaoSensor> implements SelecaoSensorDAO {

    private EntityManager entityManager;

    public SelecaoSensorDAOImpl(EntityManager em) {
        super(em);
        this.entityManager = em;
    }

    @Override
    protected Class<SelecaoSensor> getClazz() {
        return SelecaoSensor.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SelecaoSensor> listaSelecaoSensorByArea(Long idArea) {

        String sql = "SELECT a.* FROM sdum.tb_selecaosensor a WHERE a.selsensor_are_codigo = " + idArea + " ORDER BY a.selsensor_descricao;";

        Query query = entityManager.createNativeQuery(sql, SelecaoSensor.class);

        return query.getResultList();
    }

    @Override
    public void gerarInterpolacao(Long idAmostra, Long idGrade) {
        System.out.println("MANDANDO PRA FUNCAO INTERPOLAR: idAmostra: " + idAmostra + "', idGrade: " + idGrade);
        String sql = "SELECT f_interpolador_ponto_id_sensor(" + idAmostra + ", " + idGrade + ", 1, 15, 0);";
        Query query = entityManager.createNativeQuery(sql);
        query.getResultList();

    }

    /*@Override
	public void inserirOpcoesSensor(Integer numeroSensores, String arrayAmostras){
		String sql = "select f_inserecombinacao("+ numeroSensores +", 250, 1000, ARRAY["+arrayAmostras+"], 'sdum.teste');";
		Query query = entityManager.createNativeQuery(sql);
		query.getResultList();
	}*/
    public String inserirOpcoesSensor(Integer numeroSensores, String arrayAmostras, Integer combinacoes, Integer qtdepontos) {
        String sql = "select f_inserecombinacao(" + numeroSensores + ", 250," + combinacoes + ", ARRAY[" + arrayAmostras + "], 'sdum.teste', " + qtdepontos + ");";
        Query query = entityManager.createNativeQuery(sql);
        return (String) query.getResultList().get(0);
    }

    public String selecaoSensorFuzzy(String arrayAmostras, String arrayCampoMedida, Integer numeroSensores) {
        String sql = "select f_fuzzy3(ARRAY[" + arrayAmostras + "], ARRAY[" + arrayCampoMedida + "], " + numeroSensores + ", 1.35);";
        Query query = entityManager.createNativeQuery(sql);
        return (String) query.getResultList().get(0);
    }

    public void excluiTabela(String tabela) {
            System.out.println("excluiu1 "+tabela);
            String sql = "select f_deleta_tabela(ARRAY["+tabela+"]);";
            System.out.println("excluiu2 "+sql);
            Query query = entityManager.createNativeQuery(sql);
            query.getResultList();
    }

}
