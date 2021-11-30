package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.NodeOrientation;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Gamer extends Rectangle implements EventHandler<KeyEvent> {
    private final double width;
    private final double height;
    private boolean isLeftCollision;
    private boolean isRightCollision;
    private boolean isBottomCollision;
    private boolean isTopCollision;
    private boolean isJumping;
    private double gravity = 0;
    private final int step = 7;
    private final GameMap map = GameMap.getInstance();
    private boolean isMoving;

    public Gamer(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.height = height;
        this.width = width;
        isLeftCollision = false;
        isRightCollision = false;
        isBottomCollision = false;
        isMoving = false;

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
                    if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)
                            && isMoving) {
                        if(((getX() + width) <= map.getStageWidth()) && !isRightCollision){
                            setX(getX() + step);
                        }
                    }

                    checkLeftCollision();
                    if(getNodeOrientation().equals(NodeOrientation.RIGHT_TO_LEFT)
                            && isMoving) {
                        if(((getX() - step) >= 0) && !isLeftCollision) {
                            setX(getX() - step);
                        }
                    }
                }
            };

            jumpTimer.start();
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                Bullet bullet;

                if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
                    bullet = new Bullet(getX() + width, getY() + (height / 2), true);
                } else {
                    bullet = new Bullet(getX(), getY() + (height / 2), false);
                }
                map.getPane().getChildren().add(bullet);
                break;

            case LEFT:
                checkLeftCollision();
                if(((this.getX() - step) >= 0) && !isLeftCollision){
                    this.setX(this.getX() - step);
                    setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    isMoving = true;
                }
                break;
            case RIGHT:
                checkRightCollision();
                if(((this.getX() + width) <= map.getStageWidth()) && !isRightCollision){
                    this.setX(this.getX() + step);
                    setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    isMoving = true;
                }
                break;
            case UP:
                jump();
                break;
//                case DOWN:
//                    checkBottomCollision();
//                    if(((this.getY() + height + step) <= stageHeight) && !isBottomCollision) {
//                        this.setY(this.getY() + step);
//                    }
//                    break;
        }
    }

    private void handleKeyReleased() {
        isMoving = false;
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().getName().equals("KEY_PRESSED")){
            handleKeyPressed(event);
        } else if(event.getEventType().getName().equals("KEY_RELEASED")) {
            handleKeyReleased();
        }
    }
}