/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Projeto_Usuario;
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
public class ProjetoUsuarioService implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    public ProjetoUsuarioService(){
        
    }
  
    public String salvar(Projeto_Usuario pu) {
        try {
//            if (projetoUsuario(pu.getProjeto().getIdProjeto(), pu.getUsuario().getIdUsuario()) != null) {
//                return "1";
//            } else {
                em.merge(pu);
                em.flush();
                return null;
//            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    
    public Projeto_Usuario obter(Long id){
        return em.find(Projeto_Usuario.class, id);
    }
    
    public String excluir(Projeto_Usuario p){
        try{
            Projeto_Usuario pu = obter(p.getIdPU());
            em.remove(pu);
            return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    
    public List<Projeto_Usuario> todos(){
        TypedQuery<Projeto_Usuario> query = em.createQuery("Select p from Projeto_Usuario as p", Projeto_Usuario.class);
        //query.setParameter(1, idOrg);
        return query.getResultList();
    }
    
    public List<Projeto_Usuario> usuariosProjeto(Long id){
        Query query = em.createQuery("Select p from Projeto_Usuario as p Where p.projeto.idProjeto = '"+id+"'");
        //query.setParameter(1, id);
        return query.getResultList();
    }
    
    public Projeto_Usuario projetoUsuario(Long idProjeto, Long idUsuario) {
        try {
            Projeto_Usuario pu = new Projeto_Usuario();     
            Query query = em.createQuery("Select p from Projeto_Usuario p where p.projeto.idProjeto = '"+idProjeto+"' and p.usuario.IdUsuario = '"+idUsuario+"'");
            
            if (query.getSingleResult() == null) {
                return pu;
            } else {
                pu = (Projeto_Usuario) query.getSingleResult();
                return pu;
            }
        } catch (Exception ex) {
            ex.getStackTrace();
            return null;
        }
    }
}
