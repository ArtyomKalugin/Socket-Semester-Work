package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Bullet extends Rectangle {
    private final static double width = 30;
    private final static double height = 7;
    private final boolean isRight;
    private GameMap map = GameMap.getInstance();
    private Gamer sender;
    private static int damage;

    public Bullet(double x, double y, boolean isRight, Gamer sender) {
        super(x, y, width, height);
        setFill(Color.YELLOW);
        this.isRight = isRight;
        this.sender = sender;
        Random rn = new Random();
        damage = 10 + rn.nextInt(11);

        move();
    }

    public Bullet(double x, double y, boolean isRight, int damage) {
        super(x, y, width, height);
        setFill(Color.YELLOW);
        this.isRight = isRight;
        this.damage = damage;

        move();
    }

    private synchronized void move() {
        Bullet bullet = this;

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for(Platform platform : map.getPlatforms()) {
                    if(bullet.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        javafx.application.Platform.runLater(() -> {
                            map.getPane().getChildren().remove(bullet);
                        });

                        this.stop();
                    }
                }

                for(Bot bot : map.getBots()) {
                    if(bot != null && bullet.getBoundsInParent().intersects(bot.getBoundsInParent())) {
                        javafx.application.Platform.runLater(() -> {
                            bot.getDamage(damage);
                            map.getPane().getChildren().remove(bullet);
                        });

                        this.stop();
                    }
                }

                for(Gamer gamer : map.getGamers()) {
                    if(gamer != null && bullet.getBoundsInParent().intersects(gamer.getBoundsInParent())
                            && gamer != sender) {
                        javafx.application.Platform.runLater(() -> {
                            gamer.getDamage(damage);
                            map.getPane().getChildren().remove(bullet);
                        });

                        this.stop();
                    }
                }

                for(Opp opp : map.getOpps()) {
                    if(opp != null && bullet.getBoundsInParent().intersects(opp.getBoundsInParent())) {
                        javafx.application.Platform.runLater(() -> {
                            map.getPane().getChildren().remove(bullet);
                        });

                        this.stop();
                    }
                }

                javafx.application.Platform.runLater(() -> {
                    if(isRight) {
                        if(getX() <= map.getStageWidth()) {
                            setX(getX() + 20);
                        } else {
                            map.getPane().getChildren().remove(bullet);
                            this.stop();
                        }

                    } else {
                        if(getX() + width >= 0) {
                            setX(getX() - 20);
                        } else {
                            map.getPane().getChildren().remove(bullet);
                            this.stop();
                        }
                    }
                });
            }
        };

        animationTimer.start();
    }

    public synchronized int getDamage() {
        return damage;
    }
}
