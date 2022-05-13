/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vagner.gomes
 */
@Entity
@XmlRootElement
public class Contato implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idContato;
    
    @Column(name = "contato")
    private String contato;
    
    @Column(name = "descricao")
    private String  descricao;
    
    @Column(name = "telefone")
    private String telefone;
    
    @Column(name = "tel_wpp")
    private boolean tel_wpp;
    
    @Column(name  = "celular")
    private String celular;
    
    @Column(name = "cel_wpp")
    private boolean cel_wpp;
    
    @Column(name = "tel_ramal")
    private boolean tel_ramal;
    
    @Column(name = "cidade")
    private String cidade;
    
    @ManyToOne
    @JoinColumn(name = "idProjeto")
    private Projeto projeto;

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public boolean isTel_wpp() {
        return tel_wpp;
    }

    public void setTel_wpp(boolean tel_wpp) {
        this.tel_wpp = tel_wpp;
    }

    public boolean isCel_wpp() {
        return cel_wpp;
    }

    public void setCel_wpp(boolean cel_wpp) {
        this.cel_wpp = cel_wpp;
    }

    public boolean isTel_ramal() {
        return tel_ramal;
    }

    public void setTel_ramal(boolean tel_ramal) {
        this.tel_ramal = tel_ramal;
    }
    
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.idContato);
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
        final Contato other = (Contato) obj;
        if (!Objects.equals(this.idContato, other.idContato)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
