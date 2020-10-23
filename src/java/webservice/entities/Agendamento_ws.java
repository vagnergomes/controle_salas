/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;

/**
 *
 * @author vagner.gomes
 */
public class Agendamento_ws implements Serializable {

    private Long idAgendamento;
    private String titulo;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inicio;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fim;

    private Long idProjeto;
    private String nome_projeto;

    private Long idDescritivo;
    private int qtd_pessoas;
    private boolean agua;
    private boolean cafe;
    private boolean frutas;
    private boolean lanche;
    private String adicional;

    private Long idSala;
    private String nome_sala;
    private String descricao;
    private int capacidade;
    private String cor;
    private boolean visivel;

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Long getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getNome_projeto() {
        return nome_projeto;
    }

    public void setNome_projeto(String nome_projeto) {
        this.nome_projeto = nome_projeto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Long getIdDescritivo() {
        return idDescritivo;
    }

    public void setIdDescritivo(Long idDescritivo) {
        this.idDescritivo = idDescritivo;
    }

    public int getQtd_pessoas() {
        return qtd_pessoas;
    }

    public void setQtd_pessoas(int qtd_pessoas) {
        this.qtd_pessoas = qtd_pessoas;
    }

    public boolean isAgua() {
        return agua;
    }

    public void setAgua(boolean agua) {
        this.agua = agua;
    }

    public boolean isCafe() {
        return cafe;
    }

    public void setCafe(boolean cafe) {
        this.cafe = cafe;
    }

    public boolean isFrutas() {
        return frutas;
    }

    public void setFrutas(boolean frutas) {
        this.frutas = frutas;
    }

    public boolean isLanche() {
        return lanche;
    }

    public void setLanche(boolean lanche) {
        this.lanche = lanche;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public Long getIdSala() {
        return idSala;
    }

    public void setIdSala(Long idSala) {
        this.idSala = idSala;
    }

    public String getNome_sala() {
        return nome_sala;
    }

    public void setNome_sala(String nome_sala) {
        this.nome_sala = nome_sala;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

}
