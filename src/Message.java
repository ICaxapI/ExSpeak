import java.io.Serializable;

/**
 * Created by Alex on 22.08.2016.
 */
public class Message implements Serializable {
    private String fileName = null;
    private long size;
    private String ip;
    private int id;
    private int port;
    private boolean itAnswerToFileSend;
    private boolean youCanSend;
    private boolean youServer;
    private boolean itPing;
    private boolean itInfoServ;
    private boolean disconectMe;
    private String message = "null";
    private boolean toClient;
    private int socnumb;
    private String name;
    private boolean itHello = false;
    private byte[] image = null;
    private boolean itcam = false;
    public Message(int socnumber, boolean youCanSend){
        this.youCanSend = youCanSend;
        this.itAnswerToFileSend = true;
        this.socnumb = socnumber;
        this.toClient = true;
    }
    public Message(boolean youServerIn, boolean itPingIn){
        if (!itPingIn) {                                                            //Конструктор, Пинга и сервер-ли
            this.youServer = youServerIn;
        } else {
            this.itPing = itPingIn;
        }
    }
    public Message(String fileName, long size){
        this.fileName = fileName;
        this.size = size;
        this.toClient = true;
    }
    public Message(String messageIN){
        this.message = messageIN;
        this.toClient = true;                                                           //Конструктор, передача СООБЩЕНИЯ
        this.name = Main.login;
    }
    public Message(String name, boolean itHello){
        this.name = name;
        this.itHello = itHello;
        this.toClient = true;
    }
    public Message(boolean disconectMe, int id) {
        this.disconectMe = disconectMe;
        this.id = id;
    }
    public Message(byte[] img){
        this.image = img;
        this.itcam = true;
    }
    public boolean getItAnswerToFileSend(){return this.itAnswerToFileSend;}
    public boolean getCanSend(){return  this.youCanSend;}
    public String getFileName(){return this.fileName;}
    public int getSocnumb(){return this.socnumb;}
    public long getSize(){return this.size;}
    public byte[] getImage(){
        return this.image;
    }
    public boolean getToClient(){
        return this.toClient;
    }
    public String getName(){
        return this.name;
    }
    public String getMessage(){
        return this.message;
    }
    public boolean getItHello(){
        return this.itHello;
    }
    public boolean getDisconectMe(){
        return this.disconectMe;
    }
    public boolean getItcam(){
        return this.itcam;
    }
    public int getId(){
        return this.id;
    }
    public boolean getInfoServ(){
        return itInfoServ;
    }
    public String getIp() {
        return this.ip;
    }
    public int getPort() {
        return this.port;
    }
    public boolean getYouServer() {
        return this.youServer;
    }
    public boolean getItPing(){
        return this.itPing;
    }

}