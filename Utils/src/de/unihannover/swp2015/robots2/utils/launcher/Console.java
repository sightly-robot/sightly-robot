package de.unihannover.swp2015.robots2.utils.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.SwingUtilities;

import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.Window;

public class Console implements ExecuteStreamHandler {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private InputStream inputStream;
	
	private TextArea consoleOutput;
	private Window targetView;
	
	private StreamHandler streamHandler;
	
	public Console(ConsoleView view) {
		this.targetView = view.getOutputArea().getWindow();
		this.consoleOutput = view.getOutputArea();
	}
	
	public Window getView() {
		return targetView;
	}

	@Override
	public void setProcessInputStream(OutputStream os) throws IOException {
		// don't need to process 
	}

	@Override
	public void setProcessErrorStream(InputStream is) throws IOException {
		// don't need to process 
	}

	@Override
	public void setProcessOutputStream(InputStream is) throws IOException {
		this.inputStream = is;
	}

	@Override
	public void start() throws IOException {
		streamHandler = new StreamHandler(inputStream, consoleOutput);
		Thread logThread = new Thread(streamHandler);
		logThread.setDaemon(true);
		logThread.start();
	}

	@Override
	public void stop() throws IOException {
		streamHandler.shutdown();
	}
	
	private class StreamHandler implements Runnable {

		private InputStream is;		
		private TextArea consoleOutput;
		
		private boolean kill = false;
		
		public StreamHandler(InputStream is, TextArea area) {
			this.is = is;
			this.consoleOutput = area;
		}
		
		public void shutdown() {
			this.kill = true;
		}

		@Override
		public void run() {
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			
			while(!kill) {

				try {
					final String output = bf.readLine();
					
					if( output != null && output.length() > 0 ) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								consoleOutput.setText(consoleOutput.getText() + output);
							}
						});
					}
				} catch (IOException e) {
					LOGGER.debug(e);
				}			
			}
			
			try {
				bf.close();
			} catch (IOException e) {
				LOGGER.error("Closing the reader failed.", e);
			}
		}
		
	}
	
}
