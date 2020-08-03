/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author vagner.gomes
 */
@Entity
public class Agendamento implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idAgendamento;
    
    @Column(nullable=false)
    private String titulo;
    
//    @Column(nullable=false)
//    private String sala;
    
//    @Column(nullable = false)
//    @Temporal(javax.persistence.TemporalType.DATE)
//    private Date dia;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inicio;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fim;
    
    @ManyToOne
    private Sala sala;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Descritivo descritivo;
    
    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Long idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

//    public String getSala() {;
//        return sala;
//    }
//
//    public void setSala(String sala) {
//        this.sala = sala;
//    }

//    public Date getDia() {
//        return dia;
//    }
//
//    public void setDia(Date dia) {
//        this.dia = dia;
//    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Sala getSala(){
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Descritivo getDescritivo() {
        return descritivo;
    }

    public void setDescritivo(Descritivo descritivo) {
        this.descritivo = descritivo;
    }

    
    //Equals e HashCode
    @Override
    public int hashCode() {
         int hash = 7;
        hash = 67 * hash + (this.idAgendamento != null ? this.idAgendamento.hashCode() : 0);
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
        final Agendamento other = (Agendamento) obj;
        if (this.idAgendamento != other.idAgendamento && (this.idAgendamento == null || !this.idAgendamento.equals(other.idAgendamento))) {
            return false;
        }
        return true;
    }
    
}
