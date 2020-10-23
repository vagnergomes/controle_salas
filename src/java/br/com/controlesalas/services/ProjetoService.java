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
import javax.persistence.Query;
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
    
    public Projeto obter(Long id){
        return em.find(Projeto.class, id);
    }
    
     public String excluir(Long id) {
        try {
            Projeto p = obter(id);
            em.remove(p);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }
     
      public List<Projeto> todos(){
        TypedQuery<Projeto> query = em.createQuery("Select c from Projeto as c", Projeto.class);
        return query.getResultList();
    }
    
    public List<Projeto> todosAtivos(Long idUsuario){
        TypedQuery<Projeto> query = em.createQuery("Select p from Projeto as p LEFT JOIN p.usuarios as u WHERE u.IdUsuario = ?1 and p.ativo = 1 ", Projeto.class);
        query.setParameter(1, idUsuario);
        return query.getResultList();
    }
    
    public List<Projeto> todosDesativados(Long idUsuario){
        TypedQuery<Projeto> query = em.createQuery("Select p from Projeto as p LEFT JOIN p.usuarios as u WHERE u.IdUsuario = ?1 and p.ativo = 0 ", Projeto.class);
        query.setParameter(1, idUsuario);
        return query.getResultList();
    }
    
    public void desativar(Projeto projeto){
        Query query = em.createNativeQuery("update Projeto as p set p.ativo = 0 where p.idProjeto = "+projeto.getIdProjeto());
        query.executeUpdate();
    }
    
    public void ativar(Projeto projeto){
        Query query = em.createNativeQuery("update Projeto as p set p.ativo = 1 where p.idProjeto = "+projeto.getIdProjeto());
//        query.setParameter(1, projeto.getIdProjeto());
        query.executeUpdate();
    }
            
    
}
