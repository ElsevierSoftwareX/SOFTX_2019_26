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

@Entity
@Table(name = "tb_classe")
public class Classe implements Serializable {

    private static final long serialVersionUID = 6657133520161401154L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cla_codigo")
    private Long id;

    @Column(name = "cla_codigoapi")
    private Long codigo;

    @Column(nullable = false, length = 30, name = "cla_nivel")
    private String nivel;

    @Column(nullable = false, name = "cla_valormin")
    private double valorMinimo;

    @Column(nullable = false, name = "cla_valormax")
    private double valorMaximo;

    @Column(nullable = false, length = 100, name = "cla_cor")
    private String cor;

    @ManyToOne
    @JoinColumn(name = "cla_clacodigo")
    private Classificacao classificacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    
    
}
