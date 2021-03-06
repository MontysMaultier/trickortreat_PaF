package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.menus.MainMenu;
import main.java.net.Network;
import main.java.sounds.Sound;
import main.java.sprites.GraphicsUtility;

public class GameLauncher extends Application {

    private Game game;
    private Stage stage;
    private MainMenu mainMenu;

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        mainMenu = new MainMenu(stage, this);
        mainMenu.showMainMenu();
    }

    /**
     * used in locale mode or when server is launching a new game
     * @param gameMode - locale or remote
     * @param networkEngine - client or server - null if playing locale
     * @param movementTypePlayer1 - movement type of player1
     * @param movementTypePlayer2 - movement type of player2
     */
    public void startGame(Game.GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        this.game = new Game(this, stage, gameMode, networkEngine, movementTypePlayer1, movementTypePlayer2);
        startGameLoop();
    }

    /** only used by client -> it is not necessary to create a new game object -
     * server sends game object to client who uses this object to instantiate a game
      */
    public void startGame(Game game) {
        this.game = game;
        startGameLoop();
    }

    /**
     * assign the gameController created by game class - show the GUI and start game Loop of super controller (Game-Controller)
     */
    private void startGameLoop() {
        Sound.playMusic();
        GameController gameController = game.getGameController();
        game.getWindow().showGameGUI();
        gameController.startGameLoop();
    }

    public Game getGame() {
        return game;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    /**
     * start the JavaFX application
     * @param args
     */
    public static void main(String[] args){
        /**
         * make sure every sprite is loaded
         */
        GraphicsUtility.initGraphics();
        launch();
    }
}
