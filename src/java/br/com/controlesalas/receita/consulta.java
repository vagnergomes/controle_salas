/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.receita;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;


/**
 *
 * @author vagner.gomes
 */
@RequestScoped
@Named
public class consulta implements Type, Serializable{
    
    String cnpj;
    private Receita receita;
    private simples_nac simples;
    
    
    @PostConstruct
    public void init(){
        receita = new Receita();
    }
    
    public consulta(){
        
    }
    
    
    public void consulta_cnpj() throws Exception{
       try{
        String url = "http://www.receitaws.com.br/v1/cnpj/"+cnpj;
        String met = "GET";
        String json = sendGet(url, met);
        
        Gson g = new Gson();
        Type type = new TypeToken<Receita>() {}.getType();
        Type list = new TypeToken<List<Receita>>() {}.getType();
        receita = g.fromJson(json, type);
        System.out.println(receita);
//        for(Receita s: receita ){
            System.out.println(receita.getCnpj() + "-" + receita.getNome());
            
       
//        }
       }catch(Exception ex){
           ex.getStackTrace();
       }
    }
    
    public void consulta_cnpj2() throws Exception{
       try{
        String url = "https://consopt.www8.receita.fazenda.gov.br/consultaoptantes";
        String met = "POST";
        String param = "{'cnpj':'18104802000107'}";
        String json = sendPost(url, param, met);
        
        Gson g = new Gson();
        Type type = new TypeToken<simples_nac>() {}.getType();
       // Type list = new TypeToken<List<Receita>>() {}.getType();
       System.out.println("----JSON:"+json);
        simples = g.fromJson(json, type);
        System.out.println("----:"+simples);
//        for(Receita s: receita ){
            System.out.println(simples.getCnpj() + "-" + simples.getRazao_social() + "-" + simples.getSimples_nacional_situacao());
            
       
//        }
       }catch(Exception ex){
           ex.getStackTrace();
       }
    }
    
    
    private String sendGet(String url, String method) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(method);

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }

    // HTTP POST request
    private String sendPost(String url, String urlParameters, String method) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add reuqest header
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        return response.toString();
        

    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }
    
    
}
