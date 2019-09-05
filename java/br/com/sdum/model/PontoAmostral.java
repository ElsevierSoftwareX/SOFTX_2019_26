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
@Table(name = "tb_pontoamostral")
public class PontoAmostral implements Serializable {

    private static final long serialVersionUID = 2794705904330278778L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pon_codigo")
    private Long id;

    @Column(name = "pon_codigoapi")
    private Long codigo;

    @Type(type = "org.hibernatespatial.GeometryUserType")
    private Geometry the_geom;

    @Transient
    private String geom;

    @ManyToOne
    @JoinColumn(name = "pon_gracodigo")
    private GradeAmostral gradeAmostral;

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
 
    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public GradeAmostral getGradeAmostral() {
        return gradeAmostral;
    }

    public void setGradeAmostral(GradeAmostral gradeAmostral) {
        this.gradeAmostral = gradeAmostral;
    }

    public Geometry getThe_geom() {
        return the_geom;
    }

    public void setThe_geom(Geometry the_geom) {
        this.the_geom = the_geom;
    }

}
