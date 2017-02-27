import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

/**
 * Created by Alex on 19.09.2016.
 */
public class Inet {
    private static Date time;
    public static void takeMessage(Message tempMessage){
        time = java.util.Calendar.getInstance().getTime();
        ObjectOutputStream outputStream;
        if(Main.imServer) {
            outputStream = ServerThread.outputStream;
        } else {
            outputStream = ClientThread.outputStream;
        }
        if (tempMessage.getToClient()){
            if (!Objects.equals(tempMessage.getMessage(), "null") & !Objects.equals(tempMessage.getMessage(), null)){
                String message = tempMessage.getMessage();
                Gui.incomingText.append(message + "\n");
            }
        }
        if (tempMessage.getItHello() & Main.imServer){
            Main.gui.addCam();
            Gui.name[0] = Main.login;
            Gui.name[1] = tempMessage.getName();
            Gui.repaintOnlineList();
            sendId();
        }
        if (tempMessage.getItHello() & !Main.imServer){
            Main.gui.addCam();
            Main.myId = tempMessage.getId();
            Gui.name[0] = Main.login;
            Gui.name[1] = tempMessage.getName();
            Gui.repaintOnlineList();
        }
        if (tempMessage.getImage() != null){
        }
        if (tempMessage.getSize() > 0){
            out.println("ТРИГГЕР " + tempMessage.getSize());
            Gui.s = tempMessage.getSize();
            Gui.filename = tempMessage.getFileName();
            Gui.buttonRecieveFile.setEnabled(true);
            Gui.buttonRecieveFileCancel.setEnabled(true);
            Gui.fileTransferLb.setEnabled(true);
            Gui.fileTransferName.setEnabled(true);
            Gui.fileTransferSize.setEnabled(true);
            Gui.fileTransferLb.setText("Есть входящий запрос");
            Gui.fileTransferName.setText("Имя файла:'"+tempMessage.getFileName()+"'");
            Gui.fileTransferSize.setText("Размер файла: "+((tempMessage.getSize()/1024F)/1024F)/1024F+" Мбайт");
        }
        if (tempMessage.getItAnswerToFileSend()){
            if(tempMessage.getCanSend()){
                try {
                    System.out.println("1 принято");
                    System.out.println("Соеденияемся с "+ Main.connectedIp +":" + tempMessage.getPort());
                    Socket serv = new Socket(Main.connectedIp, tempMessage.getSocnumb());
                    System.out.println("2 создан сокет");
                    sendFile(Gui.file, serv);
                    System.out.println("3 в сокет отправлен файл");
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                Gui.waitAnswer = false;
                Gui.buttonSendFile.setEnabled(true);
                Gui.timerwait.stop();
                Gui.buttonSendFile.setText("Отправить");
            }
        }
    }
    public static void sendFile(final File f, Socket clientSocket) {
        Runnable r = new Runnable() {
            public void run() {
                Gui.sendinginprogress = true;
                out.println("Sending " + f.getName() + "...");
                out.println("@FILE_SEND");
                out.println(f.getName());
                try {
                    byte[] byteArray = new byte[1024];
                    FileInputStream fis = new FileInputStream(f.getPath());
                    long s;
                    s = f.length();
                    out.println(s);
                    int sp = (int)(s / 1024);
                    if (s % 1024 != 0) sp++;
                    BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
                    sleep(500);
                    Gui.allByte = s;
                    while (s > 0) {
                        int i = fis.read(byteArray);
                        bos.write(byteArray, 0, i);
                        s -= i;
                        Gui.bytesLeft = s;
                    }
                    bos.flush();
                    fis.close();
                    clientSocket.close();
                } catch (FileNotFoundException e) {
                    System.err.println("File not found!");
                } catch (IOException e) {
                    System.err.println("IOException");
                } catch (Exception e) {
                }
                new JOptionPane().showMessageDialog(null, f.getName() + " Sent");
                Gui.waitAnswer = false;
                Gui.sendinginprogress = false;
            }
        };

        new Thread(r).start();
    }

    public static void sendMessage(String messageIn){
        ObjectOutputStream outputStream;
        if(Main.imServer) {
            outputStream = ServerThread.outputStream;
        } else {
            outputStream = ClientThread.outputStream;
        }
        try {
            Main.sending = true;
            outputStream.writeObject(new Message(messageIn));
            Main.sending = false;
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static void sendPackMessage(Message messageIn){
        if(Main.connected) {
            ObjectOutputStream outputStream;
            if (Main.imServer) {
                outputStream = ServerThread.outputStream;
            } else {
                outputStream = ClientThread.outputStream;
            }
            try {
                Main.sending = true;
                outputStream.writeObject(messageIn);
                Main.sending = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void sendHello(){
        ObjectOutputStream outputStream;
        String message = "";
        Date time;
        time = java.util.Calendar.getInstance().getTime();
        if(Main.imServer) {
            outputStream = ServerThread.outputStream;
            int temp = Gui.countCam + 1;
            message = ((new Time(time.getTime())).toString() + " Вы подключились к сессии");
        } else {
            outputStream = ClientThread.outputStream;
            message = ((new Time(time.getTime())).toString() + " " + Main.login + " подключился  к сессии.");
        }
        Runnable kek = new Runnable() {
            public void run() {
                try {
                    sleep(100);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                if (!Objects.equals(Main.avatar, "pic/standartavatar.png")) {
                    byte[] sendData = Cam.encodeToStr(Cam.toBufImg(Main.avatar), "png");
                    Cam.sendImage(sendData);
                }
            }
        };
        new Thread(kek).start();
        try {
            Main.sending = true;
            outputStream.writeObject(new Message(message));
            Main.sending = false;
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static void sendId(){
        ObjectOutputStream outputStream;
        if(Main.imServer) {
            outputStream = ServerThread.outputStream;
        } else {
            outputStream = ClientThread.outputStream;
        }
        try {
            Main.sending = true;
            outputStream.writeObject(new Message(Main.login, true));
            Main.sending = false;
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static int getNotUsePort(int startport){
        for (int i = startport ; i < 60000; i++){
            try {
                ServerSocket s = new ServerSocket(i);
                s.close();
                return i;
            }
            catch (IOException e) {
                out.println("Порт " + i + " уже использован");
            }
        }
        return 900000;
    }
    public static String getCurrentIP(boolean local) {
        String result = null;
        if (local){
            try {
                InetAddress addr = InetAddress.getLocalHost();
                result = addr.getHostAddress();
            } catch (UnknownHostException ex){

            }
        } else {
            try {
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://2ip.ru/");
                    InputStream inputStream = url.openStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder allText = new StringBuilder();
                    char[] buff = new char[5120];

                    int count = 0;
                    while ((count = reader.read(buff)) != -1) {
                        allText.append(buff, 0, count);
                    }
                    // Строка содержащая IP имеет следующий вид
                    // 'IP адрес: 127.0.0.1' +
                    Integer indStart = allText.indexOf("'IP адрес: ");
                    Integer indEnd = allText.indexOf("' + ", indStart);

                    String ipAddress = allText.substring(indStart + 11, indEnd);
                    if (ipAddress.split("\\.").length == 4) { // минимальная (неполная)
                        //проверка что выбранный текст является ip адресом.
                        result = ipAddress;
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return result;
    }
    public static boolean checkInternetConnection() {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://www.google.ru/").openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
}
