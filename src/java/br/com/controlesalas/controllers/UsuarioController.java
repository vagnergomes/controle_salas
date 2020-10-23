/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Role;
import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.RoleService;
import br.com.controlesalas.services.UsuarioService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Session;
import javax.servlet.http.HttpSession;
import javax.transaction.Transaction;
import org.springframework.orm.toplink.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *
 * @author vagner.gomes
 */
@Named
@SessionScoped
public class UsuarioController implements Serializable{
    
    @Inject 
    private UsuarioService service;
    
    private Usuario usuario;

    private List<Role> select_roles;
    
    
    private List<Usuario> usuarios;
    
    public UsuarioController(){
        
    }
    
    @PostConstruct
    public void init(){
        usuario = new Usuario();
        select_roles =  new ArrayList<>();
        usuarios = new ArrayList<>();
    }
    
    public void novoAdmin(){
//        String senha = usuario.getSenha();
        usuario.setSenha(bcrypt(usuario.getSenha()));
        String erro = service.salvar(usuario);
        
        if(erro ==  null){
            getSession().setAttribute("usuario", usuario);
            usuario = new Usuario();
        select_roles =  new ArrayList<>();
            MensagemUtil.addMensagemInfo("Usuário criado."); 
        }else{
            MensagemUtil.addMensagemInfo("Falha ao criar usuário: " + erro); 
        }
    }
    
    public static String bcrypt(String password){
        int i = 0;
        String hashedPassword = "";
	while (i < 10) {
		//String password = "123456";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		hashedPassword = passwordEncoder.encode(password);

		
		i++;;
	}
        return hashedPassword;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Role> getSelect_roles() {
        return select_roles;
    }

    public void setSelect_roles(List<Role> select_roles) {
        this.select_roles = select_roles;
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
