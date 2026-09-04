package tt;

import javafx.application.Application;

/** Launches the JavaFX application without extending {@link Application}. */
public class Launcher {
    /** Starts the JavaFX chatbot. */
    public static void main(String[] args) {
        Application.launch(Gui.class, args);
    }
}
