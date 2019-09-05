package br.com.sdum.model;

import com.google.gson.annotations.SerializedName;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_gradeamostral")
public class GradeAmostral implements Serializable {

    private static final long serialVersionUID = 2759034104005795367L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gra_codigo")
    private Long id;

    @Column(name = "gra_codigoapi")
    private Long codigo;

    @Column(nullable = false, length = 60, name = "gra_descricao")
    private String descricao;

    @Column(length = 30, name = "gra_nometabela")
    private String nomeTabela;

    @ManyToOne
    @JoinColumn(name = "gra_arecodigo")
    private Area area;

    @Column(nullable = false, name = "gra_tamx")
    private float tamx;

    @Column(nullable = false, name = "gra_tamy")
    private float tamy;

    @ManyToOne
    @JoinColumn(name = "gra_usucodigo")
    private Usuario usuario;

    @OneToMany(mappedBy = "gradeAmostral", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<PontoAmostral> pontoAmostral;
    
    @Transient
    private List<PontoAmostral> pontoAmostralTrans = new ArrayList<PontoAmostral>();

    @Column(name = "gra_dataCadastro")
    private Date dataCadastro;

    @Transient
    private String dataFormatada;

    @Column(name = "gra_flagsensor")
    private boolean flagsensor;

    public List<PontoAmostral> getPontoAmostralTrans() {
        return pontoAmostralTrans;
    }

    public void setPontoAmostralTrans(List<PontoAmostral> pontoAmostralTrans) {
        this.pontoAmostralTrans = pontoAmostralTrans;
    }
    
    public boolean isFlagsensor() {
        return flagsensor;
    }

    public void setFlagsensor(boolean flagsensor) {
        this.flagsensor = flagsensor;
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public float getTamx() {
        return tamx;
    }

    public void setTamx(float tamx) {
        this.tamx = tamx;
    }

    public float getTamy() {
        return tamy;
    }

    public void setTamy(float tamy) {
        this.tamy = tamy;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<PontoAmostral> getPontoAmostral() {
        return pontoAmostral;
    }

    public void setPontoAmostral(List<PontoAmostral> pontoAmostral) {
        this.pontoAmostral = pontoAmostral;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "GradeAmostral [id=" + id + ", descricao=" + descricao + ", area=" + area + ", tamx=" + tamx + ", tamy=" + tamy
                + ", usuario=" + usuario + ", pontoAmostral=" + pontoAmostral + ", dataCadastro=" + dataCadastro + "]";
    }

}
