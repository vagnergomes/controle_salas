/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import javax.naming.NamingException;

/**
 *
 * @author vagner.gomes
 */
public class LdapConexao {

    public static void main(String[] args) throws NamingException {        

        Boolean autenticado = LdapAuthentication.getInstance().authentication("vagner.gomes", "rengavSemog2");

        if (autenticado)
        {
            System.out.println("Autenticado");
        }
        else
        {
            System.out.println("Não Autenticado!");
        }
    }
}
