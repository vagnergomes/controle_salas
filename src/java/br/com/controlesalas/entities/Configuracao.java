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
    
   
    
}
