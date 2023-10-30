package src;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import controleurs.GameStop;
import controleurs.PlayerClickGame;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import modele.Plateau;
import src.IA.MCTS;

public class Game extends StackPane{
    
    private Plateau plateau;
    private MCTS ia;
    private Main main;
    private Map<Integer,ImageView> fleches;

    public Game(Main main){

        this.main = main;

        this.fleches = new HashMap<>();

        for(int col = 0; col < GameSetting.columnCount; ++col){
            ImageView imageView = new ImageView(new Image(new File("./images/fleche.png").toURI().toString()));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setVisible(false);
            this.fleches.put(col, imageView);
        }

        byte[] lookUp = new byte[4096];

        for (short colonne = 0; colonne < 4096; colonne++) {
            lookUp[colonne] = 5;
            while (lookUp[colonne] >= 0 && (byte) ((colonne >> (2 * lookUp[colonne])) & 0b11) != (byte) 0b00) {
                lookUp[colonne]--;
            }
        }

        this.plateau = new Plateau(main, lookUp);

        if(GameSetting.soloMode) {
         
            CompletableFuture.runAsync(() -> {

                this.ia = new MCTS(this.plateau, (byte) 0b11);

                if(this.plateau.getTrait() == 0b11){
                    while(this.ia == null) System.out.println("En attente de la pool...");
                    PlayerClickGame.canPlay = false;
                    Liaison liaison = this.ia.playCoup(1);

                    Platform.runLater(() -> {
                        if (liaison != null) {
                            this.animatePionFall((int) liaison.getCol(), (int) liaison.getRow(), ((this.plateau.getTrait() == 0b10) ? GameSetting.colorPlayer2 : GameSetting.colorPlayer1), false, null);
                            CompletableFuture.runAsync(() ->{
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                };
                                PlayerClickGame.canPlay = true;
                            });
                        } else {
                            this.majAffichage();
                        }
                    });
                }
                
            });

        }

        this.majAffichage();

        super.setBackground(new Background(new BackgroundImage(new Image(new File("./images/fondJeu.png").toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));
        super.setAlignment(Pos.CENTER);

    }

    public void majAffichage(){

        super.getChildren().clear();

        VBox plateauV;
        HBox plateauH = new HBox();
        plateauH.setAlignment(Pos.CENTER);

        Circle circle;

        Color color;

        PlayerClickGame playerClickGame;

        for(byte col = 0; col < GameSetting.columnCount; ++col){

            plateauV = new VBox();
            plateauV.setAlignment(Pos.CENTER);
            playerClickGame = new PlayerClickGame(this, col);
            plateauV.setOnMouseClicked(playerClickGame);
            plateauV.setOnMouseEntered(playerClickGame);
            plateauV.setOnMouseExited(playerClickGame);
            plateauV.getChildren().add(this.fleches.get((int)col));

            for(byte row = 0; row < GameSetting.rowCount; ++row){

                color = this.plateau.getColor(row, col);

                if(this.plateau.isFinie()){

                    this.deleteIA();

                    for (Location locationGagnante : this.plateau.getLocationGagnante()) {
                        
                        if(locationGagnante.getRow() == row && locationGagnante.getCol() == col){

                            color = color.darker();

                        }

                    }

                }

                circle = new Circle(45, color);

                plateauV.getChildren().add(circle);
                
            }

            plateauH.getChildren().add(plateauV);

        }

        String gagnant = "Il n'y a aucun gagnant, c'est une égalité !";
        Color colorGagnante = Color.WHITE;
        Button partieFini = null;

        if(this.plateau.isFinie() && this.plateau.getGagnant() != -1){
            gagnant = "Le gagnant est " + ((this.plateau.getGagnant() == 0b10) ? GameSetting.namePlayer1 : GameSetting.namePlayer2);
            colorGagnante = ((this.plateau.getGagnant() == 0b10) ? GameSetting.colorPlayer1 : GameSetting.colorPlayer2);
            partieFini = new Button("Retour à l'accueil");
            partieFini.setOnMouseClicked(new GameStop(this.main));
            partieFini.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            partieFini.setTextFill(Color.WHITE);
            partieFini.setCursor(Cursor.HAND);
            partieFini.setBackground(new Background(
                new BackgroundFill(
                    Color.web(colorGagnante.toString(), 0.3),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
            ));
        }else if(!this.plateau.isFinie()){
            gagnant = ((this.plateau.getTrait() == 0b10) ? GameSetting.namePlayer1 : GameSetting.namePlayer2);
            colorGagnante = ((this.plateau.getTrait() == 0b10) ? GameSetting.colorPlayer1 : GameSetting.colorPlayer2);
        }

        Label quiJoue = new Label(gagnant);
        quiJoue.setTextFill(colorGagnante);
        quiJoue.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        HBox hBox = new HBox();
        hBox.getChildren().add(quiJoue);
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setPadding(new Insets(20));

        super.getChildren().add(hBox);

        if(partieFini != null){
            HBox partieFiniBox = new HBox(partieFini);
            partieFiniBox.setAlignment(Pos.BOTTOM_CENTER);
            partieFiniBox.setPadding(new Insets(25));
            super.getChildren().add(plateauH);
            super.getChildren().add(partieFiniBox);
        }else{
            super.getChildren().add(plateauH);
        }

    }

    public void animatePionFall(int column, int row, Color circleColor, boolean isPlayer, PlayerClickGame playerClickGame) {
        // Création du pion
        Circle pion = new Circle(45, circleColor);

        super.getChildren().add(pion);

        // Animation de la chute du pion
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.8), pion);
        transition.setFromX(-270 + (90 * column)); // -270 première column -180 deuxième column
        transition.setFromY(-230);
        transition.setToY(226 + (-90 * (5-row))); 
        transition.play();
        transition.setOnFinished(event -> {
            super.getChildren().remove(pion);
            majAffichage();
            if(isPlayer) {
                PlayerClickGame.canPlay = true;
                playerClickGame.faireJouerIA();
            }
        });
    }

    public Plateau getPlateau() {
        return this.plateau;
    }

    public MCTS getIa() {
        return this.ia;
    }

    public Map<Integer, ImageView> getFleches() {
        return this.fleches;
    }

    public void deleteIA(){
        this.ia = null;
        System.gc();
    }

}
