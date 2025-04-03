package br.ufs.dcomp.ChatRabbitMQ;

public class FileController {
    private Thread threadReceptoraFile;
    private Prompt currentPrompt;
    private String userAtual;
    private String fileQueueReceptora;
    
    public FileController(String userAtual) {
        this.userAtual = userAtual;
        this.fileQueueReceptora = userAtual + "_file";
        this.currentPrompt = Main.prompt;
        this.threadReceptoraFile = new Thread(new ThreadReceptora(fileQueueReceptora));
        this.threadReceptoraFile.start();
    }
    
    public void sendFileToUser(String userDest, String fileName) {
        Thread thUpload = new Thread(new ThreadUpload(this.userAtual, userDest+"_file", null, fileName, this.currentPrompt));
        thUpload.start();
    } 
    
    public void sendFileToGroup(String groupDest, String fileName) {
        Thread thUpload = new Thread(new ThreadUpload(this.userAtual, null, groupDest+"_file", fileName, this.currentPrompt));
        thUpload.start();
    }
}