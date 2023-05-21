package info.uvt.gui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import info.uvt.gui.chess.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChessBoardFrame extends JFrame implements GLEventListener, KeyListener {

    private GLCanvas canvas;

    private GLU glu;

    private Animator animator;

    private Integer windowWidth = 800;
    private Integer windowHeight = 800;

    // Define camera variables
    private float eyeX = 0.0f;      // X-coordinate of the eye position
    private float eyeY = 0.0f;      // Y-coordinate of the eye position
    private float eyeZ = 5.0f;      // Z-coordinate of the eye position
    private float centerX = 0.0f; // X-coordinate of the look-at point
    private float centerY = 0.0f; // Y-coordinate of the look-at point
    private float centerZ = 0.0f; // Z-coordinate of the look-at point

    private float pitch = 0.0f;     // Pitch angle of the camera (rotation around the X-axis)
    private float yaw = 0.0f;       // Yaw angle of the camera (rotation around the Y-axis)
    private float moveSpeed = 0.1f; // Speed of camera movement

    public ChessBoardFrame() {
        super("ChessBoard Exercise");

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

        glu = new GLU();

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas(capabilities);

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);
        this.canvas.addKeyListener(this);

        this.animator = new Animator();

        this.animator.add(this.canvas);

        this.animator.start();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // Set clear color to white
        gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing
        gl.glEnable(GL2.GL_COLOR_MATERIAL); // Enable coloring
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        this.setPerspectiveProjection(gl);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        this.setModelViewMatrix(gl);

        chessboard(gl);

        pieces(gl);
    }

    private void setPerspectiveProjection(GL2 gl){
        // Define the perspective projection matrix
        float fieldOfView = 60.0f; // Field of view angle in degrees
        float aspectRatio = (float) getWidth() / getHeight(); // Aspect ratio of the window
        float nearPlane = 0.1f; // Distance to the near clipping plane
        float farPlane = 100.0f; // Distance to the far clipping plane

        glu.gluPerspective(fieldOfView, aspectRatio, nearPlane, farPlane);
    }

    private void setModelViewMatrix(GL2 gl){
        float upX = 0.0f; // X-coordinate of the up vector
        float upY = 2.0f; // Y-coordinate of the up vector
        float upZ = 0.0f; // Z-coordinate of the up vector

        glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

    }

    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

        final GL2 gl = drawable.getGL().getGL2();
        if( height <= 0 )
            height = 1;

        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    private void chessboard(GL2 gl){
        final int squares = 8;

        boolean white = true;
        float posX = -1.5f, posZ = -1.0f, posY = -1f;
        float size = 0.5f; //2f/squares;

        for(int i=0; i<squares; i++){
            for(int j=0; j<squares; j++){
                if (white) {
                    gl.glColor3f(0.8f, 0.8f, 0.8f); // White square color
                } else {
                    gl.glColor3f(0.2f, 0.2f, 0.2f); // Black square color
                }
//                gl.glColor3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                new GlUtils.DrawParallelepiped(posX, posY, posZ, size, 0.1f, size).draw(gl);

                white = !white;
                posX+=size;
            }
            white = !white;
            posX = -1.5f;
            posZ += size;
        }

        // Draw border
        gl.glColor3f(0.2f, 0.12f, 0.01f); // Brown color
        // Back border
        posX = -1.5f - size;
        for(int i=0; i<squares+2; i++){
            new GlUtils.DrawParallelepiped(posX, posY, posZ, size, 0.1f, size).draw(gl);
            posX+=size;
        }

        // Front border
        posX = -1.5f - size;
        posZ = -1f - size;
        for(int i=0; i<squares+2; i++){
            new GlUtils.DrawParallelepiped(posX, posY, posZ, size, 0.1f, size).draw(gl);
            posX+=size;
        }

        // Left border
        posX = -1.5f - size;
        posZ = -1f;
        for(int i=0; i<squares; i++){
            new GlUtils.DrawParallelepiped(posX, posY, posZ, size, 0.1f, size).draw(gl);
            posZ+=size;
        }

        // Right border
        posX = -1.5f + (size*squares);
        posZ = -1f;
        for(int i=0; i<squares; i++){
            new GlUtils.DrawParallelepiped(posX, posY, posZ, size, 0.1f, size).draw(gl);
            posZ+=size;
        }

    }

    private void pieces(GL2 gl){
        float posX = -1.5f, posZ = -1.0f, posY = -0.9f;
        float size = 0.5f;

        // White
        new Rook(posX, posY, posZ, Color.CYAN).render(gl);
        posX += size;

        new Knight(posX, posY, posZ, Color.DARK_GRAY).render(gl);
        posX += size;

        new Bishop(posX, posY, posZ, Color.GREEN).render(gl);
        posX += size;

        new Queen(posX, posY, posZ, Color.RED).render(gl);
        posX += size;

        new King(posX, posY, posZ, Color.BLUE).render(gl);
        posX += size;

        new Bishop(posX, posY, posZ, Color.GREEN).render(gl);
        posX += size;

        new Knight(posX, posY, posZ, Color.DARK_GRAY).render(gl);
        posX += size;

        new Rook(posX, posY, posZ, Color.CYAN).render(gl);
        posX += size;

        posX = -1.5f;
        posZ += size;
        for(int i=0; i<8; i++){
            new Pawn(posX, posY, posZ, Color.WHITE).render(gl);
            posX += size;
        }

        // Black
        posZ += size *5;
        posX = -1.5f;
        for(int i=0; i<8; i++){
            new Pawn(posX, posY, posZ, Color.BLACK).render(gl);
            posX += size;
        }
        posZ += size;
        posX = -1.5f;

        new Rook(posX, posY, posZ, Color.CYAN).render(gl);
        posX += size;

        new Knight(posX, posY, posZ, Color.DARK_GRAY).render(gl);
        posX += size;

        new Bishop(posX, posY, posZ, Color.GREEN).render(gl);
        posX += size;

        new Queen(posX, posY, posZ, Color.RED).render(gl);
        posX += size;

        new King(posX, posY, posZ, Color.BLUE).render(gl);
        posX += size;

        new Bishop(posX, posY, posZ, Color.GREEN).render(gl);
        posX += size;

        new Knight(posX, posY, posZ, Color.DARK_GRAY).render(gl);
        posX += size;

        new Rook(posX, posY, posZ, Color.CYAN).render(gl);
        posX += size;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Handle camera movement based on the key pressed
        switch (key) {
            case KeyEvent.VK_W:
                moveForward();
                break;
            case KeyEvent.VK_S:
                moveBackward();
                break;
            case KeyEvent.VK_A:
                moveLeft();
                break;
            case KeyEvent.VK_D:
                moveRight();
                break;
        }
    }

    private void moveForward() {
        float angleRad = (float) Math.toRadians(yaw);
        float deltaX = -moveSpeed * (float) Math.sin(angleRad);
        float deltaZ = -moveSpeed * (float) Math.cos(angleRad);
        eyeX += deltaX;
        eyeZ += deltaZ;
    }

    private void moveBackward() {
        float angleRad = (float) Math.toRadians(yaw);
        float deltaX = moveSpeed * (float) Math.sin(angleRad);
        float deltaZ = moveSpeed * (float) Math.cos(angleRad);
        eyeX += deltaX;
        eyeZ += deltaZ;
    }

    private void moveLeft() {
        float angleRad = (float) Math.toRadians(yaw - 90);
        float deltaX = moveSpeed * (float) Math.sin(angleRad);
        float deltaZ = moveSpeed * (float) Math.cos(angleRad);
        eyeX += deltaX;
        eyeZ += deltaZ;
    }

    private void moveRight() {
        float angleRad = (float) Math.toRadians(yaw + 90);
        float deltaX = moveSpeed * (float) Math.sin(angleRad);
        float deltaZ = moveSpeed * (float) Math.cos(angleRad);
        eyeX += deltaX;
        eyeZ += deltaZ;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
