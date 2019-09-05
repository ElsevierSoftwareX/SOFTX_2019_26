package br.com.sdum.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_zonamanejo")
public class ZonaManejo implements Serializable{
	
	private static final long serialVersionUID = -7998396571566325612L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zon_codigo")
	private long id;
	
	@Column(nullable = false, length = 50, name = "zon_descricao")
	private String descricao;
	
	@Column(nullable = false, length = 40, name = "zon_nometabela")
	private String zonaNomeTabela;
	
	@Column(nullable = false, name = "zon_nclasses")
	private int zonaNumClasses;
	
	@Column(nullable = false, length = 40, name = "zon_metodo")
	private String metodoCriacaoZona;
	
	@Column(nullable = false, name = "zon_data")
	private Date data;
	
	@Column(nullable = false, name = "zon_niteracoes")
	private int iteracoes;
	
	@Column(nullable = false, name = "zon_expoente")
	private float expoente;
	
	@Column(nullable = false, name = "zon_erro")
	private float erro;
	
	@Column(name = "zon_fpi")
	private float fpi;
	
	@Column(name = "zon_mpe")
	private float mpe;
	
	@ManyToOne
	@JoinColumn(name = "zon_arecodigo")
    private Area area;

	//@Column
	//private String anova;
	
	//@Column
	//private float vr;
	
	//@Column
	//private float ascZM;

	@OneToMany(mappedBy = "zonaManejo", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<PixelZonaManejo> pixelZonaManejo;
	
	@Transient
	private List<PixelZonaManejo> pixelZonaManejoTrans = new ArrayList<PixelZonaManejo>();
	
	@ManyToMany
	@JoinTable(name="tb_zonamapa", 
			   joinColumns={@JoinColumn(name="zon_codigo")}, 
			   inverseJoinColumns={@JoinColumn(name="map_codigo")})
    private List<Mapa> mapas = new ArrayList<Mapa>();

	@ManyToOne
	@JoinColumn(name = "zon_usucodigo")
    private Usuario usuario;
	
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

	public String getZonaNomeTabela() {
		return zonaNomeTabela;
	}

	public void setZonaNomeTabela(String zonaNomeTabela) {
		this.zonaNomeTabela = zonaNomeTabela;
	}

	public int getZonaNumClasses() {
		return zonaNumClasses;
	}

	public void setZonaNumClasses(int zonaNumClasses) {
		this.zonaNumClasses = zonaNumClasses;
	}

	public String getMetodoCriacaoZona() {
		return metodoCriacaoZona;
	}

	public void setMetodoCriacaoZona(String metodoCriacaoZona) {
		this.metodoCriacaoZona = metodoCriacaoZona;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getIteracoes() {
		return iteracoes;
	}

	public void setIteracoes(int iteracoes) {
		this.iteracoes = iteracoes;
	}

	public float getExpoente() {
		return expoente;
	}

	public void setExpoente(float expoente) {
		this.expoente = expoente;
	}

	public float getErro() {
		return erro;
	}

	public void setErro(float erro) {
		this.erro = erro;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	/*public String getAnova() {
		return anova;
	}

	public void setAnova(String anova) {
		this.anova = anova;
	}

	public float getVr() {
		return vr;
	}

	public void setVr(float vr) {
		this.vr = vr;
	}

	public float getAscZM() {
		return ascZM;
	}

	public void setAscZM(float ascZM) {
		this.ascZM = ascZM;
	}*/

	public List<PixelZonaManejo> getPixelZonaManejo() {
		return pixelZonaManejo;
	}

	public void setPixelZonaManejo(List<PixelZonaManejo> pixelZonaManejo) {
		this.pixelZonaManejo = pixelZonaManejo;
	}

	public List<PixelZonaManejo> getPixelZonaManejoTrans() {
		return pixelZonaManejoTrans;
	}

	public void setPixelZonaManejoTrans(List<PixelZonaManejo> pixelZonaManejoTrans) {
		this.pixelZonaManejoTrans = pixelZonaManejoTrans;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Mapa> getMapas() {
		return mapas;
	}
	
	public void setMapas(List<Mapa> mapas) {
		this.mapas = mapas;
	}

}
