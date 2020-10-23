/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.services.AgendamentoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import webservice.entities.Agendamento_ws;

/**
 * REST Web Service
 *
 * @author vagner.gomes
 */
@Path("agendamento")
@RequestScoped
public class AgendamentoResource implements Serializable {
    
    @Inject
    AgendamentoService service;
    
    @Expose private List<Agendamento> agendamentos;

//    @Context
//    private UriInfo context;

    /**
     * Creates a new instance of AgendamentoResource
     */
    public AgendamentoResource() {
    }
    
    //busca todos agendamentos
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("v1/list")
    public String getAllAgendamentos(){
        List<Agendamento> lista;
        List<Agendamento_ws> retorno = new ArrayList<>();
        lista = service.todos_WS();
        
        for(Agendamento a : lista){
            Agendamento_ws aw = new Agendamento_ws();
            aw.setIdAgendamento(a.getIdAgendamento());
            aw.setTitulo(a.getTitulo());
            aw.setInicio(a.getInicio());
            aw.setFim(a.getFim());
            
            aw.setIdProjeto(a.getSala().getProjeto().getIdProjeto());
            aw.setNome_projeto(a.getSala().getProjeto().getNome());
            
            aw.setIdSala(a.getSala().getIdSala());
            aw.setCapacidade(a.getSala().getCapacidade());
            aw.setNome_sala(a.getSala().getNome_sala());
            aw.setDescricao(a.getSala().getDescricao());
            aw.setCor(a.getSala().getCor());
            aw.setVisivel(a.getSala().isVisivel());
            
            aw.setIdDescritivo(a.getDescritivo().getIdDescritivo());
            aw.setAdicional(a.getDescritivo().getAdicional());
            aw.setAgua(a.getDescritivo().isAgua());
            aw.setCafe(a.getDescritivo().isCafe());
            aw.setFrutas(a.getDescritivo().isFrutas());
            aw.setLanche(a.getDescritivo().isLanche());
            aw.setQtd_pessoas(a.getDescritivo().getQtd_pessoas());
            
            retorno.add(aw);
        }
        
//        Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson g = new Gson();
        String json = g.toJson(retorno);
        return json;
    }
    
    
    //busca agendamentos por idprojeto
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("v1/get/{idprojeto}")
    public String getAgendamentos_Projeto(@PathParam("idprojeto") int idprojeto){
        List<Agendamento> lista;
        List<Agendamento_ws> retorno = new ArrayList<>();
        lista = service.todos_projeto_WS(idprojeto);
        
        for(Agendamento a : lista){
            Agendamento_ws aw = new Agendamento_ws();
            aw.setIdAgendamento(a.getIdAgendamento());
            aw.setTitulo(a.getTitulo());
            aw.setInicio(a.getInicio());
            aw.setFim(a.getFim());
            
            aw.setIdProjeto(a.getSala().getProjeto().getIdProjeto());
            aw.setNome_projeto(a.getSala().getProjeto().getNome());
            
            aw.setIdSala(a.getSala().getIdSala());
            aw.setCapacidade(a.getSala().getCapacidade());
            aw.setNome_sala(a.getSala().getNome_sala());
            aw.setDescricao(a.getSala().getDescricao());
            aw.setCor(a.getSala().getCor());
            aw.setVisivel(a.getSala().isVisivel());
            
            aw.setIdDescritivo(a.getDescritivo().getIdDescritivo());
            aw.setAdicional(a.getDescritivo().getAdicional());
            aw.setAgua(a.getDescritivo().isAgua());
            aw.setCafe(a.getDescritivo().isCafe());
            aw.setFrutas(a.getDescritivo().isFrutas());
            aw.setLanche(a.getDescritivo().isLanche());
            aw.setQtd_pessoas(a.getDescritivo().getQtd_pessoas());
            
            retorno.add(aw);
        }
        
//        Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson g = new Gson();
        String json = g.toJson(retorno);
        return json;
    }

    /**
     * Retrieves representation of an instance of webservice.AgendamentoResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of AgendamentoResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }
}
