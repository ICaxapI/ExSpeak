 import javafx.application.Application;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.layout.StackPane;
        import javafx.stage.Stage;

 import javax.swing.*;
 import java.awt.event.WindowEvent;
 import java.awt.event.WindowListener;

 public class ExSpeak_UI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        Button button = new Button("click me");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Hello World!");
            }
        });
        StackPane pane = new StackPane();
        pane.getChildren().add(button);
        primaryStage.setScene(new Scene(pane,300,300));
        primaryStage.show();
        WindowListener listener = new WindowListener() {
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
        };
//        pane.getChildren().addListener(listener);
    }

    public static void main(String[] args) {
        launch(args);
    }
}