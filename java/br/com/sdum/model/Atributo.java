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
@Table(name = "tb_atributo")
public class Atributo implements Serializable {

    private static final long serialVersionUID = 6657133520161401154L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atr_codigo")
    @Expose
    private Long codigo;

    @Column(nullable = false, length = 60, name = "atr_pt_descricao")
    private String descricaoPT;

    @Column(nullable = false, length = 5, name = "atr_pt_sigla")
    private String siglaPT;

    @Column(name = "atr_en_descricao", length = 60, nullable = true)
    private String descricaoEN;
    
    @Column(name = "atr_en_sigla", length = 5, nullable = true)
    private String siglaEN;
    
    @Column(name = "atr_es_descricao", length = 60, nullable = false)
    private String descricaoES;
    
    @Column(name = "atr_es_sigla", length = 5, nullable = true)
    private String siglaES;
    
    @ManyToOne
    @JoinColumn(nullable = false, name = "atr_pt_unicodigo")
    private UnidadeMedida unidadeMedidaPT;
    
    @ManyToOne
    @JoinColumn(nullable = false, name = "atr_en_unicodigo")
    private UnidadeMedida unidadeMedidaEN;
    
    @ManyToOne
    @JoinColumn(nullable = false, name = "atr_es_unicodigo")
    private UnidadeMedida unidadeMedidaES;

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

    public String getDescricaoPT() {
        return descricaoPT;
    }

    public void setDescricaoPT(String descricaoPT) {
        this.descricaoPT = descricaoPT;
    }

    public String getSiglaPT() {
        return siglaPT;
    }

    public void setSiglaPT(String siglaPT) {
        this.siglaPT = siglaPT;
    }

    public String getDescricaoEN() {
        return descricaoEN;
    }

    public void setDescricaoEN(String descricaoEN) {
        this.descricaoEN = descricaoEN;
    }

    public String getSiglaEN() {
        return siglaEN;
    }

    public void setSiglaEN(String siglaEN) {
        this.siglaEN = siglaEN;
    }

    public String getDescricaoES() {
        return descricaoES;
    }

    public void setDescricaoES(String descricaoES) {
        this.descricaoES = descricaoES;
    }

    public String getSiglaES() {
        return siglaES;
    }

    public void setSiglaES(String siglaES) {
        this.siglaES = siglaES;
    }

    public UnidadeMedida getUnidadeMedidaPT() {
        return unidadeMedidaPT;
    }

    public void setUnidadeMedidaPT(UnidadeMedida unidadeMedidaPT) {
        this.unidadeMedidaPT = unidadeMedidaPT;
    }

    public UnidadeMedida getUnidadeMedidaEN() {
        return unidadeMedidaEN;
    }

    public void setUnidadeMedidaEN(UnidadeMedida unidadeMedidaEN) {
        this.unidadeMedidaEN = unidadeMedidaEN;
    }

    public UnidadeMedida getUnidadeMedidaES() {
        return unidadeMedidaES;
    }

    public void setUnidadeMedidaES(UnidadeMedida unidadeMedidaES) {
        this.unidadeMedidaES = unidadeMedidaES;
    }
    

}
