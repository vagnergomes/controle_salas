/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.services;

import br.com.controlesalas.entities.Org;
import br.com.controlesalas.entities.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
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

    public UsuarioService() {

    }
    
    @PreDestroy
    public void destruct() {
        em.close();
    }
    

    public String salvar(Usuario usuario) {
        try {
            em.merge(usuario);
            em.flush();
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getStackTrace();
        }
    }

    public Usuario obterInt(int id) {
        return em.find(Usuario.class, id);
    }

    public Usuario obter(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> todos() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            TypedQuery<Usuario> query = em.createQuery("select p from Usuario as p ", Usuario.class);
            usuarios = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return usuarios;
    }

    public List<Usuario> todosPorOrg(Long idOrg) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            TypedQuery<Usuario> query = em.createQuery("Select u from Usuario as u LEFT JOIN u.orgs as o WHERE o.idOrg = ?1", Usuario.class);
            query.setParameter(1, idOrg);
            usuarios = query.getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return usuarios;
    }

    public String excluir(Long id) {
        try {
            Usuario u = obter(id);
            em.remove(u);
            return null;
        } catch (Exception ex) {
            return "Erro: " + ex.getMessage();
        }
    }

    public boolean excluirProjetosUsuario(Usuario u) {
        Query query = em.createQuery("delete from Projeto_Usuario as pu where pu.usuario.IdUsuario = " + u.getIdUsuario());
        int deletedCount = query.executeUpdate();

        if (deletedCount >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public Usuario consultaPorUsuario(String username) {
        Usuario usuario = new Usuario();
        try {
            Query query = em.createQuery("Select u from Usuario u where u.usuario = ?1 ", Usuario.class);
            query.setParameter(1, username);
            usuario = (Usuario) query.getSingleResult();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return usuario;
    }

    public Org getOrg(Long idUsuario) {
        Org org = new Org();
        try {
            TypedQuery<Org> query = em.createQuery("Select p from Org as p LEFT JOIN p.usuarios as u WHERE u.IdUsuario = ?1", Org.class);
            query.setParameter(1, idUsuario);
            org = query.getSingleResult();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return org;
    }

}
