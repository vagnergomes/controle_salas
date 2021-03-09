/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.TaskMail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author vagner.gomes
 */
@Stateless
public class TaskMailService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public TaskMailService() {

    }

    public String salvar(TaskMail taskmail) {
        try {
            em.merge(taskmail);
            em.flush();
            return null;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public TaskMail obter(Long id) {
        return em.find(TaskMail.class, id);
    }

    public List<TaskMail> todos() {
        TypedQuery<TaskMail> query = em.createQuery("select t from TaskMail as t", TaskMail.class);
        return query.getResultList();
    }

    public List<TaskMail> todosAtivo() {
        Query query = em.createQuery("select t from TaskMail as t where t.ativo = 1 ");
        return query.getResultList();
    }
    
    
    //SEM USO
    public List<TaskMail> todosParaEnvio(Date dh) {
        List<TaskMail> tasks = new ArrayList<>();
        int hora = dh.getHours();
        int minuto = dh.getMinutes();
        System.out.println("--------5:" + hora + "-" + minuto);
        try {
            Query query = em.createQuery("Select c from TaskMail as c Where c.ativo = 1 and HOUR(c.hora) = "+hora+" and MINUTE(c.hora) = "+minuto);
            tasks = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        System.out.println("--------6:" + tasks);
        return tasks;
    }
    
    //SEM USO
    public List<Agendamento> agendamentosPorTask(Long idTask, Date data){
        System.out.println("--------9: " + idTask + "--"+ data);
        List<Agendamento> agendamentos = new ArrayList<>();
        
        try{            
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Where c.sala.projeto.taskmail.idTaskMail = ?1 and c.inicio >= ?2 and c.sala.projeto.ativo = True and c.analise.aprovado = True ", Agendamento.class);
            query.setParameter(1, idTask);
            query.setParameter(2, data);
            agendamentos = query.getResultList();
        }catch(Exception ex){
            ex.getMessage();
        }
        System.out.println("--------10: " + agendamentos);
        return agendamentos;
    }
    

}
