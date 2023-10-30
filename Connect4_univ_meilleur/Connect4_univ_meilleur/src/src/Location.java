package src;

public class Location {
    
    private byte row;
    private byte col;

    public Location(byte row, byte col){

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
