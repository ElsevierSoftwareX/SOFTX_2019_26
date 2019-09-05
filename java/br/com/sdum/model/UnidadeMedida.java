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

@Entity
@Table(name = "tb_unidademedida")
public class UnidadeMedida implements Serializable {

    private static final long serialVersionUID = -3698956747867447084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_codigo")
    @Expose
    private Long id;

    @Expose
    @Column(name = "codigo")
    private Long codigo;

    @Expose
    @Column(nullable = false, length = 60, name = "uni_descricao")
    private String descricao;

    @Expose
    @Column(nullable = false, length = 30, name = "uni_sigla")
    private String sigla;

    @Expose
    @ManyToOne
    @JoinColumn(nullable = false, name = "uni_usucodigo")
    private Usuario usuario;

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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
}
