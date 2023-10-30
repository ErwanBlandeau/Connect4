package src;
import java.io.File;

import controleurs.ChangeColorPlayer;
import controleurs.ChangeMode;
import controleurs.ChangeNamePlayer;
import controleurs.GameStart;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Home extends VBox{

    private ChangeNamePlayer changeNamePlayer;
    
    public Home(Main main){

        Background fondNode = new Background(new BackgroundImage(new Image(new File("./images/fondNode.jfif").toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT));

        ComboBox<String> gameType = this.creerComboBox(fondNode);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setBackground(fondNode);
        grid.setAlignment(Pos.CENTER);

        TextField namePlayer1 = this.creerTextField((byte)1, false);
        TextField namePlayer2 = this.creerTextField((byte)2, true);

        this.changeNamePlayer = new ChangeNamePlayer(namePlayer1, namePlayer2);

        ColorPicker couleurPlayer1 = this.creerColorPicker(GameSetting.colorPlayer1);
        ColorPicker couleurPlayer2 = this.creerColorPicker(GameSetting.colorPlayer2);

        this.changeColorPlayer(couleurPlayer1, couleurPlayer2);

        Button gameStart = this.creerButton(main, fondNode);

        grid.add(namePlayer1, 0, 0);
        grid.add(couleurPlayer1, 1, 0);

        grid.add(namePlayer2, 0, 1);
        grid.add(couleurPlayer2, 1, 1);

        Slider slider = this.creerSlider();

        Label label = this.creerLabel();

        gameType.getSelectionModel().selectedItemProperty().addListener(new ChangeMode(label, slider));

        super.getChildren().addAll(gameType, grid, label, slider, gameStart);

        super.setAlignment(Pos.CENTER);
        super.setSpacing(20);

        super.setBackground(new Background(new BackgroundImage(new Image(new File("./images/fondBois.jpg").toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

    }

    private void changeColorPlayer(ColorPicker colorPlayer1, ColorPicker colorPlayer2){
        
        ChangeColorPlayer changeColorPlayer = new ChangeColorPlayer(colorPlayer1, colorPlayer2);

        colorPlayer1.setOnAction(changeColorPlayer);
        colorPlayer2.setOnAction(changeColorPlayer);

    }

    private TextField creerTextField(byte numeroJoueur, boolean disable){

        TextField textField = new TextField();
        textField.setPromptText("Nom du joueur " + numeroJoueur);
        textField.setBackground(null);
        textField.setFont(Font.font("Arial",FontWeight.NORMAL, 20));
        textField.setStyle("-fx-text-fill: white;");

        return textField;

    }

    private ColorPicker creerColorPicker(Color color){

        ColorPicker colorPicker = new ColorPicker(color);
        colorPicker.setBackground(null);
        colorPicker.setStyle("-fx-color-label-visible: false; -fx-color-rect-width: 100px; -fx-color-rect-height: 20px;");

        return colorPicker;

    }
    
    private Slider creerSlider(){

        Slider slider = new Slider(1, 6, 1);
        slider.setMaxSize(500, 500);

        return slider;

    }

    private Label creerLabel(){

        Label label = new Label("Vous avez la possibilité de modifier les performanes de l'IA (" + GameSetting.levelIA + ")");
        label.setFont(Font.font("Arial",FontWeight.NORMAL, 20));
        label.setTextFill(Color.WHITE);

        return label;

    }

    private Button  creerButton(Main main, Background fondNode){

        Button button = new Button("Lancer la partie");
        button.setOnMouseClicked(new GameStart(main));
        button.setOnAction(this.changeNamePlayer);
        button.setPrefSize(390, 50);
        button.setBackground(fondNode);
        button.setFont(Font.font("Arial",FontWeight.NORMAL, 20));
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);

        return button;

    }

    private ComboBox<String> creerComboBox(Background fondNode){

        ComboBox<String> gameType = new ComboBox<>();
        gameType.setItems(FXCollections.observableArrayList("Solo", "Joueur VS Joueur"));
        gameType.setValue("Solo");
        gameType.setPrefSize(400, 50);
        gameType.setBackground(fondNode);

        this.setCellFactory(gameType, fondNode);

        return gameType;

    }

    private void setCellFactory(ComboBox<String> gameType, Background fondNode){

        gameType.setCellFactory(listView -> {
            
            ListCell<String> cell = new ListCell<String>() {

                @Override
                protected void updateItem(String item, boolean empty) {

                    super.updateItem(item, empty);

                    if (item != null) {

                        super.setText(item);
                        super.setTextFill(Color.WHITE);
                        super.setBackground(fondNode);
                        super.setFont(Font.font("Arial",FontWeight.NORMAL, 20));
                        super.setAlignment(Pos.CENTER);

                    } else {

                        super.setText(null);

                    }

                }

            };

            return cell;

        });

        gameType.setButtonCell(new ListCell<String>() {

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {

                    super.setText(null);
                    super.setAlignment(Pos.BASELINE_CENTER);

                } else {

                    super.setText(item);

                }

                super.setTextFill(Color.WHITE);
                super.setFont(Font.font("Arial",FontWeight.NORMAL, 20));

            }

        });

    }

}
