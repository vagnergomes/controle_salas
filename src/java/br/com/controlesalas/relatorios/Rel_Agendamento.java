/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.relatorios;

import br.com.controlesalas.entities.Agendamento;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.concurrent.Trigger;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Ramon
 */
public class Rel_Agendamento implements Serializable {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;

    public Rel_Agendamento() throws SchedulerException {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();
//        executarRelatorio();
    }

    /*
    defina um parametro: List<Objeto> lista, se usar JavaBean DataSource
     */
    public void getAgendamentos(Connection conexao, Date data_inicio, Date data_fim, String formato) {
        Timestamp ts = new Timestamp(data_inicio.getTime());
        Timestamp ts2 = new Timestamp(data_fim.getTime());        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String inicio_s = format.format(ts);
        String fim_s = format.format(ts2);
        Timestamp inicio = Timestamp.valueOf(inicio_s);
        Timestamp fim = Timestamp.valueOf(fim_s);

        stream = this.getClass().getResourceAsStream("/Report/report1.jasper");
        Map<String, Object> params = new HashMap<>();
        baos = new ByteArrayOutputStream();
        boolean download = false;
        String tipo = (download == true ? "attachement" : "inline");
        params.put("inicio", inicio);
        params.put("fim", fim);
        params.put("REPORT_CONNECTION", conexao);
        try {

            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            JasperPrint print = JasperFillManager.fillReport(report, params, conexao);
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/"+formato);
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", tipo + "; filename=agendamentos."+formato);
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (JRException ex) {
            Logger.getLogger(Rel_Agendamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Rel_Agendamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
 //   Esse método chama a classe Rel_Agendamento_auto em horário determinado, para gerar o relatório automaticamente
//       public void executarRelatorio() throws SchedulerException {
//        SchedulerFactory sf = new StdSchedulerFactory();
//        Scheduler sched = sf.getScheduler();
//
//        JobDetail job = new JobDetail("gerar_relatorio_auto", "grupo", Rel_Agendamento_Auto.class);
//        org.quartz.Trigger aMeiaNoite = TriggerUtils.makeDailyTrigger("triger", 22, 22);
//        sched.scheduleJob(job, aMeiaNoite);
//
//        sched.start();
//    }


}
