/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author vagner.gomes
 */
@Entity
public class Analise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnalise;

    @Column
    private boolean analise;

    @Column
    private boolean aprovado;

    @Column
    boolean reprovado;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data_abertura;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data_analise;

    @Column
    private String solicitante;

    @Column
    private Long id_solicitante;

    @Column
    private String responsavel;

    @Column
    private boolean notificacao;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Agendamento agendamento;

    public Long getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(Long idAnalise) {
        this.idAnalise = idAnalise;
    }

    public boolean isAnalise() {
        return analise;
    }

    public void setAnalise(boolean analise) {
        this.analise = analise;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public boolean isReprovado() {
        return reprovado;
    }

    public void setReprovado(boolean reprovado) {
        this.reprovado = reprovado;
    }

    public Date getData_abertura() {
        return data_abertura;
    }

    public void setData_abertura(Date data_abertura) {
        this.data_abertura = data_abertura;
    }

    public Date getData_analise() {
        return data_analise;
    }

    public void setData_analise(Date data_analise) {
        this.data_analise = data_analise;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Long getId_solicitante() {
        return id_solicitante;
    }

    public void setId_solicitante(Long id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public boolean isNotificacao() {
        return notificacao;
    }

    public void setNotificacao(boolean notificacao) {
        this.notificacao = notificacao;
    }

}
