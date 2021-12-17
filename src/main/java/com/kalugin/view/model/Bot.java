package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import com.kalugin.view.helper.GamerSpriteAnimation;
import javafx.animation.AnimationTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.NodeOrientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;


public class Bot extends Rectangle {
    private final double width;
    private final double height;
    private boolean isLeftCollision;
    private boolean isRightCollision;
    private boolean isBottomCollision;
    private boolean isTopCollision;
    private final GameMap map = GameMap.getInstance();
    private double hp;
    private double gravity = 0;
    private final int step = 10;
    private final Text hpLabel;
    private final Text nameLabel;
    private GamerSpriteAnimation gamerAnimation;
    private boolean isFallen;
    private boolean isDead;
    private boolean isMoving;
    private boolean isJumping;

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
        isDead = false;
        isMoving = false;
        isJumping = false;

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
                        }
                    }
                }

                hpLabel.setX(getX());
                hpLabel.setY(getY());

                nameLabel.setX(getX());
                nameLabel.setY(getY() - 20);

                Random rn = new Random();
                int action = rn.nextInt(50);

                if (action == 0 && !isDead) {
                    isMoving = true;
                    moveToRight();
                }

                if (action == 1 && !isDead) {
                    isMoving = true;
                    moveToLeft();
                }

                if (action == 2 && !isDead) {
                    shoot();
                }

                if (action == 3 && !isDead) {
                    jump();
                }
            }
        };
        animationTimer.start();
    }

    private void shoot() {
        Bullet bullet;

        if(getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)) {
            bullet = new Bullet(getX() + width + 10, getY() + 30, true, null);
        } else {
            bullet = new Bullet(getX() - 30 - 10, getY() + 30, false, null);
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
            isDead = true;
            map.showWinMenu(map.getGamers().get(0).getName());
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

    private void jump() {
        if (gravity == 0) {
            double previousY = this.getY();
            gamerAnimation.changeTurnToFall(getNodeOrientation());
            gamerAnimation.play();

            AnimationTimer jumpTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    checkBottomCollision();
                    if (isBottomCollision && gravity == 0) {
                        isJumping = true;
                    } else if (!isBottomCollision && gravity == 0) {
                        isJumping = false;
                    }

                    if (isJumping) {
                        setY(getY() - 33 + gravity);
                        gravity += 1;
                        gamerAnimation.setY(getY());
                    }

                    Platform platform = getBottomCollisionPlatform();
                    checkTopCollision();
                    if (isTopCollision) {
                        if (15 > gravity) {
                            gravity += 15 - gravity;
                        }
                    }

                    checkBottomCollision();
                    if (isBottomCollision) {
                        this.stop();
                        gravity = 0;
                        isJumping = false;
                        gamerAnimation.stopAnimation(getNodeOrientation());

                        if (platform != null) {
                            setY(platform.getY() - getHeight());
                        }
                    }

                    if (previousY <= getY()) {
                        this.stop();
                        gamerAnimation.stopAnimation(getNodeOrientation());
                        gravity = 0;
                        isJumping = false;
                    }

                    checkRightCollision();
                    if (getNodeOrientation().equals(NodeOrientation.LEFT_TO_RIGHT)
                            && isMoving) {
                        if (((getX() + width) <= map.getStageWidth()) && !isRightCollision) {
                            setX(getX() + step - 4);
                            gamerAnimation.setX(getX());
                        }
                    }

                    checkLeftCollision();
                    if (getNodeOrientation().equals(NodeOrientation.RIGHT_TO_LEFT)
                            && isMoving) {
                        if (((getX() - step) >= 0) && !isLeftCollision) {
                            setX(getX() - step + 4);
                            gamerAnimation.setX(getX());
                        }
                    }

                    hpLabel.setX(getX());
                    hpLabel.setY(getY() - 5);

                    nameLabel.setX(getX());
                    nameLabel.setY(getY() - 20);

                }
            };

            jumpTimer.start();
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

        nameLabel.setX(getX());
        nameLabel.setY(getY() - 20);

        isMoving = false;
        gamerAnimation.stopAnimation(getNodeOrientation());
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

        nameLabel.setX(getX());
        nameLabel.setY(getY() - 20);

        isMoving = false;
        gamerAnimation.stopAnimation(getNodeOrientation());
    }

    public void setGamerAnimation(GamerSpriteAnimation gamerAnimation) {
        this.gamerAnimation = gamerAnimation;
    }
}
