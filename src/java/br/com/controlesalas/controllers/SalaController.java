/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.services.SalaService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;;
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

    @PostConstruct
    public void init() {
        sala = new Sala();
        salas = service.todos();
    }

    public void salvar() {
        String erro = service.salvar(sala);
        if (erro == null) {
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemError("Erro ao salvar: " + erro);
        }
    }

    public void onRowEdit(RowEditEvent event) {

        System.out.println("----editar: " + event.getObject());
        sala = ((Sala) event.getObject());
        System.out.println("----editar2: " + sala.getCapacidade());
        String erro = service.salvar(sala);
        if (erro == null) {
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
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

}
