/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Analise;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.services.AnaliseService;
import br.com.controlesalas.util.DateUtil;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class AnaliseController implements Serializable {

    @Inject
    private AnaliseService service;

    private Analise analise;

    private List<Analise> analises;

    private String usuario_logado;

    private Projeto projeto;

    private String opcao;

    @PostConstruct
    public void init() {

        analise = new Analise();
        projeto = (Projeto) getSession().getAttribute("projetoSelecionado");
        usuario_logado = (String) getSession().getAttribute("usuario_logado");
        opcao = "1";
        analises = service.todos(projeto.getIdProjeto(), opcao);
    }

    public AnaliseController() {

    }

    public void analise(Analise an) {
        an.setAnalise(true);
        an.setAprovado(false);
        an.setReprovado(false);
        an.setResponsavel(usuario_logado);
        an.setData_analise(DateUtil.data_Timestamp());
        salvar(an);
    }

    public void aprovar(Analise an) {
        an.setAnalise(false);
        an.setAprovado(true);
        an.setReprovado(false);
        an.setResponsavel(usuario_logado);
        an.setData_analise(DateUtil.data_Timestamp());
        salvar(an);
    }

    public void reprovar(Analise an) {
        an.setAnalise(false);
        an.setAprovado(false);
        an.setReprovado(true);
        an.setResponsavel(usuario_logado);
        an.setData_analise(DateUtil.data_Timestamp());
        salvar(an);
    }

    public void salvar(Analise an) {
        String erro = service.salvar(an);

        if (erro == null) {
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemError("Erro ao salvar.");
        }
    }

    public void buscarAnalises() {
//        String idProjeto = (String) ;
        analises = service.todos(projeto.getIdProjeto(), opcao);
    }

    public String cor_analise(boolean an, boolean apr, boolean rep) {
        if (an && !apr && !rep) {
            return "evento_amarelo";
        } else if (!an && apr && !rep) {
            return "evento_verde";
        } else if (!an && !apr && rep) {
            return "evento_cinza";
        } else {
            return "evento_azul";
        }
    }

//    public List<Analise> todos() {
//        return service.todos(projeto.getIdProjeto());
//    }
    public Analise getAnalise() {
        return analise;
    }

    public void setAnalise(Analise analise) {
        this.analise = analise;
    }

    public List<Analise> getAnalises() {
        return analises;
    }

    public void setAnalises(List<Analise> analises) {
        this.analises = analises;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
