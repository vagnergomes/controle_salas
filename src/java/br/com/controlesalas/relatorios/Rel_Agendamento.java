/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.relatorios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.quartz.SchedulerException;

/**
 *
 * @author Ramon
 */
public class Rel_Agendamento implements Serializable {

    private final HttpServletResponse response;
    private final FacesContext context;
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
    public void getAgendamentos(Connection conexao, Date data_inicio, Date data_fim, String formato, String url_logo, Long idProjeto, boolean download) {
        Timestamp ts = new Timestamp(data_inicio.getTime());
        Timestamp ts2 = new Timestamp(data_fim.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String inicio_s = format.format(ts);
        String fim_s = format.format(ts2);
        Timestamp inicio = Timestamp.valueOf(inicio_s);
        Timestamp fim = Timestamp.valueOf(fim_s);

        // pega url de contexto do projeto 
        //String webcontents = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        stream = this.getClass().getResourceAsStream("/Report/report1.jasper");
        Map<String, Object> params = new HashMap<>();
        baos = new ByteArrayOutputStream();
        String tipo = (download == true ? "attachement" : "inline");
        params.put("inicio", inicio);
        params.put("fim", fim);
        params.put("logo_img", url_logo);
        params.put("idProjeto", idProjeto);
        params.put("REPORT_CONNECTION", conexao);
        try {

            JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            JasperPrint print = JasperFillManager.fillReport(report, params, conexao);
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/" + formato);
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", tipo + "; filename=agendamentos." + formato);
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        } catch (JRException | IOException ex) {
            Logger.getLogger(Rel_Agendamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
