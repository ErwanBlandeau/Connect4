package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import src.GameSetting;
import src.Location;
import src.Main;

public class Plateau {
  
    private Main main;

    private short[] board;

    private boolean finie;

    private byte trait;
    private byte gagnant;
    private byte lastCoup;

    private byte[] lookUp;

    private List<Location> locationGagnante;

    public Plateau(Main main, byte[] lookUp){

        if(GameSetting.namePlayer1.equals("")) GameSetting.namePlayer1 = "Joueur 1";
        else if(GameSetting.namePlayer2.equals("")) GameSetting.namePlayer2 = "Joueur 2";

        this.main = main;

        this.board = new short[GameSetting.columnCount];
        
        this.finie = false;
        int quiJoue = GameSetting.random.nextInt(2);

        if(quiJoue == 0){
            this.trait = 0b11;
        }else{
            this.trait = 0b10;
        }

        this.gagnant = (byte) -2;
        this.lastCoup = (byte) -1;

        this.lookUp = lookUp;

        this.locationGagnante = new ArrayList<>();

    }

    public byte joueurCase(byte ligne_index, byte colonne_index){

        return (byte) ((this.board[colonne_index] >> (2 * ligne_index)) & 0b11);

    }

    public byte extraireJoueur(byte ligne_index, short colonne){

        return (byte) ((colonne >> (2 * ligne_index)) & 0b11);

    }

    public void mettreCase(byte ligne_index, byte colonne_index, byte joueur_index){

        this.board[colonne_index] |= joueur_index << (2 * ligne_index);

    }

    public boolean inPlateau(byte row, byte col){

        return 0 <= row && row < GameSetting.rowCount && 0 <= col && col < GameSetting.columnCount;

    }

    public Color getColor(byte row, byte col){
        
        if(this.inPlateau(row, col)){

            byte state = this.joueurCase(row, col);

            if(state == (byte) 0b10) return GameSetting.colorPlayer1;
            else if(state == (byte) 0b11) return GameSetting.colorPlayer2;
            else return GameSetting.colorUndefined;

        }else return GameSetting.colorUndefined;

    }

    public Boolean[] possiblesCoups(){

        if(this.finie){

            return new Boolean[]{false,false,false,false,false,false,false};

        }else{

            Boolean[] possiblesCoups = new Boolean[GameSetting.columnCount];

            for(byte col = 0; col < GameSetting.columnCount; ++col){

                possiblesCoups[col] = (this.joueurCase( (byte) 0, col) == (byte) 0b00);

            }

            return possiblesCoups;

        }

    }

    public byte getNextRow(byte col){

        return this.lookUp[this.board[col]];

    }

    public void randomGame(){

        Random random = new Random();

        byte coup;

        while(!this.finie){

            coup = this.gagnantCoup();

            if(coup == (byte) -1){

                coup = (byte) random.nextInt(GameSetting.columnCount);

            }

            try{

                this.playCoup(coup);

            }catch(AssertionError ignored){}

        }
        
    }

    public Plateau copie(){

        Plateau copie = new Plateau(this.main, this.lookUp);

        copie.setPlateau(this.board.clone());
        copie.setTrait(this.trait);
        copie.setFinie(this.finie);
        copie.setGagnant(this.gagnant);
        copie.setLastCoup(this.lastCoup);

        return copie;
    }

    public byte gagnantCoup(){

        for(byte col = 0; col < GameSetting.columnCount; ++col){

            if(this.joueurCase((byte) 0, (byte) col) != (byte) 0b00){
                
                continue;

            }

            byte row = this.getNextRow(col);

            byte rowDy;
            byte colDx;

            for (int[][] motif : GameSetting.motifs) {

                for (int[] position : motif) {
    
                    rowDy = (byte) (row - position[0]);
                    colDx = (byte) (col + position[1]);
                            
                    if (!this.inPlateau( rowDy, colDx)) {
    
                        break;

                    }
                        
                    if (this.joueurCase( rowDy, colDx) != this.trait) {
    
                        break;
    
                    }

                    return col;
    
                }
    
            }

        }

        return (byte) -1;

    }

    public byte playCoup(byte col){

        byte row = this.getNextRow(col);
        
        if(row != -1){

            this.mettreCase( (byte) row, (byte) col, (byte) this.trait);
                
            boolean victoire;

            byte rowDy;
            byte colDx;

            for (int[][] motif : GameSetting.motifs) {

                victoire = true;

                this.locationGagnante.clear();

                this.locationGagnante.add(new Location(row, col));

                for (int[] position : motif) {

                    rowDy = (byte) (row - position[0]);
                    colDx = (byte) (col + position[1]);
                        
                    if (!this.inPlateau(rowDy, colDx)) {

                        victoire = false;

                        break;

                    }
                        
                    if (this.joueurCase( rowDy, colDx) != trait) {

                        victoire = false;

                        break;

                    }

                    this.locationGagnante.add(new Location(rowDy, colDx));

                }
                    
                if (victoire) {

                    finie = true;

                    gagnant = trait;

                    break;

                }

            }
                
            if (!finie) {

                boolean egalite = true;

                for(byte i = 0; i < 7; ++i){

                if (this.joueurCase( (byte)0, (byte)i) == (byte) 0b00) {

                    egalite = false;

                        break;

                    }

                }
                    
                if (egalite) {
                        
                    this.locationGagnante.clear();

                    finie = true;

                    gagnant = (byte) -1;

                }

            }

            this.trait = (byte) ((this.trait == (byte) 0b10) ? 0b11 : 0b10);

            this.lastCoup = col;

        }

        return row;

    }

    public boolean isFinie() {
        return this.finie;
    }

    public byte getTrait() {
        return this.trait;
    }

    public byte getGagnant() {
        return this.gagnant;
    }

    public List<Location> getLocationGagnante() {
        return this.locationGagnante;
    }

    public byte getLastCoup() {
        return this.lastCoup;
    }

    public short[] getPlateau() {
        return this.board;
    }

    public void setPlateau(short[] board) {
        this.board = board;
    }

     public void setFinie(boolean finie) {
         this.finie = finie;
     }

     public void setTrait(byte trait) {
         this.trait = trait;
     }

     public void setGagnant(byte gagnant) {
         this.gagnant = gagnant;
     }

     public void setLastCoup(byte lastCoup) {
         this.lastCoup = lastCoup;
     }

}
