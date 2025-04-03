package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;


public class ThreadReceptora implements Runnable {
    private Channel myChannel;
    private String userReceptor;
    private Prompt currentPrompt;
    
    public ThreadReceptora(String userReceptor) {
        this.myChannel = Main.connControlMain.criarCanal();
        this.userReceptor = userReceptor;
        this.currentPrompt = Main.prompt;
    }
    
    @Override
    public void run(){
        try{
            myChannel.queueDeclare(userReceptor, true, false, false, null);
            Consumer consumer = new DefaultConsumer(myChannel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] bytesMessage) throws IOException {
    
                Mensagem mensagem = new Mensagem(bytesMessage, userReceptor);
                mensagem.printMensagemConteudo();
                currentPrompt.interromperLeitura();
                }
            };
            myChannel.basicConsume(userReceptor, true,    consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
}
    