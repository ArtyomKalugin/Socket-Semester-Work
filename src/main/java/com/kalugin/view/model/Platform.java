package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public class Platform extends Rectangle {
    private final double width;
    private final double height;
    private GameMap map = GameMap.getInstance();

    public Platform(double x, double y, double width, double height){
        super(x, y, width, height);

        this.height = height;
        this.width = width;


        setFill(new ImagePattern(new Image(new File("src/main/resources/grass.png").toURI().toString())));

//        Image source = new Image("grass.png");
//        ImageView block = new ImageView(source);
//        block.setFitHeight(height);
//        block.setFitWidth(width);
//        block.setX(x);
//        block.setY(y);
//        block.setViewport(new Rectangle2D(0, 0, 370, 370));

//        map.getPane().getChildren().add(block);
    }
}