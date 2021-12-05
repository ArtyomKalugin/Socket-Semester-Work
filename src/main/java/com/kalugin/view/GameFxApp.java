package com.kalugin.view;

import javafx.application.Application;
import javafx.stage.Stage;


public class GameFxApp extends Application {

    @Override
    public void start(Stage stage) {
        GameMap map = GameMap.getInstance();
        map.setStage(stage);
    }
}
