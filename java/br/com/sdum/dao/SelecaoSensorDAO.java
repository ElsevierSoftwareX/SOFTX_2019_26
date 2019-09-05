package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.SelecaoSensor;

@Named
public interface SelecaoSensorDAO extends DAO<SelecaoSensor>{
	
	public List<SelecaoSensor> listaSelecaoSensorByArea(Long idArea);
	
	public void gerarInterpolacao(Long idAmostra, Long idGrade);
	
	//public void inserirOpcoesSensor(Integer numeroSensores, String arrayAmostras);
	public String inserirOpcoesSensor(Integer numeroSensores, String arrayAmostras, Integer combinacoes, Integer qtdepontos);
	
	public String selecaoSensorFuzzy(String arrayAmostras, String arrayCampoMedida, Integer numeroSensores);
        
        public void excluiTabela(String tabela);
	
}