/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author vagner.gomes
 */
@Entity
@XmlRootElement
public class Descritivo implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idDescritivo;
    
    @Column
    private int qtd_pessoas;
    
    @Column
    private boolean agua;
    
    @Column 
    private boolean cafe;
    
    @Column 
    private boolean frutas;
    
    @Column
    private boolean lanche;
    
    @Column
    private String adicional;

    public Long getIdDescritivo() {
        return idDescritivo;
    }

    public void setIdDescritivo(Long idDescritivo) {
        this.idDescritivo = idDescritivo;
    }

    public int getQtd_pessoas() {
        return qtd_pessoas;
    }

    public void setQtd_pessoas(int qtd_pessoas) {
        this.qtd_pessoas = qtd_pessoas;
    }

    public boolean isAgua() {
        return agua;
    }

    public void setAgua(boolean agua) {
        this.agua = agua;
    }

    public boolean isCafe() {
        return cafe;
    }

    public void setCafe(boolean cafe) {
        this.cafe = cafe;
    }

    public boolean isFrutas() {
        return frutas;
    }

    public void setFrutas(boolean frutas) {
        this.frutas = frutas;
    }

    public boolean isLanche() {
        return lanche;
    }

    public void setLanche(boolean lanche) {
        this.lanche = lanche;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }
    
    
}
