package br.com.geodev.app.relatoriosweb.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_parametro2")
public class Parametro {

	private Integer idParametro;
	private Integer idRelatorio;
	private String nome;
	private String tipo;
	private String tipoHtml;
	private String classe;
	private String metodo;
	private String propriedadeKey;
	private String propriedadeDescricao;
	private String labelHtml;
	private String nomeRelatorio;

    @Id
    @GeneratedValue
    public Integer getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public Integer getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Integer idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoHtml() {
        return tipoHtml;
    }

    public void setTipoHtml(String tipoHtml) {
        this.tipoHtml = tipoHtml;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getPropriedadeKey() {
        return propriedadeKey;
    }

    public void setPropriedadeKey(String propriedadeKey) {
        this.propriedadeKey = propriedadeKey;
    }

    public String getPropriedadeDescricao() {
        return propriedadeDescricao;
    }

    public void setPropriedadeDescricao(String propriedadeDescricao) {
        this.propriedadeDescricao = propriedadeDescricao;
    }

    public String getLabelHtml() {
        return labelHtml;
    }

    public void setLabelHtml(String labelHtml) {
        this.labelHtml = labelHtml;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String nomeRelatorio) {
        this.nomeRelatorio = nomeRelatorio;
    }
}
