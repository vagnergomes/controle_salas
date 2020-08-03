/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Agendamento;
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
public class AgendamentoService implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    public AgendamentoService(){
        
    }
    
    public String salvar(Agendamento agendamento){
        try{
            em.merge(agendamento);
            em.flush();
            em.clear();
            return null;
        }catch(Exception ex){
            return"Erro " + ex.getMessage();
        }
    }
    
    public Agendamento obter(Long id){
        return em.find(Agendamento.class, id);
    }
    
    public String editar(Agendamento ag, Long id){
        try{
            ag.setIdAgendamento(id);
            
            em.merge(ag);
            return null;
        }catch(Exception ex){
            return "Erro: " + ex.getMessage();
        }
    }
    
    public String excluir(Long id) {
        try {
            Agendamento ag = obter(id);
            em.remove(ag);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public List<Agendamento> todos(){
        TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Order By c.inicio DESC", Agendamento.class);
        return query.getResultList();
    }
    
}
