/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Configuracao;
import br.com.controlesalas.services.ConfigService;
import br.com.controlesalas.util.MensagemUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author vagner.gomes
 */
@Named
@RequestScoped
public class ConfigController implements Serializable {

    @Inject
    private ConfigService service;

    private Configuracao configuracao;
    private String url;
    private StreamedContent foto;
    private String titulo_cabelho;
    private boolean rotulo_visivel;

    private String path_logo_temp;
    private String path_logo_temp2;
    private Object idProjeto;

    @PostConstruct
    public void init() {
        idProjeto =  getSession().getAttribute("idConfigSelecionado");
        
        if(idProjeto == null){
            configuracao = new Configuracao();
        }else{
            configuracao = service.obter(convertToLong(idProjeto));
        }
        
        
    }

    ///FILE UPLOAD DEU CERTO,CONITNUAR VISUALIZAR IMAGEM E SALVAR CMAMINHO NO BANCO
    public void salvar() throws IOException {
        String path1 = null;
        String path2 = null;
        path_logo_temp = (String) getSession().getAttribute("path_logo_temp");
        path_logo_temp2 = (String) getSession().getAttribute("path_logo_temp2");
        
        if(path_logo_temp == null){
            String url_logo = service.obterUrl( Integer.valueOf(idProjeto.toString()));
            File file = new File(url_logo);
            if(file.exists()){
                path1 = file.getName();
            }
        }
        if(path_logo_temp2 == null){
            String url_logo = service.obterUrl( Integer.valueOf(idProjeto.toString()));
            File file = new File(url_logo);
            if(file.exists()){
                path2 = file.getName();
                int p = path2.lastIndexOf(".");
                path2 = path2.substring(0, p)+"_rel"+path2.substring(p);
            }
        }
        
        Path origem = null;
        Path origem2 = null;
        
        String nome = null;
        String nome_tipo;
        String nome2;
        String nome_tipo2;
        String tipo = null;
        String tipo2;
  
        if(path_logo_temp != null){
            int pos_ = path_logo_temp.lastIndexOf("/");
            nome_tipo = path_logo_temp.substring(pos_ + 1);
            path_logo_temp = path_logo_temp.substring(0, pos_);
            int pos = nome_tipo.lastIndexOf(".");
            nome = "logotipo_"+idProjeto;
            tipo = nome_tipo.substring(pos);
            origem = Paths.get(path_logo_temp);
        }else{
            nome = path1;
            tipo = "";
            origem = Paths.get("C:/controlesalas/imgs/"+nome);
        }
        
        if(path_logo_temp2 != null){
            int pos2_ = path_logo_temp2.lastIndexOf("/");
            nome_tipo2 = path_logo_temp2.substring(pos2_ + 1);
            path_logo_temp2 = path_logo_temp2.substring(0, pos2_);
            int pos2 = nome_tipo2.lastIndexOf(".");
            nome2 = "logotipo_"+idProjeto+"_rel";
            tipo2 = nome_tipo2.substring(pos2); 
            origem2 = Paths.get(path_logo_temp2);
        }else{
            nome2 = path2;
            tipo2 = "";
            origem2 = Paths.get("C:/controlesalas/imgs/"+nome2);
        }
        
        configuracao.setUrl_img_logo("C:/controlesalas/imgs/" + nome + tipo);
        String erro = service.salvar(configuracao);

        //String c = "C:\\Users\\vagner.gomes\\Documents\\NetBeansProjects\\br.com.controlesalas\\web\\Midia\\uploads\\";
        String logo = "C:\\controlesalas\\imgs\\" + nome + tipo;
        String logo2 = "C:\\controlesalas\\imgs\\" + nome2 + tipo2;
        Path destino = Paths.get(logo);
        Path destino2 = Paths.get(logo2);
        Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(origem2, destino2, StandardCopyOption.REPLACE_EXISTING);

        getSession().removeAttribute("path_logo_temp");
        getSession().removeAttribute("path_logo_temp2");
        configuracao = new Configuracao();
        if (erro == null) {
            configuracao = service.obter(convertToLong(idProjeto));
            MensagemUtil.addMensagemInfo("Configurações salvas.");
        } else {
            MensagemUtil.addMensagemError("Erro ao salvar. " + erro);
        }
    }

    public void upload(FileUploadEvent event) {
        try {
            UploadedFile arquivoUpload = event.getFile();
            Path arquivoTemp = Files.createTempFile(null, null);
            Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
            path_logo_temp = arquivoTemp.toString();
            getSession().setAttribute("path_logo_temp", path_logo_temp + "/" + arquivoUpload.getFileName());
        } catch (IOException erro) {
            MensagemUtil.addMensagemError("Erro ao fazer upload.");
            erro.getStackTrace();
        }
    }

    public void upload2(FileUploadEvent event) {
        try {
            UploadedFile arquivoUpload = event.getFile();

            Path arquivoTemp = Files.createTempFile(null, null);
            Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
            path_logo_temp2 = arquivoTemp.toString();
            getSession().setAttribute("path_logo_temp2", path_logo_temp2 + "/" + arquivoUpload.getFileName());
        } catch (IOException erro) {
            MensagemUtil.addMensagemError("Erro ao fazer upload.");
            erro.getStackTrace();
        }
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    public String getUrl() {
//        url = service.obterUrl(1);
        url = "C:/controlesalas/imgs/logo-padrao.png";
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StreamedContent getFoto() throws IOException {
//        Path path = Paths.get("C:/controlesalas/logotipo/logotipo.png");
//        InputStream stream = Files.newInputStream(path);
//        foto = new DefaultStreamedContent(stream);
        
        String caminho = "C:/controlesalas/imgs/logo-padrao.png";
        String nome = "logo_padrao";
        String tipo = "png";
        if (idProjeto != null) {
            int id = ((Long)idProjeto).intValue();
            File file = new File(service.obterUrl(id));
            if (file.exists()) {
                caminho = file.toString();
                int pos = file.getName().lastIndexOf(".");
                nome = file.getName();
                tipo = nome.substring(pos + 1);
            }
        }
        InputStream is = new BufferedInputStream(
                new FileInputStream(caminho));
        return new DefaultStreamedContent(is, "image/" + tipo, nome);

        //return foto;
    }
    
    public boolean projetoSelec(){
        Object idProjeto = getSession().getAttribute("idConfigSelecionado");
        if(idProjeto == null){
            return false;
        }else{
            return true;
        }
    }

    public void limpaIdProjeto() throws IOException{
        getSession().removeAttribute("idConfigSelecionado");
        getSession().removeAttribute("idProjetoSelecionado");
//        String webcontents = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//         ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
//        String absoluteWebPath = ctx.getRealPath("/");
//        FacesContext.getCurrentInstance().getExternalContext().redirect("../Projeto/projetos.xhtml");
    }
    
    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }

//    public boolean isRotulo_visivel() {
//        rotulo_visivel = service.obterRotuloVisivel();
//        return rotulo_visivel;
//    }

    public void setRotulo_visivel(boolean rotulo_visivel) {
        this.rotulo_visivel = rotulo_visivel;
    }

    public void setTitulo_cabelho(String titulo_cabelho) {
        this.titulo_cabelho = titulo_cabelho;
    }

    public String getPath_logo_temp() {
        return path_logo_temp;
    }

    public void setPath_logo_temp(String path_logo_temp) {
        this.path_logo_temp = path_logo_temp;
    }

    public String getPath_logo_temp2() {
        return path_logo_temp2;
    }

    public void setPath_logo_temp2(String path_logo_temp2) {
        this.path_logo_temp2 = path_logo_temp2;
    }

    public Object getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(Object idProjeto) {
        this.idProjeto = idProjeto;
    }
    

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }
}
