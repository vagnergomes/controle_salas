/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Projeto;
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
public class ProjetoService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public ProjetoService() {

    }

    public String salvar(Projeto projeto) {
        try {
            em.merge(projeto);
            em.flush();
            em.clear();
            return null;
        } catch (Exception ex) {
            return "Erro " + ex.getMessage();
        }
    }

    public Projeto obter(Long id) {
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

    public boolean excluirUsuariosProjeto(Long id) {
        Query query = em.createQuery("delete from Projeto_Usuario as pu where pu.projeto.idProjeto = " + id);
        int deletedCount = query.executeUpdate();
        if (deletedCount >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Projeto> todos() {
        List<Projeto> projetos = new ArrayList<>();
        try {
            TypedQuery<Projeto> query = em.createQuery("Select c from Projeto as c", Projeto.class);
            projetos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return projetos;
    }

    public List<Projeto> todosAtivos(Long idOrg) {
        List<Projeto> projetos = new ArrayList<>();
        try {
            TypedQuery<Projeto> query = em.createQuery("Select p from Projeto as p Where p.org.idOrg = ?1 and p.ativo = 1 ", Projeto.class);
            query.setParameter(1, idOrg);
            projetos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return projetos;
    }

    public List<Projeto> todosAtivosUsuario(Long idOrg, Long idUsuario) {
        List<Projeto> projetos = new ArrayList<>();
        try {
            TypedQuery<Projeto> query = em.createQuery("Select p.projeto from Projeto_Usuario as p Where p.projeto.org.idOrg = '" + idOrg + "' and p.usuario.IdUsuario = '" + idUsuario + "' and p.projeto.ativo = 1 ", Projeto.class);
            projetos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return projetos;
    }

    public List<Projeto> todosDesativados(Long idUsuario) {
        List<Projeto> projetos = new ArrayList<>();
        try {
            TypedQuery<Projeto> query = em.createQuery("Select p from Projeto as p WHERE p.org.idOrg = ?1 and p.ativo = 0 ", Projeto.class);
            query.setParameter(1, idUsuario);
            projetos = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return projetos;
    }

    public void desativar(Projeto projeto) {
        try {
            Query query = em.createNativeQuery("update Projeto as p set p.ativo = 0 where p.idProjeto = " + projeto.getIdProjeto());
            query.executeUpdate();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void ativar(Projeto projeto) {
        try {
            Query query = em.createNativeQuery("update Projeto as p set p.ativo = 1 where p.idProjeto = " + projeto.getIdProjeto());
            query.executeUpdate();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

}
