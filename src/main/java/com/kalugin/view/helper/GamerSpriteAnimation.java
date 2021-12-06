package com.kalugin.view.helper;

import com.kalugin.view.GameMap;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;

public class GamerSpriteAnimation extends Transition {
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private final GameMap map = GameMap.getInstance();

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

        Image IMAGE = new Image(new File("src/main/resources/shooter.png").toURI().toString());
        imageView = new ImageView(IMAGE);
        imageView.setViewport(new Rectangle2D(0, 0, 53, 94));
        imageView.setY(0);
        imageView.setX(0);
        map.getPane().getChildren().add(imageView);
    }

    public void setX(double x) {
        imageView.setX(x);
    }

    public void setY(double y) {
        imageView.setY(y);
    }

    @Override
    protected void interpolate(double v) {
        int index = Math.min((int) Math.floor(v * count), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + offsetY;

        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
