/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Configuracao;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.services.ProjetoService;
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
public class ProjetoController  implements Serializable{
    
    @Inject
    private ProjetoService service;
    
    private Projeto projeto;
    private Configuracao config;
    
    public ProjetoController(){
        
    }
    
    @PostConstruct
    public void init(){
        projeto = new Projeto();
        config = new Configuracao();
    }
     
    public void salvar(){
        //config.setExports_visivel(true);
        projeto.setConfig(config);
        String erro = service.salvar(projeto);
        if(erro == null){
            projeto = new Projeto();
            MensagemUtil.addMensagemInfo("Projeto Salvo.");
        }else{
            MensagemUtil.addMensagemError("Erro: " + erro);
        }
    }
    
    public List<Projeto> todos() {
        return service.todos();
    }
    
    public void selecionarProjeto(Projeto projeto){
        Long id = projeto.getIdProjeto();
        getSession().setAttribute("idProjetoSelecionado", id);
        System.out.println("----IdProejto:" + id);
    }
    
    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Configuracao getConfig() {
        return config;
    }

    public void setConfig(Configuracao config) {
        this.config = config;
    }
    
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    
}
