package br.ufs.dcomp.ChatRabbitMQ;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RabbitApiConsumer {
    private String urlBase;
    private String username;
    private String password;
    
    public RabbitApiConsumer() {
        urlBase = "http://"+Main.connControlMain.getHost()+"/api";
        username = Main.connControlMain.getUsername();
        password = Main.connControlMain.getPassword();

    }
    
    // Função para enviar requisição GET
    public String sendGetRequest(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        
        // Cabecalho de autenticacao
        String auth = this.username + ":" + this.password;
        String encodedAuth = new String(Base64.getEncoder().encode(auth.getBytes()));
        con.setRequestProperty("Authorization", "Basic " + encodedAuth);

        // Ler resposta
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
    
    public List<String> listarGrupos() {
        List<String> grupos = new ArrayList<>();
        try {
            String url = urlBase + "/exchanges/%2F";
            String jsonResponse = sendGetRequest(url);
            JSONArray exchanges = new JSONArray(jsonResponse);

            for (int i = 0; i < exchanges.length(); i++) {
                JSONObject exchange = exchanges.getJSONObject(i);
                String name = exchange.getString("name");
                
                if (name.startsWith("amq.") || name.endsWith("_file"))
                    continue;
                
                if (!name.isEmpty())
                    grupos.add(name);
            }
            return grupos;
        } catch (Exception e) {
            e.printStackTrace();
            return grupos;
        }
    }
    
    public List<String> listarIntegrantes(String exchangeGroup) {
        List<String> integrantes = new ArrayList<>();
        try {
            String url = urlBase + "/exchanges/%2F/" + exchangeGroup + "/bindings/source";
            String jsonResponse = sendGetRequest(url);
            JSONArray bindings = new JSONArray(jsonResponse);

            for (int i = 0; i < bindings.length(); i++) {
                JSONObject binding = bindings.getJSONObject(i);
                String queueDestino = binding.getString("destination");

                if (!queueDestino.isEmpty() && !queueDestino.endsWith("_file")) {
                    integrantes.add(queueDestino);
                }
            }
            return integrantes;
        } catch (Exception e) {
            e.printStackTrace();
            return integrantes;
        }
    }
}
