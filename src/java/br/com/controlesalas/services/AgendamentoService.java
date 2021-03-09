/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class AgendamentoService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public AgendamentoService() {

    }

    public String salvar(Agendamento agendamento) {
        try {
            em.merge(agendamento);
            em.flush();
            em.clear();
            return null;
        } catch (Exception ex) {
            return "Erro " + ex.getMessage();
        }
    }

    public Agendamento obter(Long id) {
        return em.find(Agendamento.class, id);
    }

    public String editar(Agendamento ag, Long id) {
        try {
            ag.setIdAgendamento(id);

            em.merge(ag);
            return null;
        } catch (Exception ex) {
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

    public Date ultimoAgendamento() {
        Query query = em.createNativeQuery("Select c.inicio from Agendamento as c order by c.inicio desc limit 1 ");
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return (Date) query.getSingleResult();
    }

    public List<Agendamento> todos() {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Order By c.inicio DESC", Agendamento.class);
            agendamentos = query.getResultList();;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

    public List<Agendamento> todosProjeto(Long id) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Where c.sala.projeto.idProjeto = ?1 and c.analise.reprovado = False Order By c.inicio DESC", Agendamento.class);
            query.setParameter(1, id);
            agendamentos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

    public List<Agendamento> todosApartirHoje(Long id, Date data) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Where c.inicio >= ?1 and c.sala.projeto.idProjeto = ?2 and c.analise.reprovado = False Order By c.inicio DESC", Agendamento.class);
            query.setParameter(1, data);
            query.setParameter(2, id);
            agendamentos = query.getResultList();;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

    public List<Agendamento> todosSala(Long id) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Where c.sala.idSala = ?1 and c.analise.analise = False and c.analise.aprovado = True Order By c.inicio DESC", Agendamento.class);
            query.setParameter(1, id);
            agendamentos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

    public List<Agendamento> todosData(Date inicio, Date fim, Long id, Long id_usuario, List<Role> roles) {
        TypedQuery<Agendamento> query = null;
        String role = "";

        for (Role r : roles) {
            role = r.getNome_role();
        }

        if ("usuario".equals(role)) {
            try {

                //A lógica da tela Dashboard para usuário comum é diferente. Ele consegue ver apenas as solicitações feitas por ele, independente do Status dela
                query = em.createQuery("Select c from Agendamento as c Where c.analise.id_solicitante = ?1 and c.sala.projeto.idProjeto = ?2 and c.inicio >= ?3 and c.fim <= ?4", Agendamento.class);
                query.setParameter(1, id_usuario);
                query.setParameter(2, id);
                query.setParameter(3, inicio);
                query.setParameter(4, fim);
            } catch (Exception ex) {
                ex.getMessage();
            }

        } else {
            try {
                //O usuário com mais níveis vê apenas as que estão com análise OK e aprovados. Existe a tela de Analise para ver os agendamentos pendentes.
                query = em.createQuery("Select c from Agendamento as c Where c.analise.analise = False and c.analise.aprovado = True and c.sala.projeto.idProjeto = ?1 and c.inicio >= ?2 and c.fim <= ?3", Agendamento.class);
                query.setParameter(1, id);
                query.setParameter(2, inicio);
                query.setParameter(3, fim);
            } catch (Exception ex) {
                ex.getMessage();
            }

        }
        return query.getResultList();
    }

    public List<Agendamento> todos_WS() {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Order By c.inicio DESC ", Agendamento.class);
            agendamentos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

    public List<Agendamento> todos_projeto_WS(int idProjeto) {
        List<Agendamento> agendamentos = new ArrayList<>();
        try {
            TypedQuery<Agendamento> query = em.createQuery("Select c from Agendamento as c Where c.sala.projeto.idProjeto = ?1 Order By c.inicio DESC", Agendamento.class);
            query.setParameter(1, idProjeto);
            agendamentos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return agendamentos;
    }

}
