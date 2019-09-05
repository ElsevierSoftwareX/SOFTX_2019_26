package br.com.sdum.conectionFactory;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class EntityManagerConnector implements ComponentFactory<EntityManager>{

	private final EntityManager manager;

	public EntityManagerConnector(EntityManagerFactory factory) {
		this.manager = factory.createEntityManager();
	}

	public EntityManager getInstance() {
		return this.manager;
	}

	@PreDestroy
	public void fechaManager(){
		this.manager.close();
	}
}