/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Projeto_Usuario;
import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.ProjetoUsuarioService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vagner.gomes
 */
@Named
@SessionScoped
public class ProjetoUsuarioController implements Serializable{
    
    @Inject
    private ProjetoUsuarioService service;
    
    private Projeto_Usuario pu;
//    private Projeto projeto;
    private Usuario usuario;
    private List<Projeto_Usuario> pus;
    
    public ProjetoUsuarioController(){
        
    }
    
    @PostConstruct
    public void init(){
        pu =  new Projeto_Usuario();
        pus = new ArrayList<>();
//        projeto = new Projeto();
//        usuario = new Usuario();
    }
    
    public void salvar(Projeto p){
        usuario = (Usuario) getSession().getAttribute("usuario_projeto");
        pu.setProjeto(p);
        pu.setUsuario(usuario);
        
        String erro = service.salvar(pu);
        
        if(erro == null){
            MensagemUtil.addMensagemInfo("Salvo.");
            pu =  new Projeto_Usuario();
        }else{
            MensagemUtil.addMensagemError("Erro ao salvar.");
        }
    }
    
    public void excluir(){
        pu = (Projeto_Usuario) getSession().getAttribute("pu_excluir");
        String erro = service.excluir(pu);
        
        if(erro == null){
            MensagemUtil.addMensagemInfo("Excluido.");
        }else{
            MensagemUtil.addMensagemError("Erro ao excluir.");
        }
    }
    
    public void excluirProjetoUsuario(Projeto p){
        usuario = (Usuario) getSession().getAttribute("usuario_projeto");
        pu = service.projetoUsuario(p.getIdProjeto(), usuario.getIdUsuario());

        String erro = service.excluir(pu);
        pu =  new Projeto_Usuario();
        if(erro == null){
            MensagemUtil.addMensagemInfo("Excluido.");
        }else{
            MensagemUtil.addMensagemError("Erro ao excluir.");
        }
    }
    
    public boolean projetoPorUsuario(Projeto p) {
        usuario = (Usuario) getSession().getAttribute("usuario_projeto");
        
        pu = service.projetoUsuario(p.getIdProjeto(), usuario.getIdUsuario());
        
        if(pu == null){
            pu = new Projeto_Usuario();
            return false;
        }else{
            pu = new Projeto_Usuario();
            return true;
        }

    }

    public List<Projeto_Usuario> todos() {
        pus = service.todos();
        return pus;
    }
    
    public List<Projeto_Usuario> usuariosProjeto(){
       Projeto projeto = ((Projeto) getSession().getAttribute("projetoSelecionado"));
       return service.usuariosProjeto(projeto.getIdProjeto());     
    }
    
    public void selecionar(Projeto_Usuario p){
        getSession().setAttribute("pu_excluir", p);
    }
    
    public void selecionarUsuario(Usuario u){
        //System.out.println("--UsuarioSelect: " + u.getUsuario());
        //usuario = u;
        getSession().setAttribute("usuario_projeto", u);
    }
    
//    public void selecionarProjeto(Projeto p){
//        projeto = p;
//    }

    public Projeto_Usuario getPu() {
        return pu;
    }

    public void setPu(Projeto_Usuario pu) {
        this.pu = pu;
    }

//    public Projeto getProjeto() {
//        return projeto;
//    }
//
//    public void setProjeto(Projeto projeto) {
//        this.projeto = projeto;
//    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Projeto_Usuario> getPus() {
        return pus;
    }

    public void setPus(List<Projeto_Usuario> pus) {
        this.pus = pus;
    }
    
    
    
    
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
}
