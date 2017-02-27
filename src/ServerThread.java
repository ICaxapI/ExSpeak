import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Alex on 24.08.2016.
 */
public class ServerThread extends Thread {
    String ip;
    public int servSocket;
    static ObjectOutputStream outputStream;
    static ObjectInputStream inputStream;
    static Socket client;
    Timer timerResetSr = new Timer(10000, e -> {
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
    public ServerThread(int socket) {
        this.servSocket = socket;
        this.start();
    }
    public void run() {
        try {
            boolean canScan = true;
            DatagramSocket ds = new DatagramSocket(7813);
            Gui.incomingText.append("Ответ не получен, новая сессия создана." + "\n");
            Gui.incomingText.append("Ждём собеседника..." + "\n");
            while (canScan) {
                DatagramPacket pack = new DatagramPacket(new byte[1024], 1024);
                ds.receive(pack);
                System.out.println("Получили какой-то пакет от " + new String(pack.getData()));
                Socket clientrec = new Socket(new String(pack.getData()), 7814);
                Main.connectedIp = new String(pack.getData());
                PrintWriter writer = new PrintWriter(clientrec.getOutputStream());
                writer.println(Inet.getCurrentIP(true));
                writer.close();
                System.out.println("Отправили " + Inet.getCurrentIP(true) + " на " + new String(pack.getData()) + ":7814");
                ds.close();
                clientrec.close();
                Main.server = true;
                Main.imServer = true;
                canScan = false;
            }
        } catch (Exception e) {
            Gui.incomingText.append("Нужный порт занят!" + "\n");
            e.printStackTrace();
            System.exit(0);
        }
        try {
            ServerSocket socketListener = new ServerSocket(servSocket);
            while (true) {
                client = null;
                while (client == null) {
                    client = socketListener.accept();
                }
                outputStream = new ObjectOutputStream(client.getOutputStream());
                inputStream = new ObjectInputStream(client.getInputStream());
                timerResetSr.start();
                Main.connected = true;
                socketListener.close();
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Inet.sendHello();
                //Принимаем сообщение
                while (true) {
                    try {
                        Message tempMessage = (Message) inputStream.readObject();
                        if (tempMessage.getToClient()) {
                            Inet.takeMessage(tempMessage);
                        } else if (tempMessage.getItcam()){
                            Main.recive = true;
                            Gui.arrayCam[1].setImaged(Cam.decodeToImg(tempMessage.getImage()));
                            Gui.arrayCam[1].repaint();
                            Main.recive = false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            Gui.incomingText.append("Ваш противник отключился от игры!" + "\n");
            Gui.removeCam();
        }
    }
}

