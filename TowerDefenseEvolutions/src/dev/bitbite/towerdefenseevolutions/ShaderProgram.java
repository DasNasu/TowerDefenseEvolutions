package dev.bitbite.towerdefenseevolutions;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram() throws Exception {
		this.programID = glCreateProgram();
		if(this.programID == 0) throw new Exception("Failed to create ShaderProgram");
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		this.vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		this.fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}
	
	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderID = glCreateShader(shaderType);
		if(shaderID == 0) throw new Exception("Failed to create Shader");
		glShaderSource(shaderID, shaderCode);
		glCompileShader(shaderID);
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) throw new Exception("Failed to compile Shader\n"+glGetShaderInfoLog(shaderID, 1024));
		glAttachShader(this.programID, shaderID);
		return shaderID;
	}
	
	public void link() throws Exception {
		glLinkProgram(this.programID);
		if(glGetProgrami(this.programID, GL_LINK_STATUS) == 0) throw new Exception("Failed to link Shader");
		if(this.vertexShaderID != 0) glDetachShader(this.programID, this.vertexShaderID);
		if(this.fragmentShaderID != 0) glDetachShader(this.programID, this.fragmentShaderID);
		glValidateProgram(this.programID);
		if(glGetProgrami(this.programID, GL_VALIDATE_STATUS) == 0) System.err.print("Warning on validating Shader");
	}
	
	public void bind() {
		glUseProgram(this.programID);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void cleanup() {
		this.unbind();
		if(this.programID != 0) glDeleteProgram(this.programID);
	}
}
