/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;


import br.com.controlesalas.entities.Org;
import br.com.controlesalas.entities.Role;
import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.UsuarioService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Vagner
 */
@Named
@SessionScoped
public class LoginController implements Serializable {
    
    @Inject
    private UsuarioService service;
  
    protected Usuario usuario;
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    
    @PostConstruct
    public void init() {
        usuario = new Usuario();
        Org org = new Org();
        //Long idOrg = null;
        //SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
           // Authentication authentication = context.getAuthentication();
      
                if (authentication instanceof Authentication) {
                    usuario = service.consultaPorUsuario(((User) authentication.getPrincipal()).getUsername());
                    org = service.getOrg(usuario.getIdUsuario());

                    getSession().setAttribute("org", org);
                    getSession().setAttribute("idUsuario", usuario.getIdUsuario());
                }
            
        }

    }

    public void reset() {
        init();
    }

//    public void encrypt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        String senha = "admin";
//
//        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
//        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));
//
//        System.out.println(Arrays.toString(messageDigest));
//    }
//    
//    public static String encriptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest messageDigest =  MessageDigest.getInstance("SHA-256");
//        messageDigest.update(password.getBytes("UTF-8"));
//        return new BigInteger(1, messageDigest.digest()).toString(16);
//    }
    public boolean verificaSAdmin() {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        } else {
            boolean admin = true;
            Role r = new Role();
            for (Role role : usuario.getRoles()) {
                r = role;
            }
            if (r.getNome_role().equals("super_administrador")) {
                return admin;
            } else {
                return false;
            }
        }
    }

    public boolean verificaAdmin() {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        } else {
            boolean admin = true;
            Role r = new Role();
            for (Role role : usuario.getRoles()) {
                r = role;
            }
            String papel = r.getNome_role();
            if (papel.equals("administrador")) {
                return admin;
            } else {
                return false;
            }
        }
    }

    public boolean verificaUsuario() {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        } else {
            boolean admin = true;
            Role r = new Role();
            for (Role role : usuario.getRoles()) {
                r = role;
            }
            String papel = r.getNome_role();
            if (papel.equals("usuario")) {
                return admin;
            } else {
                return false;
            }
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
  

    }


