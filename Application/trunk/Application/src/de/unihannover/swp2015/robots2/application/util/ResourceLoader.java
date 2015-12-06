package de.unihannover.swp2015.robots2.application.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		return new String(Files.readAllBytes(Paths.get(ResourceLoader.class.getResource(path).toURI())));
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
