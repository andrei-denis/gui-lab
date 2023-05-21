package info.uvt.gui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlUtils {
    public static void setGlColor(GL2 gl, Color color){
        gl.glColor3f((float) color.getRed()/255.0f, (float) color.getGreen()/255.0f, (float) color.getBlue()/255.0f);
    }

    public static class Coordinates2D{
        public Float x;
        public Float y;

        public Coordinates2D(Float x, Float y){
            this.x = x;
            this.y = y;
        }

        public float[] getVectorValues(){
            return (new float[]{this.x, this.y, 0});
        }
    }

    public static class Coordinates3D extends Coordinates2D{

        public Float z;

        public Coordinates3D(Float x, Float y, Float z){
            super(x, y);
            this.z = z;
        }

        @Override
        public float[] getVectorValues(){
            return (new float[]{this.x, this.y, this.z});
        }
    }

    public static class DrawRectangle implements DrawShape{
        List<Coordinates2D> squareCoordinates;

        public DrawRectangle(float x1, float y1, float x2, float y2){
            this.squareCoordinates = new ArrayList<Coordinates2D>();
            squareCoordinates.add(new GlUtils.Coordinates2D(x1, y1));
            squareCoordinates.add(new GlUtils.Coordinates2D(x1, y2));
            squareCoordinates.add(new GlUtils.Coordinates2D(x2, y2));
            squareCoordinates.add(new GlUtils.Coordinates2D(x2, y1));
        }

        // Draw rectangle with GL_LINE_LOOP mode
        @Override
        public void draw(GL2 gl){
            //edge1,2,3,4
            gl.glBegin( GL2.GL_LINE_LOOP );
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin( GL2.GL_POLYGON );
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();
        }

        public void drawWithTexture(GL2 gl, Texture texture){
            texture.bind(gl);

            gl.glBegin(GL2.GL_QUADS);
            // bottom-left corner
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y, 0);
            // bottom-right corner
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y, 0);
            // top-right corner
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(squareCoordinates.get(2).x, squareCoordinates.get(2).y, 0);
            // top-left corner
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(squareCoordinates.get(3).x, squareCoordinates.get(3).y, 0);
            gl.glEnd();
        }
    }

    public static class DrawCircle implements DrawShape {
        private Integer verticsNumber;
        private Float radius;
        private GlUtils.Coordinates2D pos;

        public DrawCircle(float x, float y, int nrVertics, float radius){
            this.verticsNumber = nrVertics;
            this.radius = radius;
            this.pos = new GlUtils.Coordinates2D(x, y);
        }

        @Override
        public void draw(GL2 gl){
            gl.glBegin(GL2.GL_LINE_LOOP);
            for(int i=0; i<verticsNumber; i++){
                double angle = 2.0 * Math.PI * ((double) i / (double) verticsNumber);
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));
                gl.glVertex2f(x + pos.x, y + pos.y);
            }
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin(GL2.GL_POLYGON);
            for(int i=0; i<verticsNumber; i++){
                double angle = 2.0 * Math.PI * ((double) i / (double) verticsNumber);
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));
                gl.glVertex2f(x + pos.x, y + pos.y);
            }
            gl.glEnd();
        }
    }

    public static class DrawTriangle implements DrawShape {
        protected List<GlUtils.Coordinates2D> triangleCoordinates;

        protected DrawTriangle(){ }

        public DrawTriangle(float x1, float y1, float x2, float y2) {
            this.triangleCoordinates = new ArrayList<GlUtils.Coordinates2D>();
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1, y1));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1 + (x2-x1)/2 , y2));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x2, y1));
        }

        // Draw triangle with GL_LINES mode
        @Override
        public void draw(GL2 gl) {
            //edge1
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            gl.glEnd();

            //edge2
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            gl.glEnd();

            //edge3
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin(GL2.GL_TRIANGLES);
                gl.glVertex3fv(triangleCoordinates.get(0).getVectorValues(), 0);
                gl.glVertex3fv(triangleCoordinates.get(1).getVectorValues(), 0);
                gl.glVertex3fv(triangleCoordinates.get(2).getVectorValues(), 0);
            gl.glEnd();

        }
    }

    public static class DrawRightTriangle extends GlUtils.DrawTriangle{
        public DrawRightTriangle(float x1, float y1, float x2, float y2){
            super();
            this.triangleCoordinates = new ArrayList<GlUtils.Coordinates2D>();
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1, y1));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1, y2));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x2, y1));
        }
    }

    public static class DrawParallelepiped{
        List<Coordinates3D> coordinates3DS;

        /*
         * x, y, z center pos
         * */
        public DrawParallelepiped(float x, float y, float z, float sizeX, float sizeY, float sizeZ){
            float halfSizeX = sizeX/2;
            float halfSizeY = sizeY/2;
            float halfSizeZ = sizeZ/2;

            this.coordinates3DS = new ArrayList<Coordinates3D>();
            // front face
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z + halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z + halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z + halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z + halfSizeZ)); // bottom left

            // back face
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z - halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z - halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z - halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z - halfSizeZ)); // bottom left

            // left face
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z + halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z - halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z - halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z + halfSizeZ)); // bottom left

            // right face
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z + halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z - halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z - halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z + halfSizeZ)); // bottom left

            // top face
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z + halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y + halfSizeY, z - halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z - halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y + halfSizeY, z + halfSizeZ)); // bottom left

            // bottom face
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z + halfSizeZ)); // top left
            coordinates3DS.add(new Coordinates3D(x - halfSizeX, y - halfSizeY, z - halfSizeZ)); // top right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z - halfSizeZ)); // bottom right
            coordinates3DS.add(new Coordinates3D(x + halfSizeX, y - halfSizeY, z + halfSizeZ)); // bottom left
        }

        public void draw(GL2 gl){
            // Draw the cube
            gl.glBegin(GL2.GL_QUADS);

            for(int i=0 ; i<coordinates3DS.size(); i++){
                gl.glVertex3f(coordinates3DS.get(i).x, coordinates3DS.get(i).y, coordinates3DS.get(i).z);
            }

            gl.glEnd();
        }
    }

    public static class DrawCube extends DrawParallelepiped{

        /*
        * x, y, z center pos
        * */
        public DrawCube(float x, float y, float z, float size){
            super(x, y, z, size, size, size);
        }
    }
}
