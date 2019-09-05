/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Gabriela
 */

@Entity
@Table(name = "tb_entidadeclassificadora")
public class Entidade implements Serializable {

    private static final long serialVersionUID = 8532126327372361654L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ent_codigo")
    @Expose
    private Long id;
    
    @Column(name = "ent_codigoapi")
    @Expose
    private Long codigo;

    @Column(nullable = false, length = 100, name = "ent_descricao")
    @Expose
    private String descricao;
    
    @Column(nullable = true, length = 3, name = "ent_estado")
    @Expose
    private String estado;
    
    @Column(nullable = false, length = 100, name = "ent_cidade")
    @Expose
    private String cidade;
    
    @ManyToOne
    @JoinColumn(name = "ent_usucodigo")
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
}
