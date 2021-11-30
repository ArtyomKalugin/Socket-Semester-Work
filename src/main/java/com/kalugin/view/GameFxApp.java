package com.kalugin.view;

import com.kalugin.view.model.Gamer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GameFxApp extends Application {

    @Override
    public void start(Stage stage) {
        GameMap map = GameMap.getInstance();
        Gamer gamer = new Gamer(0, 0, 50, 50);
        map.setGamer(gamer);

        Scene scene = new Scene(map.getPane(), map.getStageWidth(), map.getStageHeight());

        scene.setOnKeyPressed(gamer);
        scene.setOnKeyReleased(gamer);
        stage.setScene(scene);
        stage.show();
    }
}
