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

/**
 *
 * @author vagner.gomes
 */
@Entity
public class Configuracao implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idConfig;
    
    @Column
    private boolean terminados_opaco;
    
    @Column
    private boolean rotulos_visivel;
    
    @Column
    private boolean exports_visivel;
    
    @Column
    private String url_img_logo;
    
     @Column
    private String url_img_logo_rel;
    
    @Column 
    private String titulo_cabecalho;
    
    @Column
    private String view_agenda;
    
    @Column
    private boolean show_weekends;

    public Long getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(Long idConfig) {
        this.idConfig = idConfig;
    }

    public boolean isTerminados_opaco() {
        return terminados_opaco;
    }

    public void setTerminados_opaco(boolean terminados_opaco) {
        this.terminados_opaco = terminados_opaco;
    }

    public boolean isRotulos_visivel() {
        return rotulos_visivel;
    }

    public void setRotulos_visivel(boolean rotulos_visivel) {
        this.rotulos_visivel = rotulos_visivel;
    }

    public boolean isExports_visivel() {
        return exports_visivel;
    }

    public void setExports_visivel(boolean exports_visivel) {
        this.exports_visivel = exports_visivel;
    }

    public String getUrl_img_logo() {
        return url_img_logo;
    }

    public void setUrl_img_logo(String url_img_logo) {
        this.url_img_logo = url_img_logo;
    }

    public String getUrl_img_logo_rel() {
        return url_img_logo_rel;
    }

    public void setUrl_img_logo_rel(String url_img_logo_rel) {
        this.url_img_logo_rel = url_img_logo_rel;
    }
 
    public String getTitulo_cabecalho() {
        return titulo_cabecalho;
    }

    public void setTitulo_cabecalho(String titulo_cabecalho) {
        this.titulo_cabecalho = titulo_cabecalho;
    }

    public String getView_agenda() {
        return view_agenda;
    }

    public void setView_agenda(String view_agenda) {
        this.view_agenda = view_agenda;
    }

    public boolean isShow_weekends() {
        return show_weekends;
    }

    public void setShow_weekends(boolean show_weekends) {
        this.show_weekends = show_weekends;
    }
    
    

    
}
