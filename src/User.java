/**
 * Created by Alex on 24.11.2016.
 */
public class User {
    public short id;
    public String name;
    public User(){
        Gui.countCam++;
        id = (short) Gui.countCam;
        Gui.arrayCam[id] = new WorkPanel();
        Gui.generalPanel.add(Gui.arrayCam[id]);
    }
}
