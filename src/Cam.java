import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramSocket;

public class Cam extends Thread {
    public static VideoCapture camera;
    public static boolean enable;
    private static  byte[] imageBytes;
    private static BufferedImage imageRec;
    private static BufferedImage scaled;
    private static BufferedImage out;
    private static byte[] data;
    DatagramSocket ds;
    public void run (){
        try{
            System.loadLibrary("./lib/opencv_java300");
        } catch (UnsatisfiedLinkError ex){
            Main.gui.incomingText.append("UnsatisfiedLinkError");
        }
        camera = new VideoCapture(0);
        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {
            int index = 0;
            Mat frame = new Mat();
            while(true){
                if (camera.read(frame)){
                    BufferedImage temp = convertMatToBufferedImage(frame);
                    Main.gui.arrayCam[0].setImaged(temp);
                    Main.gui.arrayCam[0].repaint();
                    if (!Main.recive & Main.connected) {
                        byte[] sendData = encodeToStr(temp, "jpg");
                        sendImage(sendData);
                    }
                    //break;
                }
            }
        }
    }
    public static void sendImage(byte[] img){
        ObjectOutputStream outputStream;
        if(Main.imServer) {
            outputStream = ServerThread.outputStream;
        } else {
            outputStream = ClientThread.outputStream;
        }
        try {
            Main.sending = true;
            outputStream.writeObject(new Message(img));
            Main.sending = false;
            img = null;
        } catch (IOException ex){
            ex.printStackTrace();
        }
//        DataOutputStream outputStream = null;
//        BufferedOutputStream bos = null;
//        try {
//            if (Main.imServer) {
//                outputStream = new DataOutputStream(ServerThread.client.getOutputStream());
//            } else {
//                outputStream = new DataOutputStream(ClientThread.serv.getOutputStream());
//            }
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//        try {
//            bos = new BufferedOutputStream(outputStream);
//            bos.write(img);
//            bos.flush();
//            sleep(50);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }
    public static BufferedImage toBufImg (String file){
        BufferedImage bufImg = null;
        try {
            Image image = ImageIO.read(new File(Main.avatar));
            bufImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufImg.getGraphics().drawImage(image, 0, 0, null);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return bufImg;
    }
    public static byte[] encodeToStr(BufferedImage img, String type){
        int x = 0;
        int y = 0;
        if (Gui.slider.getValue() == 1){
            x = 160;
            y = 120;
        } else if (Gui.slider.getValue() == 2){
            x = 320;
            y = 240;
        } else if (Gui.slider.getValue() == 3){
            x = 640;
            y = 480;
        }
        imageBytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            scaled = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaled.createGraphics();
            g.drawImage(img, 0, 0, x, y, null);
            g.dispose();
            ImageIO.write(scaled, type, bos);
            imageBytes = bos.toByteArray();
            bos.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return imageBytes;
    }
    public static BufferedImage decodeToImg(byte[] imageBytes){
        imageRec = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            imageRec = ImageIO.read(bis);
            bis.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        imageBytes = null;
        return imageRec;
    }
    public static BufferedImage convertMatToBufferedImage(Mat mat) {
        data = new byte[mat.width() * mat.height() * (int)mat.elemSize()];
        int type;
        mat.get(0, 0, data);
        switch (mat.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                throw new IllegalStateException("Unsupported number of channels");
        }
        out = new BufferedImage(mat.width(), mat.height(), type);
        out.getRaster().setDataElements(0, 0, mat.width(), mat.height(), data);
        mat = null;
        return out;
    }
}
