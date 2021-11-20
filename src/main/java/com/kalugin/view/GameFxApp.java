package com.kalugin.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class GameFxApp extends Application {
    private static final int stageWidth = 600;
    private static final int stageHeight = 600;

    @Override
    public void start(Stage stage) throws Exception {
        Gamer gamer = new Gamer(0, 0, 50, 50);

        Scene scene = new Scene(new Pane(gamer), stageWidth, stageHeight);
        scene.setOnKeyPressed(gamer);
        stage.setScene(scene);
        stage.show();
    }

    private static class Gamer extends Rectangle implements EventHandler<KeyEvent> {
        private double width;
        private double height;

        public Gamer(double x, double y, double width, double height) {
            super(x, y, width, height);

            this.height = height;
            this.width = width;

            setFill(Color.RED);
        }

        @Override
        public void handle(KeyEvent event) {
            int step = 5;
            switch (event.getCode()) {
                case LEFT:
                    if((this.getX() - step) >= 0){
                        this.setX(this.getX() - step);
                    }
                    break;
                case RIGHT:
                    if((this.getX() + width + step) <= stageWidth){
                        this.setX(this.getX() + step);
                    }
                    break;
                case UP:
                    if((this.getY() - step) >= 0){
                        this.setY(this.getY() - step);
                    }
                    break;
                case DOWN:
                    if((this.getY() + height + step) <= stageHeight) {
                        this.setY(this.getY() + step);
                    }
                    break;
            }
        }
    }
}
