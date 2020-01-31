package dev.bitbite.towerdefenseevolutions;


import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class InputHandler implements Runnable {
	
	public InputHandler() {
		
	}
	
	@Override
	public void run() {
		while(!glfwWindowShouldClose(TowerDefenseEvolutions.getGLWindow().getWindow())) {
			glfwPollEvents();
		}
		TowerDefenseEvolutions.getGLWindow().getShaderProgram().cleanup();
	}
	
	public void handleKeyboardInput(int key) {
		System.out.println(key+" got pressed");
	}
	
	public void handleMouseMovement(double x, double y) {
		System.out.println("moved to x: "+x+", y: "+y);
	}
	
	public void handleMouseInput(int button) {
		System.out.println(button+" got pressed");
	}
	
	public void handleMouseScroll(double x, double y) {
		if(y > 0) {
			System.out.println("scrolled up");
		} else {
			System.out.println("scrolled down");
		}
	}
}
