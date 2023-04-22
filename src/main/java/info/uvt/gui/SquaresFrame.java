package info.uvt.gui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SquaresFrame extends JFrame implements GLEventListener {

    private GLCanvas canvas;
    private Animator animator;

    private Integer windowWidth = 800;
    private Integer windowHeight = 600;

    private float square1Pos = 0f;
    private float square2Pos = 0f;
    private float square1Speed = 0.007f;
    private float square2Speed = 0.01f;
    private  float squareSize = 0.4f;

    private Texture texture1;
    private Texture texture2;

    public SquaresFrame() {
        super("Squares Exercise");

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

        this.initTextures(gl);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

    }

    private void initTextures(GL2 gl){
        try {
            texture1 = TextureIO.newTexture(new File("Texturi/textura2.jpg"), true);
            texture2 = TextureIO.newTexture(new File("Texturi/textura4.jpg"), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        new GlUtils.DrawRectangle(square1Pos - squareSize/2, 0.0f - squareSize/2, square1Pos + squareSize/2, 0.0f + squareSize/2).drawWithTexture(gl, texture1);
        new GlUtils.DrawRectangle(square2Pos - squareSize/2, 0.0f - squareSize/2, square2Pos + squareSize/2, 0.0f + squareSize/2).drawWithTexture(gl, texture2);


        square1Pos += square1Speed;
        square2Pos += square2Speed;
        if(square1Pos >= 1.2f || square1Pos <= -1.2f){
            square1Speed *= -1;
        }
        if(square2Pos >= 1.2f || square2Pos <= -1.2f){
            square2Speed *= -1;
        }
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
