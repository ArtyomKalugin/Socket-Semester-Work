package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class WinMenu {
    private String title = "Menu";
    private AnchorPane pane = null;
    private VBox vBox;
    public Button closeApp;
    private Stage stage;
    private String winner;

    public WinMenu(Stage stage, String winner) {
        this.stage = stage;
        this.winner = winner;

        configure();
    }

    private final EventHandler<ActionEvent> closeAppEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (closeApp == event.getSource()) {
                stage.close();
            }
        }
    };

    private void configure() {
        javafx.application.Platform.runLater(() -> {
            pane = new AnchorPane();
            vBox = new VBox(15);
            Font font = Font.font("Courier New", FontWeight.BOLD, 20);

            closeApp = new Button("Close app");
            closeApp.setOnAction(closeAppEvent);
            closeApp.setMaxWidth(500);
            closeApp.setMaxHeight(500);
            closeApp.setFont(font);

            Label winnerLabel = new Label(winner + "is winner!");
            winnerLabel.setFont(font);
            winnerLabel.setTextFill(Color.GREEN);

            vBox.getChildren().add(winnerLabel);
            vBox.getChildren().add(closeApp);

            AnchorPane.setTopAnchor(vBox, 5.0);
            AnchorPane.setLeftAnchor(vBox, 10.0);
            AnchorPane.setRightAnchor(vBox, 10.0);
            pane.getChildren().add(vBox);

            Scene scene = new Scene(pane, 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }
}
