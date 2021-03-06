package main.java.menus;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import main.java.*;
import main.java.Game.GameMode;
import main.java.configuration.Configuration;
import main.java.net.ClientEngine;
import main.java.net.Event;
import main.java.net.NetworkController;
import main.java.net.ServerEngine;
import main.java.sounds.Sound;

import java.util.Arrays;
import java.util.List;

public class MainMenu {

    private Stage stage;
    private GameLauncher gameLauncher;
    private Game.GameMode gameMode;

    private static Scene scene;
    private static final int WIDTH = Window.WIDTH;
    private static final int HEIGHT = Window.HEIGHT;
    private static final double lineX = WIDTH / 2 - 100;
    private static final double lineY = HEIGHT / 3 + 50;
    private static final int lineHeight = 215;
    private static Configuration<Object> config = new Configuration<Object>();

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;
    private MenuTitle title;

    private String ip = ((String)config.getParam("ip"));
    private Stage controlsStage;
    private VBox controlsBox;
    private Label labelPlayerOne = new Label("CONTROLS PLAYER ONE");
    private Label labelPlayerTwo = new Label("CONTROLS PLAYER TWO");
    private RadioButton radioButtonWASD = new RadioButton("KEYBOARD - WASD");
    private RadioButton radioButtonARROW = new RadioButton("KEYBOARD - ARROWS");
    private RadioButton radioButtonMOUSE = new RadioButton("MOUSE");
    private RadioButton radioButtonWASDtwo = new RadioButton("KEYBOARD - WASD");
    private RadioButton radioButtonARROWtwo = new RadioButton("KEYBOARD - ARROWS");
    private RadioButton radioButtonMOUSEtwo = new RadioButton("MOUSE");
    private RadioButton enabled = new RadioButton("ENABLED");
    private RadioButton disabled = new RadioButton("DISABLED");
    private Button buttonOk = new Button("OK");
    private Button buttonConnect = new Button("CONNECT TO SERVER");
    private ToggleGroup group = new ToggleGroup();
    private ToggleGroup groupTwo = new ToggleGroup();
    private TextField textFieldServer = new TextField(ip);
    private MovementManager.MovementType movementTypePlayer1;
    private MovementManager.MovementType movementTypePlayer2;
    private List<Pair<String, Runnable>> menuData;
    private List<Pair<String, Runnable>> localMenuData;
    private List<Pair<String, Runnable>> hostMenuData;
    private List<Pair<String, Runnable>> clientMenuData;
    private List<Pair<String, Runnable>> pausedMenu;
    private Light.Distant light = new Light.Distant();
    private Lighting lighting = new Lighting(light);
    private Scene gameScene;
    private Separator separator = new Separator();
    private static boolean soundOnBefore;

    /**
     * constructor, inits light and button styles
     * @param stage
     * @param gameLauncher
     */
    public MainMenu(Stage stage, GameLauncher gameLauncher) {

        this.stage = stage;
        this.gameLauncher = gameLauncher;

        light.setAzimuth(0);
        lighting.setSurfaceScale(5.0);
        buttonOk.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0; -fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");
        buttonConnect.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0; -fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");

        setDefaultControls();
    }

    /**
     * data for all menus
     */
    private void loadData() {

        menuData = Arrays.asList(new Pair<String, Runnable>("Play local", () -> {

                Sound.playMenu();
                gameMode = Game.GameMode.LOCAL;
                initMenu(localMenuData, 45);

            }), new Pair<String, Runnable>("Host a Game", () -> {

                Sound.playMenu();
                gameMode = Game.GameMode.REMOTE;
                initMenu(hostMenuData, 45);

            }), new Pair<String, Runnable>("Join a Game", () -> {

                Sound.playMenu();
                gameMode = Game.GameMode.REMOTE;
                initMenu(clientMenuData, 45);

            }), new Pair<String, Runnable>("Highscore", () -> {

                Sound.playMenu();
                new HighScoreGUI().checkScore(new int[]{0}, 0, false);

            }), new Pair<String, Runnable>("Exit to Desktop", Platform::exit));

        localMenuData = Arrays.asList(new Pair<String, Runnable>("Play the Game", () -> {

                gameLauncher.startGame(gameMode, null, movementTypePlayer1, movementTypePlayer2);
                GameMenu.setRightButtons();

            }), new Pair<String, Runnable>("Set Controls", () -> {
                
                Sound.playMenu();
                setControls(gameMode);

            }), new Pair<String, Runnable>("Set Audio", () -> {

                Sound.playMenu();
                setAudio();

            }), new Pair<String, Runnable>("Back to Main menu", () -> {

                Sound.playMenu();
                initMenu(menuData, 0);

            }));

        hostMenuData = Arrays.asList(new Pair<String, Runnable>("Start the Game", () -> {

                new ServerEngine(gameLauncher, stage, movementTypePlayer1);
                GameMenu.setRightButtons();

            }), new Pair<String, Runnable>("Set Controls", () -> {

                Sound.playMenu();
                setControls(gameMode);

            }), new Pair<String, Runnable>("Set Audio", () -> {

                Sound.playMenu();
                setAudio();

            }), new Pair<String, Runnable>("Back to Main menu", () -> {

                Sound.playMenu();
                initMenu(menuData, 0);

            }));

        clientMenuData = Arrays.asList(new Pair<String, Runnable>("Start the Game", () -> {

                setHostname();
                Sound.playMenu();
                GameMenu.setRightButtons();


            }), new Pair<String, Runnable>("Set Controls", () -> {

                Sound.playMenu();
                setControls(gameMode);

            }), new Pair<String, Runnable>("Set Audio", () -> {

                Sound.playMenu();
                setAudio();

            }), new Pair<String, Runnable>("Back to Main menu", () -> {

                Sound.playMenu();
                initMenu(menuData, 0);

            }));


        pausedMenu = Arrays.asList(new Pair<String, Runnable>("Resume", () -> {

            resumeGame(gameLauncher.getGame());

            if(gameLauncher.getGame().gameMode == GameMode.REMOTE) {
                NetworkController networkController = (NetworkController)gameLauncher.getGame().getGameController();
                networkController.changeGameStateObject("", Event.EventType.UNPAUSED);
            }

        }), new Pair<String, Runnable>("Back to Main menu", () -> {

            if(soundOnBefore) Sound.unmuteSound();

            Sound.playMenu();
            root.setOpacity(1.0);
            title.getText().setText("Game menu");
            initMenu(menuData, 0);

            if(gameMode == GameMode.REMOTE) ((NetworkController)gameLauncher.getGame().getGameController()).shutDownNetwork();

        }), new Pair<String, Runnable>("Exit Game", () -> {

            if(gameMode == GameMode.REMOTE) ((NetworkController)gameLauncher.getGame().getGameController()).shutDownNetwork();
            Platform.exit();
        }));
    }

    /**
     * set default controls from config
     */
    private void setDefaultControls() {

        switch ((String) config.getParam("movementTypePlayer1")) {
            case "KEYBOARD_ARROW": 
                movementTypePlayer1 = MovementManager.MovementType.KEYBOARD_ARROW;
                break;
            case "KEYBOARD_AWSD": 
                movementTypePlayer1 = MovementManager.MovementType.KEYBOARD_AWSD;
                break;
            case "MOUSE": 
                movementTypePlayer1 = MovementManager.MovementType.MOUSE;
                break;
            default: 
                break;
        }

        switch ((String) config.getParam("movementTypePlayer2")) {
            case "KEYBOARD_ARROW": 
                movementTypePlayer2 = MovementManager.MovementType.KEYBOARD_ARROW;
                break;
            case "KEYBOARD_AWSD": 
                movementTypePlayer2 = MovementManager.MovementType.KEYBOARD_AWSD;
                break;
            case "MOUSE": 
                movementTypePlayer2 = MovementManager.MovementType.MOUSE;
                break;
            default: 
                break;
        }
    }

    /**
     * set controls modal depending on gamemode, call from menu data
     * @param gameMode
     */
    private void setControls(GameMode gameMode) {

        initStage();

        radioButtonWASD.setToggleGroup(group);
        radioButtonWASD.setStyle("-fx-text-fill: white;");
        radioButtonWASD.setDisable(false);

        radioButtonARROW.setToggleGroup(group);
        radioButtonARROW.setStyle("-fx-text-fill: white;");
        radioButtonARROW.setDisable(false);

        radioButtonMOUSE.setToggleGroup(group);
        radioButtonMOUSE.setStyle("-fx-text-fill: white;");
        radioButtonMOUSE.setDisable(false);

        switch ((String) config.getParam("movementTypePlayer1")) {
            case "KEYBOARD_ARROW": 
                radioButtonARROW.setSelected(true);
                radioButtonARROWtwo.setDisable(true);
                break;
            case "KEYBOARD_AWSD": 
                radioButtonWASD.setSelected(true);
                radioButtonWASDtwo.setDisable(true);
                break;
            case "MOUSE": 
                radioButtonMOUSE.setSelected(true);
                radioButtonMOUSEtwo.setDisable(true);
                break;
            default: 
                break;
        }
  
        controlsBox.setStyle("-fx-background-color: black;");
        controlsBox.setEffect(lighting);
        controlsBox.setAlignment(Pos.CENTER);

        if (gameMode == Game.GameMode.REMOTE) {

            controlsBox.getChildren().addAll(radioButtonWASD, radioButtonARROW, radioButtonMOUSE, buttonOk);

            initScene(controlsBox, 200, 150);

        } else if (gameMode == Game.GameMode.LOCAL) {
        
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
            { 
                public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) 
                { 
                    if ((RadioButton)group.getSelectedToggle() != null) {
                        switch (((RadioButton)group.getSelectedToggle()).getText()) {
                            case "KEYBOARD - ARROWS": 
                                radioButtonARROWtwo.setDisable(true);
                                radioButtonWASDtwo.setDisable(false);
                                radioButtonMOUSEtwo.setDisable(false);
                                break;
                            case "KEYBOARD - WASD": 
                                radioButtonARROWtwo.setDisable(false);
                                radioButtonWASDtwo.setDisable(true);
                                radioButtonMOUSEtwo.setDisable(false);
                                break;
                            case "MOUSE": 
                                radioButtonARROWtwo.setDisable(false);
                                radioButtonWASDtwo.setDisable(false);
                                radioButtonMOUSEtwo.setDisable(true);
                                break;
                            default: 
                                break;
                        }
                    }
                } 
            }); 

            radioButtonWASDtwo.setToggleGroup(groupTwo);
            radioButtonWASDtwo.setStyle("-fx-text-fill: white;");

            radioButtonARROWtwo.setToggleGroup(groupTwo);
            radioButtonARROWtwo.setStyle("-fx-text-fill: white;");

            radioButtonMOUSEtwo.setToggleGroup(groupTwo);
            radioButtonMOUSEtwo.setStyle("-fx-text-fill: white;");

            
            switch ((String) config.getParam("movementTypePlayer2")) {
                case "KEYBOARD_ARROW": 
                    radioButtonARROWtwo.setSelected(true);
                    radioButtonARROW.setDisable(true);
                    break;
                case "KEYBOARD_AWSD": 
                    radioButtonWASDtwo.setSelected(true);
                    radioButtonWASD.setDisable(true);
                    break;
                case "MOUSE": 
                    radioButtonMOUSEtwo.setSelected(true);
                    radioButtonMOUSE.setDisable(true);
                    break;
                default: 
                    break;
            }

            groupTwo.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
            { 
                public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) 
                { 
                    if ((RadioButton)groupTwo.getSelectedToggle() != null) {
                        switch (((RadioButton)groupTwo.getSelectedToggle()).getText()) {
                            case "KEYBOARD - ARROWS": 
                                radioButtonARROW.setDisable(true);
                                radioButtonWASD.setDisable(false);
                                radioButtonMOUSE.setDisable(false);
                                break;
                            case "KEYBOARD - WASD": 
                                radioButtonARROW.setDisable(false);
                                radioButtonWASD.setDisable(true);
                                radioButtonMOUSE.setDisable(false);
                                break;
                            case "MOUSE": 
                                radioButtonARROW.setDisable(false);
                                radioButtonWASD.setDisable(false);
                                radioButtonMOUSE.setDisable(true);
                                break;
                            default: 
                                break;
                        }
                    }
                } 
            }); 
    
            labelPlayerOne.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            labelPlayerTwo.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            controlsBox.getChildren().addAll(labelPlayerOne, radioButtonWASD, radioButtonARROW, radioButtonMOUSE, separator, labelPlayerTwo, radioButtonWASDtwo, radioButtonARROWtwo, radioButtonMOUSEtwo, buttonOk);

            initScene(controlsBox, 200, 300);

        }

        buttonOk.setOnAction((e) -> {

            if (radioButtonARROW.isSelected()) {
                movementTypePlayer1 = MovementManager.MovementType.KEYBOARD_ARROW;
                config.setParam("movementTypePlayer1", "KEYBOARD_ARROW");
            } else if(radioButtonWASD.isSelected()) {
                movementTypePlayer1 = MovementManager.MovementType.KEYBOARD_AWSD;
                config.setParam("movementTypePlayer1", "KEYBOARD_AWSD");
            } else if(radioButtonMOUSE.isSelected()) {
                movementTypePlayer1 = MovementManager.MovementType.MOUSE;
                config.setParam("movementTypePlayer1", "MOUSE");
            }

            if (gameMode == Game.GameMode.LOCAL) {
                if (radioButtonARROWtwo.isSelected()) {
                    movementTypePlayer2 = MovementManager.MovementType.KEYBOARD_ARROW;
                    config.setParam("movementTypePlayer2", "KEYBOARD_ARROW");
                } else if(radioButtonWASDtwo.isSelected()) {
                    movementTypePlayer2 = MovementManager.MovementType.KEYBOARD_AWSD;
                    config.setParam("movementTypePlayer2", "KEYBOARD_AWSD");
                } else if(radioButtonMOUSEtwo.isSelected()) {
                    movementTypePlayer2 = MovementManager.MovementType.MOUSE;
                    config.setParam("movementTypePlayer2", "MOUSE");
                }
            }

            Sound.playMenu();
            controlsStage.close();
        });
    }

    /**
     * set audio modal, called from menu data
     */
    private void setAudio() {

        initStage();

        enabled.setStyle("-fx-text-fill: white;");
        enabled.setToggleGroup(group);
        disabled.setStyle("-fx-text-fill: white;");
        disabled.setToggleGroup(group);
        
        if ((Boolean) config.getParam("muted")) {
            disabled.setSelected(true);
        } else {
            enabled.setSelected(true);
        }

        controlsBox.setStyle("-fx-background-color: black;");
        controlsBox.setEffect(lighting);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(enabled, disabled, buttonOk);

        initScene(controlsBox, 200, 120);

        buttonOk.setOnAction((e) -> {

            if(disabled.isSelected()) {
                config.setParam("muted", true);
            } else {
                config.setParam("muted", false);
            }

            Sound.playMenu();
            controlsStage.close();
        });
    }

    /**
     * set audio modal, call from menu data
     */
    private void setHostname() {

        initStage();
        controlsBox.setStyle("-fx-background-color: black;");
        controlsBox.setEffect(lighting);
        controlsBox.setAlignment(Pos.CENTER);
        textFieldServer.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(textFieldServer, buttonConnect);
        
        initScene(controlsBox, 200, 110);

        buttonConnect.setOnAction((e) -> {
            ip = textFieldServer.getText();
            if(ip.length() > 0) {
                Sound.playMenu();
                controlsStage.close();
                new ClientEngine(gameLauncher, stage, movementTypePlayer1, ip);
            } else {
                textFieldServer.setText("ERROR - Enter IP please!");
            }
        });
    }

    /**
     * removes old menu and adds new
     * @param data
     * @param height
     */
    private void initMenu(List<Pair<String, Runnable>> data, int height) {

        removeMenu(menuBox);
        removeLine(line);

        menuBox = new VBox(-5);

        addMenu(lineX + 5, lineY + 5, data, menuBox);
        addLine(lineX, lineY, lineHeight - height);

        startAnimation(menuBox);
    }

    /**
     * inits stage, box and button group
     */
    private void initStage() {

        controlsStage = new Stage();
        controlsBox = new VBox(10);
        group = new ToggleGroup();

        controlsStage.initStyle(StageStyle.UNDECORATED);
    }

    /**
     * inits scene and adds to stage
     * @param box
     * @param width
     * @param height
     */
    private void initScene(VBox box, int width, int height) {

        Scene controlsScene = new Scene(box, width, height);
        controlsStage.setScene(controlsScene);
        controlsStage.show();
    }

    private void addBackground() {

        ImageView imageView = new ImageView(new Image(getClass().getResource("background2.png").toExternalForm()));
        imageView.setOpacity(0.9);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        imageView.setEffect(colorAdjust);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle(String titleText) {

        title = new MenuTitle(titleText, 48);
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);

        root.getChildren().add(title);
    }

    private void addMenu(double x, double y, List<Pair<String, Runnable>> type, VBox box) {

        box.setTranslateX(x);
        box.setTranslateY(y);

        type.forEach(data -> {

            MenuItem item = new MenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            box.getChildren().addAll(item);
        });

        root.getChildren().add(box);
    }

    private void addLine(double x, double y, int height) {

        line = new Line(x, y, x, y + height);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void removeMenu(VBox box) {

        root.getChildren().remove(box);
    }   

    private void removeLine(Line line) {

        root.getChildren().remove(line);
    }   

    private void startAnimation(VBox box) {

        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);

        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < box.getChildren().size(); i++) {
                Node n = box.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }

        });

        st.play();
    }

    /**
     * creates content - adds background, title, data, line, menu and starts animation
     * @return
     */
    private Parent createContent() {

        addBackground();
        addTitle("Trick or Treat");
        loadData();
        
        addLine(lineX, lineY, lineHeight);
        addMenu(lineX + 5, lineY + 5, menuData, menuBox);

        startAnimation(menuBox);

        return root;
    }

    /**
     * shows main menu, call from GameOver and GameLauncher
     */
    public void showMainMenu() {

        if(scene == null) scene = new Scene(createContent());
        stage.setTitle("Trick or Treat");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * shows pause menu, call from pause button in MovementManager
     * @param gameScene
     */
    public void showPausedMenu(Scene gameScene) {

        this.gameScene = gameScene;
        initMenu(pausedMenu, 95);
        title.getText().setText("");
        root.setOpacity(0.7);

        addTitle("PAUSED");

        soundOnBefore = !((Boolean)config.getParam("muted")).booleanValue();
        if(soundOnBefore) {
            Sound.muteSound();
            GameMenu.setRightButtons();
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * hides pause menu, call from pause menu data
     * @param game
     */
    public void resumeGame(Game game) {

        game.paused = false;
        stage.setScene(gameScene);

        if(soundOnBefore) {
            Sound.unmuteSound();
            GameMenu.setRightButtons();
        }
    }
}
