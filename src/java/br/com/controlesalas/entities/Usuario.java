/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vagner
 */
@Entity
@XmlRootElement
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdUsuario;
    
    @Column(length = 45, nullable = false)
    private String usuario;
    
    @Column(length = 45, nullable = false)
    private String senha;
    
    @Column(nullable = false)
    private String perfil;
   

    // GET SET
    public Long getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Long IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    //HASH EQUAlS
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.IdUsuario != null ? this.IdUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.IdUsuario != other.IdUsuario && (this.IdUsuario == null || !this.IdUsuario.equals(other.IdUsuario))) {
            return false;
        }
        return true;
    }
}
