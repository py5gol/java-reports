package br.com.geodev.app.relatoriosweb.bean;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="tb_relatorio")
public class Relatorio implements Serializable {

    private Integer idRelatorio;
    private String titulo;
    private String nome;
    private byte[] arquivo;
    private byte[] arquivo_jrxml;
    private Long tamanho;
    private Long tamanho_jrxml;

    private Integer idRelatorioMestre;
    private List<Parametro> parametros;
    private List<Relatorio> subrelatorios;

    @Id
    @GeneratedValue
    @Column(name="id_relatorio")
    public Integer getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Integer idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    @NotNull
    @Column(name="titulo")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNome() {
        return nome;
    }

    @NotNull
    @Column(name="nome")
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public Long getTamanho_jrxml() {
        return tamanho_jrxml;
    }

    public void setTamanho_jrxml(Long tamanho_jrxml) {
        this.tamanho_jrxml = tamanho_jrxml;
    }

    public Integer getIdRelatorioMestre() {
        return idRelatorioMestre;
    }

    public void setIdRelatorioMestre(Integer idRelatorioMestre) {
        this.idRelatorioMestre = idRelatorioMestre;
    }

    @OneToMany
    public List<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(List<Parametro> parametros) {
        this.parametros = parametros;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @Cascade(value={CascadeType.ALL})
    public List<Relatorio> getSubrelatorios() {
        return subrelatorios;
    }

    public void setSubrelatorios(List<Relatorio> subrelatorios) {
        this.subrelatorios = subrelatorios;
    }

    @Lob
    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @Lob
    public byte[] getArquivo_jrxml() {
        return arquivo_jrxml;
    }

    public void setArquivo_jrxml(byte[] arquivo_jrxml) {
        this.arquivo_jrxml = arquivo_jrxml;
    }
    
}
