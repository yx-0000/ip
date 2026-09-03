package tt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Provides a minimal graphical entry point for the task manager. */
public class Gui extends Application {
    /** Shows the task manager window. */
    @Override
    public void start(Stage stage) {
        Label title = new Label("tt.TT");
        Label instructions = new Label("Use the text interface to manage your tasks.");
        VBox root = new VBox(10, title, instructions);
        root.setPrefSize(400, 180);
        stage.setTitle("tt.TT");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** Starts the graphical task manager. */
    public static void main(String[] args) {
        launch(args);
    }
}
