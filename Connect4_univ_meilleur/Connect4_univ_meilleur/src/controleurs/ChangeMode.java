package controleurs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import src.GameSetting;

public class ChangeMode implements ChangeListener<String> {

    private Label label;
    private Slider slider;

    public ChangeMode(Label label, Slider slider){
        
        this.label = label;
        this.slider = slider;

    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        
        if(!newValue.equals(oldValue)){

            if(newValue.equals("Solo")){

                GameSetting.soloMode = true;

            }else{

                GameSetting.soloMode = false;

            }

            this.label.setDisable(!GameSetting.soloMode);
            this.slider.setDisable(!GameSetting.soloMode);

        }

    }

}
