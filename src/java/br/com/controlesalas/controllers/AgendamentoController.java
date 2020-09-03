/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.relatorios.Rel_Agendamento;
import br.com.controlesalas.services.AgendamentoService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.quartz.SchedulerException;

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

    private ScheduleModel eventos;
    private LazyScheduleModel lazyEventModel;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_fim;

    Date data = new Date(System.currentTimeMillis());
    Timestamp dateTime = new Timestamp(data.getTime());

    public AgendamentoController() {
    }

    @PostConstruct
    public void init() {
        agendamento = new Agendamento();
        iniciaObjeto();
    }

    public void salvar() {
        if (agendamento.getSala() == null) {
            MensagemUtil.addMensagemError("Erro: os campos obrigatórios deve ser preenchidos.");
        } else {
            if (agendamento.getTitulo().isEmpty()) {
                MensagemUtil.addMensagemError("Erro: os campos obrigatórios deve ser preenchidos.");
            } else {
                if (agendamento.getFim().before(agendamento.getInicio())) {
                    MensagemUtil.addMensagemError("Erro: a data final deve ser maior que a data inicial.");
                } else {
//                agendamento.setTerminado(false);
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

    public void excluir() {
        if (!agendamento.getTitulo().isEmpty()) {
            String erro = service.excluir(agendamento.getIdAgendamento());
            if (erro == null) {
                MensagemUtil.addMensagemInfo("Excluído.");
                agendamento = new Agendamento();
                agendamento.setDescritivo(new Descritivo());

            } else {
                MensagemUtil.addMensagemError(erro);
            }
        } else {

        }
        getSession().removeAttribute("idExcluir");
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
    
//    public void onRowSelect(SelectEvent event) {
//        Object ob = event.getObject();
//        System.out.println("-----OB:"+ ob );
//        Agendamento a = (Agendamento) ob;
//        System.out.println("-----AG:"+ a.getTitulo() );
//    }
    
    public void relatorioAgendamentos(String formato) throws SQLException, SchedulerException {
        Rel_Agendamento rel = new Rel_Agendamento();
        String driver = "com.mysql.jdbc.JDBC4Connection";
        String url = "jdbc:mysql://localhost:3306/controle_salas?characterEncoding=latin1&useConfigs=maxPerformance&allowPublicKeyRetrieval=true&useSSL=false";
        String usuario = "root";
        String senha = "admin";
        Connection conexao = null;

        try {
            System.setProperty("jdbc.Drivers", driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException ex) {
        }
        rel.getAgendamentos(conexao, data_inicio, data_fim, formato);
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
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
