/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Org;
import br.com.controlesalas.entities.Projeto_Usuario;
import br.com.controlesalas.entities.Role;
import br.com.controlesalas.entities.Usuario;
import br.com.controlesalas.services.RoleService;
import br.com.controlesalas.services.UsuarioService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author vagner.gomes
 */
@Named
@SessionScoped
public class UsuarioController implements Serializable {

    @Inject
    private UsuarioService service;
    
    @Inject
    private RoleService service_role;

    private Usuario usuario;

    private List<Role> select_roles;
    
    private Role role;

    private List<Usuario> usuarios;

    private Org org;

    Long idUsuario;

    public UsuarioController() {

    }

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        org = new Org();
        select_roles = new ArrayList<>();
        role = new Role();
        usuarios = new ArrayList<>();
        idUsuario = (Long) getSession().getAttribute("idUsuario");

    }
    
    public void salvarAdmin() {
        List<Org> orgs = new ArrayList<>();

        org = new Org();
        org.setOrg("");
        orgs.add(org);

        role = service_role.roleAdministrador();
        select_roles.add(role);

        usuario.setRoles(select_roles);
        usuario.setOrgs(orgs);
        usuario.setSenha(bcrypt(usuario.getSenha()));
        String erro = service.salvar(usuario);

        if (erro == null) {
            getSession().setAttribute("usuario", usuario);
            usuario = new Usuario();
            select_roles = new ArrayList<>();
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemInfo("Falha ao salvar: " + erro);
        }
    }

    public void salvar() {
        List<Org> orgs = new ArrayList<>();
        org = (Org) getSession().getAttribute("org");
        orgs.add(org);
        select_roles.add(role);
        usuario.setRoles(select_roles);
        usuario.setOrgs(orgs);
        
        if(usuario.getIdUsuario() == null){
            usuario.setSenha(bcrypt(usuario.getSenha()));
        }

        String erro = service.salvar(usuario);

        if (erro == null) {
            getSession().setAttribute("usuario", usuario);
            usuario = new Usuario();
            select_roles = new ArrayList<>();
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemInfo("Falha ao salvar: " + erro);
        }
    }
    
    public void alterarSenha() {
        //String username = service.consultaPorUsuario(usuario.getUsuario()).getUsuario();
        
//        List<Org> orgs = new ArrayList<>();
//        org = (Org) getSession().getAttribute("org");
//        orgs.add(org);
//        select_roles.add(role);
//        usuario.setRoles(select_roles);
//        usuario.setOrgs(orgs);
        
        usuario.setSenha(bcrypt(usuario.getSenha()));

        String erro = service.salvar(usuario);

        if (erro == null) {
            getSession().setAttribute("usuario", usuario);
            usuario = new Usuario();
            select_roles = new ArrayList<>();
            MensagemUtil.addMensagemInfo("Salvo.");
        } else {
            MensagemUtil.addMensagemInfo("Falha ao salvar: " + erro);
        }
    }

    public List<Usuario> todosPorOrgs() {
        org = (Org) getSession().getAttribute("org");
        return service.todosPorOrg(org.getIdOrg());
    }
    
    public void excluir() {
        if (!usuario.getUsuario().isEmpty()) {
            boolean err = service.excluirProjetosUsuario(usuario);

            if (err == true) {
                String erro = service.excluir(usuario.getIdUsuario());
                if (erro == null) {
                    MensagemUtil.addMensagemInfo("Excluído.");
                    usuario = new Usuario();
                    usuario.setOrgs(new ArrayList<>());
                    usuario.setRoles(new ArrayList<>());

                } else {
                    MensagemUtil.addMensagemError(erro);
                }
            } else {
                MensagemUtil.addMensagemError("Erro: existem agendas vínculadas à este usuário. Tente remover o acesso antes.");
            }
        } else {
            MensagemUtil.addMensagemError("Nenhum usuário selecionado.");
        }
    }
    
    public void editarUsuario(Usuario u){
        System.out.println("EditarUsuario: " + u.getIdUsuario());
        if(u != null){
            usuario = service.obter(u.getIdUsuario());
            PrimeFaces.current().executeScript("PF('dlg_editar_usuario').show();");
        }else{
            usuario = new Usuario(); 
        }
    }
    
     
    public void editarSenha(Usuario u){
        System.out.println("EditarSenha: " + u.getIdUsuario());
        if(u != null){
            usuario = service.obter(u.getIdUsuario());
            PrimeFaces.current().executeScript("PF('dlg_editar_senha').show();");
        }else{
            usuario = new Usuario(); 
        }
    }
    
    public void novoUsuario(){
        usuario = new Usuario();
        PrimeFaces.current().executeScript("PF('dlg_novo_usuario').show();");
    }
    
    public void selecionar(Usuario u){
        usuario = u;
    }
    
    public boolean validar(){
        if(usuario != null){
            return false;
        }else{
            return true;
        }
    }

    public static String bcrypt(String password) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
