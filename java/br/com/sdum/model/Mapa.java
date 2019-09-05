package br.com.sdum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_mapa")
public class Mapa implements Serializable {

    private static final long serialVersionUID = 4685841508776491045L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_codigo")
    private Long id;
    
    @Column(name="map_codigoapi")
    private Long codigo;

    @Column(length = 100, name = "map_descricao")
    private String descricao;

    @Column(name = "map_datacriacao")
    @Temporal(TemporalType.DATE)
    private Date dataCriacao;

    @Column(length = 60, name = "map_nometabela")
    private String mapaNomeTabela;

    @Column(name = "map_tamx")
    private double tamanhoX;

    @Column(name = "map_tamy")
    private double tamanhoY;

    @Column(length = 20, name = "map_tipogeometria")
    private String tipoGeometria;

    @Column(length = 50, name = "map_tipointerpolador")
    private String tipoInterpolador;

    @Column(name = "map_expoente")
    private double expoente;

    @Column(name = "map_raio")
    private double raio;

    @Column(name = "map_npontos")
    private int numeroPontos;

    @Column(name = "map_efeitopepita")
    private double efeitoPepita;

    @Column(name = "map_patamar")
    private double patamar;

    @Column(name = "map_alcance")
    private double alcance;

    @Column(length = 20, name = "map_modelo")
    private String modelo;

    @Column(length = 3, name = "map_method")
    private String metodo;
    
    @Column(name = "map_ice")
    private float ice;
    
    @Column(name = "map_contribuicao")
    private float contribuicao;
    
    @Column(name = "map_kappa")
    private float kappa;
    
    @Column(name = "map_isi")
    private float isi;
    
    @Column(name = "map_meanerror")
    private float meanError;
     
    @Column(name = "map_standarddeviationmeanerror")
    private float standardDeviationdMeanError; 
    
    @Column(name = "map_ide")
    private float ide;
    
    @Column(name = "map_melhorsemivariograma")
    private String melhorsemivariograma;
    
    @Column(name = "map_todossemivariogramas")
    private String todossemivariogramas;
    
    @Column(length = 50, name = "map_comp_principal")
    private String compPrincipal;

    @Column(name = "map_data")
    @Temporal(TemporalType.DATE)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "map_clascodigo")
    private Classificacao classificacao;

    @ManyToOne
    @JoinColumn(name = "map_amocodigo")
    private Amostra amostra;

    @ManyToOne
    @JoinColumn(name = "map_usucodigo")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "map_arecodigo")
    private Area area;

    @OneToMany(mappedBy = "mapa", cascade = {CascadeType.ALL})
    private List<PixelMapa> pixelMapa;

    @Transient
    private List<PixelMapa> pixelMapasTrans = new ArrayList<PixelMapa>();

    @Transient
    private List<Classe> classesTran = new ArrayList<Classe>();

    @ManyToMany(mappedBy = "mapas", cascade = CascadeType.ALL)
    private List<ZonaManejo> zonasManejo = new ArrayList<ZonaManejo>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getMapaNomeTabela() {
        return mapaNomeTabela;
    }

    public void setMapaNomeTabela(String mapaNomeTabela) {
        this.mapaNomeTabela = mapaNomeTabela;
    }

    public double getExpoente() {
        return expoente;
    }

    public void setExpoente(double expoente) {
        this.expoente = expoente;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    public double getEfeitoPepita() {
        return efeitoPepita;
    }

    public void setEfeitoPepita(double efeitoPepita) {
        this.efeitoPepita = efeitoPepita;
    }

    public double getPatamar() {
        return patamar;
    }

    public void setPatamar(double patamar) {
        this.patamar = patamar;
    }

    public double getAlcance() {
        return alcance;
    }

    public void setAlcance(double alcance) {
        this.alcance = alcance;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<PixelMapa> getPixelMapa() {
        return pixelMapa;
    }

    public void setPixelMapa(List<PixelMapa> pixelMapa) {
        this.pixelMapa = pixelMapa;
    }

    public List<PixelMapa> getPixelMapasTrans() {
        return pixelMapasTrans;
    }

    public void setPixelMapasTrans(List<PixelMapa> pixelMapasTrans) {
        this.pixelMapasTrans = pixelMapasTrans;
    }

    public List<Classe> getClassesTran() {
        return classesTran;
    }

    public void setClassesTran(List<Classe> classesTran) {
        this.classesTran = classesTran;
    }

    public List<ZonaManejo> getZonasManejo() {
        return zonasManejo;
    }

    public void setZonasManejo(List<ZonaManejo> zonasManejo) {
        this.zonasManejo = zonasManejo;
    }

    public double getTamanhoX() {
        return tamanhoX;
    }

    public void setTamanhoX(double tamanhoX) {
        this.tamanhoX = tamanhoX;
    }

    public double getTamanhoY() {
        return tamanhoY;
    }

    public void setTamanhoY(double tamanhoY) {
        this.tamanhoY = tamanhoY;
    }

    public String getTipoGeometria() {
        return tipoGeometria;
    }

    public void setTipoGeometria(String tipoGeometria) {
        this.tipoGeometria = tipoGeometria;
    }

    public String getTipoInterpolador() {
        return tipoInterpolador;
    }

    public void setTipoInterpolador(String tipoInterpolador) {
        this.tipoInterpolador = tipoInterpolador;
    }

    public int getNumeroPontos() {
        return numeroPontos;
    }

    public void setNumeroPontos(int numeroPontos) {
        this.numeroPontos = numeroPontos;
    }

    public String getCompPrincipal() {
        return compPrincipal;
    }

    public void setCompPrincipal(String compPrincipal) {
        this.compPrincipal = compPrincipal;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public float getIce() {
        return ice;
    }

    public void setIce(float ice) {
        this.ice = ice;
    }

    public float getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(float contribuicao) {
        this.contribuicao = contribuicao;
    }

    public float getKappa() {
        return kappa;
    }

    public void setKappa(float kappa) {
        this.kappa = kappa;
    }

    public float getIsi() {
        return isi;
    }

    public void setIsi(float isi) {
        this.isi = isi;
    }

    public float getMeanError() {
        return meanError;
    }

    public void setMeanError(float meanError) {
        this.meanError = meanError;
    }

    public float getStandardDeviationdMeanError() {
        return standardDeviationdMeanError;
    }

    public void setStandardDeviationdMeanError(float standardDeviationdMeanError) {
        this.standardDeviationdMeanError = standardDeviationdMeanError;
    }

    public float getIde() {
        return ide;
    }

    public void setIde(float ide) {
        this.ide = ide;
    }

    public String getMelhorsemivariograma() {
        return melhorsemivariograma;
    }

    public void setMelhorsemivariograma(String melhorsemivariograma) {
        this.melhorsemivariograma = melhorsemivariograma;
    }

    public String getTodossemivariogramas() {
        return todossemivariogramas;
    }

    public void setTodossemivariogramas(String todossemivariogramas) {
        this.todossemivariogramas = todossemivariogramas;
    }
    
    @Override
    public String toString() {
        return "Mapa{" + "id=" + id + ", descricao=" + descricao + ", dataCriacao=" + dataCriacao + ", mapaNomeTabela=" + mapaNomeTabela + ", tamanhoX=" + tamanhoX + ", tamanhoY=" + tamanhoY + ", tipoGeometria=" + tipoGeometria + ", tipoInterpolador=" + tipoInterpolador + ", expoente=" + expoente + ", raio=" + raio + ", numeroPontos=" + numeroPontos + ", efeitoPepita=" + efeitoPepita + ", patamar=" + patamar + ", alcance=" + alcance + ", modelo=" + modelo + ", compPrincipal=" + compPrincipal + ", data=" + data + ", classificacao=" + classificacao + ", amostra=" + amostra + ", usuario=" + usuario + ", area=" + area + ", pixelMapa=" + pixelMapa + ", pixelMapasTrans=" + pixelMapasTrans + ", classesTran=" + classesTran + ", zonasManejo=" + zonasManejo + '}';
    }

    
}
