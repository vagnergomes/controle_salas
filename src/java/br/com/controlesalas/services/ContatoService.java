/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Contato;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ContatoService implements Serializable{
    
    @PersistenceContext
    private EntityManager em;
    
    private Contato agenda;

    public ContatoService() {
    }

   public void init(){
       agenda = new Contato();
   }
    
    public String salvar(Contato contato){
        try{
        em.merge(contato);
        em.flush();
        return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    
    public Contato obter(Long id){
        return em.find(Contato.class, id);
    }
    
    public List<Contato> todos(Long id) {
        List<Contato> salas = new ArrayList<>();
        try {
            TypedQuery<Contato> query = em.createQuery("Select c from Contato as c where c.projeto.idProjeto = ?1", Contato.class);
            query.setParameter(1, id);
            salas = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return salas;
    }
    
    public List<Contato> todos2() {
        List<Contato> lista = new ArrayList<>();
        try {
            TypedQuery<Contato> query = em.createQuery("select p from Contato as p ", Contato.class);
            lista = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return lista;
    }
    
    public String excluir(Long id) {
        try {
            Contato contato = obter(id);
            em.remove(contato);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }
    
}
