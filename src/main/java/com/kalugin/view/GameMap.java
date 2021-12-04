package com.kalugin.view;

import com.kalugin.view.model.Bot;
import com.kalugin.view.model.Gamer;
import com.kalugin.view.model.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameMap {
    private final int stageWidth = 1450;
    private final int stageHeight = 900;
    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<Gamer> gamers = new ArrayList<>();
    private ArrayList<Bot> bots = new ArrayList<>();
    private final Pane pane = new Pane();
    private static GameMap gameMap = new GameMap();
    private Stage stage;
    private Scene scene;

    private void configure() {
        Platform platform1 = new Platform(80, 650, 200, 50);
        Platform platform2 = new Platform(800, 650, 200, 50);
        Platform platform3 = new Platform(350, 540, 500, 50);
        Platform grass = new Platform(0, stageHeight - 50, stageWidth, 50);

        Text hpLabel = new Text(0, -5, "100");
        Gamer gamer = new Gamer(0, 0, 50, 50, hpLabel);
        Text hpLabel2 = new Text(0, -5, "100");
        Bot bot = new Bot(0, 0, 50, 50, hpLabel2);

        setGamer(gamer);
        setBot(bot);

        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);
        platforms.add(grass);

        pane.getChildren().add(platform1);
        pane.getChildren().add(platform2);
        pane.getChildren().add(platform3);
        pane.getChildren().add(grass);
        pane.getChildren().add(hpLabel);
        pane.getChildren().add(hpLabel2);

       scene = new Scene(pane, stageWidth, stageHeight);

        for(Gamer g : gamers) {
            scene.setOnKeyPressed(g);
            scene.setOnKeyReleased(g);
        }

        stage.setScene(scene);
        stage.show();
    }

    public int getStageWidth() {
        return stageWidth;
    }

    public int getStageHeight() {
        return stageHeight;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public Pane getPane() {
        return pane;
    }

    public void setGamer(Gamer gamer) {
        pane.getChildren().add(gamer);
        gamers.add(gamer);
    }

    public void setBot(Bot bot) {
        pane.getChildren().add(bot);
        bots.add(bot);
    }

    public void deleteBot(Bot bot) {
        pane.getChildren().remove(bot);
        bots.remove(bot);
    }

    public void deleteGamer(Gamer gamer) {
        pane.getChildren().remove(gamer);
        gamers.remove(gamer);
    }

    public static GameMap getInstance() {
        return gameMap;
    }

    public ArrayList<Gamer> getGamers() {
        return gamers;
    }

    public ArrayList<Bot> getBots() {
        return bots;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        configure();
    }
}
