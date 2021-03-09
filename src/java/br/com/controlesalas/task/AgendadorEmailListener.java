/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.task;

/**
 *
 * @author vagner.gomes
 */
import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Analise;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.entities.TaskMail;
import br.com.controlesalas.services.TaskMailService;
import br.com.controlesalas.util.SendMail;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.quartz.SchedulerException;

@Singleton
public class AgendadorEmailListener extends SendMail implements ServletContextListener {

    @Inject
    private TaskMailService service;

    private List<TaskMail> tasks;
    List<Agendamento> agendamentos = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        inicializa();
    }
    
    //@Schedule(second, minute, hour, day of month, month, day(s) of week)
    // @Schedule(minute = "*/1", hour = "*")  repete uma vez por minuto a cada hora

    @Schedule(minute="2", hour="*")  
    public void inicializa() {
        List<TaskMail> tasks2 = new ArrayList<>();
        Date data = new Date(System.currentTimeMillis());
        Timestamp dateTime = new Timestamp(data.getTime());
        data_inicio = dateTime;
        System.out.println("########## Iniciando o Schedule ##########");
        tasks = new ArrayList<>();
        tasks = service.todosAtivo();
        
        for (TaskMail tm : tasks) {
            int hora = tm.getHora().getHours();
            int minuto = tm.getHora().getMinutes();
            if (dateTime.getHours() == hora) {
                tasks2.add(tm);
            }
        }

        for (TaskMail tm : tasks2) {
            try {
                prepararEnvio(tm);
                System.out.println("########## Finalizando o Schedule ##########");
            } catch (SQLException | EmailException | SchedulerException | MalformedURLException ex) {
                Logger.getLogger(AgendadorEmailListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void prepararEnvio(TaskMail taskmail) throws SQLException, EmailException, SchedulerException, MalformedURLException {
        Date data = new Date(System.currentTimeMillis());
        Timestamp dateTime = new Timestamp(data.getTime());
        data_inicio = dateTime;

        try {
            data_inicio.setHours(0);
            data_inicio.setMinutes(0);
            data_inicio.setSeconds(0);
            if (taskmail != null) {
                agendamentos = new ArrayList<>();
                Projeto projeto = new Projeto();
                TaskMail task = new TaskMail();
                Agendamento ag = new Agendamento();
                Sala sala = new Sala();
                Descritivo desc = new Descritivo();
                Analise an = new Analise();

                String driver = "com.mysql.jdbc.JDBC4Connection";
                String url = "jdbc:mysql://localhost:3306/controle_salas_pd?characterEncoding=latin1&useConfigs=maxPerformance&allowPublicKeyRetrieval=true&useSSL=false";
                String usuario = "root";
                String senha = "admin";
                Connection conexao = null;

                try {
                    System.setProperty("jdbc.Drivers", driver);
                    conexao = DriverManager.getConnection(url, usuario, senha);
                } catch (SQLException ex) {
                    conexao = null;
                }

                String sql = "Select p.idprojeto, nome, p.descricao, email_destinatario, idagendamento, titulo, inicio, fim, s.idsala, nome_sala, iddescritivo, qtd_pessoas, agua, cafe, frutas, lanche  from Agendamento as a \n"
                        + "inner join Sala as s\n"
                        + "on s.idSala = a.idSala\n"
                        + "\n"
                        + "inner join Projeto as p\n"
                        + "on p.idProjeto = s.idProjeto\n"
                        + "\n"
                        + "inner join Descritivo as d\n"
                        + "on d.iddescritivo = a.descritivo_iddescritivo\n"
                        + "\n"
                        + "inner join TaskMail as t\n"
                        + "on t.idtaskmail = p.idtaskmail\n"
                        + "\n"
                        + "inner join Analise as an\n"
                        + "on a.analise_idAnalise = an.idAnalise\n"
                        + "\n"
                        + " Where t.idtaskmail = '" + taskmail.getIdTaskMail() + "' and inicio >= '" + data_inicio + "' and p.ativo = 1 and an.aprovado = 1";

                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    projeto.setIdProjeto(rs.getLong("p.idprojeto"));
                    projeto.setNome(rs.getString("nome"));
                    projeto.setDescricao(rs.getString("p.descricao"));
                    task.setEmail_destinatario(rs.getString("email_destinatario"));

                    ag.setIdAgendamento(rs.getLong("idagendamento"));
                    ag.setTitulo(rs.getString("titulo"));
                    ag.setInicio(rs.getTimestamp("inicio"));
                    ag.setFim(rs.getTimestamp("fim"));
                    sala.setIdSala(rs.getLong("s.idsala"));
                    sala.setNome_sala(rs.getString("nome_sala"));

                    desc.setIdDescritivo(rs.getLong("iddescritivo"));
                    desc.setQtd_pessoas(rs.getInt("qtd_pessoas"));
                    desc.setAgua(rs.getBoolean("agua"));
                    desc.setCafe(rs.getBoolean("cafe"));
                    desc.setFrutas(rs.getBoolean("frutas"));
                    desc.setLanche(rs.getBoolean("lanche"));

                    ag.setSala(sala);
                    ag.setDescritivo(desc);
                    ag.setAnalise(an);

                    agendamentos.add(ag);
                    projeto.setTaskmail(task);
                    ag = new Agendamento();
                    task = new TaskMail();
                    sala = new Sala();
                    desc = new Descritivo();

                }

                enviaEmailFormatoHtml(projeto, agendamentos, data_inicio);

                conexao.close();
                rs.close();
                stmt.close();
                conexao = null;

            } else {
                ///caso taskmails vazia
            }
            //con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Task2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
