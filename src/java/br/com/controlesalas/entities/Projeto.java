/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vagner.gomes
 */
@Entity
@XmlRootElement
public class Projeto implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idProjeto;
    
    @Column
    private String nome;
    
    @Column
    private String descricao;
    
    @Column
    private boolean ativo;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Configuracao config;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "idTaskmail")
    private TaskMail taskmail;
    
    @OneToMany(mappedBy = "Projeto", cascade = CascadeType.REMOVE)
    private List<Sala> Salas ;
     
//    @ManyToMany(cascade = CascadeType.DETACH)
//    @JoinTable(
//            name = "projetos_usuarios",
//            joinColumns = {@JoinColumn(
//                    name = "projeto_id")},
//            inverseJoinColumns = {@JoinColumn(
//                    name = "usuario_id")})
//    private List<Usuario> usuarios;
     
     
    
    @ManyToOne
    @JoinColumn(name = "idOrg")
    private Org org;

    public Long getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
   
    public Configuracao getConfig() {
        return config;
    }

    public void setConfig(Configuracao config) {
        this.config = config;
    }

    public TaskMail getTaskmail() {
        return taskmail;
    }

    public void setTaskmail(TaskMail taskmail) {
        this.taskmail = taskmail;
    }

    public List<Sala> getSalas() {
        return Salas;
    }

    public void setSalas(List<Sala> Salas) {
        this.Salas = Salas;
    }

//    public List<Usuario> getUsuarios() {
//        return usuarios;
//    }
//
//    public void setUsuarios(List<Usuario> usuarios) {
//        this.usuarios = usuarios;
//    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.idProjeto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Projeto other = (Projeto) obj;
        if (!Objects.equals(this.idProjeto, other.idProjeto)) {
            return false;
        }
        return true;
    }
     
}
