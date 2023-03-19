package info.uvt.gui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HouseFrame extends JFrame implements GLEventListener {

    private GLCanvas canvas;
    private Animator animator;

    private Integer windowWidth = 800;
    private Integer windowHeight = 600;

    private int house;
    private float sunPos = -1.2f;
    private float sunSpeed = 0.007f;

    public HouseFrame() {
        super("House Exercise");

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

        this.animator = new Animator();

        this.animator.add(this.canvas);

        this.animator.start();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        // Activate the GL_LINE_SMOOTH state variable. Other options include
        // GL_POINT_SMOOTH and GL_POLYGON_SMOOTH.
        gl.glEnable(GL.GL_LINE_SMOOTH);

        // Activate the GL_BLEND state variable. Means activating blending.
        gl.glEnable(GL.GL_BLEND);

        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // Control GL_LINE_SMOOTH_HINT by applying the GL_DONT_CARE behavior.
        // Other behaviours include GL_FASTEST or GL_NICEST.
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        house = gl.glGenLists(1);
        // Generate the Display List
        gl.glNewList(house, GL2.GL_COMPILE);
        drawHouse(gl);
        gl.glEndList();

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        GlUtils.setGlColor(gl, Color.YELLOW);
        GlUtils.DrawCircle sun = new GlUtils.DrawCircle(sunPos, 0.7f, 36, 0.1f);
        sun.drawFill(gl);
        sunPos += sunSpeed;
        if(sunPos >= 1.2f || sunPos <= -1.2f){
            sunSpeed *= -1;
        }

        gl.glCallList(house);
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

    // Draw a house by using GL_TRIANGLES. The Sun should be a filled yellow circle and should move through the scene from left to right.
    // Fill each section of the house with a different color. Use display lists
    private void drawHouse(GL2 gl){
        GlUtils.setGlColor(gl, Color.GRAY);
        GlUtils.DrawRightTriangle houseBodyBL = new GlUtils.DrawRightTriangle(-0.35f, -0.95f, 0.35f, -0.2f);
        houseBodyBL.drawFill(gl); // draw bottom left part

        GlUtils.setGlColor(gl, Color.LIGHT_GRAY);
        GlUtils.DrawRightTriangle houseBodyTR = new GlUtils.DrawRightTriangle(0.35f, -0.2f, -0.35f, -0.95f);
        houseBodyTR.drawFill(gl); // draw top right part

        GlUtils.setGlColor(gl, Color.CYAN);
        GlUtils.DrawRightTriangle houseDoorBL = new GlUtils.DrawRightTriangle(-0.07f, -0.95f, 0.07f, -0.7f);
        houseDoorBL.drawFill(gl);

        GlUtils.setGlColor(gl, Color.PINK);
        GlUtils.DrawRightTriangle houseDoorTR = new GlUtils.DrawRightTriangle(0.07f, -0.7f, -0.07f, -0.95f);
        houseDoorTR.drawFill(gl);

        GlUtils.setGlColor(gl, Color.BLUE);
        GlUtils.DrawRightTriangle houseWindow1BL = new GlUtils.DrawRightTriangle(-0.3f, -0.45f, -0.15f, -0.3f);
        houseWindow1BL.drawFill(gl);

        GlUtils.setGlColor(gl, Color.MAGENTA);
        GlUtils.DrawRightTriangle houseWindow1TR = new GlUtils.DrawRightTriangle(-0.15f, -0.3f, -0.3f, -0.45f);
        houseWindow1TR.drawFill(gl);

        GlUtils.setGlColor(gl, Color.YELLOW);
        GlUtils.DrawRightTriangle houseWindow2BL = new GlUtils.DrawRightTriangle(0.15f, -0.45f, 0.3f, -0.3f);
        houseWindow2BL.drawFill(gl);

        GlUtils.setGlColor(gl, Color.GREEN);
        GlUtils.DrawRightTriangle houseWindow2TR = new GlUtils.DrawRightTriangle(0.3f, -0.3f, 0.15f, -0.45f);
        houseWindow2TR.drawFill(gl);

        GlUtils.setGlColor(gl, Color.RED);
        GlUtils.DrawTriangle houseRoof = new GlUtils.DrawTriangle(-0.35f, -0.2f, 0.35f, 0.1f);
        houseRoof.drawFill(gl);
    }

}
