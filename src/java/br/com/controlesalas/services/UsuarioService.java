/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;


import br.com.controlesalas.entities.Usuario;
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
public class UsuarioService implements Serializable {
    
    @PersistenceContext
    protected EntityManager em;
    
    public UsuarioService(){
       
    }
    
    public String salvar(Usuario usuario){
        try{
            em.merge(usuario);
            em.flush();
            return null;
        }catch(Exception ex){
            return "Erro: " + ex.getMessage();
        }
    }
    
    public Usuario obter(int id){
        return em.find(Usuario.class, id);
    }
    
    public List<Usuario> todos(){
        
        TypedQuery<Usuario> query = em.createQuery("select p from Usuario as p ", Usuario.class);
        return query.getResultList();
    }
    
    public String excluir (int id)
    {
        try{
            Usuario p = obter(id);
            em.remove(p);
            return null;
        }catch(Exception ex){
            return "Erro: " + ex.getMessage();
        }
    }
    
    public Usuario consultaPorUsuario(String username){
          Query query = em.createQuery("Select u from Usuario u where u.usuario = ?1 ", Usuario.class);
          query.setParameter(1, username);
          return (Usuario) query.getSingleResult();
    }
}
