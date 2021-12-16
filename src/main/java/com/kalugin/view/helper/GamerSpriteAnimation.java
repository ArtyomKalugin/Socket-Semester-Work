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
    private static int count;
    private static int columns;
    private static int offsetX;
    private static int offsetY;
    private static int width;
    private static int height;
    private static int index;
    private static final GameMap map = GameMap.getInstance();

    public GamerSpriteAnimation(int count, int columns, int offsetX, int offsetY, int width,
                                int height, Duration duration) {
        GamerSpriteAnimation.count = count;
        GamerSpriteAnimation.columns = columns;
        GamerSpriteAnimation.offsetX = offsetX;
        GamerSpriteAnimation.offsetY = offsetY;
        GamerSpriteAnimation.width = width;
        GamerSpriteAnimation.height = height;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);

        Image image = new Image(new File("src/main/resources/shooter.png").toURI().toString());
        imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, 53, 94));
        imageView.setY(0);
        imageView.setX(0);

        javafx.application.Platform.runLater(() -> map.getPane().getChildren().add(imageView));
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

    public synchronized void delete() {
        javafx.application.Platform.runLater(() -> map.getPane().getChildren().remove(imageView));
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

        index = Math.min((int) Math.floor(0), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));

        this.stop();
    }

    @Override
    protected synchronized void interpolate(double v) {
        index = Math.min((int) Math.floor(v * count), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + offsetY;

        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }

    public synchronized void setParameters(int spriteIndex, int spriteColumns, int spriteWidth, int spriteHeight,
                                           int spriteOffsetX, int spriteOffsetY) {
        javafx.application.Platform.runLater(() -> {
            int x = (spriteIndex % spriteColumns) * spriteWidth + spriteOffsetX;
            int y = (spriteIndex / spriteColumns) * spriteHeight + spriteOffsetY;

            imageView.setViewport(new Rectangle2D(x, y, spriteWidth, spriteHeight));
        });
    }

    public synchronized int getWidth() {
        return width;
    }

    public synchronized int getHeight() {
        return height;
    }

    public synchronized int getColumns() {
        return columns;
    }

    public synchronized int getOffsetX() {
        return offsetX;
    }

    public synchronized int getOffsetY() {
        return offsetY;
    }

    public synchronized int getIndex() {
        return index;
    }
}
