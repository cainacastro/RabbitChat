package br.ufs.dcomp.ChatRabbitMQ;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class Mensagem {
    private PacoteProto.Pacote pacoteProto;
    private PacoteProto.Conteudo conteudo;
    private ByteString bodyContent;
    private String userDest;
    private String data;
    private String hora;
    private String userEmissor;
    private String texto;
    private String tipoMIME;
    private String nomeConteudo;
    private String fileName;
    
    // Recebendo Mensagem
    public Mensagem(byte[] bytesMessage, String userReceptor) throws InvalidProtocolBufferException{
        pacoteProto = PacoteProto.Pacote.parseFrom(bytesMessage);
        this.data = pacoteProto.getData();
        this.hora = pacoteProto.getHora();
        this.userDest = userReceptor;
        this.userEmissor = pacoteProto.getEmissor();
        this.conteudo = pacoteProto.getConteudo();
        this.bodyContent = conteudo.getCorpo();
        this.tipoMIME = conteudo.getTipo();
        this.nomeConteudo = conteudo.getNome();
        this.texto = null;
        if(this.tipoMIME.equals("text/plain")) {
            this.texto = this.bodyContent.toStringUtf8();
        }
        
        else {
            createFile();
        }
    }
    
    // Enviando qualquer tipo de Mensagem
    public Mensagem(String userEmissor, String userDest, String texto, String fileName){
        setDateHora();
        this.fileName = fileName;
        byte[] byteArray;
        this.userEmissor = userEmissor;
        this.userDest = userDest;
        
        // Mensagem de texto
        if(this.fileName == null) {
            this.texto = texto;
            this.tipoMIME = "text/plain";
            this.nomeConteudo = "";
            byteArray = texto.getBytes();
        }
        
        // Mensagem de arquivo
        else {
            byteArray = readFile();
        }
        
        this.bodyContent = ByteString.copyFrom(byteArray);
        
        this.conteudo = PacoteProto.Conteudo.newBuilder()
                .setTipo(this.tipoMIME)
                .setCorpo(this.bodyContent)
                .setNome(this.nomeConteudo)
                .build();
                
        this.pacoteProto = PacoteProto.Pacote.newBuilder()
                .setEmissor(this.userEmissor)
                .setData(this.data)
                .setHora(this.hora)
                .setConteudo(conteudo)
                .build();
    }
    
    public void setDateHora(){
        ZoneId zoneIdBrasil = ZoneId.of("America/Sao_Paulo");

        ZonedDateTime now = ZonedDateTime.now(zoneIdBrasil);
        
        LocalDate data = now.toLocalDate();
        LocalTime time = now.toLocalTime();

        DateTimeFormatter formatoDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        this.data = data.format(formatoDate);
        this.hora = time.format(formatoHora);
    }
    
    public void printMensagemConteudo(){
        
        // Chegou mensagem de texto
        if(this.texto != null) {
            printDateHora();
            System.out.println(this.userEmissor + " diz: " + this.texto);
        }
        
        // Chegou arquivo
        else {
            printDateHora();
            System.out.println("Arquivo \"" + this.nomeConteudo + "\" recebido de " + this.userEmissor.replaceFirst("_file$", ""));
        }
    }
    
    private void printDateHora() {
        System.out.print("\n(" + this.data + " às " + this.hora + ") ");
    }

    public byte[] serializar() {
        byte[] buffer = pacoteProto.toByteArray();
        return buffer;
    }
    
    private byte[] readFile() {
        Path path = Paths.get(this.fileName);
    
        if (!Files.exists(path)) {
            throw new RuntimeException("Arquivo não encontrado em : " + this.fileName);
        }
    
        try {
            this.tipoMIME = Files.probeContentType(path); // Obtém o tipo MIME do arquivo
            this.nomeConteudo = path.getFileName().toString();
            return Files.readAllBytes(path); // Lê todo o conteúdo do arquivo e retorna como byte[]
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + this.fileName, e);
        }
    }
    
    private void createFile() {
        byte[] fileBytes = this.bodyContent.toByteArray();
    
        // Obtém a pasta "Downloads" do usuário
        String home = System.getProperty("user.home");
        String downloadsPath = home + "/environment/sistema-de-troca-de-mensagens-instant-neas-grupo-j/Downloads/Downloads_" + this.userDest.replaceFirst("_file$", "") + "/" + this.nomeConteudo;
    
        Path path = Paths.get(downloadsPath);
        Path diretorio = path.getParent();
    
        try {
            if (diretorio != null && !Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }
    
            Files.write(path, fileBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar arquivo na pasta Downloads", e);
        }
    }

    
    public String getFilePath() {
        return this.fileName;
    }
    
    public String getUserDest() {
        return this.userDest;
    }
}