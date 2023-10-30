package src.IA;

import modele.Plateau;
import src.GameSetting;
import src.Liaison;

public class MCTS {
    
    private Plateau plateau;

    private byte joueur;

    private int poolSize;
    private int arbreIndex;
    private int poolIndex;

    private Noeud[] pool;

    public MCTS(Plateau plateau, byte joueur){

        this.plateau = plateau;
        this.joueur = joueur;

        this.poolSize = 20999999;

        this.pool = new Noeud[this.poolSize];

        System.out.println("Initialisation de la pool size ");

        for(int i = 0; i < this.poolSize; ++i){

            this.pool[i] = new Noeud(this);

        }

        System.out.println("Pool size initialisé !");

        this.arbreIndex = 0;

        this.poolIndex = 1;

    }

    public int newNoeud(){

        int index = this.poolIndex;

        this.poolIndex += 1;
        this.poolIndex %= this.poolSize;

        return index;

    }

    public double etape(int noeudIndex, Plateau simulation){

        Noeud noeud = this.pool[noeudIndex];

        double gain;

        if(!noeud.isEtendue()){

            noeud.etendre(simulation);

            byte trait = simulation.getTrait();

            simulation.randomGame();

            if(simulation.getGagnant() == (byte) -1){

                gain = 0.5;

            }else if(simulation.getGagnant() == trait){

                gain = 0;

            }else{

                gain = 1;

            }

            noeud.setN(noeud.getN()+1);
            noeud.setW(noeud.getW()+gain);

            return gain;

        }

        if(simulation.isFinie()){

            if(simulation.getGagnant() == (byte) -1){

                gain = 0.5;

            }else if(simulation.getGagnant() == simulation.getTrait()){

                gain = 0;

            }else{

                gain = 1;

            }

            noeud.setN(noeud.getN()+1);
            noeud.setW(noeud.getW()+gain);

            return gain;

        }

        float meilleurUct = 0;
        byte meilleurCoup = 0;

        for(byte coup = 0; coup < GameSetting.columnCount; ++coup){

            if(noeud.getEnfants()[coup] == -1){
            
                continue;
            
            }
            
            float uct = noeud.UCT(coup);
            
            if(uct > meilleurUct){
            
                meilleurUct = uct;
                meilleurCoup = coup;
            
            }
        
        }

        int enfantIndex = noeud.getEnfants()[meilleurCoup];
        
        simulation.playCoup(meilleurCoup);
        
        gain = ((double) 1 - this.etape(enfantIndex, simulation));

        noeud.setN(noeud.getN()+1);
        noeud.setW(noeud.getW()+gain);
        
        return gain;

    }

    public Liaison playCoup(int temps){

        if(this.plateau.getTrait() == this.joueur){

            if(this.plateau.getLastCoup() != -1 && this.pool[this.arbreIndex].isEtendue()){

                this.arbreIndex = this.pool[this.arbreIndex].getEnfants()[this.plateau.getLastCoup()];

            }

            int etapes = 0;

            long fin = System.currentTimeMillis() + (temps*1000);

            Plateau simulation;

            while(System.currentTimeMillis() < fin){

                simulation = this.plateau.copie();
                this.etape(this.arbreIndex, simulation);
                etapes += 1;

            }

            System.out.println(etapes + " simulations effectuées.");

            byte meilleurCoup = 0;
            int meilleurN = 0;

            for(byte coup = 0; coup < GameSetting.columnCount; ++coup){

                int enfantIndex = this.pool[this.arbreIndex].getEnfants()[coup];

                if(enfantIndex == -1){

                    continue;

                }

                Noeud enfant = this.pool[enfantIndex];

                if(enfant.getN() > meilleurN){

                    meilleurN = enfant.getN();
                    meilleurCoup = coup;

                }

            }

            byte row = this.plateau.playCoup(meilleurCoup);

            this.arbreIndex = this.pool[this.arbreIndex].getEnfants()[meilleurCoup];

            return new Liaison(row, meilleurCoup);

        }

        return null;

    }

    public Noeud[] getPool() {
        return this.pool;
    }

    public Plateau getPlateau() {
        return this.plateau;
    }

    public int getArbreIndex() {
        return this.arbreIndex;
    }

}
