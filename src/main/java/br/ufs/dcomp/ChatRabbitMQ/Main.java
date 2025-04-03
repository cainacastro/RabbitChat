package br.ufs.dcomp.ChatRabbitMQ;

import java.util.Scanner;

public class Main {
    public static ConnectionController connControlMain;
    public static ComandoController comandControl;
    public static FileController fileControl;
    public static Prompt prompt;
    public static RabbitApiConsumer rabbApiCons;
    public static void main(String[] args) {
        connControlMain = new ConnectionController();
        comandControl = new ComandoController();
        
        Scanner scanner = new Scanner(System.in);

        System.out.print("User: ");
        String currentUser = scanner.nextLine();
        
        Emissor emissor = new Emissor(currentUser);
        
        prompt = new Prompt(currentUser, emissor);
        fileControl = new FileController(currentUser);
        
        rabbApiCons = new RabbitApiConsumer();
        
        Thread threadReceptora = new Thread(new ThreadReceptora(currentUser));
        threadReceptora.start();
        
        prompt.iniciarPrompt();
    }
}