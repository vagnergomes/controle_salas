/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

/**
 *
 * @author vagner.gomes
 */
public class Order {

    private int number;
    private String imagePath;
    public Order(int orderNumber, String imagePath) {
        this.number = orderNumber;
        this.imagePath = imagePath;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    
    
    
    
}
