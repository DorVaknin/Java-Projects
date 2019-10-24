package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

public class Materials {
	// TODO: Use this class to update the color of the vertices when drawing.
	private static final float DARK_GRAY[] = { 0.2f, 0.2f, 0.2f };
	private static final float DARK_RED[] = { 0.25f, 0.01f, 0.01f };
	private static final float RED[] = { 0.7f, 0f, 0f };
	private static final float BLACK[] = { 0.05f, 0.05f, 0.05f };
	private static final float DARK_GREEN[] = {0.0f, 0.5f, 0.0f};
	

	public static void SetMetalMaterial(GL2 gl, float[] color) {
		gl.glColor3fv(color, 0);
	}

	public static void SetBlackMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, BLACK);
	}

	public static void SetRedMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, RED);
	}

	public static void SetDarkRedMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, DARK_RED);
	}
	
	public static void SetDarkGreyMetalMaterial(GL2 gl) {
		SetMetalMaterial(gl, DARK_GRAY);
	}

	public static void setMaterialTire(GL2 gl) {
		float col[] = { .05f, .05f, .05f };
		gl.glColor3fv(col,0);
	}

	public static void setMaterialRims(GL2 gl) {
		gl.glColor3fv(DARK_GRAY,0);
	}
	// we took material settings from : http://www.it.hiof.no/~borres/j3d/explain/light/p-materials.html
    public static void setGreenMaterial(GL2 gl) {
    	float[] mat_ambient ={ 0.135f, 0.2225f, 0.1575f, 0.95f };
    	float[] mat_diffuse ={0.54f, 0.89f, 0.63f, 0.95f };
    	float[] mat_specular ={0.316228f, 0.316228f, 0.316228f, 0.95f };
    	float shine = 12.8f;
    	gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, shine);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, mat_ambient, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, mat_specular, 0);
    }

    public static void setAsphaltMaterial(GL2 gl) {
    	float[] mat_ambient ={ 1f,1f,1f,1.0f };
    	float[] mat_diffuse ={ 0.5f,1f,1f,1.0f};
    	float[] mat_specular ={ 0.7f,0.7f,0.7f,1.0f};
    	float shine = 10.0f;
        gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, shine);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, mat_ambient, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, mat_specular, 0);
    }

    public static void setWoodenBoxMaterial(GL2 gl) {
    	float[] mat_ambient ={ 0.5f,0.5f,0.5f,1.0f };
    	float[] mat_diffuse ={0.7f,0.7f,0.4f,1.0f};
    	float[] mat_specular ={0.7f,0.7f,0.04f,1.0f };
    	float shine = 12.0f;
        gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, shine);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, mat_ambient, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, mat_specular, 0);
    }
}
