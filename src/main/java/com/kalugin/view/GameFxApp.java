package com.kalugin.view;

import javafx.animation.AnimationTimer;
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
        Gamer gamer = new Gamer(0, 0, 50, 50, stageWidth, stageHeight);

        Scene scene = new Scene(new Pane(gamer), stageWidth, stageHeight);
        scene.setOnKeyPressed(gamer);
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case LEFT:
                    gamer.setLeft(false);
                case RIGHT:
                    gamer.setRight(false);
            }
        });
    }

    private static class Gamer extends Rectangle implements EventHandler<KeyEvent> {
        private final double width;
        private final double height;
        private final int stageWidth;
        private final int stageHeight;
        private boolean right = false;
        private boolean left = false;
        private double gravity = 0;

        public Gamer(double x, double y, double width, double height, int stageWidth, int stageHeight) {
            super(x, y, width, height);

            this.height = height;
            this.width = width;
            this.stageWidth = stageWidth;
            this.stageHeight = stageHeight;

            setFill(Color.RED);
        }

        public void setRight(boolean right) {
            this.right = right;
        }

        public void setLeft(boolean left) {
            this.left = left;
        }

        @Override
        public void handle(KeyEvent event) {
            int step = 7;
            switch (event.getCode()) {
                case LEFT:
                    if((this.getX() - step) >= 0){
                        this.setX(this.getX() - step);
                        left = true;
                        right = false;
                    }
                    break;
                case RIGHT:
                    if((this.getX() + width) <= stageWidth){
                        this.setX(this.getX() + step);
                        left = false;
                        right = true;
                    }
                    break;
                case UP:
                    if(gravity == 0) {
                        double previousY = this.getY();
                        Gamer gamer = this;
                        AnimationTimer jumpTimer = new AnimationTimer() {
                            @Override
                            public void handle(long l) {
                                gamer.setY(gamer.getY() - 10 + gravity);
                                gravity += 0.6;

                                if(previousY <= gamer.getY()) {
                                    this.stop();
                                    gravity = 0;
                                }

                                if(right) {
                                    if((gamer.getX() + width) <= stageWidth){
                                        gamer.setX(gamer.getX() + step);
                                    }
                                }

                                if(left) {
                                    if((gamer.getX() - step) >= 0) {
                                        gamer.setX(gamer.getX() - step);
                                    }
                                }
                            }
                        };

                        jumpTimer.start();
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
