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
        	// Initiate a Schedule Factory
        	SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        	// Retrieve a scheduler from schedule factory
        	Scheduler scheduler = schedulerFactory.getScheduler();
   
        	// Initiate JobDetail with job name, job group, and executable job class
        	JobDetail jobDetail = new JobDetail("jobDetail-s1","jobDetailGroup-s1", Rel_Agendamento_Auto.class);
        	// Initiate SimpleTrigger with its name and group name
        	SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTrigger", "triggerGroup-s1");
        	// set its start up time
        	simpleTrigger.setStartTime(new Date());
        	// set the interval, how often the job should run (30 seconds here)
        	simpleTrigger.setRepeatInterval(3000);
        	// set the number of execution of this job, set to 10 times.
        	// simpleTrigger.setRepeatCount(100);
        	// It will run 10 time and exhaust.
        	// Using the variable below, it will indefinitely
        	simpleTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        	// set the ending time of this job.
        	// We set it for 60 seconds from its startup time here
        	// Even if we set its repeat count to 10,
        	// this will stop its process after 6 repeats as it gets it endtime by then.
        	// simpleTrigger.setEndTime(new Date(ctime + 60000L));
        	// set priority of trigger. If not set, the default is 5
        	// simpleTrigger.setPriority(10);
        	// schedule a job with JobDetail and Trigger
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
