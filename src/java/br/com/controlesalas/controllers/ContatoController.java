/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Contato;
import br.com.controlesalas.entities.Projeto;
import br.com.controlesalas.services.ContatoService;
import br.com.controlesalas.util.MensagemUtil;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import org.primefaces.util.LangUtils;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author vagner.gomes
 */
@Named
@ViewScoped
public class ContatoController implements Serializable{
    
    @Inject
    private ContatoService service;
    
    private Contato contato;
    
    private Projeto projeto;
    
    private String uf;
    
    private String cidade;
    
    private List<String> listaCidades;
    
    private List<Enum> listaUf;
    private List<Contato> contatosFiltrados;
    
    public ContatoController(){   
    }
    
    @PostConstruct
    public void init(){
       contato = new Contato();
       projeto = (Projeto) getSession().getAttribute("projetoSelecionado");
       listaUf = Arrays.asList(Uf.values());
       listaCidades = null;
       uf = "";
       cidade = "";
    }
    
    public void salvar() {
        try {
            if(!contato.getTelefone().isEmpty() || !contato.getCelular().isEmpty() ){
                Projeto proj = (Projeto) getSession().getAttribute("projetoSelecionado");
                contato.setProjeto(proj);
                contato.setCidade(cidade);
                String erro = service.salvar(contato);
                if (erro == null) {
                    contato = new Contato();
                    MensagemUtil.addMensagemInfo("Salvo.");
                }
                else {
                    MensagemUtil.addMensagemInfo("Erro ao salvar: " + erro);
                }
            }else{
                MensagemUtil.addMensagemError("Erro ao salvar. É necessário preencher o campo Ramal ou Telefone.");
            }
        } catch (Exception ex) {
            MensagemUtil.addMensagemError("Erro ao salvar.");
            ex.getStackTrace();
        }
    }
    
    public List<Contato> todos(){
        return service.todos(projeto.getIdProjeto());
    }
  
    
    public void excluir(){
        try {
            String erro = service.excluir(contato.getIdContato());
            if (erro == null) {
                contato = new Contato();
                MensagemUtil.addMensagemInfo("Excluído.");
            } else {
                MensagemUtil.addMensagemError("Erro ao excluir: " + erro);
            }
        } catch (Exception ex) {
            MensagemUtil.addMensagemError("Erro ao excluir: pode haver dependências para o Contato.");
            ex.getStackTrace();
        }
    }

    public void selecionar(Contato a) {
        contato = a;
        cidade = a.getCidade();
    }
    
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        Contato c = (Contato) value;
        return c.getContato().toLowerCase().contains(filterText)
                || c.getCidade().toLowerCase().contains(filterText)
                || c.getCelular().toLowerCase().contains(filterText)
                || c.getTelefone().toLowerCase().contains(filterText)
                || c.getDescricao().toLowerCase().contains(filterText);
    }
    
    public void consultaCidades() {
        listaCidades = new ArrayList<>();
       try{
        String url = "http://educacao.dadosabertosbr.com/api/cidades/"+uf;
        String met = "GET";
        String json = sendGet(url, met);
        Gson g = new Gson();
        Type type = new TypeToken<Object>() {}.getType();
        //Type list = new TypeToken<List<Cidade>>() {}.getType();
  
        Object ob = g.fromJson(json, type);
        List lista = (List) ob;
        
        for(int i=0; i<=lista.size();i++){
            String c =  lista.get(i).toString();
            int p = c.indexOf(":");
            
            listaCidades.add(c.substring(p+1));
        } 
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
 
    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public List<Contato> getContatosFiltrados() {
        return contatosFiltrados;
    }

    public void setContatosFiltrados(List<Contato> contatosFiltrados) {
        this.contatosFiltrados = contatosFiltrados;
    }

    public List<Enum> getListaUf() {
        return listaUf;
    }

    public void setListaUf(List<Enum> listaUf) {
        this.listaUf = listaUf;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public List<String> getListaCidades() {
        return listaCidades;
    }

    public void setListaCidades(List<String> listaCidades) {
        this.listaCidades = listaCidades;
    }

    
    
    
    
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
    
    
    
}
