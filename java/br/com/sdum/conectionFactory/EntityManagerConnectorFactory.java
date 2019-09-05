package br.com.sdum.conectionFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class EntityManagerConnectorFactory implements ComponentFactory<EntityManagerFactory>{

	private final EntityManagerFactory factory;

	public EntityManagerConnectorFactory() {
		this.factory = Persistence.createEntityManagerFactory("sdum");
	}

	public EntityManagerFactory getInstance() {
		return this.factory;
	}
}