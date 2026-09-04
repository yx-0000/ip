package tt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Displays the task manager as an interactive JavaFX chatbot. */
public class Gui extends Application {
    private final TT taskManager = new TT();
    private final VBox dialogContainer = new VBox(12);
    private final TextField userInput = new TextField();
    private final Button sendButton = new Button("Send");

    @Override
    public void start(Stage stage) {
        ScrollPane scrollPane = createConversationPane();
        HBox inputArea = createInputArea();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(scrollPane);
        mainLayout.setBottom(inputArea);
        mainLayout.getStyleClass().add("main-layout");

        Scene scene = new Scene(mainLayout, 520, 640);
        scene.getStylesheets().add(getClass().getResource("/styles/chat.css").toExternalForm());

        stage.setTitle("tt.TT");
        stage.setMinWidth(420);
        stage.setMinHeight(520);
        stage.setScene(scene);
        stage.show();

        dialogContainer.getChildren().add(DialogBox.getBotDialog(
                "Hello! I'm tt.TT. What can I do for you?\n\nTry: todo read a book"));
        userInput.requestFocus();
    }

    private ScrollPane createConversationPane() {
        dialogContainer.setPadding(new Insets(16));

        ScrollPane scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("conversation-pane");
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
        return scrollPane;
    }

    private HBox createInputArea() {
        userInput.setPromptText("Enter a command...");
        userInput.setOnAction(event -> handleUserInput());
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> handleUserInput());

        HBox inputArea = new HBox(10, userInput, sendButton);
        inputArea.setPadding(new Insets(12));
        inputArea.getStyleClass().add("input-area");
        HBox.setHgrow(userInput, Priority.ALWAYS);
        return inputArea;
    }

    /** Adds the user's command and the task manager's response to the conversation. */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getBotDialog(taskManager.getResponse(input)));
        userInput.clear();

        if (Command.fromString(input.split(" ", 2)[0]) == Command.BYE) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
