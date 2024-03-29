/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Configuracao;
import br.com.controlesalas.entities.Org;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.entities.TaskMail;
import br.com.controlesalas.services.AnaliseService;
import br.com.controlesalas.services.ProjetoService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJBTransactionRolledbackException;
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
public class ProjetoController implements Serializable {

    @Inject
    private ProjetoService service;

    @Inject
    private AnaliseService service_analise;

    private Projeto projeto;
    private Projeto projetoEditar;
    private Configuracao config;
    private TaskMail taskmail;
    private List<Sala> salas;
    private Object idProjeto;

    Long idUsuario;
    Long idOrg;

    Date data = new Date(System.currentTimeMillis());
    Timestamp dateTime = new Timestamp(data.getTime());

    public ProjetoController() {

    }

    @PostConstruct
    public void init() {

        idUsuario = (Long) getSession().getAttribute("idUsuario");
        Org org = (Org) getSession().getAttribute("org");
        idOrg = org.getIdOrg();
        idProjeto = getSession().getAttribute("idConfigSelecionado");
        if (idProjeto == null) {
            projeto = new Projeto();
            config = new Configuracao();
            taskmail = new TaskMail();
        } else {
            projeto = service.obter(convertToLong(idProjeto));
            config = projeto.getConfig();
            taskmail = projeto.getTaskmail();
        }
    }

    public void salvar() {
        if (idProjeto == null) {
            Org org = (Org) getSession().getAttribute("org");

            org.setOrg("");
            projeto.setOrg(org);
            config.setExports_visivel(true);
            config.setRotulos_visivel(true);
            config.setTerminados_opaco(false);
            config.setTitulo_cabecalho("");
            config.setUrl_img_logo("C:/controlesalas/imgs/logo-padrao.png");
            config.setShow_weekends(true);
            config.setView_agenda("agendaWeek");
            taskmail.setAtivo(false);
            taskmail.setEmail_destinatario(" ");
            taskmail.setHora(dateTime);
            projeto.setAtivo(true);
        }
        projeto.setTaskmail(taskmail);
        projeto.setConfig(config);
        String erro = service.salvar(projeto);
        projeto = new Projeto();
        if (erro == null) {
            if (idProjeto != null) {
                projeto = service.obter(convertToLong(idProjeto));
            }
            MensagemUtil.addMensagemInfo("Configuração salva.");
        } else {
            MensagemUtil.addMensagemError("Erro: " + erro);
        }
    }

    public void excluir() throws IOException {
        projeto = (Projeto) getSession().getAttribute("projeto_desat");
        boolean err = service.excluirUsuariosProjeto(projeto.getIdProjeto());

        if (err == true) {
            String erro = service.excluir(projeto.getIdProjeto());

            if (erro == null) {
                getSession().removeAttribute("projeto_desat");
                getSession().removeAttribute("projetoSelecionado");
                getSession().removeAttribute("idConfigSelecionado");

                FacesContext.getCurrentInstance().getExternalContext().redirect("projetos_desat.xhtml");
                MensagemUtil.addMensagemInfo("Projeto excluído.");
            } else {
                MensagemUtil.addMensagemError("Erro ao excluir projeto.");
            }
        } else {
            MensagemUtil.addMensagemError("Erro ao excluir usuários do projeto. Tente excluir cada um antes.");
        }

    }

    public List<Projeto> todosAtivos() {
        return service.todosAtivos(idOrg);
    }

    public List<Projeto> todosAtivosUsuario() throws EJBTransactionRolledbackException{
        return service.todosAtivosUsuario(idOrg, idUsuario);
    }

    public List<Projeto> todosDesativados() {
        return service.todosDesativados(idOrg);
    }

    public List<Projeto> todos() {
        return service.todos();
    }

    public void selecionarProjeto(Projeto projeto) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        Long idConfig = projeto.getConfig().getIdConfig();
        getSession().setAttribute("projetoSelecionado", projeto);
        getSession().setAttribute("idConfigSelecionado", idConfig);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("../Agendamento/schedule.xhtml");
    }

    public void editar(Projeto p) {
        projetoEditar = p;
    }

    public void selecionar(Projeto p) {
        getSession().setAttribute("projeto_desat", p);
    }

    public void desativar() {
        try {
            projeto = (Projeto) getSession().getAttribute("projeto_desat");
            String nome_projeto = projeto.getNome();
            service.desativar(projeto);
            MensagemUtil.addMensagemInfo("O Projeto " + nome_projeto + " foi desativado e pode ser encontrado em Configurações > Projetos desativados.");
            getSession().removeAttribute("projeto_desat");
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void ativar() {
        try {
            projeto = (Projeto) getSession().getAttribute("projeto_desat");
            String nome_projeto = projeto.getNome();
            service.ativar(projeto);
            MensagemUtil.addMensagemInfo("O projeto " + nome_projeto + " foi ativado e está disponível na lista prinicipal de projetos.");
            getSession().removeAttribute("projeto_desat");
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public String countAnalises(Long id) {
        try {
            String r = ".";
            int n = service_analise.countAnalises(id);
            if (n >= 0 && n <= 999) {
                r = Integer.toString(n);
            }
            return r;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public String countAnalisesUsuario(Long id) {
        try {
            String r = ".";
            int n = service_analise.countAnalisesUsuario(id, idUsuario);
            if (n >= 0 && n <= 999) {
                r = Integer.toString(n);
            }
            return r;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
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

    public TaskMail getTaskmail() {
        return taskmail;
    }

    public void setTaskmail(TaskMail taskmail) {
        this.taskmail = taskmail;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public ProjetoService getService() {
        return service;
    }

    public void setService(ProjetoService service) {
        this.service = service;
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
