package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Menu {
    private String title = "Menu";
    private AnchorPane pane = null;
    private VBox vBox;
    public Button singlePlayer;
    private Stage stage;
    private final GameMap map = GameMap.getInstance();
    private TextField nameTextField;

    public Menu(Stage stage) {
        this.stage = stage;

        configure();
    }

    private final EventHandler<ActionEvent> singlePlayerEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (singlePlayer == event.getSource()) {
                map.setName(nameTextField.getText());
                map.setStage(stage);
            }
        }
    };

    private void configure() {
        pane = new AnchorPane();
        vBox = new VBox(5);
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);

        singlePlayer = new Button("singlePlayer");
        singlePlayer.setOnAction(singlePlayerEvent);
        singlePlayer.setMaxWidth(1000);
        singlePlayer.setMaxHeight(2000);
        singlePlayer.setFont(font);

        Label nameLabel = new Label("Enter your nickname:");

        nameTextField = new TextField();
        nameTextField.setMaxWidth(1000);
        nameTextField.setMaxHeight(2000);

        vBox.getChildren().add(nameLabel);
        vBox.getChildren().add(nameTextField);
        vBox.getChildren().add(singlePlayer);

        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 10.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        pane.getChildren().add(vBox);

        Scene scene = new Scene(pane, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
}

