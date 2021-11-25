package com.kalugin.view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;


public class GameFxApp extends Application {
    private static final int stageWidth = 600;
    private static final int stageHeight = 600;
    private static ArrayList<Platform> platforms = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        Gamer gamer = new Gamer(0, 0, 50, 50, stageWidth, stageHeight);
        Platform platform = new Platform(60, 100, 200, 20, true);

        platforms.add(platform);

        Scene scene = new Scene(new Pane(gamer, platform), stageWidth, stageHeight);
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

    private static class Platform extends Rectangle {
        private final double width;
        private final double height;

        public Platform(double x, double y, double width, double height, boolean isFilled) {
            super(x, y, width, height);

            this.height = height;
            this.width = width;

            if(isFilled) {
                setFill(Color.GRAY);
            }
        }
    }

    private static class Gamer extends Rectangle implements EventHandler<KeyEvent> {
        private final double width;
        private final double height;
        private final int stageWidth;
        private final int stageHeight;
        private boolean right;
        private boolean left;
        private boolean isLeftCollision;
        private boolean isRightCollision;
        private boolean isBottomCollision;
        private boolean isTopCollision;
        private double gravity = 0;
        private final int step = 5;

        public Gamer(double x, double y, double width, double height, int stageWidth, int stageHeight) {
            super(x, y, width, height);

            this.height = height;
            this.width = width;
            this.stageWidth = stageWidth;
            this.stageHeight = stageHeight;
            right = false;
            left = false;
            isLeftCollision = false;
            isRightCollision = false;
            isBottomCollision = false;

            setFill(Color.RED);

            Gamer gamer = this;
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if(((gamer.getY() + height + step) <= stageHeight) && !gamer.isBottomCollision){
                        gamer.setY(gamer.getY() + 5);
                    }
                }
            };
            animationTimer.start();
        }

        public void setRight(boolean right) {
            this.right = right;
        }

        public void setLeft(boolean left) {
            this.left = left;
        }

        private Platform checkCollision() {
            BoundingBox rightBox = new BoundingBox(this.getX() + width, this.getY(), 0.1, height - 0.1);
            BoundingBox leftBox = new BoundingBox(this.getX() - 0.1, this.getY(), 0.1, height - 0.1);
            BoundingBox bottomBox = new BoundingBox(this.getX(), this.getY() + height, width, 0.1);
            BoundingBox topBox = new BoundingBox(this.getX(), this.getY() - 0.1, width, 0.1);

            for(Platform platform : platforms) {
                isRightCollision = rightBox.intersects(platform.getBoundsInParent());
                isLeftCollision = leftBox.intersects(platform.getBoundsInParent());
                isBottomCollision = bottomBox.intersects(platform.getBoundsInParent());
                isTopCollision = topBox.intersects(platform.getBoundsInParent());

                return platform;
            }

            return null;
        }

        @Override
        public void handle(KeyEvent event) {
            checkCollision();

            switch (event.getCode()) {
                case LEFT:
                    if(((this.getX() - step) >= 0) && !isLeftCollision){
                        this.setX(this.getX() - step);
                        left = true;
                        right = false;
                    }
                    break;
                case RIGHT:
                    if(((this.getX() + width) <= stageWidth) && !isRightCollision){
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
                                gamer.setY(gamer.getY() - 15 + gravity);
                                gravity += 1;

                                Platform platform = checkCollision();

                                if(gamer.isTopCollision) {
                                    if(15 > gravity) {
                                        gravity += 15 - gravity;
                                    }
                                }

                                if(gamer.isBottomCollision) {
                                    this.stop();
                                    gravity = 0;

                                    if(platform != null) {
                                        gamer.setY(platform.getY() - gamer.getHeight());
                                    }
                                }

                                if(previousY <= gamer.getY()) {
                                    this.stop();
                                    gravity = 0;
                                }

                                if(right) {
                                    if(((gamer.getX() + width) <= stageWidth) && !isRightCollision){
                                        gamer.setX(gamer.getX() + step);
                                    }
                                }

                                if(left) {
                                    if(((gamer.getX() - step) >= 0) && !isLeftCollision) {
                                        gamer.setX(gamer.getX() - step);
                                    }
                                }
                            }
                        };

                        jumpTimer.start();
                    }
                    break;
                case DOWN:
                    if(((this.getY() + height + step) <= stageHeight) && !isBottomCollision) {
                        this.setY(this.getY() + step);
                    }
                    break;
            }
        }
    }
}
