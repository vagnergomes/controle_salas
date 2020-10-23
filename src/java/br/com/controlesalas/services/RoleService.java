/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;


import br.com.controlesalas.entities.Role;
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
public class RoleService implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    public RoleService(){
        
    } 
    
    public String salvar(Role role){
        try{
            em.merge(role);
            em.flush();                    
            return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    
    public List<Role> todos(){
        Query query = em.createQuery("select c from Role as c", Role.class);
        return query.getResultList();
    }  
    
}
