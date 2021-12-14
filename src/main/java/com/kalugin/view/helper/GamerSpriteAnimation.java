package com.kalugin.view.helper;

import com.kalugin.view.GameMap;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;

public class GamerSpriteAnimation extends Transition {
    private final ImageView imageView;
    private int count;
    private int columns;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
    private final GameMap map = GameMap.getInstance();
    private int index;
    private int x;
    private int y;

    public GamerSpriteAnimation(int count, int columns, int offsetX, int offsetY, int width,
                           int height, Duration duration) {
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);

        Image image = new Image(new File("src/main/resources/shooter.png").toURI().toString());
        imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, 53, 94));
        imageView.setY(0);
        imageView.setX(0);

        javafx.application.Platform.runLater(() -> {
            map.getPane().getChildren().add(imageView);
        });
    }

    public void changeTurnToRight() {
        imageView.setViewport(new Rectangle2D(0, 0, 53, 94));
        width = 53;
        height = 94;
        offsetY = 100;
        offsetX = 53;
        count = 5;
        columns = 5;
    }

    public void changeTurnToLeft() {
        imageView.setViewport(new Rectangle2D(0, 0, 53, 94));
        width = 53;
        height = 94;
        offsetY = 0;
        offsetX = 53;
        count = 5;
        columns = 5;
    }

    public void delete() {
       map.getPane().getChildren().remove(imageView);
    }

    public void changeTurnToFall(NodeOrientation orientation) {
        imageView.setViewport(new Rectangle2D(0, 0, 50, 50));
        width = 43;
        height = 54;
        offsetX = 0;
        count = 4;
        columns = 4;

        if(orientation.equals(NodeOrientation.RIGHT_TO_LEFT)) {
            offsetY = 200;
        } else {
            offsetY = 250;
        }
    }

    public void setX(double x) {
        imageView.setX(x);
    }

    public void setY(double y) {
        imageView.setY(y);
    }

    public void stopAnimation(NodeOrientation orientation) {
        if(orientation.equals(NodeOrientation.RIGHT_TO_LEFT)) {
            offsetY = 0;
        } else {
            offsetY = 100;
        }

        offsetX = 0;
        count = 6;
        columns = 6;
        width = 53;
        height = 94;

        int index = Math.min((int) Math.floor(0), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));

        this.stop();
    }

    @Override
    protected void interpolate(double v) {
        index = Math.min((int) Math.floor(v * count), count - 1);
        x = (index % columns) * width + offsetX;
        y = (index / columns) * height + offsetY;

        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }

    public void setParameters(int x, int y) {
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
