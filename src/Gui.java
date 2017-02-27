import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;


/**
 * Created by Alex on 23.09.2016.
 */
public class Gui extends JFrame {
    private static final String panel1 = "panel1";
    private static Dimension phase[] = new Dimension[8];
    private static BufferedImage dragged[] = new BufferedImage[8];
    private static int nowDragableCam = 0;
    private short heightBottomPanel = 180;
    private String notThisPLZ;
    public static JTextArea incomingText;
    public static JPanel generalPanel;
    private static Date time;
    public JTextField textField;
    public static JTextArea onlineList;
    public static int countCam;
    public static JSlider slider;
    public static boolean waitAnswer;
    public static long s;
    private static JButton send = new JButton("Отправить");
    public static File file;
    public static int bias = 0;
    public static int wtf = 10; //Я не знаю как так получилось, но без этого чат будет съезжать на 10 пикселей вправо ._.
    public static WorkPanel arrayCam[] = new WorkPanel[8];
    private static JScrollPane pane;
    private static JScrollPane panek;
    public static String name[] = new String[8];
    public static float fcoof = 1.5F;
    public static String filename;
    public static Timer timerwait;
    public static boolean sendinginprogress;
    public static long allByte;
    private JPanel olPanel = new JPanel();
    public static long bytesLeft;
    JPanel chat = new JPanel();
    public static Timer timerRecieve;
    public static JButton buttonRecieveFileCancel;
    public static JButton buttonRecieveFile;
    private static JPanel fT = new JPanel();
    public static JButton buttonSendFile;
    public static boolean recieveProcess;
    public static JLabel fileTransferLb;
    public static JLabel fileTransferName;
    public static JLabel fileTransferSize;
    public Gui(String title){
        super(title);
        name[0] = Main.login;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent event) {
            }
            public void windowClosed(WindowEvent event) {
            }
            public void windowClosing(WindowEvent event) {
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane.showOptionDialog(event.getWindow(), "Вы действительно хотите выйти?", "Подтверждение",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    event.getWindow().setVisible(false);
                    try {
                        Cam.camera.release();
                        Main.upnpServiceTCP.shutdown();
                        Main.upnpServiceUDP.shutdown();
                    } catch (NullPointerException ex){
                    }finally {
                        System.exit(0);
                    }
                }
            }
            public void windowDeactivated(WindowEvent event) {
            }
            public void windowDeiconified(WindowEvent event) {
            }
            public void windowIconified(WindowEvent event) {
            }
            public void windowOpened(WindowEvent event) {
            }
        });
        setSize(1280 , 720);
        setLocationRelativeTo(null);
        setLayout(null);
        paintGeneralPanel();
        paintChat((short) (getWidth() - 800) ,heightBottomPanel);
        paintOption((short) 782, heightBottomPanel);
        paintOnlineList((short) 160);
        setResizable(false);
        setMinimumSize(new Dimension(1280, 720));
        //setMaximumSize(new Dimension(1280, 720));
        setVisible(true);
        repaintOnlineList();
        getRootPane ().setOpaque ( true );
        getRootPane ().setBackground ( Color.BLACK );
        getContentPane ().setBackground ( Color.BLACK );
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                repaintFrame(bias,wtf);
            }
        });
    }
    public static void repaintOnlineList(){
        onlineList.append(Main.login + "\n");
        for(int i = 1; i <= countCam-1; i++){
            onlineList.append(name[i]+"\n");
        }
    }
    private void repaintFrame(int bias, int wtf){
        generalPanel.setSize(getWidth() - (178 + bias), getHeight() - (40 + heightBottomPanel + bias));
        olPanel.setLocation(getWidth() - (olPanel.getWidth() + 10 + bias), 5);
        olPanel.setSize(olPanel.getWidth(), getHeight() - (40 + heightBottomPanel + bias));
        fT.setSize((short) 782, heightBottomPanel);
        fT.setLocation(5 , getHeight() - (heightBottomPanel + 32 + bias));
        chat.setSize((short) (getWidth() - 800 - bias) ,heightBottomPanel);
        chat.setLocation(getWidth() - ((short) (getWidth() - 800 + wtf + bias)) ,getHeight() - (heightBottomPanel + 32 + bias));
        pane.setSize((short) (getWidth() - 800 - bias), heightBottomPanel  - 20);
        send.setLocation((short) (getWidth() - 800 - bias) - 99, heightBottomPanel - 20);
        panek.setSize((short) 160, getHeight() - (40 + heightBottomPanel) - bias);
        textField.setSize((short) (getWidth() - 800 - bias) - 100,20);
        repaintCam();
    }
    private void paintGeneralPanel(){
        WorkPanel image;
        short centerx = (short)((getWidth() - 178)/2);
        short centery = (short)((getHeight() - (40 + heightBottomPanel))/2);
        generalPanel = new JPanel();
        generalPanel.setSize(getWidth() - (178), getHeight() - (40 + heightBottomPanel));
        generalPanel.setLocation(5,5);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        generalPanel.setLayout(null);
        image = new WorkPanel();
        image.setSize(320,240);
        image.setLocation(centerx - 320/2,centery - 240/2);
        image.setVisible(true);
        add(generalPanel);
        image.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        image.setLayout ( new BorderLayout ( 4, 4 ) );
        setDragable(image, generalPanel);
        arrayCam[0] = image;
        countCam++;
        JLabel vers = new JLabel(Main.version);
        vers.setSize(500,20);
        vers.setLocation(5,5);
        vers.setEnabled(false);
        generalPanel.add(vers);
    }
    public void setDragable(JPanel transfer, JPanel contains){
        transfer.add(new JLabel( "", JLabel.CENTER ){
            {
                setTransferHandler ( new TransferHandler() {
                    public int getSourceActions ( JComponent c )
                    {
                        return TransferHandler.MOVE;
                    }
                    public boolean canImport ( TransferSupport support ) {
                        // Для "прозрачности" панели при сбросе ДнД
                        // Позволяет располагать панель даже когда курсор не над dragContainer'ом
                        return transfer.getTransferHandler ().canImport ( support );
                    }
                    public boolean importData ( TransferSupport support ) {
                        // Для "прозрачности" панели при сбросе ДнД
                        // Позволяет располагать панель даже когда курсор не над dragContainer'ом
                        return transfer.getTransferHandler ().importData ( support );
                    }
                    protected Transferable createTransferable ( JComponent c ) {
                        return new StringSelection ( panel1 );
                    }
                } );
                addMouseListener ( new MouseAdapter() {
                    public void mousePressed ( MouseEvent e ) {
                        if ( SwingUtilities.isLeftMouseButton ( e ) ) {
                            // Для корректной вставки панели позднее
                            Point los = transfer.getLocationOnScreen ();
                            phase[nowDragableCam] = new Dimension ( e.getLocationOnScreen ().x - los.x,
                                    e.getLocationOnScreen ().y - los.y );

                            // Для отрисовки перетаскиваемого образа
                            dragged[nowDragableCam] = new BufferedImage ( transfer.getWidth (),
                                    transfer.getHeight (), BufferedImage.TYPE_INT_ARGB );
                            Graphics2D g2d = dragged[nowDragableCam].createGraphics ();
                            g2d.setComposite (
                                    AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, 0.5f ) );
                            transfer.paintAll ( g2d );
                            g2d.dispose ();

                            JComponent c = ( JComponent ) e.getSource ();
                            TransferHandler handler = c.getTransferHandler ();
                            handler.exportAsDrag ( c, e, TransferHandler.MOVE );
                        }
                    }
                } );
            }
        } );
        setTransferHandler ( new TransferHandler() {
            public int getSourceActions ( JComponent c )
            {
                return TransferHandler.NONE;
            }
            public boolean canImport ( TransferSupport support )
            {
                try {
                    return support.getTransferable ().getTransferData ( DataFlavor.stringFlavor ) !=
                            null;
                }
                catch ( Throwable e ) {
                    return false;
                }
            }
            public boolean importData ( TransferHandler.TransferSupport info ) {
                if ( info.isDrop () ) {
                    try {
                        String panelId = ( String ) info.getTransferable ()
                                .getTransferData ( DataFlavor.stringFlavor );
                        Point dropPoint = info.getDropLocation ().getDropPoint ();
                        if ( panelId.equals ( panel1 ) ) {
                            transfer.setLocation ( dropPoint.x - phase[nowDragableCam].width,
                                    dropPoint.y - phase[nowDragableCam].height );
                            transfer.revalidate ();
                        }
                        return true;
                    }
                    catch ( Throwable e ) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        } );
        contains.add ( transfer );
        // Слушатель для корректного расположния панели при её сбросе
        transfer.setTransferHandler ( new TransferHandler()
        {
            public int getSourceActions ( JComponent c )
            {
                return TransferHandler.NONE;
            }

            public boolean canImport ( TransferSupport support )
            {
                try
                {
                    return support.getTransferable ().getTransferData ( DataFlavor.stringFlavor ) !=
                            null;
                }
                catch ( Throwable e )
                {
                    return false;
                }
            }

            public boolean importData ( TransferHandler.TransferSupport info )
            {
                if ( info.isDrop () )
                {
                    try
                    {
                        String panelId = ( String ) info.getTransferable ()
                                .getTransferData ( DataFlavor.stringFlavor );
                        Point dropPoint = info.getDropLocation ().getDropPoint ();
                        if ( panelId.equals ( panel1 ) )
                        {
                            transfer.setLocation ( dropPoint.x - phase[nowDragableCam].width,
                                    dropPoint.y - phase[nowDragableCam].height );
                            transfer.revalidate ();
                        }
                        return true;
                    }
                    catch ( Throwable e )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        } );

        final ImageGlassPane glassPane = new ImageGlassPane ();
        contains.getRootPane().setGlassPane ( glassPane );

        // Слушатель для перерисовки перетаскиваемого образа
        DragSourceAdapter dsa = new DragSourceAdapter()
        {
            public void dragEnter ( DragSourceDragEvent dsde )
            {
                updateGlassPane ( dsde );
            }

            public void dragMouseMoved ( DragSourceDragEvent dsde )
            {
                updateGlassPane ( dsde );
            }

            private void updateGlassPane ( DragSourceDragEvent dsde )
            {
                glassPane.setImage ( dragged[nowDragableCam] );
                glassPane.setPoint ( new Point (
                        dsde.getLocation ().x - contains.getLocationOnScreen ().x -
                                phase[nowDragableCam].width,
                        dsde.getLocation ().y - contains.getLocationOnScreen ().y -
                                phase[nowDragableCam].height ) );
            }

            public void dragDropEnd ( DragSourceDropEvent dsde )
            {
                glassPane.setImage ( null );
                glassPane.setPoint ( null );
            }
        };
        DragSource.getDefaultDragSource ().addDragSourceListener ( dsa );
        DragSource.getDefaultDragSource ().addDragSourceMotionListener ( dsa );
        glassPane.setVisible ( true );
        nowDragableCam ++;
    }
    public void addCam(){
        WorkPanel image;
        image = new WorkPanel();
        image.setSize(320,240);
        image.setVisible(true);
        setDragable(image, generalPanel);
        image.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        Gui.arrayCam[countCam] = image;
        countCam++;
        repaintCam();
    }
    public static void removeCam(){
        Gui.arrayCam[countCam-1].setVisible(false);
        generalPanel.remove(Gui.arrayCam[countCam-1]);
        Gui.arrayCam[countCam-1] = null;
        countCam --;
        repaintCam();
    }
    public static void repaintCam(){
        short iwhile = 0;
        short globalwheight = 0;
        while (iwhile < countCam){
            globalwheight += Gui.arrayCam[iwhile].getWidth();
            iwhile ++;
        }
        short spaceDraw = (short) ((generalPanel.getWidth()-(globalwheight))/(countCam+1));
        int x = 0;
        for(int i = 0; i <= Gui.countCam-1; i++){
            x = x + spaceDraw + (Gui.arrayCam[i].getWidth()/2);
            Gui.arrayCam[i].setLocOwer(x,(generalPanel.getHeight()/2));
            x = x + (Gui.arrayCam[i].getWidth()/2);
        }
    }
    public void paintOnlineList(short width){
        olPanel.setSize(width, getHeight() - (40 + heightBottomPanel));
        olPanel.setLocation(getWidth() - (width + 10), 5);
        olPanel.setLayout(null);
        olPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        add(olPanel);
        onlineList = new JTextArea(1,1);
        onlineList.setLineWrap(true);
        onlineList.setWrapStyleWord(true);
        onlineList.setEditable(false);
        panek = new JScrollPane(onlineList);
        panek.setSize(width, getHeight() - (40 + heightBottomPanel));
        panek.setLocation(0,0);
        olPanel.add(panek);
        panek.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void paintOption(short width, short height){
        int indent = 5;
        fT.setSize(width, height);
        fT.setLocation(5 , getHeight() - (height + 32));
        fT.setLayout(null);
        fT.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        JPanel camP = new JPanel(null);
        camP.setLocation(indent, indent);
        camP.setSize(width/3, height-indent*2);
        camP.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        JCheckBox camEnabled = new JCheckBox("Камера выключена");
        ActionListener listenerCamSwith = event -> {
            if (Cam.enable){
                Cam.enable = !Cam.enable;
                camEnabled.setText("Камера выключена");
                if (Main.connected) {
                    Cam.sendImage(Cam.encodeToStr(Cam.toBufImg(Main.avatar), "png"));
                }
                try {
                    arrayCam[0].image = ImageIO.read(new File(Main.avatar));
                    Main.gui.arrayCam[0].repaint();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                Main.cam.stop();
                Main.cam = null;
            } else {
                Cam.enable = !Cam.enable;
                camEnabled.setText("Камера включена");
                Main.cam = new Cam();
                Main.cam.start();
            }
        };
        camEnabled.addActionListener(listenerCamSwith);
        camEnabled.setSize(150,15);
        camEnabled.setLocation(5,20);
        slider = new JSlider(0,1,3,1);
        slider.setLocation(2,camP.getHeight()-110);
        slider.setSize(camP.getWidth()-4,20);
        slider.setUI ( new MySliderUI ( slider ) );
        slider.setOpaque ( true );
        slider.setSnapToTicks ( true );
        slider.setMajorTickSpacing ( 1 );
        slider.setPaintTicks ( true );
        slider.setValue(2);
        slider.setBorder ( BorderFactory.createEmptyBorder ( 10, 10, 10, 10 ) );
        JLabel sliderL = new JLabel("Настройки качества передачи");
        sliderL.setSize(500,20);
        sliderL.setLocation(5,40);
        camP.add(sliderL);
        JLabel slider1 = new JLabel("1:4");
        slider1.setSize(500,20);
        slider1.setLocation(10,75);
        camP.add(slider1);
        JLabel slider2 = new JLabel("1:2");
        slider2.setSize(500,20);
        slider2.setLocation(122,75);
        camP.add(slider2);
        JLabel slider3 = new JLabel("1:1");
        slider3.setSize(500,20);
        slider3.setLocation(234,75);
        camP.add(slider3);
        camP.add(slider);
        JButton addcam = new JButton("+");
        addcam.setSize(20,20);
        addcam.setLocation(5, 70);
        JButton setAv = new JButton("Изменить аватар");
        setAv.setSize(camP.getWidth()-10,20);
        setAv.setLocation(5, camP.getHeight()-25);
        camP.add(setAv);
        setAv.addActionListener(e -> {
            setAvatar();
        });
        JButton remcam = new JButton("-");
        remcam.setSize(20,20);
        remcam.setLocation(30, 70);
        JLabel camOpt = new JLabel("Настройки камеры");
        camOpt.setSize(500,20);
        camOpt.setLocation(5,0);
        camOpt.setVisible(true);
        JSeparator separatorCamOpt1 = new JSeparator();
        separatorCamOpt1.setSize(camP.getWidth()-10, 5);
        separatorCamOpt1.setLocation(5,40);
        separatorCamOpt1.setForeground(Color.GRAY);
        camP.add(separatorCamOpt1);
        JButton buttonChooseOutput = new JButton("Выбрать путь");
        camP.add(buttonChooseOutput);
        JButton buttonRecord = new JButton("Записать");
        JSeparator separatorCamOpt2 = new JSeparator();
        separatorCamOpt2.setSize(camP.getWidth()-10, 5);
        separatorCamOpt2.setLocation(5,camP.getHeight()- 70);
        separatorCamOpt2.setForeground(Color.GRAY);
        camP.add(separatorCamOpt2);
        JLabel recordL = new JLabel("Настройки записи");
        recordL.setSize(500,20);
        recordL.setLocation(5,camP.getHeight()- 70);
        recordL.setVisible(true);
        camP.add(recordL);
        buttonRecord.setLocation((camP.getWidth()/2)+2,camP.getHeight()- 50);
        buttonRecord.setSize((camP.getWidth()/2)-5-2,20);
        buttonChooseOutput.setLocation(5,camP.getHeight()- 50);
        buttonChooseOutput.setSize((camP.getWidth()/2)-5-2,20);
        camP.add(buttonRecord);
        ActionListener addCamList = event -> {
            addCam();
        };
        ActionListener remCamList = event -> {
           removeCam();
        };
        addcam.addActionListener(addCamList);
        remcam.addActionListener(remCamList);
        add(fT);
        fT.add(camP);
        JPanel fTPanel = new JPanel(null);
        fTPanel.setLocation((width/3)*2-3, indent);
        fTPanel.setSize(width/3, height-indent*2);
        fTPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        JLabel fileOS = new JLabel("Передача файлов");
        fileOS.setSize(500,20);
        fileOS.setLocation(5,0);
        fTPanel.add(fileOS);
        fileTransferLb = new JLabel("Нет входящих запросов");
        fileTransferLb.setSize(500,20);
        fileTransferLb.setLocation(5,85);
        fTPanel.add(fileTransferLb);
        fileTransferLb.setEnabled(false);
        fileTransferName = new JLabel("Имя файла: ''");
        fileTransferName.setSize(500,20);
        fileTransferName.setLocation(5,105);
        fTPanel.add(fileTransferName);
        fileTransferName.setEnabled(false);
        fileTransferSize = new JLabel("Размер файла: байт");
        fileTransferSize.setSize(500,20);
        fileTransferSize.setLocation(5,125);
        fTPanel.add(fileTransferSize);
        fileTransferSize.setEnabled(false);
        JButton buttonSelect = new JButton("Выбрать файл");
        buttonSelect.setLocation(5,20);
        buttonSelect.setSize(fTPanel.getWidth()-10,20);
        buttonSelect.setVisible(true);
        JLabel fileSelOS = new JLabel("Вы выбрали файл:''");
        buttonSelect.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                fileSelOS.setText("Вы выбрали файл:'" + file.getName() + "'");
            }
        });
        JSeparator separatorFileOutput = new JSeparator();
        separatorFileOutput.setSize(fTPanel.getWidth()-10, 5);
        separatorFileOutput.setLocation(5,85);
        separatorFileOutput.setForeground(Color.GRAY);
        fTPanel.add(separatorFileOutput);
        buttonSendFile = new JButton("Отправить");
        buttonSendFile.setLocation(5,60);
        buttonSendFile.setSize((fTPanel.getWidth()/2)-5-2,20);
        buttonSendFile.setVisible(true);
        buttonSendFile.addActionListener(e -> {
            if(file.getTotalSpace() > 0){
                Inet.sendPackMessage(new Message(file.getName(), file.getTotalSpace()));
                System.out.println("Триггер! " + file.getTotalSpace());
                waitAnswer = true;
                buttonSendFile.setEnabled(false);
                buttonRecieveFileCancel.setEnabled(false);
                buttonSendFile.setText("Ждём ответа .");
                final int[] iteration = {1};
                timerwait = new Timer(500, ev -> {
                    if (iteration[0] == 0){
                        if(!sendinginprogress) {
                            buttonSendFile.setText("Ждём ответ .");
                        } else {
                            buttonSendFile.setText("Отправка .");
                            fileOS.setText("Отправка файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                        }
                        iteration[0]++;
                    } else if (iteration[0] == 1){
                        if(!sendinginprogress) {
                            buttonSendFile.setText("Ждём ответ ..");
                        } else {
                            buttonSendFile.setText("Отправка ..");
                            fileOS.setText("Отправка файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                        }
                        iteration[0]++;
                    } else if (iteration[0] == 2){
                        if(!sendinginprogress) {
                            buttonSendFile.setText("Ждём ответ ...");
                        } else {
                            buttonSendFile.setText("Отправка ...");
                            fileOS.setText("Отправка файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                        }
                        iteration[0] = 0;
                    }
                    if (!waitAnswer){
                        buttonSendFile.setText("Отправить");
                        fileOS.setText("Отправка файлов");
                        buttonSendFile.setEnabled(true);
                        buttonRecieveFileCancel.setEnabled(true);
                        stopTimerWait();
                    }
                });
                timerwait.start();
            }
        });
        buttonRecieveFile = new JButton("Принять");
        buttonRecieveFile.setLocation(5,fTPanel.getHeight()-25);
        buttonRecieveFile.setSize((fTPanel.getWidth()/2)-5-2,20);
        buttonRecieveFile.setEnabled(false);
        buttonRecieveFile.addActionListener(e -> {
            Runnable r = new Runnable() {
                public void run() {
                    FileOutputStream fos = null;
                    BufferedInputStream bis = null;
                    Socket clientFile = null;
                    ServerSocket socketListener = null;
                    recieveProcess = true;
                    try {
                        System.out.println("1 старт");
                        int socknumb = Inet.getNotUsePort(7820);
                        socketListener = new ServerSocket(socknumb);
                        clientFile = null;
                        System.out.println("2 создали сокет");
                        Inet.sendPackMessage(new Message(socknumb, true));
                        System.out.println("3 отправили пакет с разрешением и портом " + socknumb);
                        while (clientFile == null) {
                            clientFile = socketListener.accept();
                        }
                        System.out.println("File size: " + s);
                        byte[] byteArray = new byte[1024];
                        new File("Recieved").mkdir();
                        File f = new File("./Recieved/" + filename);
                        f.createNewFile();
                        fos = new FileOutputStream(f);
                        int sp = (int) (s / 1024);
                        if (s % 1024 != 0) sp++;
                        bis = new BufferedInputStream(clientFile.getInputStream());
                        Gui.allByte = s;
                        while (s > 0) {
                            int i = bis.read(byteArray);
                            fos.write(byteArray, 0, i);
                            s -= i;
                            Gui.bytesLeft = s;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                            bis.close();
                            clientFile.close();
                            socketListener.close();
                            System.out.println("Файл какбэ принят");
                            new JOptionPane().showMessageDialog(null, filename + " recieve");
                            recieveProcess = false;
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            };
            new Thread(r).start();
            final int[] iteration = {1};
            fileTransferSize.setEnabled(false);
            fileTransferName.setEnabled(false);
            fileTransferLb.setText("Идёт приём файла.");
            buttonRecieveFile.setEnabled(false);
            buttonRecieveFileCancel.setEnabled(false);
            timerRecieve = new Timer(500, ev -> {
                if (iteration[0] == 0){
                    fileOS.setText("Идёт приём файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                    iteration[0]++;
                } else if (iteration[0] == 1){
                    fileOS.setText("Идёт приём файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                    iteration[0]++;
                } else if (iteration[0] == 2){
                    fileOS.setText("Идёт приём файла. Завершенно: "+(allByte-bytesLeft)/(allByte/100)+"%");
                    iteration[0] = 0;
                }
                if (!recieveProcess){
                    fileTransferLb.setEnabled(false);
                    fileTransferLb.setText("Нет входящих запросов");
                    fileTransferName.setText("Имя файла: ''");
                    fileTransferName.setEnabled(false);
                    fileTransferSize.setText("Размер файла: 0 байт");
                    fileTransferSize.setEnabled(false);
                    buttonRecieveFile.setEnabled(false);
                    buttonRecieveFileCancel.setEnabled(false);
                    stopTimerRec();
                }
            });
            timerRecieve.start();
        });
        buttonRecieveFileCancel = new JButton("Отклонить");
        buttonRecieveFileCancel.setLocation((fTPanel.getWidth()/2)+2,fTPanel.getHeight()-25);//(camP.getWidth()/2)+2,camP.getHeight()- 50
        buttonRecieveFileCancel.setSize((fTPanel.getWidth()/2)-5-2,20);
        buttonRecieveFileCancel.setEnabled(false);
        buttonRecieveFileCancel.addActionListener(e -> {
            Inet.sendPackMessage(new Message(0, false));
            Gui.buttonRecieveFile.setEnabled(false);
            Gui.buttonRecieveFileCancel.setEnabled(false);
            Gui.fileTransferLb.setEnabled(false);
            Gui.fileTransferName.setEnabled(false);
            Gui.fileTransferSize.setEnabled(false);
            Gui.fileTransferLb.setText("Нет входящих запросов");
            Gui.fileTransferName.setText("Имя файла:''");
            Gui.fileTransferSize.setText("Размер файла: 0 байт");
        });
        JButton buttonCancelSend = new JButton("Отменить");
        buttonCancelSend.addActionListener(e -> {
            waitAnswer = false;
            buttonSendFile.setEnabled(true);
            timerwait.stop();
            buttonSendFile.setText("Отправить");
        });
        buttonCancelSend.setLocation((fTPanel.getWidth()/2)+2,60);
        buttonCancelSend.setSize((fTPanel.getWidth()/2)-5-2,20);
        fileSelOS.setSize(500,20);
        fileSelOS.setLocation(5,40);
        fTPanel.add(buttonRecieveFile);
        fTPanel.add(buttonRecieveFileCancel);
        fTPanel.add(buttonSelect);
        fTPanel.add(buttonSendFile);
        fTPanel.add(buttonCancelSend);
        camP.add(camOpt);
        fT.add(fTPanel);
        fTPanel.add(fileSelOS);
        camP.add(camEnabled);
        JCheckBox resiseEnabled = new JCheckBox("Разрешить маштабирование окна (beta)");
        ActionListener listenerResSwith = event -> {
            if(!isResizable()){
                bias = 10;
                wtf = 0;
                setResizable(true);
                repaintFrame(bias, wtf);
            }else{
                bias = 0;
                wtf = 10;
                setResizable(false);
                repaintFrame(bias, wtf);
            }
        };
        resiseEnabled.addActionListener(listenerResSwith);
        resiseEnabled.setSize(500, 20);
        resiseEnabled.setLocation(camP.getWidth() + 3 ,camP.getHeight()-15);
        fT.add(resiseEnabled);
    }
    public void setAvatar(){
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть фото");
        if (ret == JFileChooser.APPROVE_OPTION) {
            Main.avatarFile = fileopen.getSelectedFile();
            Main.avatar = Main.avatarFile.getAbsolutePath();
        }
        try {
            arrayCam[0].image = ImageIO.read(new File(Main.avatar));
            Main.gui.arrayCam[0].repaint();
            if (Main.connected) {
                byte[] sendData = Cam.encodeToStr(Cam.toBufImg(Main.avatar), "png");
                Cam.sendImage(sendData);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void stopTimerWait(){
        timerwait.stop();
    }
    public void stopTimerRec(){
        timerRecieve.stop();
    }
    public  void paintChat(short width, short height){         //Создаём панель для RadioButton
        chat.setSize(width,height);
        chat.setLocation(getWidth() - (width + 10) ,getHeight() - (height + 32));
        chat.setLayout(null);
        add(chat);
        incomingText = new JTextArea(1,1);
        incomingText.setLineWrap(true);
        incomingText.setWrapStyleWord(true);
        incomingText.setEditable(false);
        pane = new JScrollPane(incomingText);
        pane.setSize(width, height - 20);
        pane.setLocation(0,0);
        textField = new JTextField(20);
        textField.setSize(width - 100,20);
        textField.setLocation(0, height - 20);
        notThisPLZ = textField.getText();
        send.setSize(98,19);
        send.setLocation(width - 99, height - 20);
        String voidString = textField.getText();
        textField.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER & !Objects.equals(textField.getText(), voidString)){//Если нажата кнопка ENTER
                    sndMsg(textField.getText());
                }
            }
        });
        ActionListener listenerSend = event -> {
            if (!Objects.equals(textField.getText(), voidString)){
                sndMsg(textField.getText());
            }
        };
        send.addActionListener(listenerSend);
        chat.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.gray, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        chat.add(textField);
        chat.add(pane);
        chat.add(send);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    }
    public void sndMsg(String msg){
        try {
            time = java.util.Calendar.getInstance().getTime();
            msg = (new Time(time.getTime())).toString() + " " + Main.login + ": " + msg;
            textField.setText("");
            incomingText.append(msg + " \n");
            Inet.sendMessage(msg);
        }catch (NullPointerException noConnect){
            msg = (new Time(time.getTime())).toString() + " Вы: " + textField.getText() + " (В пустоту, т.к. нет других людей в чате.)";
            incomingText.append(msg + "\n");
            textField.setText("");
        }
    }

    private static class ImageGlassPane extends JComponent {
        private Point point = null;
        private BufferedImage image = null;
        public ImageGlassPane () {
            super ();
            setOpaque ( false );
        }
        public Point getPoint ()
        {
            return point;
        }
        public void setPoint ( Point point ) {
            this.point = point;
            repaint ();
        }
        public BufferedImage getImage ()
        {
            return image;
        }
        public void setImage ( BufferedImage image )
        {
            this.image = image;
            repaint ();
        }
        public void paint ( Graphics g ) {
            super.paint ( g );
            if ( point != null && image != null )
            {
                g.drawImage ( image, point.x, point.y, null );
            }
        }
    }
}

