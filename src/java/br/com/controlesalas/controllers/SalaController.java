/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.services.SalaService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class SalaController implements Serializable {

    @Inject
    private SalaService service;

    private Sala sala;
    private List<Sala> salas;
    private List<Agendamento> agendamentos;

    @PostConstruct
    public void init() {
        sala = new Sala();
        salas = service.todos();
        agendamentos =  new ArrayList<>();
    }

    public void salvar(){
        System.out.println("---ID salvar: " +sala.getIdSala());
        String erro = service.salvar(sala);
        if (erro == null) {
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemError("Erro ao salvar: " + erro);
        }
    }
    
    public void editar(Sala sala){
        this.sala = sala;
    }
  
    public void excluir(Sala sala) {
        try {
            agendamentos = service.agendamentosSala(sala.getIdSala());
            if (agendamentos.size() > 0) {
                MensagemUtil.addMensagemError("Erro: existem Agendamentos vinculados à esse Local/Sala.");
            } else {
                String erro = service.excluir(sala.getIdSala());
                if (erro == null) {
                    MensagemUtil.addMensagemInfo("Excluído.");
                    agendamentos = new ArrayList<>();
                } else {
                    MensagemUtil.addMensagemError("Erro ao excluir: " + erro);
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public void agendamentosSala(Sala sala){
        agendamentos = service.agendamentosSala(sala.getIdSala());
        this.sala = sala;
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.execute("PF('dialogAgendamentosSala').show();");
    }
 
    public void onRowEdit(RowEditEvent event) {
        sala = ((Sala) event.getObject());
        System.out.println("---Sala:" + sala.getNome_sala());
        String erro = service.salvar(sala);
        if (erro == null) {
            System.out.println("----Salvou 3");
            MensagemUtil.addMensagemInfo("Salvo.");
            
        } else {
            System.out.println("----Não salvou 4");
            MensagemUtil.addMensagemError("Erro ao salvar: " + erro);
        }
    }

    public void onRowCancel() {
        FacesMessage msg = new FacesMessage("Cancelado");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Sala> todos() {
        return service.todos();
    }

    public List<Sala> todosVisiveis() {
        return service.todosVisiveis();
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
    
    
}
