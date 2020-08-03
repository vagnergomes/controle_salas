/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.services.AgendamentoService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author vagner.gomes
 */
@Named
@ViewScoped
public class ScheduleController implements Serializable {

    @Inject
    private AgendamentoService service;

    private Agendamento agendamento;
    private List<Agendamento> agendamentos;

    private ScheduleModel eventos;
    private LazyScheduleModel lazyEventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();
    
    public ScheduleController() {
        agendamento = new Agendamento();

    }
    
    
    @PostConstruct
    public void init() {
        agendamentos = service.todos();
        eventos = new DefaultScheduleModel();

        for (final Agendamento e : agendamentos) {
            String hora = String.valueOf(e.getFim().getHours());
            String minuto = String.valueOf(e.getFim().getMinutes());
            String cor_sala = e.getSala().getCor();
            String desc = "";
            if (e.getFim().getHours() <= 9) {
                hora = "0" + e.getFim().getHours();
            }
            if (e.getFim().getMinutes() <= 9) {
                minuto = "0" + e.getFim().getMinutes();
            }

            if(e.getDescritivo().isAgua()){ desc = desc + "A";}
            if(e.getDescritivo().isCafe()){ desc = desc + "C";}
            if(e.getDescritivo().isFrutas()){ desc = desc + "F";}
            
            //Passo um metodo para definir o estilo da caixa do evento
            DefaultScheduleEvent ev = new DefaultScheduleEvent(
                     e.getTitulo() + "-" + e.getDescritivo().getQtd_pessoas()+"P "+desc,
                     e.getInicio(),
                     e.getFim(),
                     cor_evento(cor_sala));

            //Passo o id do agendamento pelo getDescrition()
            ev.setDescription(String.valueOf(e.getIdAgendamento()));
            eventos.addEvent(ev);

            lazyEventModel = new LazyScheduleModel() {
                @Override
                public void loadEvents(Date start, Date end) {
                    eventos.addEvent(new DefaultScheduleEvent(e.getTitulo(), e.getInicio(), e.getFim(), e));
                }
            };
        }
    }

    public void atualizaEventos(){
        eventos = getEventos();
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
        }else if (cor.equals("red")) {
            return "evento_vermelho";
        } else { 
            return "evento_cinza";
        }
    }
    
    public void onDateSelect(SelectEvent e) throws ParseException {
        getSession().removeAttribute("idEventSelect");
        getSession().setAttribute("dateSelect", e.getObject());
    }

    public void onEventSelect(SelectEvent e) {
        event = (ScheduleEvent) e.getObject();
        //pega o id do agendamento que foi lancado no getDescription
        getSession().removeAttribute("dateSelect");
        getSession().setAttribute("idEventSelect", event.getDescription());
    }
    
    public void onEventResize(ScheduleEntryResizeEvent e) {
          //ScheduleEntryResizeEvent eventResize = (ScheduleEntryResizeEvent) e.getScheduleEvent();
//          System.out.println("---EventResize:" + e.getScheduleEvent().getEndDate());
//          System.out.println("---Evento: " + e.getScheduleEvent().getDescription());
          
          getSession().setAttribute("idResizeSelect", e.getScheduleEvent().getDescription());
          getSession().setAttribute("dateResize", e.getScheduleEvent().getEndDate());
//          System.out.println("---dateResize: " + e.getScheduleEvent().getEndDate());
//        MensagemUtil.addMensagemInfo("Dia:" + event.getDayDelta() + ", Horário:" + event.getMinuteDelta());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        System.out.println("---EventMove:" + event);
//        MensagemUtil.addMensagemInfo("Dia:" + event.getDayDelta() + ", Horário:" + event.getMinuteDelta());
    }
    public List<Agendamento> todos() {
        return service.todos();
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
//    @Schedule(second="*/1", minute="*", hour="*", persistent=false)
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

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    
}
