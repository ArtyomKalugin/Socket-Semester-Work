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
    private static final int stageWidth = 1200;
    private static final int stageHeight = 900;
    private static ArrayList<Platform> platforms = new ArrayList<>();
    private static Pane pane = new Pane();

    @Override
    public void start(Stage stage) throws Exception {
        Platform platform1 = new Platform(80, 650, 200, 20, true);
        Platform platform2 = new Platform(800, 650, 200, 20, true);
        Platform platform3 = new Platform(350, 540, 500, 20, true);
        Platform grass = new Platform(0, 745, stageWidth, 20, true);

        Gamer gamer = new Gamer(0, 0, 50, 50, stageWidth, stageHeight);

        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);
        platforms.add(grass);

        pane.getChildren().add(gamer);
        pane.getChildren().add(platform1);
        pane.getChildren().add(platform2);
        pane.getChildren().add(platform3);
        pane.getChildren().add(grass);

        Scene scene = new Scene(pane, stageWidth, stageHeight);
        scene.setOnKeyPressed(gamer);
        stage.setScene(scene);
        stage.show();

//        scene.setOnKeyReleased(keyEvent -> {
//            switch (keyEvent.getCode()) {
//                case LEFT:
//                    gamer.setLeft(false);
//                case RIGHT:
//                    gamer.setRight(false);
//            }
//        });
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

    private static class Bullet extends Rectangle {
        private final static double width = 30;
        private final static double height = 10;
        private final boolean isRight;

        public Bullet(double x, double y, boolean isRight) {
            super(x, y, width, height);
            setFill(Color.YELLOW);
            this.isRight = isRight;

            Bullet bullet = this;
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    for(Platform platform : platforms) {
                        if(bullet.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                            this.stop();
                            pane.getChildren().remove(bullet);
                        }
                    }

                    if(isRight) {
                        if(getX() <= stageWidth) {
                            setX(getX() + 15);
                        } else {
                            this.stop();
                            pane.getChildren().remove(bullet);
                        }
                    } else {
                        if(getX() >= 0) {
                            setX(getX() - 15);
                        } else {
                            this.stop();
                            pane.getChildren().remove(bullet);
                        }
                    }
                }
            };

            animationTimer.start();
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
        private boolean isJumping;
        private double gravity = 0;
        private final int step = 7;

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

            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    checkBottomCollision();

                    if(!isBottomCollision && !isJumping){
                        setY(getY() + 10);
                    }

                    if(isBottomCollision) {
                        Platform platform = getBottomCollisionPlatform();

                        if(platform != null) {
                            setY(platform.getY() - getHeight());
                        }
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

        private void checkRightCollision() {
            BoundingBox rightBox = new BoundingBox(this.getX() + width, this.getY(), 0.1, height - 0.1);

            for(Platform platform : platforms) {
                isRightCollision = rightBox.intersects(platform.getBoundsInParent());

                if(isRightCollision) {
                    break;
                }
            }
        }

        private void checkLeftCollision() {
            BoundingBox leftBox = new BoundingBox(this.getX() - 0.1, this.getY(), 0.1, height - 0.1);

            for(Platform platform : platforms) {
                isLeftCollision = leftBox.intersects(platform.getBoundsInParent());

                if(isLeftCollision) {
                    break;
                }
            }
        }

        private void checkBottomCollision() {
            BoundingBox bottomBox = new BoundingBox(this.getX(), this.getY() + height, width, 0.1);

            for(Platform platform : platforms) {
                isBottomCollision = bottomBox.intersects(platform.getBoundsInParent());

                if(isBottomCollision) {
                    break;
                }
            }
        }

        private void checkTopCollision() {
            BoundingBox topBox = new BoundingBox(this.getX(), this.getY() - 0.1, width, 0.1);

            for(Platform platform : platforms) {
                isTopCollision = topBox.intersects(platform.getBoundsInParent());

                if(isTopCollision) {
                    break;
                }
            }
        }

        private Platform getBottomCollisionPlatform() {
            BoundingBox bottomBox = new BoundingBox(this.getX(), this.getY() + height, width, 0.1);

            for(Platform platform : platforms) {
                isBottomCollision = bottomBox.intersects(platform.getBoundsInParent());

                if(isBottomCollision) {
                    return platform;
                }
            }

            return null;
        }

        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case SPACE:
                    Bullet bullet;

                    if(right) {
                        bullet = new Bullet(getX() + width, getY() + (height / 2), true);
                    } else {
                        bullet = new Bullet(getX(), getY() + (height / 2), false);
                    }
                    pane.getChildren().add(bullet);
                    break;

                case LEFT:
                    checkLeftCollision();
                    if(((this.getX() - step) >= 0) && !isLeftCollision){
                        this.setX(this.getX() - step);
                        left = true;
                        right = false;
                    }
                    break;
                case RIGHT:
                    checkRightCollision();
                    if(((this.getX() + width) <= stageWidth) && !isRightCollision){
                        this.setX(this.getX() + step);
                        left = false;
                        right = true;
                    }
                    break;
                case UP:
                    if(gravity == 0) {
                        double previousY = this.getY();
                        AnimationTimer jumpTimer = new AnimationTimer() {
                            @Override
                            public void handle(long l) {
                                checkBottomCollision();
                                if(isBottomCollision && gravity == 0) {
                                    isJumping = true;
                                } else if(!isBottomCollision && gravity == 0) {
                                    isJumping = false;
                                }

                                if(isJumping) {
                                    setY(getY() - 15 + gravity);
                                    gravity += 1;
                                }

                                Platform platform = getBottomCollisionPlatform();
                                checkTopCollision();
                                if(isTopCollision) {
                                    if(15 > gravity) {
                                        gravity += 15 - gravity;
                                    }
                                }

                                checkBottomCollision();
                                if(isBottomCollision) {
                                    this.stop();
                                    gravity = 0;
                                    isJumping = false;

                                    if(platform != null) {
                                        setY(platform.getY() - getHeight());
                                    }
                                }

                                if(previousY <= getY()) {
                                    this.stop();
                                    gravity = 0;
                                    isJumping = false;
                                }

                                checkRightCollision();
                                if(right) {
                                    if(((getX() + width) <= stageWidth) && !isRightCollision){
                                        setX(getX() + step);
                                    }
                                }

                                checkLeftCollision();
                                if(left) {
                                    if(((getX() - step) >= 0) && !isLeftCollision) {
                                        setX(getX() - step);
                                    }
                                }
                            }
                        };

                        jumpTimer.start();
                    }
                    break;
//                case DOWN:
//                    checkBottomCollision();
//                    if(((this.getY() + height + step) <= stageHeight) && !isBottomCollision) {
//                        this.setY(this.getY() + step);
//                    }
//                    break;
            }
        }
    }
}
