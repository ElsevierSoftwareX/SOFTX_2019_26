package br.com.sdum.model;

import com.google.gson.annotations.Expose;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_amostra")
public class Amostra implements Serializable {

    private static final long serialVersionUID = -6814440438895531933L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amo_codigo")
    private Long id;
    
    @Expose
    @Column(name="amo_codigoapi")
    private Long codigo;

    @Expose
    @Column(nullable = false, length = 100, name = "amo_descricao")
    private String descricao;
    
    @Column(nullable = false, length = 30, name="amo_tabelaapi")
    private String nomeTabela;

    @Column(nullable = false, name = "amo_data")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;

    @Transient
    private String dataFormatada;

    @Expose
    @ManyToOne
    @JoinColumn(name = "amo_arecodigo")
    private Area area;

    @Expose
    @ManyToOne
    @JoinColumn(name = "amo_atrcodigo")
    private Atributo atributo;

    //private int pixelX;
    //private int pixelY;
    @OneToMany(mappedBy = "amostra", cascade = {CascadeType.ALL})
    private List<PixelAmostra> pixelAmostra;

    @Transient
    private List<PixelAmostra> pixelAmostrasTrans = new ArrayList<PixelAmostra>();

    //@Column(length = 30)
    //private String metodoSelecaoVariavel;
    //@Column
    //private float percentualVarianciaOriginal;
    @Column(length = 30, name = "amo_tabela")
    private String amotabela;

    @Expose
    @ManyToOne
    @JoinColumn(name = "amo_usucodigo")
    private Usuario usuario;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tb_amostra_selecaosensor",
            joinColumns = {
                @JoinColumn(name = "sel_amocodigo")},
            inverseJoinColumns = {
                @JoinColumn(name = "amo_selcodigo")})
    private List<SelecaoSensor> selecoesSensores = new ArrayList<SelecaoSensor>();

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

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    /*public int getPixelX() {
		return pixelX;
	}

	public void setPixelX(int pixelX) {
		this.pixelX = pixelX;
	}

	public int getPixelY() {
		return pixelY;
	}

	public void setPixelY(int pixelY) {
		this.pixelY = pixelY;
	}*/
    public List<PixelAmostra> getPixelAmostra() {
        return pixelAmostra;
    }

    public void setPixelAmostra(List<PixelAmostra> pixelAmostra) {
        this.pixelAmostra = pixelAmostra;
    }

    public List<PixelAmostra> getPixelAmostrasTrans() {
        return pixelAmostrasTrans;
    }

    public void setPixelAmostrasTrans(List<PixelAmostra> pixelAmostrasTrans) {
        this.pixelAmostrasTrans = pixelAmostrasTrans;
    }

    /*public String getMetodoSelecaoVariavel() {
		return metodoSelecaoVariavel;
	}

	public void setMetodoSelecaoVariavel(String metodoSelecaoVariavel) {
		this.metodoSelecaoVariavel = metodoSelecaoVariavel;
	}

	public float getPercentualVarianciaOriginal() {
		return percentualVarianciaOriginal;
	}

	public void setPercentualVarianciaOriginal(float percentualVarianciaOriginal) {
		this.percentualVarianciaOriginal = percentualVarianciaOriginal;
	}*/
    public String getAmotabela() {
        return amotabela;
    }

    public void setAmotabela(String amotabela) {
        this.amotabela = amotabela;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<SelecaoSensor> getSelecoesSensores() {
        return selecoesSensores;
    }

    public void setSelecoesSensores(List<SelecaoSensor> selecoesSensores) {
        this.selecoesSensores = selecoesSensores;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    

    @Override
    public String toString() {
        return "Amostra [id=" + id + ", descricao=" + descricao + ", data=" + dataCadastro + ", dataFormatada=" + dataFormatada
                + ", area=" + area + ", atributo=" + atributo + ", pixelAmostra=" + pixelAmostra
                + ", pixelAmostrasTrans=" + pixelAmostrasTrans + ", amotabela=" + amotabela + ", usuario=" + usuario
                + ", selecoesSensores=" + selecoesSensores + "]";
    }

}
