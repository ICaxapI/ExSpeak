import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 27.09.2016.
 */
public class SoundOut {
    private boolean released = false;
    private Clip clip = null;
    private FloatControl volumeC = null;
    boolean playing = false;

    public SoundOut(File f) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            volumeC = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch(IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;
        }
    }
    public SoundOut(AudioInputStream mic) {
        try {
            clip = AudioSystem.getClip();
            clip.open(mic);
            clip.addLineListener(new Listener());
            volumeC = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch(Exception exc) {
            exc.printStackTrace();
            released = false;
        }
    }

    //true если звук успешно загружен, false если произошла ошибка
    public boolean isReleased() {
        return released;
    }

    //проигрывается ли звук в данный момент
    public boolean isPlaying() {
        return playing;
    }

    //Запуск
    /*
      breakOld определяет поведение, если звук уже играется
      Если reakOld==true, о звук будет прерван и запущен заново
      Иначе ничего не произойдёт
    */
    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    //То же самое, что и play(true)
    public void play() {
        play(true);
    }

    //Останавливает воспроизведение
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    //Установка громкости
    /*
      x долже быть в пределах от 0 до 1 (от самого тихого к самому громкому)
    */
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        volumeC.setValue((max-min)*x+min);
    }

    //Возвращает текущую громкость (число от 0 до 1)
    public float getVolume() {
        float v = volumeC.getValue();
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        return (v-min)/(max-min);
    }

    //Дожидается окончания проигрывания звука
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing) clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    //Статический метод, для удобства
    public static SoundOut playSound(String s) {
        File f = new File(s);
        SoundOut snd = new SoundOut(f);
        snd.play();
        return snd;
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}