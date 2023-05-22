package info.uvt.gui.chess;

import com.jogamp.opengl.GL2;
import info.uvt.gui.GlUtils;
import net.java.joglutils.ThreeDS.Face;
import net.java.joglutils.ThreeDS.Model3DS;
import net.java.joglutils.ThreeDS.Obj;
import net.java.joglutils.ThreeDS.Vec3;

import java.awt.*;

public class ChessPiece {

    protected float positionX;
    protected float positionY;
    protected float positionZ;
    protected Color color;

    protected float scalingFactor = 300f;

    protected Model3DS model;

    // Constructor
    public ChessPiece(float positionX, float positionY, float positionZ, Color color, String modelPath) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.color = color;

        // Create an instance of the model class.
        this.model = new Model3DS();
        this.model.load(modelPath);
    }

    public ChessPiece(float positionX, float positionY, float positionZ, Color color, String modelPath, float scal) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.color = color;

        this.scalingFactor = scal;

        // Create an instance of the model class.
        this.model = new Model3DS();
        this.model.load(modelPath);
    }

    public ChessPiece(float positionX, float positionY, float positionZ, Color color, Model3DS model, float scal) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.color = color;

        this.scalingFactor = scal;

        // Create an instance of the model class.
        this.model = model;
    }

    // Render method
    public void render(GL2 gl) {
        // Set the material and color based on the piece's attributes
        GlUtils.setGlColor(gl, this.color);

        gl.glPushMatrix();
        gl.glScaled(1.0 / scalingFactor, 1.0 / scalingFactor, 1.0 / scalingFactor);

        gl.glTranslatef(positionX * scalingFactor, positionY * scalingFactor, positionZ * scalingFactor);

        // Apply rotation transformation
//        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);

        this.renderObject(gl, this.model);

        gl.glPopMatrix();
    }

    private void renderObject(GL2 gl, final Model3DS model) {
        for (int objectI = 0; objectI < model.getNumberOfObjects(); objectI++) {
            Obj object = model.getObject(objectI);

            gl.glBegin(GL2.GL_TRIANGLES);
            for (int faceI = 0; faceI < object.numOfFaces; faceI++) {
                Face face = object.faces[faceI];

                for (int vertexI = 0; vertexI < 3; vertexI++) {
                    Vec3 vertex = object.verts[face.vertIndex[vertexI]];
                    Vec3 normal = object.normals[face.vertIndex[vertexI]];

                    if (object.hasTexture) {
                        Vec3 texture = object.texVerts[face.vertIndex[vertexI]];
                        gl.glTexCoord2d(texture.x, texture.y);
                    }
                    gl.glNormal3f(normal.x, normal.y, normal.z);
                    gl.glVertex3f(vertex.x, vertex.y, vertex.z);
                }
            }

            gl.glEnd();
        }
    }

}
