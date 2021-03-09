/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.TaskMail;
import br.com.controlesalas.services.TaskMailService;
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
public class TaskMailControler implements Serializable {

    @Inject
    private TaskMailService service;

    private List<TaskMail> taskmails;
    private TaskMail taskmail;
    private Object idTaskmail;

    public TaskMailControler() {
    }

    @PostConstruct
    public void init() {

        idTaskmail = getSession().getAttribute("idConfigSelecionado");
        if (idTaskmail == null) {
            taskmail = new TaskMail();
        } else {
            taskmail = service.obter(convertToLong(idTaskmail));
        }
    }

    public void salvar() {
        String mail = taskmail.getEmail_destinatario().trim();
        taskmail.setEmail_destinatario(mail);
        String erro = service.salvar(taskmail);
        taskmail = new TaskMail();

        if (erro == null) {
            taskmail = service.obter(convertToLong(idTaskmail));
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemError("Erro ao salvar.");
        }
    }

    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    public List<TaskMail> getTaskmails() {
        return taskmails;
    }

    public void setTaskmails(List<TaskMail> taskmails) {
        this.taskmails = taskmails;
    }

    public TaskMail getTaskmail() {
        return taskmail;
    }

    public void setTaskmail(TaskMail taskmail) {
        this.taskmail = taskmail;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
}
