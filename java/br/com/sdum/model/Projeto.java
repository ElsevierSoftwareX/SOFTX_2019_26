package br.com.sdum.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_projeto")
public class Projeto implements Serializable {

	private static final long serialVersionUID = 4685841508776491045L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_codigo")
	private Long id;

	@Column(length=100, nullable = false, name = "pro_nome")
	private String nome;

	@ManyToOne
	@JoinColumn(nullable = false, name = "pro_usucodigo")
	private Usuario usuario;
	
	@Column(nullable = false, name = "pro_data")
	private Date data;	
	
	@Column(length=255, name = "pro_descricao")
	private String descricao;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}