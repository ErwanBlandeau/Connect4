package src;
import java.util.Random;

import javafx.scene.paint.Color;

public class GameSetting {
    
    private GameSetting(){}

    public static byte rowCount = 6;
    public static byte columnCount = 7;
    public static Color colorPlayer1 = Color.RED;
    public static Color colorPlayer2 = Color.YELLOW;
    public static Color colorUndefined = Color.WHITE;
    public static String namePlayer1 = "Player 1";
    public static String namePlayer2 = "Player 2";
    public static Random random = new Random();
    public static boolean soloMode = true;
    public static byte levelIA = 3;
    public static int[][][] motifs = new int[][][]{
                    {{0, -1}, {0, -2}, {0, -3}},
                    {{0, 1}, {0, 2}, {0, 3}},
                    {{0, -1}, {0, 1}, {0, 2}},
                    {{0, -1}, {0, -2}, {0, 1}},  // Vertical
                    {{-3, 0}, {-2, 0}, {-1, 0}},   // Horizontaux
                    {{-2, 0}, {-1, 0}, {1, 0}},
                    {{-1, 0}, {1, 0}, {2, 0}},
                    {{1, 0}, {2, 0}, {3, 0}},
                    {{-3, -3}, {-2, -2}, {-1, -1}},  // Diagonales /
                    {{-2, -2}, {-1, -1}, {1, 1}},
                    {{-1, -1}, {1, 1}, {2, 2}},
                    {{1, 1}, {2, 2}, {3, 3}},
                    {{-3, 3}, {-2, 2}, {-1, 1}},   // Diagonales \
                    {{-2, 2}, {-1, 1}, {1, -1}},
                    {{-1, 1}, {1, -1}, {2, -2}},
                    {{1, -1}, {2, -2}, {3, -3}}
            };

}
