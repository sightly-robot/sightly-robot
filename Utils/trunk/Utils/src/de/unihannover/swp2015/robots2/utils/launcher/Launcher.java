package de.unihannover.swp2015.robots2.utils.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

public class Launcher extends Window implements Bindable {

	@BXML private TextInput hostInput;
	@BXML private ImageView imageServerRunning;
	@BXML private ImageView imageServerStopped;
	@BXML private PushButton startGameserver;
	@BXML private PushButton stopGameserver;
	@BXML private PushButton openGameserverConsole;
	@BXML private PushButton startControlpanel;
	@BXML private PushButton startVisualization;
	@BXML private ImageView imageRobotRunning;
	@BXML private ImageView imageRobotStopped;
	@BXML private PushButton startRobot;
	@BXML private PushButton stopRobot;
	@BXML private PushButton openRobotConsole;
	
	private Executor gameserver;
	private DefaultExecuteResultHandler gameserverResult;
	private Console gameserverConsole;

	private Executor robot;
	private DefaultExecuteResultHandler robotResult;
	private Console robotConsole;
	
	private Executor vis;
	private DefaultExecuteResultHandler visResult;
	private Console visConsole;

	private Executor app;
	private DefaultExecuteResultHandler appResult;
	private Console appConsole;

	private String javaExecutable;
	private String classPath;

	@Override
	public void initialize(Map<String, Object> namespace, URL arg1, Resources arg2) {

		this.prepareJavaPaths();
		this.prepareConsole();
		this.prepareExecutor();

		this.startGameserver.getButtonPressListeners().add(this.startGameserverAction);
		this.stopGameserver.getButtonPressListeners().add(this.stopGameserverAction);
		this.openGameserverConsole.getButtonPressListeners().add(this.openGameserverConsoleAction);
		this.startControlpanel.getButtonPressListeners().add(this.startControlpanelAction);
		this.startVisualization.getButtonPressListeners().add(this.startVisualizationAction);
		this.startRobot.getButtonPressListeners().add(this.startRobotAction);
		this.stopRobot.getButtonPressListeners().add(this.stopRobotAction);
		this.openRobotConsole.getButtonPressListeners().add(this.openRobotConsoleAction);
	}

	public void killAllProcess() {
		if (gameserver != null && gameserver.getWatchdog() != null) {
			gameserver.getWatchdog().destroyProcess();
		}

		if (robot != null && robot.getWatchdog() != null) {
			robot.getWatchdog().destroyProcess();
		}
	}

	private void prepareJavaPaths() {
		this.javaExecutable = System.getProperty("java.home") + "/bin/java";

		URL[] classPathUrls = ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs();

		for (int i = 0; i < classPathUrls.length; i++) {
			classPath += classPathUrls[i];
			if (i < classPathUrls.length - 1) {
				this.classPath += File.pathSeparatorChar;
			}
		}
	}
	
	private void prepareConsole() {
		BXMLSerializer roboSerial = new BXMLSerializer();
		BXMLSerializer appSerial = new BXMLSerializer();
		BXMLSerializer visSerial = new BXMLSerializer();
		BXMLSerializer serverSerial = new BXMLSerializer();
		
		try {
			ConsoleView robotOutput = (ConsoleView) roboSerial.readObject(getClass().getResource(
				"/de/unihannover/swp2015/robots2/utils/launcher/bxml/consoleView.bxml"));
			robotConsole = new Console(robotOutput);

			ConsoleView visOutput = (ConsoleView) visSerial.readObject(getClass().getResource(
				"/de/unihannover/swp2015/robots2/utils/launcher/bxml/consoleView.bxml"));
			visConsole = new Console(visOutput);
			
			ConsoleView appOutput = (ConsoleView) appSerial.readObject(getClass().getResource(
					"/de/unihannover/swp2015/robots2/utils/launcher/bxml/consoleView.bxml"));
			appConsole = new Console(appOutput);

			ConsoleView serverOutput = (ConsoleView) serverSerial.readObject(getClass().getResource(
				"/de/unihannover/swp2015/robots2/utils/launcher/bxml/consoleView.bxml"));
			gameserverConsole = new Console(serverOutput);
			
		} catch (SerializationException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareExecutor() {
		app = new DefaultExecutor();
		app.setStreamHandler(appConsole);
		
		vis = new DefaultExecutor();
		vis.setStreamHandler(visConsole);
		
		gameserver = new DefaultExecutor();
		gameserver.setStreamHandler(gameserverConsole);
		
		robot = new DefaultExecutor();
		robot.setStreamHandler(robotConsole);
	}
	
	/**
	 * Will be called, if we click on the start game-server button.
	 */
	private ButtonPressListener startGameserverAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (gameserverResult == null || gameserverResult.hasResult()) {
				String cmd = String.format("%s -cp %s -jar SightlyRobot_Server.jar %s", javaExecutable, classPath,
						hostInput.getText());
				
				try {
					gameserverResult = new DefaultExecuteResultHandler();
					
					ExecuteWatchdog watchDog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
					gameserver.setWatchdog(watchDog);
					gameserver.execute(CommandLine.parse(cmd), gameserverResult);
				} 
				catch (IOException e) {
					e.printStackTrace();
				}

				// toggle buttons
				startGameserver.setEnabled(false);
				stopGameserver.setEnabled(true);

				// toggle images
				imageServerRunning.setVisible(true);
				imageServerStopped.setVisible(false);
			}
		}
	};

	private ButtonPressListener stopGameserverAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (!gameserverResult.hasResult()) {
				gameserver.getWatchdog().destroyProcess();
			}
			// toggle buttons
			startGameserver.setEnabled(true);
			stopGameserver.setEnabled(false);

			// toggle images
			imageServerRunning.setVisible(false);
			imageServerStopped.setVisible(true);
		}
	};

	private ButtonPressListener openGameserverConsoleAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			DesktopApplicationContext.createDisplay(400, 300, 100, 100, false, true, false,
					getDisplay().getHostWindow(), new ConsoleListener(Launcher.this.gameserverConsole.getView()));
		}
	};

	private ButtonPressListener startControlpanelAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			String cmd = String.format("%s -cp %s -jar SightlyRobot_Controlpanel.jar", javaExecutable, classPath);

			try {
				appResult = new DefaultExecuteResultHandler();
				
				ExecuteWatchdog watchDog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
				app.setWatchdog(watchDog);
				app.execute(CommandLine.parse(cmd), appResult);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private ButtonPressListener startVisualizationAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			String cmd = String.format("%s -cp %s -jar SightlyRobot_Visualization.jar -ip %s", javaExecutable,
					classPath, hostInput.getText());

			try {
				visResult = new DefaultExecuteResultHandler();
				
				ExecuteWatchdog watchDog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
				vis.setWatchdog(watchDog);
				vis.execute(CommandLine.parse(cmd), visResult);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private ButtonPressListener startRobotAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (robotResult == null || robotResult.hasResult()) {
				String cmd = String.format("%s -cp %s -jar SightlyRobot_SoftwareRobot.jar %s true", javaExecutable,
						classPath, hostInput.getText());

				try {
					robotResult = new DefaultExecuteResultHandler();
					
					ExecuteWatchdog watchDog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
					robot.setWatchdog(watchDog);
					robot.execute(CommandLine.parse(cmd), robotResult);
				} 
				catch (IOException e) {
					e.printStackTrace();
				}

				// toggle buttons
				startRobot.setEnabled(false);
				stopRobot.setEnabled(true);

				// toggle images
				imageRobotRunning.setVisible(true);
				imageRobotStopped.setVisible(false);
			}
		}
	};

	private ButtonPressListener stopRobotAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (!robotResult.hasResult()) {
				robot.getWatchdog().destroyProcess();
			}

			// toggle buttons
			startRobot.setEnabled(true);
			stopRobot.setEnabled(false);

			// toggle images
			imageRobotRunning.setVisible(false);
			imageRobotStopped.setVisible(true);
		}
	};

	private ButtonPressListener openRobotConsoleAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			DesktopApplicationContext.createDisplay(400, 300, 0, 0, false, true, false, getDisplay().getHostWindow(),
					new ConsoleListener(robotConsole.getView()));
		}
	};

}
