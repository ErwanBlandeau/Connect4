package src.IA;

import modele.Plateau;
import src.GameSetting;

public class Noeud {
    
    private int n;
    private double w;
    private boolean etendue;

    private int[] enfants;

    private MCTS mcts;

    public Noeud(MCTS mcts){

        this.n = 0;
        this.w = 0;
        this.etendue = false;

        this.enfants = new int[GameSetting.columnCount];

        this.mcts = mcts;

        for(byte col = 0; col < GameSetting.columnCount; ++col){

            this.enfants[col] = -1;

        }

    }

    public float UCT(byte ligne){

        if(this.etendue && this.enfants[ligne] != -1){

            Noeud enfant = this.mcts.getPool()[this.enfants[ligne]];

            if(enfant.getN() == 0){

                return Float.POSITIVE_INFINITY;

            }
    
            double c = Math.sqrt(2);
            double exploitation = ((double) enfant.getW() / enfant.getN());
            double exploration = Math.sqrt(Math.log(this.n) / enfant.getN());

            return (float) (exploitation + c * exploration);

        }

        return 0;

    }

    public void etendre(Plateau simulation){

        this.etendue = true;

        Boolean[] possiblesCoups = simulation.possiblesCoups();

        for(byte col = 0; col < GameSetting.columnCount; ++col){

            if(possiblesCoups[col]){

                this.enfants[col] = this.mcts.newNoeud();

            }
            
        }

    }
    
    public int getN() {
        return this.n;
    }

    public double getW() {
        return this.w;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setW(double w) {
        this.w = w;
    }

    public int[] getEnfants() {
        return this.enfants;
    }

    public boolean isEtendue() {
        return this.etendue;
    }

}
