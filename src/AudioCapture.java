import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AudioCapture extends JFrame{
    boolean stopCapture = false;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    public static void main(String args[]){
        new AudioCapture();
    }
    public AudioCapture(){
        final JButton captureBtn = new JButton("Capture");
        final JButton stopBtn = new JButton("Stop");
        final JButton playBtn = new JButton("Playback");
        captureBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        //Регистрация анонимных слушателей
        captureBtn.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        captureBtn.setEnabled(false);
                        stopBtn.setEnabled(true);
                        playBtn.setEnabled(false);
                        //Захват ввода данных с микрофон до тех пор, пока не нажата кнопка Stop
                        captureAudio();
                    }
                }
        );
        getContentPane().add(captureBtn);
        stopBtn.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        captureBtn.setEnabled(true);
                        stopBtn.setEnabled(false);
                        playBtn.setEnabled(true);
                        //Остановка ввода данных с микрофона
                        stopCapture = true;
                    }
                }
        );
        getContentPane().add(stopBtn);
        playBtn.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        //Воспроизведения сохраненных данных
                        playAudio();
                    }
                }
        );
        getContentPane().add(playBtn);
        getContentPane().setLayout(new FlowLayout());
        setTitle("Capture/Playback Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(250,70);
        setVisible(true);
    }
    //Этот метод захватывает аудио вход от микрофона и сохраняет в ByteArrayOutputStream
    private void captureAudio(){
        try{
            //Получение данных для захвата аудио
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine( dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            //Создание и запуск потока для захвата данных с микрофона
            Thread captureThread = new Thread(new CaptureThread());
            captureThread.start();
        }catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }
    }
    //Метод для проигрывания аудио данных из сохраненных в ByteArrayOutputStream данных
    private void playAudio(){
        try{
            //Получение данных аудио формата для воспроизводства
            byte audioData[] = byteArrayOutputStream.toByteArray();
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioFormat audioFormat = getAudioFormat();
            audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat, audioData.length/audioFormat.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //Создание и запуск потока для проигрывания сохраненных данных
            Thread playThread = new Thread(new PlayThread());
            playThread.start();
        }catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }
    }
    //Этот метод создает и возвращает AudioFormat
    private AudioFormat getAudioFormat(){
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    //Внутренний класс для сбора данных с микрофона
    class CaptureThread extends Thread{
        byte tempBuffer[] = new byte[10000];
        public void run(){
            byteArrayOutputStream = new ByteArrayOutputStream();
            stopCapture = false;
            try{
                while(!stopCapture){
                    // прочитать данные
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    if(cnt>0){
                        //Сохранение данных
                        byteArrayOutputStream.write(tempBuffer, 0, cnt);
                    }
                }
                byteArrayOutputStream.close();
            }catch(Exception e){
                System.out.println(e);
                System.exit(0);
            }
        }
    }
    //Внутренний класс для воспроизведения данных
    class PlayThread extends Thread{
        byte tempBuffer[] = new byte[10000];
        public void run(){
            try{
                int cnt;
                while((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1){
                    if(cnt>0){
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }
                sourceDataLine.drain();
                sourceDataLine.close();
            }catch(Exception e){
                System.out.println(e);
                System.exit(0);
            }
        }
    }
}