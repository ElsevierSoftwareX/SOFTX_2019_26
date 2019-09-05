package br.com.sdum.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_classificacao")
public class Classificacao implements Serializable {

    private static final long serialVersionUID = 6657133520161401154L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cla_codigo")
    private Long id;
    
    @Column(name = "cla_codigoapi")
    @Expose
    private Long codigo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "cla_atrcodigo")
    @Expose
    private Atributo atributo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "cla_usucodigo")
    private Usuario usuario;

    @OneToMany(mappedBy = "classificacao", cascade = {CascadeType.ALL})
    private List<Classe> classes;

    @ManyToOne
    @JoinColumn(nullable = false, name = "cla_tipcodigo")
    @Expose
    @SerializedName("solo")
    private TipoSolo tipoSolo;

    
    @ManyToOne
    @JoinColumn(nullable = false, name = "cla_entcodigo")
    @Expose
    private Entidade entidade;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Classe> getClasses() {
        return classes;
    }

    public void setClasses(List<Classe> classes) {
        this.classes = classes;
    }

    public TipoSolo getTipoSolo() {
        return tipoSolo;
    }

    public void setTipoSolo(TipoSolo tipoSolo) {
        this.tipoSolo = tipoSolo;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }
    

}
