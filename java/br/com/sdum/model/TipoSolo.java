package br.com.sdum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_tiposolo")
public class TipoSolo implements Serializable {

    private static final long serialVersionUID = 6657133520161401154L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(name = "tip_codigo")
    private Long codigo;

    @Expose
    @Column(nullable = false, length = 60, name = "tip_descricao")
    private String descricaoPT;
    
    @Expose
    @Column(name = "tip_en_descricao", length = 60)
    private String descricaoEN;

    @Expose
    @Column(name = "tip_es_descricao", length = 60)
    private String descricaoES;



    @Column(nullable = false, name = "tip_datacadastro")
    @SerializedName("dataCadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    
    @Expose
    @Column
    private Usuario usuario;

    @Expose
    @ManyToMany
    @JoinTable(name = "tb_solousuario",
            joinColumns = {
                @JoinColumn(name = "usu_tipcodigo")},
            inverseJoinColumns = {
                @JoinColumn(name = "tip_usucodigo")})
    private List<Usuario> usuarios = new ArrayList<Usuario>();

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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDescricaoPT() {
        return descricaoPT;
    }

    public void setDescricaoPT(String descricaoPT) {
        this.descricaoPT = descricaoPT;
    }

    public String getDescricaoEN() {
        return descricaoEN;
    }

    public void setDescricaoEN(String descricaoEN) {
        this.descricaoEN = descricaoEN;
    }

    public String getDescricaoES() {
        return descricaoES;
    }

    public void setDescricaoES(String descricaoES) {
        this.descricaoES = descricaoES;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
