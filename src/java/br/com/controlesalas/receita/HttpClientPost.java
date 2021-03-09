/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.receita;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/**
 *
 * @author vagner.gomes
 */




public class HttpClientPost {

    public static void main(String[] args) throws IOException, InterruptedException {

        URL url = new URL("http://www8.receita.fazenda.gov.br/SimplesNacional/aplicacoes.aspx?id=21");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST"); // PUT é outra opção válida 
        http.setDoOutput(true);
        

        Map<String, String> arguments = new HashMap<>();
        arguments.put("cnpj", "18104802000101");
//arguments.put("password", "sjh76HSn!"); // This is a fake password obviously
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application / x-www-form-urlencoded; charset = UTF-8");
        http.connect();
        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        } // Faça algo com http.getInputStream () 

        System.out.println("---" + sj);

    }

}
