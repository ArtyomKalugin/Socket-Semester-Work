package com.kalugin.view;

import com.kalugin.view.helper.GamerSpriteAnimation;
import com.kalugin.view.model.Bot;
import com.kalugin.view.model.Gamer;
import com.kalugin.view.model.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
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
        Text hpLabel = new Text(0, -5, "100");
        Gamer gamer = new Gamer(0, 0, 50, 50, hpLabel);
        Text hpLabel2 = new Text(0, -5, "100");
        Bot bot = new Bot(0, 0, 50, 50, hpLabel2);

        Image backgroundImg = new Image(new File("src/main/resources/background.png").toURI().toString());
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(stageHeight);
        backgroundIV.setFitWidth(stageWidth);

        pane.getChildren().add(backgroundIV);
        pane.getChildren().add(hpLabel);
        pane.getChildren().add(hpLabel2);

        setGamer(gamer);
        setBot(bot);

        scene = new Scene(pane, stageWidth, stageHeight);

        for(Gamer g : gamers) {
            scene.setOnKeyPressed(g);
            scene.setOnKeyReleased(g);
        }

        stage.setScene(scene);
        readMap();
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
        GamerSpriteAnimation gamerAnimation = new GamerSpriteAnimation(6, 6, 0, 100,
                53, 94, Duration.millis(450));
        gamer.setGamerAnimation(gamerAnimation);
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

    private void readMap() {
        try {
            int i = 0;
            File map = new File("src/main/resources/map.txt");
            FileReader fr = new FileReader(map);
            BufferedReader reader = new BufferedReader(fr);
            char[][] result = new char[30][15];

            String line = reader.readLine();
            while (line != null) {
                char[] objects = line.toCharArray();
                result[i] = objects;
                line = reader.readLine();
                i++;
            }

            buildMap(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildMap(char[][] line) {
        for (int i = 0; i < line.length; i++) {
            for (int j = 0; j < line[i].length; j++) {
                if(line[i][j] == '2') {
                    Platform platform = new Platform(j * 100, (i + 1) * 30, 100, 30);
                    platforms.add(platform);
                    pane.getChildren().add(platform);
                }
            }
        }
    }

}
