/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.services.AgendamentoService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class AgendamentoController implements Serializable {

    @Inject
    private AgendamentoService service;

    private Agendamento agendamento;
    private List<Agendamento> agendamentos;

    private ScheduleModel eventos;
    private LazyScheduleModel lazyEventModel;

    @PostConstruct
    public void init() {
        agendamentos = service.todos();
        agendamento = new Agendamento();
        iniciaObjeto();
    }

    public void salvar() {
        System.out.println("-----1: " + agendamento.getSala());
        System.out.println("-----2: " + agendamento.getSala().getNome_sala());
        if(agendamento.getSala() == null){
            MensagemUtil.addMensagemError("Erro1: os campos obrigatórios deve ser preenchidos.");   
        }else{
        if (agendamento.getTitulo().isEmpty()) {
            MensagemUtil.addMensagemError("Erro: os campos obrigatórios deve ser preenchidos.");
        } else {
            if (agendamento.getFim().before(agendamento.getInicio())) {
                MensagemUtil.addMensagemError("Erro: a data final deve ser maior que a data inicial.");
            } else {
                String erro = service.salvar(agendamento);
                if (erro == null) {
                    agendamento = new Agendamento();
                    agendamento.setDescritivo(new Descritivo());

                    //RequestContext context = RequestContext.getCurrentInstance();
//            context.execute("PF('dialogNovoEvento').hide();");
                    MensagemUtil.addMensagemInfo("Evento salvo.");
                } else {
                    MensagemUtil.addMensagemError(erro);
                }
            }
        }
        }

    }

    //ainda nao funciona para dashboard
    public void editarPorDashboard(Agendamento a) {
        agendamento = service.obter(a.getIdAgendamento());
    }

    public void excluir() {
        if(!agendamento.getTitulo().isEmpty()){
        String erro = service.excluir(agendamento.getIdAgendamento());

        if (erro == null) {
            MensagemUtil.addMensagemInfo("Excluído.");
            agendamento = new Agendamento();
            agendamento.setDescritivo(new Descritivo());
        } else {
            MensagemUtil.addMensagemError(erro);
        }
        }else{
            
        }
    }

    public String cor_evento(String cor) {
        if (cor.equals("orange")) {
            return "evento_amarelo";
        } else if (cor.equals("blue")) {
            return "evento_azul";
        } else if (cor.equals("maroon")) {
            return "evento_marrom";
        } else if (cor.equals("green")) {
            return "evento_verde";
        } else if (cor.equals("red")) {
            return "evento_vermelho";
        } else {
            return "evento_cinza";
        }
    }

    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    public List<Agendamento> todos() {
        return service.todos();
    }

    public void iniciaObjeto() {
        //aqui comeca a recuperar objeto da sessao 
        Object idEvent = getSession().getAttribute("idEventSelect");
        Object dateSelect = getSession().getAttribute("dateSelect");
        if (idEvent == null && dateSelect != null) {
            agendamento = new Agendamento();
            agendamento.setDescritivo(new Descritivo());

            Date dataSelecionada = (Date) dateSelect;
            Timestamp dateTime = new Timestamp(dataSelecionada.getTime());
            Timestamp dateTimeFim = new Timestamp(dataSelecionada.getTime());
            dateTimeFim.setHours(dateTime.getHours() + 1);
            agendamento.setInicio(dateTime);
            agendamento.setFim(dateTimeFim);

        } else if (dateSelect == null && idEvent != null) {
            agendamento = service.obter(convertToLong(idEvent));

        } else {
            agendamento = new Agendamento();
            agendamento.setDescritivo(new Descritivo());
        }
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    

    /*--------Schedule---------*/
    public ScheduleModel getEventos() {
        return eventos;
    }

    public void setEventos(ScheduleModel eventos) {
        this.eventos = eventos;
    }

    public LazyScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(LazyScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
