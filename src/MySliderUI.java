import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MySliderUI extends BasicSliderUI
{
    public static final ImageIcon BG_LEFT_ICON =
            new ImageIcon("pic/bg_left.png");
    public static final ImageIcon BG_MID_ICON =
            new ImageIcon ( "pic/bg_mid.png" );
    public static final ImageIcon BG_RIGHT_ICON =
            new ImageIcon ( "pic/bg_right.png" );
    public static final ImageIcon BG_FILL_ICON =
            new ImageIcon ( "pic/bg_fill.png" );
    public static final ImageIcon GRIPPER_ICON =
            new ImageIcon ( "pic/gripper.png" );
    public static final ImageIcon GRIPPER_PRESSED_ICON =
            new ImageIcon ( "pic/gripper_pressed.png" );

    public MySliderUI ( final JSlider b )
    {
        super ( b );

        // Для корректной перерисовки слайдера
        b.addChangeListener ( new ChangeListener()
        {
            public void stateChanged ( ChangeEvent e )
            {
                b.repaint ();
            }
        } );
        b.addMouseListener ( new MouseAdapter()
        {
            public void mousePressed ( MouseEvent e )
            {
                b.repaint ();
            }

            public void mouseReleased ( MouseEvent e )
            {
                b.repaint ();
            }
        } );
    }

    // Возвращаем новый размер гриппера
    protected Dimension getThumbSize ()
    {
        return new Dimension ( GRIPPER_ICON.getIconWidth (), GRIPPER_ICON.getIconHeight () );
    }

    // Отрисовываем сам гриппер в необходимом месте
    public void paintThumb ( Graphics g )
    {
        int positionX = thumbRect.x + thumbRect.width / 2;
        int positionY = thumbRect.y + thumbRect.height / 2;
        g.drawImage ( isDragging () ? GRIPPER_PRESSED_ICON.getImage () : GRIPPER_ICON.getImage (),
                positionX - GRIPPER_ICON.getIconWidth () / 2,
                positionY - GRIPPER_ICON.getIconHeight () / 2, null );
    }

    // Отрисовываем «путь» слайдера
    public void paintTrack ( Graphics g )
    {
        if ( slider.getOrientation () == JSlider.HORIZONTAL )
        {
            // Сам путь
            g.drawImage ( BG_LEFT_ICON.getImage (), trackRect.x,
                    trackRect.y + trackRect.height / 2 - BG_LEFT_ICON.getIconHeight () / 2, null );
            g.drawImage ( BG_MID_ICON.getImage (), trackRect.x + BG_LEFT_ICON.getIconWidth (),
                    trackRect.y + trackRect.height / 2 - BG_MID_ICON.getIconHeight () / 2,
                    trackRect.width - BG_LEFT_ICON.getIconWidth () - BG_RIGHT_ICON.getIconWidth (),
                    BG_MID_ICON.getIconHeight (), null );
            g.drawImage ( BG_RIGHT_ICON.getImage (),
                    trackRect.x + trackRect.width - BG_RIGHT_ICON.getIconWidth (),
                    trackRect.y + trackRect.height / 2 - BG_RIGHT_ICON.getIconHeight () / 2, null );

            // Заполнение участка пути слева от гриппера
            g.drawImage ( BG_FILL_ICON.getImage (), trackRect.x + 1,
                    trackRect.y + trackRect.height / 2 - BG_FILL_ICON.getIconHeight () / 2,
                    thumbRect.x + thumbRect.width / 2 - trackRect.x - BG_LEFT_ICON.getIconWidth (),
                    BG_FILL_ICON.getIconHeight (), null );
        }
        else
        {
            // Для вертикального слайдера аналог приведен в приложенном jar'е с демкой и сурцами
        }
    }
}