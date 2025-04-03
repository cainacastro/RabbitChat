package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;

public class ConnectionController {
    private Connection connection;
    private ConnectionFactory factory;
    
    public ConnectionController(){
        try {
            this.factory = new ConnectionFactory();
            configConnection((factory));
            connection = factory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Channel criarCanal() {
        Channel newChannel;
        try {
            newChannel = this.connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return newChannel;
    }

    public Connection getConnection() {
        return this.connection;
    }
    
    public String getHost() {
        return this.factory.getHost();
    }
    
    public String getUsername() {
        return this.factory.getUsername();
    }
    
    public String getPassword() {
        return this.factory.getPassword();
    }
    
    private static void configConnection(ConnectionFactory factory) {
        factory.setHost("load-balancer-chat-rabbitmq-2f980fe8376ea341.elb.us-east-1.amazonaws.com");
        factory.setUsername("admin"); 
        factory.setPassword("senha");
        factory.setVirtualHost("/");
    }
}
