package controleurs;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import src.Game;
import src.Main;

public class GameStart implements EventHandler<MouseEvent> {

    private final Main main;
    
    public GameStart(Main main){

        this.main = main;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        this.main.setCenter( this.main.setGame(new Game(this.main)) );
        
    }
    
}
