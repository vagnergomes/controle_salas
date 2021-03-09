/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.util;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Projeto;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author vagner.gomes
 */
public class SendMail {

    /**
     * Envia email no formato HTML
     *
     * @param projeto
     * @param agendamentos
     * @throws EmailException
     * @throws MalformedURLException
     */
    public void enviaEmailFormatoHtml(Projeto projeto, List<Agendamento> agendamentos, Date data_inicio) throws EmailException, MalformedURLException, SQLException {
        DateFormat df_h = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String data = df.format(data_inicio);

        HtmlEmail email = new HtmlEmail();
        email.setHtmlMsg("&lt;html&gt;Logo do Apache - <img >&lt;/html&gt;");

        // configure uma mensagem alternativa caso o servidor não suporte HTML
        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo(projeto.getTaskmail().getEmail_destinatario(), "Destinatário"); //destinatário
        email.setFrom("controlesalaspd@gmail.com", "Agendamentos"); // remetente
        email.setSubject("Agendamentos - " + projeto.getNome() + "."); // assunto do e-mail
//        email.setMsg("Relatório de agendamentos. \n -Gerado DATA"); //conteudo do e-mail
        StringBuilder sb1 = new StringBuilder(200);
        sb1.append("<html><body >Relatório da agenda:  ").append(projeto.getNome()).append(". <br/> Gerado no dia: ").append(data).append(".<br/> <table border=\"1\" style=\"border-collapse:collapse;text-align:center\"><br/><br/>");
        sb1.append("<tr style=\"margin-top:50px;\">");
                sb1.append( "    <th style=\"padding:5px\">SALA/LOCAL</th>");
                sb1.append( "    <th style=\"padding:5px\">TÍTULO</th>");
                sb1.append( "    <th style=\"padding:10px\">INÍCIO</th>");
                sb1.append( "    <th style=\"padding:10px\">FIM</th>");
                sb1.append( "    <th style=\"padding:5px\">PESSOAS</th>");
                sb1.append( "    <th style=\"padding:5px\">ÁGUA</th>");
                sb1.append( "    <th style=\"padding:5px\">CAFÉ</th>");
                sb1.append( "    <th style=\"padding:5px\">FRUTAS</th>");
                sb1.append( "    <th style=\"padding:5px\">LANCHE</th>");
                sb1.append( "  </tr>");

        for (Agendamento ag : agendamentos) {
            String agua = "", cafe = "", frutas = "", lanche = "";
            String inicio = df_h.format(ag.getInicio());
            String fim = df_h.format(ag.getFim());
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
                    .append(inicio)
                    .append("</td><td style=\"padding:10px\">")
                    .append(fim)
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
            sb1.append("</td></tr>");
            //String in = sb1.toString();

        }
        sb1.append("</table></br></body></html>");

        email.setHtmlMsg(sb1.toString());

        email.setAuthentication("controlesalaspd", "C0ntr0l3");;
        email.setSmtpPort(587);
        email.setSSL(true);
        email.setTLS(true);
        // envia email
        email.send();
        System.out.println("----Email Enviado." + data_inicio);
    }

    public Connection conexao() {
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
        return conexao;
    }

    public void enviaEmailSimples(Long id) throws EmailException {
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

}
