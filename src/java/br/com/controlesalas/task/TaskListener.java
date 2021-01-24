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
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class TaskListener implements ServletContextListener {
    Toolkit toolkit;
    Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("########## Iniciando o TimerTask ##########");
//        taskmails = service_task.todosAtivo();
//        for(TaskMail t: taskmails)
//            System.out.println("----Tasks1: " + t.getIdTaskMail());
//        try{
//        getSession().setAttribute("taskmails", taskmails);
//        }catch(Exception ex){
//            ex.getMessage();
//        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        timer.schedule(new Task(), time);  //delay, intervalo
    }

//    public void teste() throws SchedulerException{
//        SchedulerFactory sf = new StdSchedulerFactory();
//        Scheduler sched = sf.getScheduler();
//
//        JobDetail job = new JobDetail("dispara_email", "grupo", MailController.class);
//        Trigger aMeiaNoite = TriggerUtils.makeDailyTrigger(19, 22);
//        sched.scheduleJob(job, aMeiaNoite);
//
//        sched.start();
//    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
      public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    
}