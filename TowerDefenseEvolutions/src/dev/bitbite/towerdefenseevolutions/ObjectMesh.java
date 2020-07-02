package dev.bitbite.towerdefenseevolutions;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

public class ObjectMesh {
	private final int vaoID;
	private final int vboID;
	private final int indexVboID;
	private final int colourVboID;
	private final int vertexCount;
	
	public ObjectMesh(float[] positions, float[] colours, int[] indices) {
		FloatBuffer verticesBuffer = null;
		FloatBuffer colourBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			this.vertexCount = indices.length;
			verticesBuffer.put(positions).flip();
			
			this.vaoID = glGenVertexArrays();
			glBindVertexArray(this.vaoID);
			
			this.vboID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			this.indexVboID = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexVboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			this.colourVboID = glGenBuffers();
			colourBuffer = MemoryUtil.memAllocFloat(colours.length);
			colourBuffer.put(colours).flip();
			glBindBuffer(GL_ARRAY_BUFFER, this.colourVboID);
			glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
			
			
			glBindVertexArray(0);
		} finally {
			if(verticesBuffer != null) MemoryUtil.memFree(verticesBuffer);
			if(indicesBuffer != null) MemoryUtil.memFree(indicesBuffer);
			if(colourBuffer != null) MemoryUtil.memFree(colourBuffer);
		}
	}
	
	public void cleanup() {
		glDisableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(this.vboID);
		glDeleteBuffers(this.indexVboID);
		
		glBindVertexArray(0);
		glDeleteVertexArrays(this.vaoID);
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public int getVaoID() {
		return this.vaoID;
	}
}
