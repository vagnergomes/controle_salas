/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.task;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Descritivo;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.entities.TaskMail;
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
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.quartz.SchedulerException;

/**
 *
 * @author vagner.gomes
 */
//@RequestScoped
public class Task extends TimerTask {

    Connection con;
    List<TaskMail> taskmails = new ArrayList();
    
    List<Agendamento> agendamentos = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inicio;

    Date data = new Date(System.currentTimeMillis());
    Timestamp dateTime = new Timestamp(data.getTime());

    @Override
    public void run() {

        try {
            //        try {
//            if(listaTasks() != null){
//                System.out.println("---Result: " + listaTasks().size());
//            }else{
//                System.out.println("---Result: Lista vazia" );
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
//        }
            validaCondicao();
        } catch (SQLException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmailException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchedulerException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("beep!");
    }

    public void validaCondicao() throws SQLException, EmailException, SchedulerException, MalformedURLException {
        data_inicio = dateTime;
        data_inicio.setHours(0);
        data_inicio.setMinutes(0);
        data_inicio.setMinutes(0);

        try {
            taskmails = listaTasks();

            if (taskmails != null) {
                 
                for (TaskMail t : taskmails) {  
                    agendamentos = new ArrayList<>();
                    Projeto projeto = new Projeto();
                    TaskMail task = new TaskMail();
                    Agendamento ag = new Agendamento();
                    Sala sala = new Sala();
                    Descritivo desc = new Descritivo();

                    String sql = "Select   p.idprojeto, nome, p.descricao, email_destinatario, idagendamento, titulo, inicio, fim, s.idsala, nome_sala, iddescritivo, qtd_pessoas, agua, cafe, frutas, lanche  from Agendamento as a \n"
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
                            + " \n"
                            + " Where t.idtaskmail = '" + t.getIdTaskMail() + "' and inicio >= '" + data_inicio + "' and p.ativo = 1";

                    PreparedStatement stmt = con.prepareStatement(sql);
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

                            agendamentos.add(ag);
                            projeto.setTaskmail(task);
                            ag = new Agendamento();
                            task = new TaskMail();
                            sala = new Sala();
                            desc = new Descritivo();
                        
                    }
                  enviaEmailFormatoHtml(projeto, agendamentos);
                //con.close();
                }
               
            } else {
                ///caso taskmails vazia
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<TaskMail> listaTasks() throws SQLException {

        con = conexao();
//        System.out.println("---Conexao: " + con);
        String sql = "select * from taskmail where ativo = 1";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<TaskMail> tasks = new ArrayList<TaskMail>();

        if (rs != null) {
            
            while (rs.next()) {
                TaskMail taskmail = new TaskMail();
                taskmail.setIdTaskMail(Long.parseLong(rs.getString("idTaskMail")));
                taskmail.setAtivo(Boolean.parseBoolean(rs.getString("ativo")));
                taskmail.setHora(rs.getDate("hora"));

                tasks.add(taskmail);
            }
        } else {
            tasks = null;
        }

//        conexao().close();
//        rs.close();
//        stmt.close();
        return tasks;
    }

    public Connection conexao() {
        String driver = "com.mysql.jdbc.JDBC4Connection";
        String url = "jdbc:mysql://localhost:3306/controle_salas_pd?characterEncoding=latin1&useConfigs=maxPerformance&allowPublicKeyRetrieval=true&useSSL=false";
        String usuario = "root";
        String senha = "T3rr3n@2021";
        Connection conexao = null;

        try {
            System.setProperty("jdbc.Drivers", driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException ex) {
            conexao = null;
        }

        return conexao;
    }

    public void enviaEmailSimples(Long id) throws EmailException {
        System.out.println("----Evniando Email: " + id);
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo("vagner_.gomes@hotmail.com", "Teste"); //destinatário
        email.setFrom("vagnergomes27@gmail.com", "Eu"); // remetente
        email.setSubject("Teste Email simples"); // assunto do e-mail
        email.setMsg("Teste de Email utilizando commons-email"); //conteudo do e-mail        
        email.setAuthentication("vagnergomes27", "simf136wr");
        email.setSmtpPort(587);
        email.setSSL(true);
        email.setTLS(true);
        email.send();

    }

    public void enviaEmailComAnexo() throws EmailException {

        // cria o anexo 1.
        EmailAttachment anexo1 = new EmailAttachment();
        anexo1.setPath("C:/controlesalas/teste.txt"); //caminho do arquivo (RAIZ_PROJETO/teste/teste.txt)
        anexo1.setDisposition(EmailAttachment.ATTACHMENT);
        anexo1.setDescription("Exemplo de arquivo anexo");
        anexo1.setName("teste.txt");

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo("vagnergomes27@gmail.com", "Guilherme"); //destinatário
        email.setFrom("vagnergomes27@gmail.com", "Eu"); // remetente
        email.setSubject("Teste -&gt; Email com anexos"); // assunto do e-mail
        email.setMsg("Teste de Email utilizando commons-email"); //conteudo do e-mail
        email.setAuthentication("vagnergomes27", "simf136wr");
        email.setSmtpPort(587);
        email.setSSL(true);
        email.setTLS(true);

        // adiciona arquivo(s) anexo(s)
        email.attach(anexo1);
        // envia o email
        email.send();
    }

    /**
     * Envia email no formato HTML
     *
     * @throws EmailException
     * @throws MalformedURLException
     */
    public void enviaEmailFormatoHtml(Projeto projeto, List<Agendamento> agendamentos) throws EmailException, MalformedURLException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String data = df.format(data_inicio);

        HtmlEmail email = new HtmlEmail();

        // adiciona uma imagem ao corpo da mensagem e retorna seu id
//        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
//        String cid = email.embed(url, "Apache logo");
        // configura a mensagem para o formato HTML
        email.setHtmlMsg("&lt;html&gt;Logo do Apache - <img >&lt;/html&gt;");

        // configure uma mensagem alternativa caso o servidor não suporte HTML
        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo(projeto.getTaskmail().getEmail_destinatario(), "Destinatário"); //destinatário
        email.setFrom("controlesalaspd@gmail.com", "Agendamentos"); // remetente
        email.setSubject("Agendamentos - Relatório " + projeto.getNome() + "."); // assunto do e-mail
        email.setMsg("Relatório de agendamentos. \n -Gerado DATA"); //conteudo do e-mail

        StringBuilder sb1 = new StringBuilder(200);
        sb1.append("<html><body >Relatório da agenda:  " + projeto.getNome() + ". <br/> Gerado no dia: " + data + ".<br/> <table border=\"1\" style=\"border-collapse:collapse;text-align:center\"><br/>");
        sb1.append("<tr>\n"
                    + "    <th style=\"padding:5px\">SALA/LOCAL</th>\n"
                    + "    <th style=\"padding:5px\">TÍTULO</th>\n"
                    + "    <th style=\"padding:10px\">INÍCIO</th>\n"
                    + "    <th style=\"padding:10px\">FIM</th>\n"
                    + "    <th style=\"padding:5px\">PESSOAS</th>\n"
                    + "    <th style=\"padding:5px\">ÁGUA</th>\n"
                    + "    <th style=\"padding:5px\">CAFÉ</th>\n"
                    + "    <th style=\"padding:5px\">FRUTAS</th>\n"
                    + "    <th style=\"padding:5px\">LANCHE</th>\n"
                    + "  </tr>");
         
        for (Agendamento ag : agendamentos) {
            String agua = "", cafe = "", frutas = "", lanche = "";
            if (ag.getDescritivo().isAgua()) {
                agua = "X";
            }
            if (ag.getDescritivo().isCafe()) {
                cafe = "X";
            }
            if (ag.getDescritivo().isFrutas()) {
                frutas = "X";
            }
            if (ag.getDescritivo().isLanche()) {
                lanche = "X";
            }

            sb1.append("<tr ><td style=\"padding:5px\">");
            sb1.append(ag.getSala().getNome_sala())
                    .append("</td><td style=\"padding:5px\">")
                    .append(ag.getTitulo())
                    .append("</td><td style=\"padding:10px\">")
                    .append(ag.getInicio())
                    .append("</td><td style=\"padding:10px\">")
                    .append(ag.getFim())
                    .append("</td><td style=\"padding:5px\">")
                    .append(ag.getDescritivo().getQtd_pessoas())
                    .append("</td><td style=\"padding:5px\">")
                    .append(agua)
                    .append("</td><td style=\"padding:5px\">")
                    .append(cafe)
                    .append("</td><td style=\"padding:5px\">")
                    .append(frutas)
                    .append("</td><td style=\"padding:5px\">")
                    .append(lanche);
            sb1.append("</td><br/></tr>");
            String in = sb1.toString();

        }
        sb1.append("</table></br></body></html>");

        email.setHtmlMsg(sb1.toString());

        email.setAuthentication("controlesalaspd", "C0ntr0l3");
        email.setSmtpPort(587);
        email.setSSL(true);
        email.setTLS(true);
        // envia email
        email.send();
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
