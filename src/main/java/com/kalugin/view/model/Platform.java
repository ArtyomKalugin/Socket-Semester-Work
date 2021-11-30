package com.kalugin.view.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform extends Rectangle {
    private final double width;
    private final double height;

    public Platform(double x, double y, double width, double height, boolean isFilled) {
        super(x, y, width, height);

        this.height = height;
        this.width = width;

        if(isFilled) {
            setFill(Color.GRAY);
        }
    }
}