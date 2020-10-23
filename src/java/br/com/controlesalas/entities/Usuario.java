/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    
    @Column(length = 100)
    private String nome_completo;
    
    @Column(length = 50)
    private String email;
    
    @Column(length = 64, nullable = false)
    private String senha;
    

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = {@JoinColumn(
                    name = "usuario_id")},
            inverseJoinColumns = {@JoinColumn(
                    name = "role_id")})
    private List<Role> roles;
    
     @ManyToMany(mappedBy = "usuarios")
    private List<Projeto> projetos;

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

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
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
