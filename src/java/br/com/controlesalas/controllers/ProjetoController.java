/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Configuracao;
import br.com.controlesalas.entities.Org;
import br.com.controlesalas.entities.Projeto;
import static br.com.controlesalas.entities.Projeto_.Salas;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.ProjetoService;
import br.com.controlesalas.services.UsuarioService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ProjetoController implements Serializable{
    
    @Inject
    private ProjetoService service;
    
    @Inject
    private UsuarioService usuario_service;
    
    private Projeto projeto;
    private Projeto projetoEditar;
    private Configuracao config;
    private List<Sala> salas;
    private Object idProjeto;
    
    Long idUsuario;
    Long idOrg;
    
    
    public ProjetoController(){
        
    }
    
    @PostConstruct
    public void init(){
        idUsuario = (Long) getSession().getAttribute("idUsuario");
        Org org = (Org) getSession().getAttribute("org");
        idOrg = org.getIdOrg();
        idProjeto =  getSession().getAttribute("idConfigSelecionado");
        //Long idUsuario = (Long) getSession().getAttribute("idUsuario");
        if(idProjeto == null){
            projeto = new Projeto();
            config = new Configuracao();
        }else{
            projeto = service.obter(convertToLong(idProjeto));
            config = projeto.getConfig();
        }
        
//        projeto = new Projeto();
//        projetoEditar = new Projeto();
//        config = new Configuracao();
    }
     
    public void salvar() {
       // System.out.println("------IdSessao:" + usuario.getUsuario());
        if (idProjeto == null) {
            Org org =  (Org) getSession().getAttribute("org");
            
            projeto.setOrg(org);
            config.setExports_visivel(true);
            config.setRotulos_visivel(true);
            config.setTerminados_opaco(true);
            config.setTitulo_cabecalho("");
            config.setUrl_img_logo("C:/controlesalas/imgs/logo-padrao.png");
            config.setShow_weekends(true);
            config.setView_agenda("agendaWeek");
            projeto.setAtivo(true);
        }

        projeto.setConfig(config);
        String erro = service.salvar(projeto);
        if (erro == null) {
            projeto = new Projeto();
            MensagemUtil.addMensagemInfo("Projeto Salvo.");
        } else {
            MensagemUtil.addMensagemError("Erro: " + erro);
        }
    }
    
    public void excluir(){
        projeto = (Projeto) getSession().getAttribute("projeto_desat");
        String erro  = service.excluir(projeto.getIdProjeto());
        
        if(erro == null){
            getSession().removeAttribute("projeto_desat");
            MensagemUtil.addMensagemInfo("Projeto excluído.");
        } else{
            MensagemUtil.addMensagemError("Erro ao excluir projeto.");
        }    
    }
    
    public List<Projeto> todosAtivos() {
        return service.todosAtivos(idOrg);
    }
    
    public List<Projeto> todosAtivosUsuario() {
        System.out.println("---idOrg: " + idOrg);
        System.out.println("---idOrg: " + idUsuario);
        return service.todosAtivosUsuario(idOrg, idUsuario);
    }
    
    public List<Projeto> todosDesativados() {
        return service.todosDesativados(idOrg);
    }
    
    public List<Projeto> todos() {
        return service.todos();
    }
    
    public void selecionarProjeto(Projeto projeto) throws IOException{
        //Long idProjeto = projeto.getIdProjeto();
        FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        Long idConfig = projeto.getConfig().getIdConfig();
        getSession().setAttribute("projetoSelecionado", projeto);
        getSession().setAttribute("idConfigSelecionado", idConfig);
        
//        FacesContext.getCurrentInstance().getExternalContext().redirect("../Agendamento/schedule.xhtml");
    }
    
    public void editar(Projeto p){
        projetoEditar = p;
    }
    
    public void selecionar(Projeto p){
        getSession().setAttribute("projeto_desat", p);
    }
    
//    public void excluir(Projeto projeto) {
//        try {
//            agendamentos = service.agendamentosSala(sala.getIdSala());
//            if (agendamentos.size() > 0) {
//                MensagemUtil.addMensagemError("Erro: existem Agendamentos vinculados à esse Local/Sala.");
//            } else {
//                String erro = service.excluir(sala.getIdSala());
//                if (erro == null) {
//                    MensagemUtil.addMensagemInfo("Excluído.");
//                    agendamentos = new ArrayList<>();
//                } else {
//                    MensagemUtil.addMensagemError("Erro ao excluir: " + erro);
//                }
//            }
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//    }
    
    public void desativar(){
        try{
            projeto = (Projeto) getSession().getAttribute("projeto_desat");
            String nome_projeto = projeto.getNome();
            service.desativar(projeto);
            MensagemUtil.addMensagemInfo("O Projeto "+nome_projeto+" foi desativado e pode ser encontrado em Configurações > Projetos desativados.");
            getSession().removeAttribute("projeto_desat");
        }catch(Exception ex){
            ex.getMessage();
        }
    }
    
     public void ativar(){
        try{
            projeto = (Projeto) getSession().getAttribute("projeto_desat");
            String nome_projeto = projeto.getNome();
            service.ativar(projeto);
            MensagemUtil.addMensagemInfo("O projeto "+nome_projeto+" foi ativado e está disponível na lista prinicipal de projetos.");
            getSession().removeAttribute("projeto_desat");
        }catch(Exception ex){
            ex.getMessage();
        }
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

    public Projeto getProjetoEditar() {
        return projetoEditar;
    }

    public void setProjetoEditar(Projeto projetoEditar) {
        this.projetoEditar = projetoEditar;
    }

    public Configuracao getConfig() {
        return config;
    }

    public void setConfig(Configuracao config) {
        this.config = config;
    }

    public Object getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Object idProjeto) {
        this.idProjeto = idProjeto;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(Long idOrg) {
        this.idOrg = idOrg;
    }
    
    
    
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    
}
