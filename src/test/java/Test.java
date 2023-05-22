
import info.uvt.gui.ChessBoardFrame;
import info.uvt.gui.chess.*;

public class Test
{
    public static void main(String[] arguments)
    {
        Pawn.initModel();
        Bishop.initModel();
        King.initModel();
        Knight.initModel();
        Queen.initModel();
        Rook.initModel();

        new ChessBoardFrame();
    }

}
