package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;


public class Emissor {
    private Channel channelEmissor;
    
    public Emissor(String userEmissor) {
        this.channelEmissor = Main.connControlMain.criarCanal();
        try{
            this.channelEmissor.queueDeclare(userEmissor, true, false, false, null);
            if(!userEmissor.endsWith("_file"))
                this.channelEmissor.queueDeclare(userEmissor+"_file", true, false, false, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void sendToUser(String queue, byte[] bytesArray) {
        try{
            this.channelEmissor.queueDeclare(queue, true, false, false, null);
            if(!queue.endsWith("_file"))
                this.channelEmissor.queueDeclare(queue+"_file", true, false, false, null);
            this.channelEmissor.basicPublish("", queue, null,  bytesArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public void sendToGroup(String exchange, byte[] bytesArray) {        
        try{
            this.channelEmissor.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);
            
            this.channelEmissor.basicPublish(exchange, "", null,  bytesArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}