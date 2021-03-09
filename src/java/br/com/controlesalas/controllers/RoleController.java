/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Role;
import br.com.controlesalas.services.RoleService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class RoleController implements Serializable {
    
    @Inject
    private RoleService service;
    
    private Role role;
    private List<Role> roles =  new ArrayList<>();
    public RoleController(){
        
    }
    
    @PostConstruct
    public void init(){
        role = new Role();
        roles = service.todos();
    }
    
    public void salvar(){
        String erro = service.salvar(role);
        
        if(erro == null){
            MensagemUtil.addMensagemInfo("Grupo salvo.");
        }else{
            MensagemUtil.addMensagemError("Erro ao salvar.");
        }
    }
    
    public List<Role> todos(){
        return service.todos();
    }
    
    public List<Role> todosCadastro(){
        return service.todosCadastro();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    
    
}
