package src;

public class Liaison {
    
    private byte row;
    private byte col;

    public Liaison(byte row, byte col){
        this.row = row;
        this.col = col;
    }

    public byte getRow() {
        return this.row;
    }

    public byte getCol() {
        return this.col;
    }

}
