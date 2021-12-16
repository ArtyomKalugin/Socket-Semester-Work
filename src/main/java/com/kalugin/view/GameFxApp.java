package com.kalugin.view;

import com.kalugin.view.model.Menu;
import javafx.application.Application;
import javafx.stage.Stage;


public class GameFxApp extends Application {

    @Override
    public void start(Stage stage) {
        new Menu(stage);
    }
}
