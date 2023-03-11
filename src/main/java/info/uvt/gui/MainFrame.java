package info.uvt.gui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements GLEventListener
{
    private GLCanvas canvas;
    private Animator animator;
    private Integer windowWidth = 800;
    private Integer windowHeight = 600;

    public MainFrame()
    {
        super("Java OpenGL");

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(windowWidth, windowHeight);

        // This method will be explained later
         this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl()
    {
        // Obtaining a reference to the default GL profile
        GLProfile glProfile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glProfile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas();

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);

        this.animator = new Animator();

        this.animator.add(this.canvas);

        this.animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Obtain the GL instance associated with the canvas.
        GL2 gl = canvas.getGL().getGL2();

        // Set the clear color -- the color which will be used to reset the color buffer.
        gl.glClearColor(0, 0, 0, 0);

        // Select the Projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        // Clear the projection matrix and set it to be the identity matrix.
        gl.glLoadIdentity();

        // Set the projection to be orthographic.
        // It could have been as well chosen to be perspective.
        // Select the view volume to be x in the range of 0 to 1, y from 0 to 1 and z from -1 to 1.
        gl.glOrtho(0, windowWidth, 0, windowHeight, -1, 1);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        return;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Retrieve a reference to a GL object. We need it because it contains all the useful OGL methods.
        GL2 gl = canvas.getGL().getGL2();

        // Each time the scene is redrawn we clear the color buffers which is perceived by the user as clearing the scene.
        // Clear the color buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

//    // Set the width of the lines
//        gl.glLineWidth(0.5f);
//
//        gl.glBegin(GL2.GL_LINES);
//            gl.glVertex2f(0.2f, 0.2f);
//            gl.glVertex2f(0.4f, 0.2f);
//            gl.glVertex2f(0.2f, 0.4f);
//            gl.glVertex2f(0.4f, 0.4f);
//        gl.glEnd();
//
//        // Set the size of the point
//        gl.glPointSize(0.5f);
//
//        gl.glBegin(GL2.GL_POINTS);
//            gl.glVertex2f(0.2f, 0.2f);
//            gl.glVertex2f(0.4f, 0.2f);
//            gl.glVertex2f(0.2f, 0.4f);
//            gl.glVertex2f(0.4f, 0.4f);
//        gl.glEnd();
//
//        gl.glLineStipple(1, (short) 0x3F07);
//        gl.glEnable(GL2.GL_LINE_STIPPLE);
//
//        gl.glBegin(GL2.GL_LINES);
//            gl.glVertex2f(0.2f, 0.2f);
//            gl.glVertex2f(0.4f, 0.2f);
//            gl.glVertex2f(0.2f, 0.4f);
//            gl.glVertex2f(0.4f, 0.4f);
//        gl.glEnd();
//
//        gl.glDisable(GL2.GL_LINE_STIPPLE);

        gl.glBegin(GL2.GL_POINTS);
            // Set the vertex color to Red.
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glVertex2f(0.2f, 0.2f);
            // Set the vertex color to Green.
            gl.glColor3f(0.0f, 1.0f, 0.0f);
            gl.glVertex2f(0.4f, 0.2f);
            // Set the vertex color to Blue.
            gl.glColor3f(0.0f, 0.0f, 1.0f);
            gl.glVertex2f(0.2f, 0.4f);
            // Set the vertex color to White.
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glVertex2f(0.4f, 0.4f);
        gl.glEnd();

        // Forcing the scene to be rendered.
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = canvas.getGL().getGL2();

        // Select the viewport -- the display area -- to be the entire widget.
        gl.glViewport(0, 0, width, height);

        // Determine the width to height ratio of the widget.
        double ratio = (double) width / (double) height;

        // Select the Projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        gl.glLoadIdentity();

        // Select the view volume to be x in the range of 0 to 1, y from 0 to 1 and z from -1 to 1.
        // We are careful to keep the aspect ratio and enlarging the width or the height.
        if (ratio < 1)
            gl.glOrtho(0, 1, 0, 1 / ratio, -1, 1);
        else
            gl.glOrtho(0, 1 * ratio, 0, 1, -1, 1);

        // Return to the Modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }
}
