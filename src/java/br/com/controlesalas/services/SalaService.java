/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Sala;
import java.io.Serializable;
import java.util.ArrayList;
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
public class SalaService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public SalaService() {
    }

    public String salvar(Sala sala) {
        try {
            em.merge(sala);
            em.flush();
            return null;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public Sala obter(Long id) {
        return em.find(Sala.class, id);
    }

    public String editar(Sala sala, Long id) {
        try {
            sala.setIdSala(id);
            em.merge(sala);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public String excluir(Long id) {
        try {
            Sala ag = obter(id);
            em.remove(ag);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public List<Sala> todos(Long id) {
        List<Sala> salas = new ArrayList<>();
        try {
            TypedQuery<Sala> query = em.createQuery("Select c from Sala as c where c.projeto.idProjeto = ?1", Sala.class);
            query.setParameter(1, id);
            salas = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return salas;
    }

    public List<Sala> rotulos(Long id) {
        List<Sala> salas = new ArrayList<>();
        try {
            TypedQuery<Sala> query = em.createQuery("Select c from Sala as c Where c.visivel = True and c.projeto.idProjeto = ?1", Sala.class);
            query.setParameter(1, id);
            salas = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return salas;
    }

    public List<Agendamento> agendamentosSala(Long id) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            Query query = em.createQuery("Select c from Agendamento as c Where c.analise.analise = False and c.analise.aprovado = True and c.sala.idSala = ?1", Agendamento.class);
            query.setParameter(1, id);
            agendamentos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

}
