/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Analise;
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
public class AnaliseService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public AnaliseService() {

    }

    public String salvar(Analise analise) {
        try {
            em.merge(analise);
            em.flush();
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public Analise obter(Long id) {
        return em.find(Analise.class, id);
    }

    public String editar(Analise an, Long id) {
        try {
            an.setIdAnalise(id);
            em.merge(an);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public String excluir(Long id) {
        try {
            Analise an = obter(id);
            em.remove(an);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }
    
    public List<Analise> todos(Long id, String op) {
        boolean an = false;
        boolean apr = false;
        boolean rep = false;
        TypedQuery<Analise> query;
        if(op.equals("1")){
             query = em.createQuery("Select c from Analise as c where c.agendamento.sala.projeto.idProjeto = ?1 Order By c.data_abertura DESC", Analise.class);
             query.setParameter(1, id);
        }else{
            if(op.equals("2")){ an = true; apr = false; rep = false;}
            if(op.equals("3")){ an = false; apr = true; rep = false;}
            if(op.equals("4")){ an = false; apr = false; rep = true;}
            query = em.createQuery("Select c from Analise as c where c.agendamento.sala.projeto.idProjeto = ?1 and c.analise = ?2 and c.aprovado = ?3 and c.reprovado = ?4 Order By c.data_abertura DESC", Analise.class);
            query.setParameter(1, id);
            query.setParameter(2, an);
            query.setParameter(3, apr);
            query.setParameter(4, rep);
        }
        return query.getResultList();
    }
}
