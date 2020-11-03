/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.TaskMail;
import java.io.Serializable;
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
    
    public TaskMailService(){
        
    }
    
    public String salvar(TaskMail taskmail){
        try{
            em.merge(taskmail);
            em.flush();
            return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    
    public TaskMail obter(Long id){
        return em.find(TaskMail.class, id);
    }
    
    public List<TaskMail> todos(){
        TypedQuery<TaskMail> query = em.createQuery("select t from TaskMail as t", TaskMail.class);
        return query.getResultList();
    }
    
    public List<TaskMail> todosAtivo(){
        System.out.println("-----0:");
        Query query = em.createQuery("select t from TaskMail as t where t.ativo = 1");
        return query.getResultList();
    }
    
    
}
