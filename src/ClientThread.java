import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by Alex on 23.09.2016.
 */
public class ClientThread extends Thread {
    private int port;
    private static Message tempMessage;
    private boolean saidHello = false;
    private String ip;
    public static  String ipserv;
    Timer timerResetCl = new Timer(10000, e -> {
        try {
            if (!Main.sending) {
                outputStream.reset();
                System.out.println("Успешно!");
            } else {
                int i = 0;
                while (Main.sending){
                    i++;
                }
                outputStream.reset();
                System.out.println("Успешно, ждали " + i + "раз");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    });
    static ObjectInputStream inputStream;
    static ObjectOutputStream outputStream;
    static Socket serv;
    public ClientThread(String serv) {
        this.start();
        this.ipserv = serv;
        Main.connectedIp = serv;
    }
    public void run() {
        clientToServer(ipserv);
    }
    public void clientToServer(String ipserv){
        try {
            serv = new Socket(ipserv, 7812);
            Main.imServer = false;
            inputStream = new ObjectInputStream(serv.getInputStream());
            outputStream = new ObjectOutputStream(serv.getOutputStream());
            timerResetCl.start();
            while (true) {
                try {
                    //Принимаем сообщение
                    tempMessage = (Message) inputStream.readObject();
                    if (Main.connected & !saidHello) {
                        Inet.sendHello();
                        Inet.sendId();
                        this.saidHello = true;
                    }
                    //Если это ping
                    if (!tempMessage.getToClient()) {
                        if (tempMessage.getItPing()) {
                            Main.sending = true;
                            outputStream.writeObject(new Ping());
                            Main.sending = false;
                        }
                        if (tempMessage.getDisconectMe()) {
                            Gui.name[tempMessage.getId()] = null;
                            Gui.removeCam();
                            Gui.repaintOnlineList();
                            this.stop();
                        }
                    } else if (tempMessage.getToClient()) {
                        Inet.takeMessage(tempMessage);
                    }
                    if (tempMessage.getItcam()){
                        Gui.arrayCam[1].setImaged(Cam.decodeToImg(tempMessage.getImage()));
                        Gui.arrayCam[1].repaint();
                        tempMessage = null;
                    }
                } catch (Exception ex){
                   ex.printStackTrace();
                }
            }
        } catch (IOException ex){
        } finally {
            try {
                serv.close();
            }catch (Exception ex) {
            }
            Main.connected = false;
            Gui.removeCam();
            Gui.repaintOnlineList();
            new ServerThread(7812);
            this.stop();
        }
    }

}
