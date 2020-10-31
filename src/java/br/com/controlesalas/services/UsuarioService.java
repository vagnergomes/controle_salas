/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;


import br.com.controlesalas.entities.Org;
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
            return "Erro: " + ex.getStackTrace();
        }
    }
    
    public Usuario obterInt(int id){
        return em.find(Usuario.class, id);
    }
    
    public Usuario obter(Long id){
        return em.find(Usuario.class, id);
    }
    
    public List<Usuario> todos(){
        
        TypedQuery<Usuario> query = em.createQuery("select p from Usuario as p ", Usuario.class);
        return query.getResultList();
    }
    
    public List<Usuario> todosPorOrg(Long idOrg){
        TypedQuery<Usuario> query = em.createQuery("Select u from Usuario as u LEFT JOIN u.orgs as o WHERE o.idOrg = ?1", Usuario.class);
        query.setParameter(1, idOrg);
        return query.getResultList();
    }
    
    public String excluir (Long id)
    {
        try{
            Usuario u = obter(id);
            em.remove(u);
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
    
    public Org getOrg(Long idUsuario){
        TypedQuery<Org> query = em.createQuery("Select p from Org as p LEFT JOIN p.usuarios as u WHERE u.IdUsuario = ?1", Org.class);
        query.setParameter(1, idUsuario);
        return query.getSingleResult();
    }
    
}
