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

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "tb_pixelzm")
public class PixelZonaManejo implements Serializable{

	private static final long serialVersionUID = -4825493377581661831L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pix_codigo")
	private Long id;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Geometry the_geom;
	
	@Column(nullable = false, name = "pix_valor")
	private int valorpixel;
	
	@ManyToOne
	@JoinColumn(name = "pix_zoncodigo")
	private ZonaManejo zonaManejo;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Geometry getThe_geom() {
		return the_geom;
	}

	public void setThe_geom(Geometry the_geom) {
		this.the_geom = the_geom;
	}

	public int getValorpixel() {
		return valorpixel;
	}

	public void setValorpixel(int valorpixel) {
		this.valorpixel = valorpixel;
	}

	public ZonaManejo getZonaManejo() {
		return zonaManejo;
	}

	public void setZonaManejo(ZonaManejo zonaManejo) {
		this.zonaManejo = zonaManejo;
	}
	
}
