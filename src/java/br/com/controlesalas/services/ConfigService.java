/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Configuracao;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author vagner.gomes
 */

@Stateless
public class ConfigService implements Serializable {
    @PersistenceContext
    private EntityManager em;
    
    public ConfigService(){
        
    }
    
    public String salvar(Configuracao config){
        try{
            em.merge(config);
            em.flush();
            return null;
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    
    public Configuracao obter(Long id){
        return em.find(Configuracao.class, id);
    }
    
    public String obterUrl(int id){
        Query query = em.createQuery("Select c.url_img_logo from Configuracao as c where c.idConfig = ?1");
        query.setParameter(1, id);
        return (String) query.getSingleResult();
    }
    
    public String obterUrlRel(){
        Query query = em.createQuery("Select c.url_img_logo_rel from Configuracao as c where c.idConfig = 1");
        return (String) query.getSingleResult();
    }
    
//    public String obterTitulo(int id){
//        Query query = em.createQuery("Select c.titulo_cabecalho from Configuracao as c where c.idConfig = ?1");
//        query.setParameter(1, id);
//        return (String) query.getSingleResult();
//    }
//    
//    public boolean obterRotuloVisivel(){
//         Query query = em.createQuery("Select c.rotulos_visivel from Configuracao as c where c.idConfig = 1");
//        return (boolean) query.getSingleResult();
//    }
}
