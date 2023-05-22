package info.uvt.gui.chess;

import net.java.joglutils.ThreeDS.Model3DS;

import java.awt.*;

public class Bishop extends ChessPiece{

    private static Model3DS _model = new Model3DS();
    public Bishop(float positionX, float positionY, float positionZ, Color color) {
        super(positionX, positionY, positionZ, color, _model, 5);
    }

    public static void initModel(){
        _model = new Model3DS();
        _model.load("Modele/bishop.3ds");
    }
}
