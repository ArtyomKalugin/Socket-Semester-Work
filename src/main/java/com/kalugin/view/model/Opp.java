package com.kalugin.view.model;

import com.kalugin.view.GameMap;
import com.kalugin.view.helper.GamerSpriteAnimation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Opp extends Rectangle {
    private final GameMap map = GameMap.getInstance();
    private double hp;
    private boolean isDead;
    private final Text hpLabel;
    private final Text nameLabel;
    private GamerSpriteAnimation gamerAnimation;
    private String name;

    public Opp(Text hpLabel, Text nameLabel, String name) {
        super(0, 0, 30, 94);
        hp = 100;
        this.hpLabel = hpLabel;
        this.nameLabel = nameLabel;
        this.name = name;

//        setFill(Color.TRANSPARENT);
        setFill(Color.BLACK);
    }

    public String getName() {
        return name;
    }

    public void setGamerAnimation(GamerSpriteAnimation gamerAnimation) {
        this.gamerAnimation = gamerAnimation;
    }
}
