package br.com.sdum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_selecaosensor")
public class SelecaoSensor implements Serializable {

	private static final long serialVersionUID = -5473598238821452146L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "selsensor_codigo")
	private long id;
	
	@Column(nullable = false, length = 255, name = "selsensor_descricao")
	private String descricao;
	
	@OneToMany(mappedBy = "selecaosensor", cascade = {CascadeType.ALL})
    private List<PontoSensor> pontoSensor;
	
	@ManyToOne
	@JoinColumn(name="selsensor_zon_codigo")
	private ZonaManejo zonaManejo;
	
	@ManyToOne
	@JoinColumn(name = "selsensor_are_codigo")
	private Area area;
	
	@ManyToMany(mappedBy="selecoesSensores", cascade = {CascadeType.PERSIST, CascadeType.MERGE})  
	private List<Amostra> amostras = new ArrayList<Amostra>();

	@Column(name = "selsensor_fpi")
	private float fpi;
	
	@Column(name = "selsensor_mpe")
	private float mpe;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<PontoSensor> getPontoSensor() {
		return pontoSensor;
	}

	public void setPontoSensor(List<PontoSensor> pontoSensor) {
		this.pontoSensor = pontoSensor;
	}

	public ZonaManejo getZonaManejo() {
		return zonaManejo;
	}

	public void setZonaManejo(ZonaManejo zonaManejo) {
		this.zonaManejo = zonaManejo;
	}

	public List<Amostra> getAmostras() {
		return amostras;
	}

	public void setAmostras(List<Amostra> amostras) {
		this.amostras = amostras;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	public float getFpi() {
		return fpi;
	}

	public void setFpi(float fpi) {
		this.fpi = fpi;
	}

	public float getMpe() {
		return mpe;
	}

	public void setMpe(float mpe) {
		this.mpe = mpe;
	}
	
}
