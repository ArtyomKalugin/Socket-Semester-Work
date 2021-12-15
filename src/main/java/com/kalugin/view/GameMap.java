package com.kalugin.view;

import com.kalugin.client.GameClient;
import com.kalugin.view.helper.GamerSpriteAnimation;
import com.kalugin.view.model.Bot;
import com.kalugin.view.model.Gamer;
import com.kalugin.view.model.Opp;
import com.kalugin.view.model.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameMap {
    private final int stageWidth = 1450;
    private final int stageHeight = 900;
    private CopyOnWriteArrayList<Platform> platforms = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Gamer> gamers = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Bot> bots = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<Opp> opps = new CopyOnWriteArrayList<>();
    private final Pane pane = new Pane();
    private static GameMap gameMap = new GameMap();
    private Stage stage;
    private Scene scene;
    private String name;
    private GameClient gameClient;
    private final Font font = Font.font("Courier New", FontWeight.BOLD, 20);


    private void configureMultiPlayer() throws IOException {
        gameClient = new GameClient();
        gameClient.start();

        Text hpLabel = new Text(0, -5, "100");
        Text nameLabel = new Text(0, -10, name);
        nameLabel.setFont(font);
        hpLabel.setFont(font);
        Gamer gamer = new Gamer(0, 0, 30, 94, hpLabel, nameLabel);

        Image backgroundImg = new Image(new File("src/main/resources/background.png").toURI().toString());
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(stageHeight);
        backgroundIV.setFitWidth(stageWidth);

        pane.getChildren().add(backgroundIV);
        pane.getChildren().add(hpLabel);
        pane.getChildren().add(nameLabel);

        setGamer(gamer);

        scene = new Scene(pane, stageWidth, stageHeight);

        for(Gamer g : gamers) {
            scene.setOnKeyPressed(g);
            scene.setOnKeyReleased(g);
        }

        stage.setScene(scene);
        readMap();
        stage.show();
        stage.setFullScreen(true);

        String message = "new " + name + "\n";
        gameClient.sendMessage(message);
    }

    private void configureSinglePlayer() throws IOException {
        gameClient = new GameClient();
        gameClient.start();

        Text hpLabel = new Text(0, -5, "100");
        Text nameLabel = new Text(0, -10, name);
        nameLabel.setFont(font);
        hpLabel.setFont(font);
        Gamer gamer = new Gamer(0, 0, 30, 94, hpLabel, nameLabel);

        Text hpLabel2 = new Text(0, -5, "100");
        Text botNameLabel = new Text(0, -10, "Bot");
        botNameLabel.setFont(font);
        hpLabel2.setFont(font);
        Bot bot = new Bot(0, 0, 30, 94, hpLabel2, botNameLabel);

        Image backgroundImg = new Image(new File("src/main/resources/background.png").toURI().toString());
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(stageHeight);
        backgroundIV.setFitWidth(stageWidth);

        pane.getChildren().add(backgroundIV);
        pane.getChildren().add(hpLabel);
        pane.getChildren().add(hpLabel2);
        pane.getChildren().add(nameLabel);
        pane.getChildren().add(botNameLabel);

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
        stage.setFullScreen(true);
    }

    public int getStageWidth() {
        return stageWidth;
    }

    public int getStageHeight() {
        return stageHeight;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Pane getPane() {
        return pane;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGamer(Gamer gamer) {
        GamerSpriteAnimation gamerAnimation = new GamerSpriteAnimation(6, 6, 0, 100,
                53, 94, Duration.millis(380));
        gamer.setGamerAnimation(gamerAnimation);
        pane.getChildren().add(gamer);
        gamers.add(gamer);
    }

    public void setBot(Bot bot) {
        GamerSpriteAnimation gamerAnimation = new GamerSpriteAnimation(6, 6, 0, 100,
                53, 94, Duration.millis(380));
        bot.setGamerAnimation(gamerAnimation);
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

    public List<Gamer> getGamers() {
        return gamers;
    }

    public List<Bot> getBots() {
        return bots;
    }

    public void setStage(Stage stage) throws IOException {
        this.stage = stage;
    }

    public void startSinglePlayer() throws IOException {
        configureSinglePlayer();
    }

    public void startMultiPlayer() throws IOException {
        configureMultiPlayer();
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

    public synchronized void createNewOpp(String oppName) {
        boolean isFound = false;

        for(Opp opp : opps) {
            if (opp.getName().equals(oppName)) {
                isFound = true;
            }
        }

        if(!isFound) {
            Text hpLabel = new Text(0, -5, "100");
            Text nameLabel = new Text(0, -10, oppName);
            nameLabel.setFont(font);
            hpLabel.setFont(font);
            GamerSpriteAnimation gamerAnimation = new GamerSpriteAnimation(6, 6, 0, 100,
                    53, 94, Duration.millis(380));
            Opp opp = new Opp(hpLabel, nameLabel, oppName);
            opp.setGamerAnimation(gamerAnimation);

            javafx.application.Platform.runLater(() -> {
                opps.add(opp);
                pane.getChildren().add(opp);
                pane.getChildren().add(hpLabel);
                pane.getChildren().add(nameLabel);
            });
        }
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public synchronized void moveOpp(String oppName, double x, double y, int animationIndex, int animationColumns,
                                     int animationWidth, int animationHeight, int animationOffsetX, int animationOffsetY) {
        boolean isFound = false;

        for (Opp opp : opps) {
            if (opp.getName().equals(oppName)) {
                opp.setX(x);
                opp.setY(y);
                opp.changeAnimation(animationIndex, animationColumns, animationWidth, animationHeight,
                        animationOffsetX, animationOffsetY);
                isFound = true;
            }
        }

        if (!isFound) {
            createNewOpp(oppName);
        }
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public void startGameClient() throws IOException {
        getGameClient().start();
    }

    public String getName() {
        return name;
    }

    public List<Opp> getOpps() {
        return opps;
    }
}
