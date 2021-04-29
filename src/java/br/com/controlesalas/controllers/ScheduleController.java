/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Role;
import br.com.controlesalas.services.AgendamentoService;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
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

    private Projeto projeto;

    private List<Role> roles;
    private Role role;
    private Long id_usuario;

    private ScheduleModel eventos;
    private LazyScheduleModel lazyEventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    Date date = new Date();
    Timestamp dateTime = new Timestamp(date.getTime());

    public ScheduleController() {
        agendamento = new Agendamento();
    }

    @PostConstruct
    public void init() {
        projeto = (Projeto) getSession().getAttribute("projetoSelecionado");

        if (projeto.getConfig().isTerminados_opaco()) {
            agendamentos = service.todosProjeto(projeto.getIdProjeto());
        } else {
            data_inicio = dateTime;
            data_inicio.setHours(0);
            data_inicio.setMinutes(0);
            data_inicio.setSeconds(0);;
            agendamentos = service.todosApartirHoje(projeto.getIdProjeto(), data_inicio);
        }

        eventos = new DefaultScheduleModel();

        id_usuario = (Long) getSession().getAttribute("idUsuario");
        roles = (List<Role>) getSession().getAttribute("roles");
        for (Role r : roles) {
            role = r;
        }

        for (final Agendamento e : agendamentos) {
            String cor_sala = e.getSala().getCor();
            String desc = "";
            boolean venc = false;

            //valida se evento venceu
            if (e.getFim().before(date)) {
                venc = true;
            }

            if (e.getDescritivo().isAgua()) {
                desc = desc + "A";
            }
            if (e.getDescritivo().isCafe()) {
                desc = desc + "C";
            }
            if (e.getDescritivo().isFrutas()) {
                desc = desc + "F";
            }
            if (e.getDescritivo().isLanche()) {
                desc = desc + "L";
            }

            //Passo um metodo para definir o estilo da caixa do evento
            DefaultScheduleEvent ev = new DefaultScheduleEvent();

            if ("usuario".equals(role.getNome_role())) {
                if (id_usuario.equals(e.getAnalise().getId_solicitante())) {
                    ev.setTitle(e.getTitulo() + "-" + e.getDescritivo().getQtd_pessoas() + "P " + desc);
                } else {
                    ev.setTitle("");
                }
            } else {
                ev.setTitle(e.getTitulo() + "-" + e.getDescritivo().getQtd_pessoas() + "P " + desc);
            }
            ev.setStartDate(converteDateToDateTime(e.getInicio())); //normal sem o metodo
            ev.setEndDate(converteDateToDateTime(e.getFim())); //normal sem o metodo

            if (e.getAnalise().isAnalise()) {
                ev.setStyleClass("analise");
            } else {
                ev.setStyleClass(cor_evento(cor_sala, venc));
            }
            //Passo o id do agendamento pelo getDescrition()
            ev.setDescription(String.valueOf(e.getIdAgendamento()));
            eventos.addEvent(ev);

//            lazyEventModel = new LazyScheduleModel() { ----voltar
//                @Override
//                public void loadEvents(Date start, Date end) {
//                    eventos.addEvent(new DefaultScheduleEvent(e.getTitulo(), e.getInicio(), e.getFim(), e));
//                }
//            };
        }
    }

    public LocalDateTime converteDateToDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public void atualizaEventos() {
        eventos = getEventos();
    }

    public String cor_evento(String cor, boolean venc) {
        String retorno = null;
        if (venc && projeto.getConfig().isTerminados_opaco()) {
            if (cor.equals("orange")) {
                retorno = "evento_amarelo_venc";
            } else if (cor.equals("blue")) {
                retorno = "evento_azul_venc";
            } else if (cor.equals("maroon")) {
                retorno = "evento_marrom_venc";
            } else if (cor.equals("green")) {
                retorno = "evento_verde_venc";
            } else if (cor.equals("red")) {
                retorno = "evento_vermelho_venc";
            } else if (cor.equals("gray")) {
                retorno = "evento_cinza_venc";
            } else {
                retorno = "evento_branco_venc";
            }
        } else {
            if (cor.equals("orange")) {
                retorno = "evento_amarelo";
            } else if (cor.equals("blue")) {
                retorno = "evento_azul";
            } else if (cor.equals("maroon")) {
                retorno = "evento_marrom";
            } else if (cor.equals("green")) {
                retorno = "evento_verde";
            } else if (cor.equals("red")) {
                retorno = "evento_vermelho";
            } else if (cor.equals("gray")) {
                retorno = "evento_cinza";
            } else {
                retorno = "evento_branco";
            }
        }
        return retorno;
    }

    public void cleanAttributes() {
        getSession().removeAttribute("idEventoSelect");
    }

    public void onDateSelect(SelectEvent<LocalDateTime> e) throws ParseException {
        event = DefaultScheduleEvent.builder().startDate(e.getObject()).endDate(e.getObject().plusHours(1)).build();
        getSession().removeAttribute("idEventoSelect");

        getSession().setAttribute("dateSelect", event.getStartDate());

//        RequestContext context = RequestContext.getCurrentInstance(); ---voltar
//        context.execute("PF('dialogNovoEvento').show();"); ---voltar
        PrimeFaces.current().executeScript("PF('dialogNovoEvento').show();");

    }

    public void onEventSelect(SelectEvent e) {
        event = (ScheduleEvent) e.getObject();
        //pega o id do agendamento que foi lancado no getDescription
        getSession().removeAttribute("dateSelect");
        getSession().setAttribute("idEventoSelect", event.getDescription());
        PrimeFaces.current().executeScript("PF('dialogNovoEvento').show();");
    }

    public void onEventResize(ScheduleEntryResizeEvent e) {
        getSession().setAttribute("idResizeSelect", e.getScheduleEvent().getDescription());
        getSession().setAttribute("dateResize", e.getScheduleEvent().getEndDate());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
//        MensagemUtil.addMensagemInfo("Dia:" + event.getDayDelta() + ", Horário:" + event.getMinuteDelta());
    }
    
    public void fullscreen() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("schedule_fs.xhtml");
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
