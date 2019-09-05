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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "tb_pixelmapa")
public class PixelMapa implements Serializable {

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
    private String geom;

    @Column(name = "pix_valor", columnDefinition = "float")
    private float valor;

    @ManyToOne
    @JoinColumn(name = "pix_mapcodigo")
    private Mapa mapa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    
}
