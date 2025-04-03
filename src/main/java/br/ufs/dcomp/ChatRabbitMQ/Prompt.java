package br.ufs.dcomp.ChatRabbitMQ;

import java.util.Scanner;

public class Prompt {
    private String currentUser;
    private Emissor emissor;
    private String userDest;
    private String exchangeDest;

    public Prompt(String currentUser, Emissor emissor) {
        this.currentUser = currentUser;
        this.emissor = emissor;
        this.userDest = null;
        this.exchangeDest = null;
    }

    public void iniciarPrompt() {
        while (true) {
            try {
                System.out.print(estadoPrompt());
                
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                
                if (input == null)
                    continue;

                if (input.startsWith("!")) {
                    Main.comandControl.menuComandos(input, this.currentUser, this.userDest, this.exchangeDest);
                    continue;
                }
                
                if (input.startsWith("@")) {
                    this.userDest = input.substring(1);
                    this.exchangeDest = null;
                    continue;
                }


                if (input.startsWith("#")) {
                    this.exchangeDest = input.substring(1);
                    this.userDest = null;
                    continue;
                } 
                
                else {
                    if (this.exchangeDest != null) {
                        Mensagem mensagem = new Mensagem(this.currentUser + "#" + this.exchangeDest, this.userDest, input, null);
                        this.emissor.sendToGroup(this.exchangeDest, mensagem.serializar());
                    }
                    if (this.userDest != null) {
                        Mensagem mensagem = new Mensagem(this.currentUser, this.userDest, input, null);
                        this.emissor.sendToUser(this.userDest, mensagem.serializar());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void interromperLeitura() {
        System.out.print(estadoPrompt());
    }
    
    public String estadoPrompt() {
        if (this.userDest != null) {
            return("@" + this.userDest + ">> ");
        }
        else if (this.exchangeDest != null) {
            return("#" + this.exchangeDest + ">> ");
        }
        return(">> ");
    }
    
    public Emissor getEmissor() {
        return this.emissor;
    }
}
