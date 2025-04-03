package br.ufs.dcomp.ChatRabbitMQ;

public class ThreadUpload implements Runnable {
    private String queueDest;
    private String exchangeDest;
    private String fileName;
    private String dest;
    private String userAtual;
    private Prompt currentPrompt;
    private Mensagem mnsg;
    
    public ThreadUpload(String userAtual, String queueDest, String exchangeDest, String fileName, Prompt currentPrompt) {
        this.queueDest = queueDest;
        this.exchangeDest = exchangeDest;
        this.fileName = fileName;
        this.currentPrompt = currentPrompt;
        this.userAtual = userAtual;
    }
    
    @Override
    public void run(){
        try{
            Emissor emissorFile = new Emissor(this.userAtual + "_file");
            
            if(this.exchangeDest == null) {
                this.dest = "@" + this.queueDest;
                System.out.println("\nEnviando \"" + this.fileName + "\" para " + this.dest.replaceFirst("_file$", ""));
                currentPrompt.interromperLeitura();
                this.mnsg = new Mensagem("@"+this.userAtual, this.queueDest, null, this.fileName);
            }
        
            else if(this.queueDest == null) {
                this.dest = "#" + this.exchangeDest;
                System.out.println("\nEnviando \"" + this.fileName + "\" para " + this.dest.replaceFirst("_file$", ""));
                currentPrompt.interromperLeitura();
                this.mnsg = new Mensagem(this.userAtual+"#"+this.exchangeDest, this.exchangeDest, null, this.fileName);
            }
            
            // Mensagem para usuario
            if(this.exchangeDest == null) 
                emissorFile.sendToUser(this.queueDest, mnsg.serializar());
            
            // Mensagem para grupo
            else 
                emissorFile.sendToGroup(this.exchangeDest, mnsg.serializar());
            
            Thread.sleep(5000);
            fileUploaded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    private void fileUploaded() {
        System.out.println("\nArquivo \"" + this.mnsg.getFilePath() + "\" foi enviado para " + this.dest.replaceFirst("_file$", ""));
        currentPrompt.interromperLeitura();
    }
}