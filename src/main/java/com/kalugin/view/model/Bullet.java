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

    public Bullet(double x, double y, boolean isRight, Gamer sender) {
        super(x, y, width, height);
        setFill(Color.YELLOW);
        this.isRight = isRight;
        this.sender = sender;

        Bullet bullet = this;
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Random rn = new Random();

                for(Platform platform : map.getPlatforms()) {
                    if(bullet.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        map.getPane().getChildren().remove(bullet);
                        this.stop();
                    }
                }

                for(Bot bot : map.getBots()) {
                    if(bot != null && bullet.getBoundsInParent().intersects(bot.getBoundsInParent())) {
                        bot.getDamage(10 + rn.nextInt(11));
                        map.getPane().getChildren().remove(bullet);
                        this.stop();
                    }
                }

                for(Gamer gamer : map.getGamers()) {
                    if(gamer != null && bullet.getBoundsInParent().intersects(gamer.getBoundsInParent())
                            && gamer != sender) {
                        gamer.getDamage(10 + rn.nextInt(11));
                        map.getPane().getChildren().remove(bullet);
                        this.stop();
                    }
                }

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
            }
        };

        animationTimer.start();
    }
}
