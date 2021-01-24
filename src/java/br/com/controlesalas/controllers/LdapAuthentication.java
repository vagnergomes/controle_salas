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
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

@SuppressWarnings("rawtypes")
public class LdapAuthentication {  


 

    /** 
     * Classe que realiza a conexão e autenticação ao LDAP 
     *  
     * @author Adriano Anderson 
     */  

    /* 
     * Singleton 
     */  
    protected static LdapAuthentication instanceLdap;  

    /* 
     * Implementação do Initial context para LDAP 
     */  
    public static String INITIAL_CTX = "com.sun.jndi.ldap.LdapCtxFactory";  

    /* 
     * Servidor LDAP 
     */  
    public static String SERVIDOR = "ldap://10.1.3.64:389"; //Defina aqui o ip onde está o ldap. o que vem após os ":" é a porta do ldap 

    /* 
     * Tipo de conexão realizada 
     */  
    public static String CONNECTION_TYPE = "simple";  

    /* 
     * Nome distinto do admin 
     */  
    public static String ADMIN_DN = "Administrador";  

    /* 
     * Senha 
     */  
    public static String ADMIN_PW = "F&r@daTerr@)2020?";  

    /* 
     * Diretório Base 
     */  
    public static String BASE_DN = "OU=Usuários,OU=Patos de Minas,OU=Unidades de Negócio,DC=terrenaagro,DC=com,DC=br";  

    /* 
     * Mensagem de Erro de Conexão ao Ldap 
     */  
    public static String MSG_ERROR_LDAP_CONNECTION = "Não foi possível obter um contexto LDAP";  

    /* 
     * Mensagem de Erro sobre Validação do Login e Password 
     */  
    public static String MSG_ERROR_LDAP_VALIDATION_USER = "Username ou Password Inválida";  

    private LdapAuthentication() {  
        super();  
    }  

    /** 
     * Obtém a mesma instância de LdapAuthentication para todas as chamadas 
     *  
     * @author Adriano Anderson 
     * @return um objeto LdapAuthentication 
     */  
    public static LdapAuthentication getInstance() {  

        if (instanceLdap == null) {  
            instanceLdap = new LdapAuthentication();  
        }  

        return instanceLdap;  
    }  

    /** 
     * Método responsável por realizar a chamada para autenticação via ldap do 
     * login e password passados como parâmetros. 
     *  
     * @author Adriano Anderson 
     */  
    public boolean authentication(String login, String password) throws NamingException {  

        DirContext ctx = null;  
        SearchControls sc = null;  
        String filtro = null;  
        NamingEnumeration cursor = null;  
        boolean bResult = false;  

        /* 
         * Cria conexão padrão com LDAP 
         */  
        ctx = createLdapConnection();  

        if (ctx != null) {  
            sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            /* 
             * Define atributos de retorno da consulta 
             */  
            String[] atributosParaRetornar = { "distinguishedName","sAMAccountType" };  
            sc.setReturningAttributes(atributosParaRetornar);  
            /* 
             * Especifica login para consulta 
             */  
            filtro = "(&(sAMAccountName=" + login + "))";  

            try {  
                cursor = ctx.search(BASE_DN, filtro, sc);  
                if (cursor.hasMoreElements()) {  

                    SearchResult result = (SearchResult) cursor.nextElement();  
                    Attributes att = result.getAttributes();
                    String dn = (String) att.get("distinguishedName").get(); 
                    //String st = (String) att.get("sAMAccountType").get();

                    /* 
                     * Se o login existe, tenta autenticar no LDAP com a senha 
                     * fornecida pelo usuário 
                     */  
                    bResult = validateUser(dn, password);  
                }  

            } catch (NamingException e) {  
                System.out.println(MSG_ERROR_LDAP_CONNECTION);  
                e.printStackTrace();  
            }  
        }  
        return bResult;  
    }  

    /** 
     * Método responsável por realizar a conexão padrão com o Ldap. 
     *  
     * @author Adriano Anderson 
     */ 

    @SuppressWarnings("unchecked")
    private DirContext createLdapConnection() {  

        DirContext ctx = null;  
        Hashtable env = new Hashtable();  

        // Especifica INITIAL CONTEXT  
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CTX);  

        // Especifica o IP/Nome e a porta do servidor LDAP  
        env.put(Context.PROVIDER_URL, SERVIDOR);  

        // Usuário ADMIN  
        env.put(Context.SECURITY_PRINCIPAL, ADMIN_DN);  

        // Senha ADMIN  
        env.put(Context.SECURITY_CREDENTIALS, ADMIN_PW);  

        // Tipo de Conexão  
        env.put(Context.SECURITY_AUTHENTICATION, CONNECTION_TYPE);  

        try {  
            // Cria um Initial Context  
            ctx = new InitialDirContext(env);  

        } catch (NamingException e) {  
            System.out.println(MSG_ERROR_LDAP_CONNECTION);  
            e.printStackTrace();  
        }  
        return ctx;  
    }  

    /** 
     * Método responsável por realizar a validação do login no Ldap. O campo dn 
     * é distinguished name formado anteriormente a partir da consulta do login 
     * no Ldap. 
     *  
     * @author Adriano Anderson 
     */  
    @SuppressWarnings("unchecked")
    private boolean validateUser(String dn, String senha) {  

        DirContext ldapCtx = null;  
        boolean bResult = false;  

        Hashtable env = new Hashtable();  

        // Especifica INITIAL CONTEXT  
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CTX);  

        // Especifica o IP/Nome e a porta do servidor LDAP  
        env.put(Context.PROVIDER_URL, SERVIDOR);  

        // Ldap Distingued Name  
        env.put(Context.SECURITY_PRINCIPAL, dn);  

        // Senha Usuário  
        env.put(Context.SECURITY_CREDENTIALS, senha);  

        // Tipo de Conexão  
        env.put(Context.SECURITY_AUTHENTICATION, CONNECTION_TYPE);  

        try {  
            // Cria um Initial Context  
            ldapCtx = new InitialDirContext(env);


        } catch (AuthenticationException auEx) {  
            System.out.println(MSG_ERROR_LDAP_VALIDATION_USER);  
            auEx.printStackTrace();  
        } catch (NamingException ne) {  
            System.out.println(MSG_ERROR_LDAP_CONNECTION);  
            ne.printStackTrace();  
        } finally {  

            if (ldapCtx != null) {  
                bResult = true;  
            }  
        }  

        return bResult;  
    }  
}  