import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 * Created by Alex on 27.08.2016.
 */
public class Brodcast extends Thread{
    int port;
    String ip;
    Socket server;
    Socket serverIn;
    ServerSocket ipPortServIn;

    public Brodcast(String ip, int port) {
        this.port = port;
        this.ip = ip;
        this.start();
    }
    public void run(){
        try{
            ipPortServIn = new ServerSocket(7814);
            String mes = Inet.getCurrentIP(true);
            byte[] data = mes.getBytes();
            InetAddress addr = InetAddress.getByName(ip);
            DatagramPacket pack = new DatagramPacket(data, data.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pack);
            ds.close();
            System.out.println("Отправили широковещательное сообщение в сеть.");
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Необходимый порт программы уже занят! Возможно запущенно 2 копии приложения.");
            System.out.println("Порт уже занят!");
            e.printStackTrace();
            System.exit(0);
        }
        try{
            System.out.println("Ждём ответ");
            while (serverIn == null){
                serverIn = ipPortServIn.accept();
            }
            InputStreamReader streamReader = new InputStreamReader(serverIn.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            System.out.println("Ответ получен!");
            String kek = reader.readLine();
            System.out.println(kek);
            Main.ipServer = kek;
            ipPortServIn.close();
            Main.connected = true;
        } catch(IOException e){
            System.exit(0);
        }
    }
}
