import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class Main {
    public static String version = "Ver. 0.8 beta";
    public static boolean sending = false;
    public static Gui gui;
    public static boolean recive = false;
    public static JOptionPane getLogin = new JOptionPane("Введите никнейм");
    public static Cam cam = new Cam();
    public static boolean server;
    public static String login;
    public static boolean connected = false;
    public static String avatar = "pic/standartavatar.png";
    public static boolean imServer = false;
    public static UpnpService upnpServiceUDP;
    public static UpnpService upnpServiceTCP;
    public static int myId = 0;
    public static String ipServer;
    public static File avatarFile = null;
    public static String connectedIp;
    public static void main(String[] args) {
        login = getLogin.showInputDialog(null, "Введите никнейм");
        if (login != null & !Objects.equals(login, "") & !Objects.equals(login, new String())) {
            gui = new Gui("ExSpeak");
            String voidString1 = null;
            String voidString2 = "";
            if (!Objects.equals(login, voidString1) & !Objects.equals(login, voidString2)) {
                try {
                    boolean s = false;
                    Brodcast sndr = new Brodcast("255.255.255.255", 7813);
                    sleep(1000);
                    if (ipServer == null) {
                        sndr.stop();
                        new ServerThread(7812);
                    } else {
                        new ClientThread(ipServer);
                        sndr.stop();
                    }
                } catch (InterruptedException ex) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Вы не ввели никнейм!");
            System.exit(0);
        }
    }
    public static void reservPort(){
        PortMapping desiredMappingUDP = new PortMapping(7812, Inet.getCurrentIP(true), PortMapping.Protocol.UDP, "ExSpeak UDP port");
        upnpServiceUDP = new UpnpServiceImpl(new PortMappingListener(desiredMappingUDP));
        upnpServiceUDP.getControlPoint().search();
        PortMapping desiredMappingTCP = new PortMapping(7812, Inet.getCurrentIP(true), PortMapping.Protocol.TCP, "ExSpeak TCP port");
        upnpServiceTCP = new UpnpServiceImpl(new PortMappingListener(desiredMappingTCP));
        upnpServiceTCP.getControlPoint().search();
    }
}