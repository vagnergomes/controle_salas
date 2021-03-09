/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vagner.gomes
 */
@Entity
public class TaskMail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTaskMail;

    @Column
    private boolean ativo;

    @Column
    private String email_destinatario;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;

    public Long getIdTaskMail() {
        return idTaskMail;
    }

    public void setIdTaskMail(Long idTaskMail) {
        this.idTaskMail = idTaskMail;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getEmail_destinatario() {
        return email_destinatario;
    }

    public void setEmail_destinatario(String email_destinatario) {
        this.email_destinatario = email_destinatario;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.idTaskMail);
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
        final TaskMail other = (TaskMail) obj;
        if (!Objects.equals(this.idTaskMail, other.idTaskMail)) {
            return false;
        }
        return true;
    }

}
