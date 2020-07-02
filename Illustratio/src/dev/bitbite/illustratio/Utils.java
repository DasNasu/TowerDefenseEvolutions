package dev.bitbite.illustratio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
	
	public static String loadResource(String filePath) throws Exception {
		String source = "";
		BufferedReader bReader = new BufferedReader(new InputStreamReader(Utils.class.getResourceAsStream("/resources/shader/"+filePath)));
		String line = null;
		try {
			while((line = bReader.readLine()) != null) {
				source += line+"\n";
			}
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
