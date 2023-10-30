package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

    private BorderPane primaryPane;
    private Home accueil;

    private Game game;

    public Game setGame(Game game) {
        this.game = game;
        return this.game;
    }

    @Override
    public void init() throws Exception {
        
        this.primaryPane = new BorderPane();
        this.accueil = new Home(this);
        
        this.game = null;
        
        this.setCenter(this.accueil);
        
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Connect 4");
        stage.setResizable(false);
        stage.setScene(this.createScene());
        stage.centerOnScreen();
        stage.show();

    }

    public static void main(String[] args) {

        launch(Main.class, args);

    }

    public void setCenter(Pane pane){

        this.primaryPane.setCenter(pane);

    }

    private Scene createScene(){

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(this.primaryPane);
        
        return new Scene(borderPane, 750, 750);

    }

    public Game getGame() {
        return this.game;
    }

    public Home getAccueil() {
        return this.accueil;
    }

}