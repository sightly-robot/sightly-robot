package de.unihannover.swp2015.robots2.application.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
	/**
	 * Load resource to string.
	 * 
	 * @param path Resource path.
	 * @return Resource content.
	 * @throws IOException Thrown when resource was not found.
	 * @throws URISyntaxException Thrown when resource path is not a valid path.
	 */
	public static String loadResourceAsString(String path) throws IOException, URISyntaxException {
		BufferedReader bf = new BufferedReader( new InputStreamReader(ResourceLoader.class.getResourceAsStream(path)));
		
		String res = "";
		String line = "";
		
		while( (line = bf.readLine()) != null ) {
			res += line;
		}
	
		return res;
	}
	
	/**
	 * Load resource to InputStream
	 * 
	 * @param path Resource file path
	 * @return Resource stream.
	 */
	public static InputStream loadResourceAsInputStream(String path) {
		return ResourceLoader.class.getResourceAsStream(path);	
	}
}
