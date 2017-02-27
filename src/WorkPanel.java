import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Thread.sleep;

public class WorkPanel extends javax.swing.JPanel {

    int wheight;
    int height;
    int needwhe;
    int needhei;
    int stepwhe;
    int stephei;
    public BufferedImage image = null;
    short frames;
    short rlyFrames;
    public boolean bigImage;
    int number = Gui.countCam + 1;
    public static Font fontwp = new Font("Times New Roman",Font.PLAIN,5);
    public boolean needAction = false;
    public JButton sizeSwitcher;
    public Timer timer = new Timer(4, e -> {
            if (needAction){
                if(!bigImage) {
                    if (getWidth() <= needwhe) {
                        setSize(getWidth() + stepwhe*4, getHeight());
                    }
                    if (getHeight() <= needhei) {
                        setSize(getWidth(), getHeight() + stephei*3);
                    }
                    Gui.repaintCam();
                    sizeSwitcher.setLocation((getWidth() - sizeSwitcher.getWidth() - 3), 3);
                    if (getWidth() >= needwhe) {
                        setSize(needwhe, needhei);
                        needAction = false;
                        sizeSwitcher.setLocation((getWidth() - sizeSwitcher.getWidth() - 3), 3);
                        bigImage = !bigImage;
                    }
                } else {
                    if (getWidth() >= needwhe) {
                        setSize(getWidth() + stepwhe*4, getHeight());
                    }
                    if (getHeight() >= needhei) {
                        setSize(getWidth(), getHeight() + stephei*3);
                    }
                    Gui.repaintCam();
                    sizeSwitcher.setLocation((getWidth() - sizeSwitcher.getWidth() - 3), 3);
                    if (getWidth() <= needwhe) {
                        setSize(needwhe, needhei);
                        needAction = false;
                        sizeSwitcher.setLocation((getWidth() - sizeSwitcher.getWidth() - 3), 3);
                        bigImage = !bigImage;
                    }
                }
            }

    });

    public WorkPanel(){
        try {
            image = ImageIO.read(new File("pic/standartavatar.png"));
            repaint();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        setLayout(null);
        setSize(320,240);
        JLabel fpsCount = new JLabel("0 fps");
        sizeSwitcher = new JButton("+");
        sizeSwitcher.setFont(fontwp);
        sizeSwitcher.setSize(20,20);
        sizeSwitcher.setLocation((getWidth()-sizeSwitcher.getWidth()-3),3);
        fpsCount.setLocation(5,5);
        fpsCount.setSize(50,15);
        fpsCount.setForeground(Color.GREEN);
        ActionListener listenerCamSize = event -> {
            wheight = this.getWidth();
            height = this.getHeight();
            this.needAction = true;
            if(this.bigImage){
                this.needwhe =(int)(this.wheight/Gui.fcoof);
                this.needhei =(int)(this.height/Gui.fcoof);
                this.stepwhe = -1;
                this.stephei = -1;
            }else{
                this.needwhe =(int)(this.wheight*Gui.fcoof);
                this.needhei =(int)(this.height*Gui.fcoof);
                this.stepwhe = 1;
                this.stephei = 1;
            }
            //this.stepwhe = (this.needwhe-this.wheight)/100;
            //this.stephei = (this.needhei-this.height)/100;
            timer.start();
        };
        sizeSwitcher.addActionListener(listenerCamSize);
        add(fpsCount);
        add(sizeSwitcher);
        new Thread(new Runnable() {
            public void run() {
                while(true) { //бесконечно крутим
                    try {
                        sleep(1000); // 1 секунда в милисекундах
                        fpsCount.setText(frames + " fps");
                        if (rlyFrames > 1) {
                            frames = 20;
                        } else {
                            frames = 0;
                        }
                        if(!needAction){
                            timer.stop();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setImaged (BufferedImage img){
        image = img;
        img = null;
        frames++;
        rlyFrames++;
    }
    public void setLocOwer(int x,int y){
        int width = this.getWidth();
        int height = this.getHeight();
        this.setLocation(x-(width/2),y-(height/2));
    }
    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, (getWidth()-3), (getHeight()-3), this);
        g.fillRoundRect(0, 0, 44, 24, 10, 10);
    }
}