package br.com.sdum.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "tb_pixelamostra")
public class PixelAmostra implements Serializable {

    private static final long serialVersionUID = -4033549800787913896L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pix_codigo")
    private Long id;

    @Column(name = "pix_codigoapi")
    private Long codigo;

    @Column(name = "the_geom")
    @Type(type = "org.hibernatespatial.GeometryUserType")
    private Geometry geometry;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String geom;

    @Column(length = 255, name = "pix_valor")
    private float valor;

    @ManyToOne
    @JoinColumn(name = "pix_amocodigo")
    private Amostra amostra;

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

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }
}
