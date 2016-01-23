package de.unihannover.swp2015.robots2.utils.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.UIDefaults.LazyInputMap;

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

	@BXML
	private TextInput hostInput;

	@BXML
	private ImageView imageServerRunning;
	@BXML
	private ImageView imageServerStopped;
	@BXML
	private PushButton startGameserver;
	@BXML
	private PushButton stopGameserver;
	@BXML
	private PushButton openGameserverConsole;
	private Process gameserver;
	private ServerConsole gameserverConsole;

	@BXML
	private PushButton startControlpanel;
	@BXML
	private PushButton startVisualization;

	@BXML
	private ImageView imageRobotRunning;
	@BXML
	private ImageView imageRobotStopped;
	@BXML
	private PushButton startRobot;
	@BXML
	private PushButton stopRobot;
	@BXML
	private PushButton openRobotConsole;
	private Process robot;
	private RobotConsole robotConsole;

	private String javaExecutable;
	private String classPath;

	@Override
	public void initialize(Map<String, Object> namespace, URL arg1, Resources arg2) {

		this.prepareJavaPaths();
		this.prepareConsole();

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
		if (this.gameserver != null) {
			this.gameserver.destroy();
		}

		if (this.robot != null) {
			this.robot.destroy();
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
		BXMLSerializer bxmlSerializer1 = new BXMLSerializer();
		BXMLSerializer bxmlSerializer2 = new BXMLSerializer();

		try {
			this.gameserverConsole = (ServerConsole) bxmlSerializer1.readObject(
					getClass().getResource("/de/unihannover/swp2015/robots2/utils/launcher/bxml/serverConsole.bxml"));
			this.robotConsole = (RobotConsole) bxmlSerializer2.readObject(
					getClass().getResource("/de/unihannover/swp2015/robots2/utils/launcher/bxml/robotConsole.bxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SerializationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Will be called, if we click on the start gameserver button.
	 */
	private ButtonPressListener startGameserverAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (gameserver == null || !gameserver.isAlive()) {
				String cmd = String.format("%s -cp %s -jar SightlyRobot_Server.jar %s", javaExecutable, classPath,
						hostInput.getText());

				System.out.println("cmd: " + cmd);

				try {
					gameserver = Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}

				gameserverConsole.setProcess(gameserver);

				// new Thread(Launcher.this.gameserverConsole).start();

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
			if (gameserver.isAlive()) {
				gameserver.destroy();
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
			DesktopApplicationContext.createDisplay(600, 400, 0, 0, false, true, false, getDisplay().getHostWindow(),
					new ConsoleListener(gameserverConsole));
		}
	};

	private ButtonPressListener startControlpanelAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			String cmd = String.format("%s -cp %s -jar SightlyRobot_Controlpanel.jar", javaExecutable, classPath);

			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private ButtonPressListener startVisualizationAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			String cmd = String.format("%s -cp %s -jar SightlyRobot_Visualization.jar -ip %s", javaExecutable,
					classPath, hostInput.getText());

			System.out.println("cmd: " + cmd);

			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private ButtonPressListener startRobotAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			if (robot == null || !robot.isAlive()) {
				String cmd = String.format("%s -cp %s -jar SightlyRobot_SoftwareRobot.jar %s true", javaExecutable,
						classPath, hostInput.getText());

				System.out.println("cmd: " + cmd);

				try {
					robot = Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}

				robotConsole.setProcess(robot);

				//new Thread(robotConsole).start();

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
			if (robot.isAlive()) {
				robot.destroy();
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
			DesktopApplicationContext.createDisplay(600, 400, 0, 0, false, true, false, getDisplay().getHostWindow(),
					new ConsoleListener(robotConsole));
		}
	};

}
