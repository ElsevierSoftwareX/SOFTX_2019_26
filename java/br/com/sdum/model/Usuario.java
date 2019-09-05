package br.com.sdum.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 6326492447654637515L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    @Column(name = "usu_codigo")
    private Long codigo;

    @Expose
    @Column(nullable = false, length = 100, name = "usu_nome")
    private String nome;

    @Expose
    @Column(nullable = false, length = 100, name = "usu_email")
    private String email;

    @Expose
    @Column(length = 20, name = "usu_fone", nullable = false)
    private String telefone;

    @Expose
    @Column(length = 20, name = "usu_senha")
    @Transient
    private String senha;

    @Expose
    @Column(length = 250, name = "usu_token")
    private String token;

    
    @ManyToMany(mappedBy = "usuarios", cascade = CascadeType.ALL)
    private List<TipoSolo> tipoSolos = new ArrayList<TipoSolo>();
    

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<TipoSolo> getTipoSolos() {
        return tipoSolos;
    }

    public void setTipoSolos(List<TipoSolo> tipoSolos) {
        this.tipoSolos = tipoSolos;
    }

}
