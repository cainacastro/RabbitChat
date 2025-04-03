package br.ufs.dcomp.ChatRabbitMQ;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.*;

public class ComandoController {
    private Channel channelComandos;
    
    public ComandoController() {
        channelComandos = Main.connControlMain.criarCanal();
    }
    
    public void menuComandos(String lineString, String userCommander, String userDest, String exchangeDest) {
        String[] partes = lineString.split(" ");
        String comando = partes[0].substring(1);
        
        switch (comando) {
            case "addGroup":
                addGroup(partes[1], userCommander);
                break;
                
            case "removeGroup":
                removeGroup(partes[1]);
                break;
                
            case "addUser":
                addUser(partes[1], partes[2]);
                break;
            
            case "delFromGroup":
                delFromGroup(partes[1], partes[2]);
                break;
                
            case "upload":
                upload(partes[1], userDest, exchangeDest);
                break;
                
            case "listUsers":
                listUsers(partes[1]);
                break;
                
            case "listGroups":
                listGroups(userCommander);
                break;
        }
    }
    
    public void addGroup(String groupName, String userCommander) {
        try {
            this.channelComandos.exchangeDeclare(groupName, BuiltinExchangeType.FANOUT, true);
            this.channelComandos.exchangeDeclare(groupName+"_file", BuiltinExchangeType.FANOUT, true);
            addUser(userCommander, groupName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void removeGroup(String groupName) {
        try {
            this.channelComandos.exchangeDelete(groupName);
            this.channelComandos.exchangeDelete(groupName+"_file");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void addUser(String userAdd, String groupName) {
        try {
            this.channelComandos.queueDeclare(userAdd, true, false, false, null);
            this.channelComandos.queueDeclare(userAdd+"_file", true, false, false, null);
            
            this.channelComandos.exchangeDeclare(groupName, BuiltinExchangeType.FANOUT, true);
            this.channelComandos.exchangeDeclare(groupName+"_file", BuiltinExchangeType.FANOUT, true);
            
            this.channelComandos.queueBind(userAdd, groupName, "");
            this.channelComandos.queueBind(userAdd+"_file", groupName+"_file", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void delFromGroup(String userDel, String groupName) {
        try {
            this.channelComandos.queueUnbind(userDel, groupName, "");
            this.channelComandos.queueUnbind(userDel+"_file", groupName+"_file", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void upload(String fileName, String userDest, String exchangeDest) {
        if(exchangeDest == null)
            Main.fileControl.sendFileToUser(userDest, fileName);
        else if(userDest == null)
            Main.fileControl.sendFileToGroup(exchangeDest, fileName);
    }
    
    public void listUsers(String exchangeGroup){
        printArray(Main.rabbApiCons.listarIntegrantes(exchangeGroup));
    }
    
    public void listGroups(String userCommander){
        List<String> grupos = Main.rabbApiCons.listarGrupos();
        List<String> gruposPart = new ArrayList<String>();
        
        for(String grupo : grupos)
        {
            List<String> integrantes = Main.rabbApiCons.listarIntegrantes(grupo);
            
            if (integrantes.contains(userCommander))
                gruposPart.add(grupo);
        }
        printArray(gruposPart);
    }
    
    
    public static void printArray(List<String> array) {
        for (int i = 0; i < array.size(); i++) {
            String item = array.get(i);
            
            if (i == array.size() - 1) {
                System.out.println(item);
            } else {
                System.out.print(item + ", ");
            }
        }
    }
    
}
