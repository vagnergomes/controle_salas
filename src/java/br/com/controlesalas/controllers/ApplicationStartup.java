/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.relatorios.Rel_Agendamento_Auto;
import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author vagner.gomes
 */

public class ApplicationStartup implements ServletContextListener  {

    
        @Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("THE APPLICATION STOPPED");
	}

        @Override
	public void contextInitialized(ServletContextEvent arg0) {
	    System.out.println("THE APPLICATION STARTED");
        try{
        	System.out.println("Agendando execução do Job");
        	SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        	Scheduler scheduler = schedulerFactory.getScheduler();
                System.out.println("---------PASSOU1");
        	JobDetail jobDetail = new JobDetail("jobDetail-s1","jobDetailGroup-s1", Rel_Agendamento_Auto.class);
                System.out.println("---------PASSOU2");
        	SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTrigger", "triggerGroup-s1");
        	simpleTrigger.setStartTime(new Date());
                System.out.println("---------PASSOU3");
        	// set the interval, how often the job should run (30 seconds here)
        	simpleTrigger.setRepeatInterval(3000);
        	// set the number of execution of this job, set to 10 times.
//        	simpleTrigger.setRepeatCount(100);
        	// It will run 10 time and exhaust.
        	// Using the variable below, it will indefinitely
        	simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        	scheduler.scheduleJob(jobDetail, simpleTrigger);
   
        	// start the scheduler
        	System.out.println("Inicia o agendamento");
        	scheduler.start();
           
        }catch (SchedulerException se) {
        	System.out.println("Erro no agendamento do Job");
        	//NotificacaoEmail.enviaEmail(se.getMessage());
        }catch (Exception e) {
        	System.out.println("Erro não tratado do agendador de Jobs");
        	//NotificacaoEmail.enviaEmail(e.getMessage());
        }
		
	}
}
