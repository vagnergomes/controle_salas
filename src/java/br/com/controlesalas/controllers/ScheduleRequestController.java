/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class ScheduleRequestController extends ScheduleController{

    @Override
    public ScheduleModel getEventos() {
        return super.getEventos(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
