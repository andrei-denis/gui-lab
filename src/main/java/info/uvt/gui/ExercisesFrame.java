package info.uvt.gui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExercisesFrame extends JFrame implements GLEventListener {

    private GLCanvas canvas;
    private Animator animator;

    private Integer windowWidth = 800;
    private Integer windowHeight = 600;
    private boolean house;
    private float sunPos = -1.2f;
    private float sunSpeed = 0.007f;

    public ExercisesFrame(boolean house) {
        super(house ? "House Exercise" : "Forms Exercise");
        this.house = house;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(windowWidth, windowHeight);

        this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl() {
        // Creating a new GL profile.
        GLProfile glprofile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glprofile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Try to enable 2x anti aliasing. It should be supported on most hardware.
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas(capabilities);

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);

        if(house){
            this.animator = new Animator();

            this.animator.add(this.canvas);

            this.animator.start();
        }
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if(house){
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        else {
            gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        }

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLineWidth(2.0f);

        if(this.house){
            house(gl);
        }
        else {
            forms(gl);
        }

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, windowWidth, windowHeight);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float aspect = (float) windowWidth / (float) windowHeight;
        if (windowWidth <= windowHeight) {
            gl.glOrtho(-1.0, 1.0, -1.0 / aspect, 1.0 / aspect, -1.0, 1.0);
        } else {
            gl.glOrtho(-1.0 * aspect, 1.0 * aspect, -1.0, 1.0, -1.0, 1.0);
        }
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    // Draw a square by using GL_LINES, GL_LINE_STRIP and GL_LINE_LOOP. Each vertex should have a different colour
    // Draw a circle by using GL_LINE_LOOP
    private void forms(GL2 gl){
        DrawSquare drawSquare1 = new DrawSquare(-1.2f, 0.8f, -0.8f, 0.4f);
        drawSquare1.useGlLines(gl);

        DrawSquare drawSquare2 = new DrawSquare(-0.4f, 0.8f, 0f, 0.4f);
        drawSquare2.useGlLineStrip(gl);

        DrawSquare drawSquare3 = new DrawSquare(0.4f, 0.8f, 0.8f, 0.4f);
        drawSquare3.useGlLineLoop(gl);

        DrawCircle drawCircle = new DrawCircle(0, -0.2f, 72, 0.4f);
        GlUtils.setGlColor(gl, Color.BLACK);
        drawCircle.useGlLineLoop(gl);
    }

    // Draw a house by first using GL_LINES. Color it in different colours. Add the Sun (yellow circle) and make it move from left to right
    private void house(GL2 gl){
        // Actually a rectangle :)
        DrawSquare houseBody = new DrawSquare(-0.35f, -0.95f, 0.35f, -0.2f);
        houseBody.useGlLines(gl);

        DrawSquare houseDoor = new DrawSquare(-0.07f, -0.95f, 0.07f, -0.7f);
        houseDoor.useGlLines(gl);

        DrawSquare houseWindow1 = new DrawSquare(-0.3f, -0.45f, -0.15f, -0.3f);
        houseWindow1.useGlLines(gl);

        DrawSquare houseWindow2 = new DrawSquare(0.15f, -0.45f, 0.3f, -0.3f);
        houseWindow2.useGlLines(gl);

        DrawTriangle houseRoof = new DrawTriangle(-0.35f, -0.2f, 0.35f, 0.1f);
        houseRoof.useGlLines(gl);

        DrawCircle sun = new DrawCircle(sunPos, 0.7f, 36, 0.1f);
        GlUtils.setGlColor(gl, Color.YELLOW);
        sun.useGlLineLoop(gl);
        sunPos += sunSpeed;
        if(sunPos >= 1.2f || sunPos <= -1.2f){
            sunSpeed *= -1;
        }

    }

    public class DrawSquare{
        List<GlUtils.Coordinates2D> squareCoordinates;

        public DrawSquare(float x1, float y1, float x2, float y2){
            this.squareCoordinates = new ArrayList<GlUtils.Coordinates2D>();
            squareCoordinates.add(new GlUtils.Coordinates2D(x1, y1));
            squareCoordinates.add(new GlUtils.Coordinates2D(x1, y2));
            squareCoordinates.add(new GlUtils.Coordinates2D(x2, y2));
            squareCoordinates.add(new GlUtils.Coordinates2D(x2, y1));
        }

        // Draw square with GL_LINES mode
        public void useGlLines(GL2 gl){
            //edge1
            gl.glBegin( GL2.GL_LINES );
            GlUtils.setGlColor(gl, Color.RED);
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            GlUtils.setGlColor(gl, Color.GREEN);
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            gl.glEnd();

            //edge2
            gl.glBegin( GL2.GL_LINES );
            GlUtils.setGlColor(gl, Color.ORANGE);
            gl.glVertex3f( squareCoordinates.get(1).x,squareCoordinates.get(1).y,0 );
            GlUtils.setGlColor(gl, Color.BLUE);
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            gl.glEnd();

            //edge3
            gl.glBegin( GL2.GL_LINES );
            GlUtils.setGlColor(gl, Color.MAGENTA);
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            GlUtils.setGlColor(gl, Color.YELLOW);
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();

            //edge4
            gl.glBegin( GL2.GL_LINES );
            GlUtils.setGlColor(gl, Color.PINK);
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            GlUtils.setGlColor(gl, Color.CYAN);
            gl.glVertex3f( squareCoordinates.get(0).x,squareCoordinates.get(0).y,0 );
            gl.glEnd();
        }

        // Draw square with GL_LINE_STRIP mode
        public void useGlLineStrip(GL2 gl){
            //edge1,2,3
            gl.glBegin( GL2.GL_LINE_STRIP );
            GlUtils.setGlColor(gl, Color.RED);
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            GlUtils.setGlColor(gl, Color.GREEN);
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            GlUtils.setGlColor(gl, Color.BLUE);
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            GlUtils.setGlColor(gl, Color.PINK);
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();

            //edge4
            gl.glBegin( GL2.GL_LINE_STRIP );
            GlUtils.setGlColor(gl, Color.BLACK);
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            GlUtils.setGlColor(gl, Color.ORANGE);
            gl.glVertex3f( squareCoordinates.get(0).x,squareCoordinates.get(0).y,0 );
            gl.glEnd();
        }

        // Draw square with GL_LINE_LOOP mode
        public void useGlLineLoop(GL2 gl){
            //edge1,2,3,4
            gl.glBegin( GL2.GL_LINE_LOOP );
            GlUtils.setGlColor(gl, Color.PINK);
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            GlUtils.setGlColor(gl, Color.BLACK);
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            GlUtils.setGlColor(gl, Color.DARK_GRAY);
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            GlUtils.setGlColor(gl, Color.YELLOW);
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();
        }
    }

    public class DrawCircle{
        private Integer verticsNumber;
        private Float radius;
        private GlUtils.Coordinates2D pos;

        public DrawCircle(float x, float y, int nrVertics, float radius){
            this.verticsNumber = nrVertics;
            this.radius = radius;
            this.pos = new GlUtils.Coordinates2D(x, y);
        }

        public void useGlLineLoop(GL2 gl){
            gl.glBegin(GL2.GL_LINE_LOOP);
            for(int i=0; i<verticsNumber; i++){
                double angle = 2.0 * Math.PI * ((double) i / (double) verticsNumber);
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));
                gl.glVertex2f(x + pos.x, y + pos.y);
            }
            gl.glEnd();
        }
    }

    public class DrawTriangle {
        List<GlUtils.Coordinates2D> triangleCoordinates;

        public DrawTriangle(float x1, float y1, float x2, float y2) {
            this.triangleCoordinates = new ArrayList<GlUtils.Coordinates2D>();
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1, y1));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x1 + (x2-x1)/2 , y2));
            triangleCoordinates.add(new GlUtils.Coordinates2D(x2, y1));
        }

        // Draw triangle with GL_LINES mode
        public void useGlLines(GL2 gl) {
            //edge1
            gl.glBegin(GL2.GL_LINES);
            GlUtils.setGlColor(gl, Color.RED);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            GlUtils.setGlColor(gl, Color.GREEN);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            gl.glEnd();

            //edge2
            gl.glBegin(GL2.GL_LINES);
            GlUtils.setGlColor(gl, Color.ORANGE);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            GlUtils.setGlColor(gl, Color.BLUE);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            gl.glEnd();

            //edge3
            gl.glBegin(GL2.GL_LINES);
            GlUtils.setGlColor(gl, Color.MAGENTA);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            GlUtils.setGlColor(gl, Color.YELLOW);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            gl.glEnd();
        }
    }
}
