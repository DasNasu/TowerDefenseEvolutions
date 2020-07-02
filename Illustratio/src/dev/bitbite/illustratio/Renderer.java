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
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {
	private static ShaderProgram shaderProgram;
	private static GLWindow window;
	

	private int vsync;
	
	public Renderer(int vsync, GLWindow window) {
		this.vsync = vsync;
		Renderer.window = window;
		Renderer.window.createWindow();
		try {
			Renderer.shaderProgram = new ShaderProgram();
			Renderer.shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
			Renderer.shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
			Renderer.shaderProgram.link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		glfwSwapInterval(this.vsync);
	}
	
	public void render(ObjectMesh mesh) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.shaderProgram.bind();
		glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		Renderer.shaderProgram.unbind();
		glfwSwapBuffers(Renderer.window.getWindow());
	}
	
	public ShaderProgram getShaderProgram() {
		return Renderer.shaderProgram;
	}
	
	public GLWindow getGLWindow() {
		return Renderer.window;
	}
}
