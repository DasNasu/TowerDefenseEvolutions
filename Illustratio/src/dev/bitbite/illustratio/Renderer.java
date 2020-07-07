package dev.bitbite.illustratio;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

import org.joml.Matrix4f;

public class Renderer {
	private static ShaderProgram shaderProgram;
	private static GLWindow window;
	private static float fieldOfView;
	private static float zNear;
	private static float zFar;
	private Transformation transformation;
	private int vsync;
	
	public Renderer(int vsync, GLWindow window, float radiants, float zNear, float zFar) {
		this.vsync = vsync;
		Renderer.window = window;
		Renderer.fieldOfView = (float) Math.toRadians(radiants);
		Renderer.zNear = zNear;
		Renderer.zFar = zFar;
		this.transformation = new Transformation();
		Renderer.window.createWindow();
		this.enableCulling();
		try {
			Renderer.shaderProgram = new ShaderProgram();
			Renderer.shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
			Renderer.shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
			Renderer.shaderProgram.link();
			Renderer.shaderProgram.createUniform("projectionMatrix");
			Renderer.shaderProgram.createUniform("worldMatrix");
			Renderer.shaderProgram.createUniform("textureSampler");
		} catch (Exception e) {
			e.printStackTrace();
		}
		glfwSwapInterval(this.vsync);
	}
	
	public void render(Model[] models) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.shaderProgram.bind();
		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix(Renderer.fieldOfView,Renderer.window.getWidth(), Renderer.window.getHeight(), Renderer.zNear, Renderer.zFar);
		Renderer.shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		Renderer.shaderProgram.setUniform("textureSampler",  0);
		for(Model m : models) {
			Matrix4f worldMatrix = this.transformation.getWorldMatrix(m.getPosition(), m.getRotation(), m.getScale());
			Renderer.shaderProgram.setUniform("worldMatrix", worldMatrix);
			
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, Illustratio.model.getMesh().getTexture().getID());
			glBindVertexArray(m.getMesh().getVaoID());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glDrawElements(GL_TRIANGLES, m.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glBindVertexArray(0);
			
		}
		Renderer.shaderProgram.unbind();
		glfwSwapBuffers(Renderer.window.getWindow());
	}
	
	public void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	public void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public ShaderProgram getShaderProgram() {
		return Renderer.shaderProgram;
	}
	
	public GLWindow getGLWindow() {
		return Renderer.window;
	}
}
