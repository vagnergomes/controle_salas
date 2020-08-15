/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.UsuarioService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class UsuarioController implements Serializable{
    
    @Inject 
    private UsuarioService service;
    
    private Usuario usuario;
    
    private List<Usuario> usuarios;
    
    public UsuarioController(){
        usuario = new Usuario();
        usuarios = new ArrayList<>();
    }
    
    public void novoAdmin(){
        usuario.setPerfil("administrador");
        String erro = service.salvar(usuario);
        if(erro ==  null){
            getSession().setAttribute("usuario", usuario);
            System.out.println("--usuario:" + usuario.getUsuario());
            MensagemUtil.addMensagemInfo("Usuário criado."); 
        }else{
            MensagemUtil.addMensagemInfo("Falha ao criar usuário"); 
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    
    
}
