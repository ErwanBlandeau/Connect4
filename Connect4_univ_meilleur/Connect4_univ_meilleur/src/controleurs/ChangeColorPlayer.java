package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import src.GameSetting;

public class ChangeColorPlayer implements EventHandler<ActionEvent> {

    private ColorPicker colorPlayer1;
    private ColorPicker colorPlayer2;

    public ChangeColorPlayer(ColorPicker colorPlayer1, ColorPicker colorPlayer2){
        
        this.colorPlayer1 = colorPlayer1;
        this.colorPlayer2 = colorPlayer2;

    }

    @Override
    public void handle(ActionEvent actionEvent) {

        Color colorPlayer1 = this.colorPlayer1.getValue();
        Color colorPlayer2 = this.colorPlayer2.getValue();

        if(colorPlayer1.equals(colorPlayer2)){

            this.colorPlayer1.setValue(GameSetting.colorPlayer1);
            this.colorPlayer2.setValue(GameSetting.colorPlayer2);

        }else{

            if(colorPlayer1.equals(GameSetting.colorUndefined)){

                this.colorPlayer1.setValue(GameSetting.colorPlayer1);

            }else if(colorPlayer2.equals(GameSetting.colorUndefined)){

                this.colorPlayer2.setValue(GameSetting.colorPlayer2);

            }else{

                GameSetting.colorPlayer1 = colorPlayer1;
                GameSetting.colorPlayer2 = colorPlayer2;

            }

        }

    }

}
