package main.java.Menu;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import main.java.*;
import main.java.Network.ClientEngine;
import main.java.Network.ServerEngine;

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
    private static final int lineHeight = 260;
    private static Configuration<Object> config = new Configuration<Object>();

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;

    private String ip;
    private Stage controlsStage;
    private Scene controlsScene;
    private VBox controlsBox;
    private RadioButton radioButtonWASD = new RadioButton("KEYBOARD - WASD");;
    private RadioButton radioButtonARROW = new RadioButton("KEYBOARD - ARROWS");
    private RadioButton radioButtonMOUSE = new RadioButton("MOUSE");
    private RadioButton enabled = new RadioButton("ENABLED");
    private RadioButton disabled = new RadioButton("DISABLED");
    private Button buttonOk = new Button("OK");
    private ToggleGroup group;
    private TextField textFieldServer = new TextField("");
    private MovementManager.MovementType movementType;
    private MovementManager.MovementType movementTypePlayer1 = MovementManager.MovementType.KEYBOARD_AWSD;
    private MovementManager.MovementType movementTypePlayer2 = MovementManager.MovementType.KEYBOARD_ARROW;
    private List<Pair<String, Runnable>> menuData;
    private List<Pair<String, Runnable>> localMenuData;
    private List<Pair<String, Runnable>> hostMenuData;
    private List<Pair<String, Runnable>> clientMenuData;

    public MainMenu(Stage stage, GameLauncher gameLauncher) {

        this.stage = stage;
        this.gameLauncher = gameLauncher;
    }

    private void loadData() {

        menuData = Arrays
            .asList(new Pair<String, Runnable>("Play local", () -> {

                initMenu(localMenuData, 40);

            }), new Pair<String, Runnable>("Host a Game", () -> {

                initMenu(hostMenuData, 90);

            }), new Pair<String, Runnable>("Join a Game", () -> {

                initMenu(clientMenuData, 40);

            }), new Pair<String, Runnable>("Highscore", () -> {
                // gameMode = Game.GameMode.REMOTE;
                // new ClientEngine(gameLauncher, stage);

            }), new Pair<String, Runnable>("Credits", () -> {

            }), new Pair<String, Runnable>("Exit to Desktop", Platform::exit));

        localMenuData = Arrays
            .asList(new Pair<String, Runnable>("Play the Game", () -> {

                gameMode = Game.GameMode.LOCAL;
                gameLauncher.startGame(gameMode, null, movementTypePlayer1, movementTypePlayer2);

            }), new Pair<String, Runnable>("Set Controls Player 1", () -> {
                setControlElements();

                buttonOk.setOnAction((e) -> {
                    movementTypePlayer1 = setMovementType();
                    controlsStage.close();
                });

            }), new Pair<String, Runnable>("Set Controls Player 2", () -> {
                setControlElements();

                buttonOk.setOnAction((e) -> {
                    movementTypePlayer2 = setMovementType();
                    controlsStage.close();
                });

            }), new Pair<String, Runnable>("Set Audio", () -> {

                setAudioSettings();


            }), new Pair<String, Runnable>("Back to Main Menu", () -> {

                removeMenu(menuBox);
                removeLine(line);
                menuBox = new VBox(-5);

                addMenu(lineX + 5, lineY + 5, menuData, menuBox);
                addLine(lineX, lineY, lineHeight);

                startAnimation(menuBox);

            }));

        hostMenuData = Arrays
            .asList(new Pair<String, Runnable>("Start the Game", () -> {

                gameMode = Game.GameMode.REMOTE;
                new ServerEngine(gameLauncher, stage, movementTypePlayer1);

            }), new Pair<String, Runnable>("Set Controls", () -> {

                setControlElements();

                buttonOk.setOnAction((e) -> {
                    movementTypePlayer1 = setMovementType();
                    controlsStage.close();
                });

            }), new Pair<String, Runnable>("Set Audio", () -> {
                setAudioSettings();

            }), new Pair<String, Runnable>("Exit to Desktop", Platform::exit));

        clientMenuData = Arrays
            .asList(new Pair<String, Runnable>("Start the Game", () -> {

                gameMode = Game.GameMode.REMOTE;
                if (ip != null) new ClientEngine(gameLauncher, stage, movementTypePlayer1, ip);

            }), new Pair<String, Runnable>("Set Hostname*", () -> {

                initStage();

                controlsBox.setAlignment(Pos.CENTER);
                controlsBox.getChildren().addAll(textFieldServer, buttonOk);

                initScene(controlsBox, 200, 150);

                buttonOk.setOnAction((e) -> {
                    ip = textFieldServer.getText();
                    controlsStage.close();
                });

            }), new Pair<String, Runnable>("Set Controls", () -> {

                setControlElements();

                buttonOk.setOnAction((e) -> {
                    movementTypePlayer1 = setMovementType();
                    controlsStage.close();
                });

            }), new Pair<String, Runnable>("Set Audio", () -> {

                setAudioSettings();

            }), new Pair<String, Runnable>("Exit to Desktop", Platform::exit));
    }

    private void setControlElements() {

        initStage();

        radioButtonWASD.setToggleGroup(group);
        radioButtonARROW.setToggleGroup(group);
        radioButtonMOUSE.setToggleGroup(group);
        radioButtonWASD.setSelected(true);

        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(radioButtonWASD, radioButtonARROW, radioButtonMOUSE, buttonOk);

        initScene(controlsBox, 200, 150);
    }

    private void setAudioSettings() {
        initStage();

        enabled.setToggleGroup(group);
        disabled.setToggleGroup(group);
        enabled.setSelected(true);

        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(enabled, disabled, buttonOk);

        initScene(controlsBox, 200, 100);

        buttonOk.setOnAction((e) -> {
            setAudio();
            controlsStage.close();
        });
    }


    private void initMenu(List<Pair<String, Runnable>> data, int height) {

        removeMenu(menuBox);
        removeLine(line);

        menuBox = new VBox(-5);

        addMenu(lineX + 5, lineY + 5, data, menuBox);
        addLine(lineX, lineY, lineHeight - height);

        startAnimation(menuBox);
    }

    private void initStage() {

        controlsStage = new Stage();
        controlsBox = new VBox(10);
        group = new ToggleGroup();
    }

    private void initScene(VBox box, int width, int height) {

        controlsScene = new Scene(box, width, height);
        controlsStage.setScene(controlsScene);
        controlsStage.show();
    }

    private void setAudio() {

        if(disabled.isSelected()) {
            config.setParam("muted", true);
        } else {
            config.setParam("muted", false);
        }
    }

    private MovementManager.MovementType setMovementType() {

        if(radioButtonARROW.isSelected()) {
            movementType = MovementManager.MovementType.KEYBOARD_ARROW;
        } else if(radioButtonWASD.isSelected()) {
            movementType = MovementManager.MovementType.KEYBOARD_AWSD;
        } else if(radioButtonMOUSE.isSelected()) {
            movementType = MovementManager.MovementType.MOUSE;
        }

        return movementType;
    }

    private Parent createContent() {

        addBackground();
        addTitle();
        loadData();
        
        addLine(lineX, lineY, lineHeight);
        addMenu(lineX + 5, lineY + 5, menuData, menuBox);

        startAnimation(menuBox);

        return root;
    }

    private void addBackground() {

        ImageView imageView = new ImageView(new Image(getClass().getResource("main_menu_converted.png").toExternalForm()));
        imageView.setOpacity(0.9);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        imageView.setEffect(colorAdjust);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {

        MenuTitle title = new MenuTitle("TRICK OR TREAT V.0.4", 48);
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);

        root.getChildren().add(title);
    }

    private void addLine(double x, double y, int height) {

        line = new Line(x, y, x, y + height);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
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

    private void removeMenu(VBox box) {

        root.getChildren().remove(box);
    }   

    private void removeLine(Line line) {

        root.getChildren().remove(line);
    }   

    public void showMainMenu() {

        if(scene == null) scene = new Scene(createContent());
        stage.setTitle("Trick or Treat");
        stage.setScene(scene);
        stage.show();
    }
}
