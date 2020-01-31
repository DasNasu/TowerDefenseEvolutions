package dev.bitbite.towerdefenseevolutions;

import static org.lwjgl.glfw.GLFW.glfwInit;

import org.lwjgl.glfw.GLFWErrorCallback;

public class TowerDefenseEvolutions {
	private static GLWindow glWindow;
	private static InputHandler inputHandler;
	private Thread windowThread;
	private Thread inputThread;
	
	public TowerDefenseEvolutions(int vsync) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) throw new IllegalStateException("Could not initialize GLFW");
		TowerDefenseEvolutions.glWindow = new GLWindow(vsync, 1440, 800, "TowerDefense: Evolutions");
		TowerDefenseEvolutions.inputHandler = new InputHandler();
		TowerDefenseEvolutions.glWindow.createWindow();
		this.windowThread = new Thread(TowerDefenseEvolutions.glWindow);
		this.inputThread = new Thread(TowerDefenseEvolutions.inputHandler);
		this.windowThread.start();
		this.inputThread.run();
	}
	
	public static GLWindow getGLWindow() {
		return TowerDefenseEvolutions.glWindow;
	}
	
	public static InputHandler getInputHandler() {
		return TowerDefenseEvolutions.inputHandler;
	}
	
	public static void main(String args[]) {
		new TowerDefenseEvolutions(0);
	}
}
