package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.animation.AnimationTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.NodeOrientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Bot extends Rectangle {
    private final double width;
    private final double height;
    private boolean isLeftCollision;
    private boolean isRightCollision;
    private boolean isBottomCollision;
    private boolean isTopCollision;
    private final GameMap map = GameMap.getInstance();
    private double hp;
    private Text hpLabel;

    public Bot(double x, double y, double width, double height, Text hpLabel) {
        super(x, y, width, height);
        this.height = height;
        this.width = width;
        isLeftCollision = false;
        isRightCollision = false;
        isBottomCollision = false;
        hp = 100;
        this.hpLabel = hpLabel;

        setFill(Color.BLACK);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkBottomCollision();

                if(!isBottomCollision){
                    setY(getY() + 15);
                }

                if(isBottomCollision) {
                    Platform platform = getBottomCollisionPlatform();

                    if(platform != null) {
                        setY(platform.getY() - getHeight());
                    }
                }

                hpLabel.setX(getX());
                hpLabel.setY(getY());
            }
        };
        animationTimer.start();
    }

    private void shoot() {
        Bullet bullet;

        if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
            bullet = new Bullet(getX() + width + 1, getY() + (height / 2), true);
        } else {
            bullet = new Bullet(getX() - 30 - 1, getY() + (height / 2), false);
        }
        map.getPane().getChildren().add(bullet);

    }

    private void checkRightCollision() {
        BoundingBox rightBox = new BoundingBox(this.getX() + width, this.getY(),
                0.1, height - 0.1);

        for(Platform platform : map.getPlatforms()) {
            isRightCollision = rightBox.intersects(platform.getBoundsInParent());

            if(isRightCollision) {
                break;
            }
        }
    }

    public void getDamage(double damage) {
        hp -= damage;
        hpLabel.setText(String.valueOf(hp));
        setFill(Color.RED);
        shoot();

        if(hp <= 0) {
            map.deleteBot(this);
            hpLabel.setText("");
        }
    }

    private void checkLeftCollision() {
        BoundingBox leftBox = new BoundingBox(this.getX() - 0.1, this.getY(),
                0.1, height - 0.1);

        for(Platform platform : map.getPlatforms()) {
            isLeftCollision = leftBox.intersects(platform.getBoundsInParent());

            if(isLeftCollision) {
                break;
            }
        }
    }

    private void checkBottomCollision() {
        BoundingBox bottomBox = new BoundingBox(this.getX(), this.getY() + height,
                width, 0.1);

        for(Platform platform : map.getPlatforms()) {
            isBottomCollision = bottomBox.intersects(platform.getBoundsInParent());

            if(isBottomCollision) {
                break;
            }
        }
    }

    private void checkTopCollision() {
        BoundingBox topBox = new BoundingBox(this.getX(), this.getY() - 0.1,
                width, 0.1);

        for(Platform platform : map.getPlatforms()) {
            isTopCollision = topBox.intersects(platform.getBoundsInParent());

            if(isTopCollision) {
                break;
            }
        }
    }

    private Platform getBottomCollisionPlatform() {
        BoundingBox bottomBox = new BoundingBox(this.getX(), this.getY() + height,
                width, 0.1);

        for(Platform platform : map.getPlatforms()) {
            isBottomCollision = bottomBox.intersects(platform.getBoundsInParent());

            if(isBottomCollision) {
                return platform;
            }
        }

        return null;
    }
}
