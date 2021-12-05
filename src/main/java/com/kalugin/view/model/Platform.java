package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class Platform extends Rectangle {
    private final double width;
    private final double height;
    private GameMap map = GameMap.getInstance();

    public Platform(double x, double y, double width, double height){
        super(x, y, width, height);

        this.height = height;
        this.width = width;

        setFill(new ImagePattern(new Image(new File("src/main/resources/grass.png").toURI().toString())));
    }
}