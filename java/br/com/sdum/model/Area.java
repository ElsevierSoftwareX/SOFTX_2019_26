package br.com.sdum.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.geotools.geojson.geom.GeometryJSON;
import org.hibernate.annotations.Type;

//import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "tb_area")
public class Area implements Serializable {

    private static final long serialVersionUID = -548529663458912740L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "are_codigo")
    @Expose
    private Long codigo;

    @Column(nullable = false, length = 30, name = "are_descricao")
    @Expose
    private String descricao;

    @Column(name = "are_tamanho")
    @Expose
    private double tamanho;

    @ManyToOne
    @JoinColumn(name = "are_tipsolocodigo")
    @SerializedName("solo")
    @Expose
    private TipoSolo tipoSolo;

    @ManyToOne
    @JoinColumn(name = "are_procodigo")
    private Projeto projeto;

    @Column(name = "the_geom")
    @Type(type = "org.hibernatespatial.GeometryUserType")
    private Geometry geometry;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @Expose
    private String geom;

    @Transient
    private List<Amostra> amostras;

    @Transient
    private Amostra amostra;

    @Transient
    private List<GradeAmostral> gradesAmostrais;

    @Transient
    private GradeAmostral gradeAmostral;

    @Expose
    @Column(nullable = false, name = "are_datacadastro")
    @SerializedName("dataCadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;

    @ManyToOne
    @JoinColumn(name = "are_usucodigo")
    @Expose
    private Usuario usuario;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getTamanho() {
        return tamanho;
    }

    public void setTamanho(double tamanho) {
        this.tamanho = tamanho;
    }

    public TipoSolo getTipoSolo() {
        return tipoSolo;
    }

    public void setTipoSolo(TipoSolo tipoSolo) {
        this.tipoSolo = tipoSolo;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getGeom() {
        /*if(this.getGeometry() != null) {
			GeometryJSON g = new GeometryJSON();
			this.setGeom("{" + '"' + "type"     + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(this.getGeometry()) + "}");
			return geom;
		}
		return null;
         */
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public List<Amostra> getAmostras() {
        return amostras;
    }

    public void setAmostras(List<Amostra> amostras) {
        this.amostras = amostras;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<GradeAmostral> getGradesAmostrais() {
        return gradesAmostrais;
    }

    public void setGradesAmostrais(List<GradeAmostral> gradesAmostrais) {
        this.gradesAmostrais = gradesAmostrais;
    }

    public GradeAmostral getGradeAmostral() {
        return gradeAmostral;
    }

    public void setGradeAmostral(GradeAmostral gradeAmostral) {
        this.gradeAmostral = gradeAmostral;
    }

}
