package controleurs;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import src.Main;

public class GameStop implements EventHandler<MouseEvent> {

    private final Main main;
    
    public GameStop(Main main){

        this.main = main;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        this.main.setCenter( this.main.getAccueil());
        
    }

}
