/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Projeto;
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
public class ProjetoService implements Serializable{
    
    @PersistenceContext
    private EntityManager em;
    
    public ProjetoService(){
        
    }
    
    public String salvar(Projeto projeto){
        try{
            em.merge(projeto);
            em.flush();
            em.clear();
            return null;
        }catch(Exception ex){
            return "Erro " + ex.getMessage();
        }
    }
    
    public List<Projeto> todos(){
        TypedQuery<Projeto> query = em.createQuery("Select c from Projeto as c", Projeto.class);
        return query.getResultList();
    }
            
    
}
