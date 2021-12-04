package com.kalugin.view;

import com.kalugin.view.model.Bot;
import com.kalugin.view.model.Gamer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GameFxApp extends Application {

    @Override
    public void start(Stage stage) {
        GameMap map = GameMap.getInstance();
        map.setStage(stage);


    }
}
