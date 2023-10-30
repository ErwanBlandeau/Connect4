package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import src.GameSetting;

public class ChangeNamePlayer implements EventHandler<ActionEvent> {

    private TextField namePlayer1;
    private TextField namePlayer2;

    public ChangeNamePlayer(TextField namePlayer1, TextField namePlayer2){
        
        this.namePlayer1 = namePlayer1;
        this.namePlayer2 = namePlayer2;

    }

    @Override
    public void handle(ActionEvent actionEvent) {

        String namePlayer1 = this.namePlayer1.getText();
        String namePlayer2 = this.namePlayer2.getText();

        if(namePlayer1.equals(namePlayer2)){

            this.namePlayer1.setText("");
            this.namePlayer2.setText("");

            GameSetting.namePlayer1 = "Player 1";
            GameSetting.namePlayer2 = "Player 2";

        }else{

            GameSetting.namePlayer1 = namePlayer1;
            GameSetting.namePlayer2 = namePlayer2;

        }

    }

}
