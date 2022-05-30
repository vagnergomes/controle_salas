/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Analise;
import br.com.controlesalas.entities.Contato;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Role;
import br.com.controlesalas.services.AgendamentoService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    private Projeto projeto;
    
    private Contato contato;

    private Analise analise;

    private List<Role> roles;
    private Role role;

    private String usuario_logado;

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
        analise = new Analise();
        projeto = (Projeto) getSession().getAttribute("projetoSelecionado");
        usuario_logado = (String) getSession().getAttribute("usuario_logado");
        roles = (List<Role>) getSession().getAttribute("roles");
     
        roles.forEach((r) -> {
            role = r;
        });
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

                    Long id_usuario = (Long) getSession().getAttribute("idUsuario");
                    if (role.getNome_role().equals("super_administrador") || role.getNome_role().equals("administrador") || role.getNome_role().equals("supervisor")) {
                        analise.setResponsavel(usuario_logado);
                        analise.setId_solicitante(id_usuario);
                        analise.setAnalise(false);
                        analise.setAprovado(true);
                        analise.setReprovado(false);
                        analise.setData_abertura(dateTime);
                        analise.setData_analise(dateTime);
                        analise.setNotificacao(true);
                    } else {
                        analise.setSolicitante(usuario_logado);
                        analise.setId_solicitante(id_usuario);
                        analise.setResponsavel("");
                        analise.setAnalise(true);
                        analise.setAprovado(false);
                        analise.setReprovado(false);
                        analise.setData_abertura(dateTime);
                        analise.setData_analise(dateTime);
                        analise.setNotificacao(false);
                    }
                    if (agendamento.getIdAgendamento() == null) {
                        analise.setAgendamento(agendamento);
                        agendamento.setAnalise(analise);
                    }
                    String erro = service.salvar(agendamento);
                    if (erro == null) {
                        agendamento = new Agendamento();
                        agendamento.setDescritivo(new Descritivo());

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
                agendamento.setAnalise(new Analise());

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
        return service.todosProjeto(projeto.getIdProjeto());
    }

    public void iniciaObjeto() {
        //aqui comeca a recuperar objeto da sessao 
        Object idEvent = getSession().getAttribute("idEventoSelect");
        Object dateSelect = getSession().getAttribute("dateSelect");

        if (idEvent == null && dateSelect != null) {
            agendamento = new Agendamento();
            agendamento.setDescritivo(new Descritivo());
            agendamento.setAnalise(new Analise());
            LocalDateTime ldt = (LocalDateTime) dateSelect;
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            Date dataSelecionada = Date.from(zdt.toInstant());
            //Date dataSelecionada = (Date) dateSelect;
            Timestamp dt = new Timestamp(dataSelecionada.getTime());
            Timestamp dateTimeFim = new Timestamp(dataSelecionada.getTime());
            dateTimeFim.setHours(dt.getHours() + 1);
            agendamento.setInicio(dt);
            agendamento.setFim(dateTimeFim);

        } else if (dateSelect == null && idEvent != null) {
            agendamento = service.obter(convertToLong(idEvent));

        } else {
            agendamento = new Agendamento();
            agendamento.setDescritivo(new Descritivo());
            agendamento.setAnalise(new Analise());
            Timestamp dateTimeFim = new Timestamp(dateTime.getTime());
            dateTimeFim.setHours(dateTimeFim.getHours() + 1);
            agendamento.setInicio(dateTime);
            agendamento.setFim(dateTimeFim);
            
        }
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

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public LazyScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(LazyScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public Analise getAnalise() {
        return analise;
    }

    public void setAnalise(Analise analise) {
        this.analise = analise;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }
    
    

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
