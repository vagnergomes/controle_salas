/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.relatorios.Rel_Agendamento;
import br.com.controlesalas.services.AgendamentoService;
import br.com.controlesalas.services.ConfigService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.quartz.SchedulerException;

/**
 *
 * @author vagner.gomes
 */
@Named
@ViewScoped
public class AgendamentoViewController implements Serializable {

    @Inject
    private AgendamentoService service;
    
    @Inject
    private ConfigService service_config;

    private Agendamento agendamento;
    private List<Agendamento> agendamentos;
    
    private Projeto projeto;
    private Object idProjeto;

    private ScheduleModel eventos;
    private LazyScheduleModel lazyEventModel;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_fim;

    Date data = new Date(System.currentTimeMillis());
    Timestamp dateTime = new Timestamp(data.getTime());

    @PostConstruct
    public void init(){
        agendamento = new Agendamento();
        idProjeto =  getSession().getAttribute("idConfigSelecionado");
        projeto = (Projeto) getSession().getAttribute("projetoSelecionado");
//        agendamentos = new ArrayList<>();
        data_inicio = dateTime;
        data_fim = ultimoAgendamento();
        data_inicio.setHours(0);
        data_inicio.setMinutes(0);
        data_inicio.setSeconds(0);
        data_fim.setHours(23);
        data_fim.setMinutes(0);
        data_fim.setSeconds(0);   
        agendamentos = service.todosData(data_inicio, data_fim, projeto.getIdProjeto());
        
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

    public void selecionar(Agendamento ag) {
        agendamento = ag;
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

    public void buscarAgendamentos() {
//        String idProjeto = (String) ;
        agendamentos = service.todosData(data_inicio, data_fim, projeto.getIdProjeto());
    }

    public void relatorioAgendamentos(String formato) throws SQLException, SchedulerException {
        Rel_Agendamento rel = new Rel_Agendamento();
        
        String path = "C:/controlesalas/imgs/logo-padrao.png";
        
        if (idProjeto != null) {
            int id = ((Long)idProjeto).intValue();
            String url_logo = service_config.obterUrl(id);
            File file = new File(url_logo);
            if (file.exists()) {
                int pos = file.getName().lastIndexOf(".");
                //nome = file.getName();
                String tipo = file.getName().substring(pos + 1);
                String nome = file.getName().substring(0, pos) + "_rel";
                int pos2 = url_logo.lastIndexOf("/");
                path = url_logo.substring(0, pos2) + "/" + nome + "." + tipo;
            }
        }

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
        rel.getAgendamentos(conexao, data_inicio, data_fim, formato, path, projeto.getIdProjeto(), false);
    }
    
    public void relatorioAutoAgendamentos(String formato, Long idPj) throws SQLException, SchedulerException {
        System.out.println("----Entrou no metodo PDF!:" + idPj);
        Rel_Agendamento rel = new Rel_Agendamento();
        
        String path = "C:/controlesalas/imgs/logo-padrao.png";
        
        if (idPj != null) {
            int id = idPj.intValue();
            String url_logo = service_config.obterUrl(id);
            File file = new File(url_logo);
            if (file.exists()) {
                int pos = file.getName().lastIndexOf(".");
                //nome = file.getName();
                String tipo = file.getName().substring(pos + 1);
                String nome = file.getName().substring(0, pos) + "_rel";
                int pos2 = url_logo.lastIndexOf("/");
                path = url_logo.substring(0, pos2) + "/" + nome + "." + tipo;
            }
        }

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
        rel.getAgendamentos(conexao, data_inicio, data_fim, formato, path, idPj, false);
    }


    public Date ultimoAgendamento() {    
        data_fim = service.ultimoAgendamento();
        if(data_fim == null){
            data_fim = dateTime;
        }
        return data_fim;
    }

    public List<Agendamento> todos() {
        return service.todosProjeto(projeto.getIdProjeto());
    }

    public List<Agendamento> todosData() {
        return service.todosData(data_inicio, data_fim, projeto.getIdProjeto());
    }

//    public void onRowSelect(SelectEvent event) {
//        Object ob = event.getObject();
//        System.out.println("-----OB:"+ ob );
//        Agendamento a = (Agendamento) ob;
//        System.out.println("-----AG:"+ a.getTitulo() );
//    }
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

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
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
