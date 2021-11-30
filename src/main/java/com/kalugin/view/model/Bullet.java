package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private final static double width = 30;
    private final static double height = 10;
    private final boolean isRight;
    private GameMap map = GameMap.getInstance();

    public Bullet(double x, double y, boolean isRight) {
        super(x, y, width, height);
        setFill(Color.YELLOW);
        this.isRight = isRight;

        Bullet bullet = this;
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for(Platform platform : map.getPlatforms()) {
                    if(bullet.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        this.stop();
                        map.getPane().getChildren().remove(bullet);
                    }
                }

                if(isRight) {
                    if(getX() <= map.getStageWidth()) {
                        setX(getX() + 15);
                    } else {
                        this.stop();
                        map.getPane().getChildren().remove(bullet);
                    }
                } else {
                    if(getX() >= 0) {
                        setX(getX() - 15);
                    } else {
                        this.stop();
                        map.getPane().getChildren().remove(bullet);
                    }
                }
            }
        };

        animationTimer.start();
    }
}
