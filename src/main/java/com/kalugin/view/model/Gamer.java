package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import com.kalugin.view.helper.GamerSpriteAnimation;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

public class Gamer extends Rectangle implements EventHandler<KeyEvent> {
    private final double width;
    private final double height;
    private boolean isLeftCollision;
    private boolean isRightCollision;
    private boolean isBottomCollision;
    private boolean isTopCollision;
    private boolean isJumping;
    private double gravity = 0;
    private final int step = 10;
    private final GameMap map = GameMap.getInstance();
    private boolean isMoving;
    private boolean canShoot;
    private double hp;
    private boolean isDead;
    private final Text hpLabel;
    private GamerSpriteAnimation gamerAnimation;
    private boolean isFallen;
    private int fallDamage = 0;

    public Gamer(double x, double y, double width, double height, Text hpLabel) {
        super(x, y, width, height);
        this.height = height;
        this.width = width;
        isLeftCollision = false;
        isRightCollision = false;
        isBottomCollision = false;
        isMoving = false;
        canShoot = true;
        hp = 100;
        isDead = false;
        this.hpLabel = hpLabel;
        isFallen = false;

        setFill(Color.TRANSPARENT);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkBottomCollision();

                if(!isBottomCollision && !isJumping){
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
                hpLabel.setY(getY() - 5);
            }
        };
        animationTimer.start();
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

    public void setGamerAnimation(GamerSpriteAnimation gamerAnimation) {
        this.gamerAnimation = gamerAnimation;
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

    private void jump() {
        if(gravity == 0) {
            double previousY = this.getY();
            gamerAnimation.changeTurnToFall(getNodeOrientation());
            gamerAnimation.play();

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
                        setY(getY() - 17 + gravity);
                        gravity += 1;
                        gamerAnimation.setY(getY());
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
                        gamerAnimation.stopAnimation(getNodeOrientation());

                        if(platform != null) {
                            setY(platform.getY() - getHeight());
                        }
                    }

                    if(previousY <= getY()) {
                        this.stop();
                        gamerAnimation.stopAnimation(getNodeOrientation());
                        gravity = 0;
                        isJumping = false;
                    }

                    checkRightCollision();
                    if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)
                            && isMoving) {
                        if(((getX() + width) <= map.getStageWidth()) && !isRightCollision){
                            setX(getX() + step - 4);
                            gamerAnimation.setX(getX());
                        }
                    }

                    checkLeftCollision();
                    if(getNodeOrientation().equals(NodeOrientation.RIGHT_TO_LEFT)
                            && isMoving) {
                        if(((getX() - step) >= 0) && !isLeftCollision) {
                            setX(getX() - step + 4);
                            gamerAnimation.setX(getX());
                        }
                    }

                    hpLabel.setX(getX());
                    hpLabel.setY(getY() - 5);
                }
            };

            jumpTimer.start();
        }
    }

    private void moveToLeft() {
        final int[] distance = {0};
        gamerAnimation.changeTurnToLeft();
        gamerAnimation.play();

        AnimationTimer move = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(distance[0] <= step) {
                    checkLeftCollision();

                    if(((getX() - step) >= 0) && !isLeftCollision){
                        setX(getX() - 1);
                        gamerAnimation.setX(getX());
                        gamerAnimation.setY(getY());
                        setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    } else {
                        stop();
                    }
                } else {
                    stop();
                }

                distance[0] += 1;
            }
        };

        move.start();
        hpLabel.setX(getX());
        hpLabel.setY(getY() - 5);
    }

    private void moveToRight() {
        final int[] distance = {0};
        gamerAnimation.changeTurnToRight();
        gamerAnimation.play();

        AnimationTimer move = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(distance[0] <= step) {
                    checkRightCollision();

                    if(((getX() + width) <= map.getStageWidth()) && !isRightCollision){
                        setX(getX() + 1);
                        gamerAnimation.setX(getX());
                        gamerAnimation.setY(getY());
                        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    } else {
                        stop();
                    }
                } else {
                    stop();
                }

                distance[0] += 1;
            }
        };

        move.start();
        hpLabel.setX(getX());
        hpLabel.setY(getY() - 5);
    }

    public void getDamage(double damage) {
        hp -= damage;
        hpLabel.setText(String.valueOf(hp));

        if(hp <= 0) {
            map.deleteGamer(this);
            isDead = true;
            hpLabel.setText("");
            gamerAnimation.delete();
        }
    }

    private void shoot() {
        if(canShoot && !isDead) {
            canShoot = false;
            Bullet bullet;

            if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
                bullet = new Bullet(getX() + width + 1, getY() + 30, true, this);
            } else {
                bullet = new Bullet(getX() - 30 - 1, getY() + 30, false, this);
            }
            map.getPane().getChildren().add(bullet);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                shoot();
                break;
            case LEFT:
                isMoving = true;
                moveToLeft();
                break;
            case RIGHT:
                isMoving = true;
                moveToRight();
                break;
            case UP:
                jump();
                break;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                canShoot = true;
                break;
            case LEFT:
            case RIGHT:
                isMoving = false;
                gamerAnimation.stopAnimation(getNodeOrientation());
                break;
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().getName().equals("KEY_PRESSED")){
            handleKeyPressed(event);
        } else if(event.getEventType().getName().equals("KEY_RELEASED")) {
            handleKeyReleased(event);
        }
    }
}