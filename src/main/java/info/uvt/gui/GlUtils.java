package info.uvt.gui;

import com.jogamp.opengl.GL2;

import java.awt.*;

public class GlUtils {
    public static void setGlColor(GL2 gl, Color color){
        gl.glColor3f(color.getRed()/255, color.getGreen()/255, color.getBlue()/255);
    }

    public static class Coordinates2D{
        public Float x;
        public Float y;

        public Coordinates2D(Float x, Float y){
            this.x = x;
            this.y = y;
        }
    }
}
