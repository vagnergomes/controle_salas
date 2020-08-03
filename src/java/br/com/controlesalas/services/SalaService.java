/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Sala;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author vagner.gomes
 */
@Stateless
public class SalaService implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    public SalaService(){
    }
    
    public String salvar(Sala sala){
        try{
            em.merge(sala);
            em.flush();
            return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }  
    
    public List<Sala> todos(){
        TypedQuery<Sala> query = em.createQuery("Select c from Sala as c", Sala.class);
        return query.getResultList();
    }
    
    public List<Sala> todosVisiveis(){
        TypedQuery<Sala> query = em.createQuery("Select c from Sala as c Where c.visivel = True", Sala.class);
        return query.getResultList();
    }
    
}
