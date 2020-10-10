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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author vagner.gomes
 */
@Entity
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
    
    @OneToMany(mappedBy = "Projeto", cascade = CascadeType.REMOVE)
    private List<Sala> Salas ;
    
//    @ManyToOne
//    private Usuario usuario;

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

    public List<Sala> getSalas() {
        return Salas;
    }

    public void setSalas(List<Sala> Salas) {
        this.Salas = Salas;
    }
    
    
    

//    public Usuario getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }

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
