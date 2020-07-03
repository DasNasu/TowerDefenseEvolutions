package dev.bitbite.illustratio;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

public class GLWindow {
	private long window;
	private int width;
	private int height;
	private String title;
	
	public GLWindow(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void createWindow() {
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwDefaultWindowHints();
		this.window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if(this.window == NULL) throw new RuntimeException("Failed to create window");
		try(MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(this.window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(this.window, (vidmode.width()-pWidth.get(0))/2, (vidmode.height()-pHeight.get(0))/2);
		}
		glfwMakeContextCurrent(this.window);
		GL.createCapabilities();
		GLUtil.setupDebugMessageCallback();

		glfwShowWindow(this.window);
		
		glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
			Illustratio.getInputHandler().handleKeyboardInput(key);
		});
		glfwSetMouseButtonCallback(this.window, (window, button, action, mods) -> {
			Illustratio.getInputHandler().handleMouseInput(button);
		});
		glfwSetCursorPosCallback(this.window, (window, xpos, ypos) -> {
			Illustratio.getInputHandler().handleMouseMovement(xpos, ypos);
		});
		glfwSetScrollCallback(this.window, (window, xoffset, yoffset) -> {
			Illustratio.getInputHandler().handleMouseScroll(xoffset, yoffset);
		});
		glfwSetFramebufferSizeCallback(this.window, (window, width, height) -> {
			System.out.println("new width: "+width+", new height: "+height);
		});
	}
	
	public void terminateWindow() {
		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public long getWindow() {
		return this.window;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
