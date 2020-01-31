package dev.bitbite.towerdefenseevolutions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
	private static File f;
	
	
	public Utils(String filePath) {
		File f = new File(getClass().getClassLoader().getResource("resource/shader/"+filePath).getFile());
	}
	
	public static String loadResource(String filePath) throws Exception {
		new Utils(filePath);
		String source = null;
		BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line = null;
		try {
			while((line = bReader.readLine()) != null) source += line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(source == null) {
			System.out.println("nothing to write");
			throw new Exception("no content in shaderfile");
		} else {
			return source;
		}
	}
}
