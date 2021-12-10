package com.kalugin.view;

import com.kalugin.view.model.Menu;
import javafx.application.Application;
import javafx.stage.Stage;


public class GameFxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GameMap map = GameMap.getInstance();
        Menu menu = new Menu(stage);
    }
}
