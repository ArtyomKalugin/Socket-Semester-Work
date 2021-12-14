package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import com.kalugin.view.helper.GamerSpriteAnimation;
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
    private final Text nameLabel;
    private GamerSpriteAnimation gamerAnimation;
    private boolean isFallen;
    private int fallDamage = 0;

    public Bot(double x, double y, double width, double height, Text hpLabel, Text nameLabel) {
        super(x, y, width, height);
        this.height = height;
        this.width = width;
        isLeftCollision = false;
        isRightCollision = false;
        isBottomCollision = false;
        hp = 100;
        this.hpLabel = hpLabel;
        isFallen = false;
        this.nameLabel = nameLabel;

        setFill(Color.TRANSPARENT);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkBottomCollision();

                if(!isBottomCollision){
                    if(!isFallen) {
                        gamerAnimation.changeTurnToFall(getNodeOrientation());
                        gamerAnimation.play();
                        isFallen = true;
                    }

                    setY(getY() + 15);
                    fallDamage++;
                    gamerAnimation.setX(getX());
                    gamerAnimation.setY(getY());
                }

                if(isBottomCollision) {
                    Platform platform = getBottomCollisionPlatform();

                    if(platform != null) {
                        setY(platform.getY() - getHeight());

                        if(isFallen) {
                            isFallen = false;
                            gamerAnimation.stopAnimation(getNodeOrientation());
                            getDamage(fallDamage / 10);
                            fallDamage = 0;
                        }
                    }
                }

                hpLabel.setX(getX());
                hpLabel.setY(getY());

                nameLabel.setX(getX());
                nameLabel.setY(getY() - 20);
            }
        };
        animationTimer.start();
    }

    private void shoot() {
        Bullet bullet;

        if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
            bullet = new Bullet(getX() + width + 1, getY() + 30, true, null);
        } else {
            bullet = new Bullet(getX() - 30 - 1, getY() + 30, false, null);
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
        shoot();

        if(hp <= 0) {
            map.deleteBot(this);
            hpLabel.setText("");
            nameLabel.setText("");
            map.getPane().getChildren().remove(nameLabel);
            gamerAnimation.delete();
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

    public void setGamerAnimation(GamerSpriteAnimation gamerAnimation) {
        this.gamerAnimation = gamerAnimation;
    }
}
