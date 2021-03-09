/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.receita;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author vagner.gomes
 */

public class qsa implements Serializable {

    private String nome;
    private String qual;
    private String pais_origem;
    private String nome_rep_lega;
    private String qual_rep_legal;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQual() {
        return qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getPais_origem() {
        return pais_origem;
    }

    public void setPais_origem(String pais_origem) {
        this.pais_origem = pais_origem;
    }

    public String getNome_rep_lega() {
        return nome_rep_lega;
    }

    public void setNome_rep_lega(String nome_rep_lega) {
        this.nome_rep_lega = nome_rep_lega;
    }

    public String getQual_rep_legal() {
        return qual_rep_legal;
    }

    public void setQual_rep_legal(String qual_rep_legal) {
        this.qual_rep_legal = qual_rep_legal;
    }
    
    
    
    
}
