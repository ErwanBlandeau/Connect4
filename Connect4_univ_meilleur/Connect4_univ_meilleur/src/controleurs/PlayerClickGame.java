package controleurs;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import src.Game;
import src.GameSetting;
import src.Liaison;

public class PlayerClickGame implements EventHandler<MouseEvent> {

    private Game game;
    private byte col;
    public static boolean canPlay = true;

    public PlayerClickGame(Game game, byte col){

        this.game = game;
        this.col = col;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
            
            this.game.getFleches().get((int)this.col).setVisible(true);

        }else if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_EXITED)){

            this.game.getFleches().get((int)this.col).setVisible(false);

        }else if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            if((!this.game.getPlateau().isFinie() && (!GameSetting.soloMode || this.game.getPlateau().getTrait() == (byte) 0b10)) && canPlay ){

                byte row = this.game.getPlateau().playCoup(col);

                if(row != -1){
                    canPlay = false;
                    this.game.animatePionFall(this.col, (int) row, ((this.game.getPlateau().getTrait() == 0b10) ? GameSetting.colorPlayer2 : GameSetting.colorPlayer1), true, this);
                }else{
                    this.game.majAffichage();
                }

            }
        }
        
    }

    public void faireJouerIA(){
        if(GameSetting.soloMode && !this.game.getPlateau().isFinie()){

            CompletableFuture.runAsync(() -> {
                while(this.game.getIa() == null) System.out.println("En attente de la pool...");
                canPlay = false;
                Liaison liaison = this.game.getIa().playCoup(1);

                Platform.runLater(() -> {
                    if (liaison != null) {
                        this.game.animatePionFall((int) liaison.getCol(), (int) liaison.getRow(), ((this.game.getPlateau().getTrait() == 0b10) ? GameSetting.colorPlayer2 : GameSetting.colorPlayer1), false, this);
                            CompletableFuture.runAsync(() ->{
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                };
                                canPlay = true;
                            });
                        } else {
                            this.game.majAffichage();
                        }
                    });
                });
        }
    }
    
}
