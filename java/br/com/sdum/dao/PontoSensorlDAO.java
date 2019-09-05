package br.com.sdum.dao;

import java.util.List;

import javax.inject.Named;

import br.com.sdum.model.PontoSensor;

@Named
public interface PontoSensorlDAO extends DAO<PontoSensor>{
	
	public List<PontoSensor> buscaPorIdPontoSensor(Long id);
}