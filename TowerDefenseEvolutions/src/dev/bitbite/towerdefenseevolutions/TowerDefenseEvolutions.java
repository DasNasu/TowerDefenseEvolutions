package dev.bitbite.towerdefenseevolutions;

import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwInit;

public class TowerDefenseEvolutions {
	private GLWindow glWindow;
	
	public TowerDefenseEvolutions(int vsync) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) throw new IllegalStateException("Could not initialize GLFW");
		this.glWindow = new GLWindow(vsync, 1440, 800, "TowerDefense: Evolutions");
		this.glWindow.createWindow();
		this.glWindow.loop();
		this.glWindow.terminateWindow();
	}
	
	public static void main(String args[]) {
		new TowerDefenseEvolutions(1);
	}
}
