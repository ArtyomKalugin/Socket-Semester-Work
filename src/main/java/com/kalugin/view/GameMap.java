package com.kalugin.view;

import com.kalugin.view.model.Gamer;
import com.kalugin.view.model.Platform;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class GameMap {
    private final int stageWidth = 1200;
    private final int stageHeight = 900;
    private ArrayList<Platform> platforms = new ArrayList<>();
    private final Pane pane = new Pane();
    private static GameMap gameMap = new GameMap();

    private void configure() {
        Platform platform1 = new Platform(80, 650, 200, 20, true);
        Platform platform2 = new Platform(800, 650, 200, 20, true);
        Platform platform3 = new Platform(350, 540, 500, 20, true);
        Platform grass = new Platform(0, 745, stageWidth, 20, true);

        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);
        platforms.add(grass);

        pane.getChildren().add(platform1);
        pane.getChildren().add(platform2);
        pane.getChildren().add(platform3);
        pane.getChildren().add(grass);
    }

    public int getStageWidth() {
        return stageWidth;
    }

    public int getStageHeight() {
        return stageHeight;
    }

    public ArrayList<Platform> getPlatforms() {
        if(platforms.size() == 0) {
            configure();
        }

        return platforms;
    }

    public Pane getPane() {
        return pane;
    }

    public void setGamer(Gamer gamer) {
        pane.getChildren().add(gamer);
    }

    public static GameMap getInstance() {
        return gameMap;
    }

}
