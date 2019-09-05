package br.com.sdum.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_pontosensor")
public class PontoSensor implements Serializable{

	private static final long serialVersionUID = 8799951873090092988L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pon_codigo")
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false, name="pon_pontoamostralcodigo")
	PontoAmostral pontoAmostral;
	
	@ManyToOne
	@JoinColumn(name = "pon_selsensorcodigo")
	private SelecaoSensor selecaosensor;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SelecaoSensor getSelecaosensor() {
		return selecaosensor;
	}

	public void setSelecaosensor(SelecaoSensor selecaosensor) {
		this.selecaosensor = selecaosensor;
	}

	public PontoAmostral getPontoAmostral() {
		return pontoAmostral;
	}

	public void setPontoAmostral(PontoAmostral pontoAmostral) {
		this.pontoAmostral = pontoAmostral;
	}
	
	
}
