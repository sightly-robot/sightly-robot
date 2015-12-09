package de.unihannover.swp2015.robots2.controller.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * This class wraps an MQTT client of the eclipse paho library, handles
 * connecting to an MQTT broker, subscription of topics, publishing messages and
 * forwarding of received messages.
 * 
 * Sending and receiving messages is executed by two separate threads using
 * BlockingQueues of Messages for both directions.
 * 
 * @author Michael Thies
 */
public class MqttController implements IMqttController {
	/** Queue of received messages waiting for processing */
	private final BlockingQueue<MqttFullMessage> receiveQueue;
	/** Queue of messages waiting to be sent */
	private final BlockingQueue<MqttFullMessage> sendQueue;
	/** The MQTT client */
	private final MqttClient client;
	/* Object used to notify the send worker about an established connection */
	private final Object waitForConnect = new Object();

	/** The Connection options including broker URL and last will */
	private final MqttConnectOptions connOpt;
	/**
	 * The Callback handler that is able to process messages and will be
	 * informed about connection status changes
	 */
	private final IMqttMessageHandler receiveHandler;
	/** List of topics to subscribe after successful connection establishment */
	private final List<String> subscribeTopics;

	/**
	 * Initialize a new MqttController.
	 * 
	 * This constructor also initializes and starts the paho MQTT client,
	 * connects to the MQTT broker and starts the send and receive worker
	 * threads.
	 * 
	 * @param mqttBrokerUrl
	 *            URL of the MQTT broker to connect to
	 * @param mqttClientId
	 *            MQTT Client ID to be used to connect to the broker. Must be
	 *            uniqe for each connecting device/client.
	 * @param receiveHandler
	 *            A Controller to call a method for each received MQTT message
	 *            and encountered errors on.
	 * @param subscribeTopics
	 *            A list of MQTT topic names to subscribe. May contain wildcards
	 *            according to the MQTT standard.
	 */
	public MqttController(String mqttClientId,
			IMqttMessageHandler receiveHandler, List<String> subscribeTopics)
			throws MqttException {

		this.receiveHandler = receiveHandler;
		this.subscribeTopics = subscribeTopics;

		// The broker url is only temprorary and will be specified finally via
		// the MQTTConnectOptions in connect().
		this.client = new MqttClient("tcp://localhost", mqttClientId,
				new MemoryPersistence());

		// mqtt connection options
		this.connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(5);
		// TODO Set last will
		// connOpt.setWill("error/connection",
		// "The important client lost its connection.".getBytes(), 0, false);
		connOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

		// MQTT client callback methods
		client.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				try {
					MqttController.this.receiveQueue.put(new MqttFullMessage(
							topic, message));
				} catch (InterruptedException e) {
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {

			}

			@Override
			public void connectionLost(Throwable arg0) {
				MqttController.this.receiveHandler.onMqttStateChange(false);

				// Try to reconnect every 2 seconds
				try {
					while (!client.isConnected()) {
						Thread.sleep(2000);
						try {
							MqttController.this.connect(null);
						} catch (MqttException e) {
						}
					}
				} catch (InterruptedException e) {
				}
			}
		});

		// message queues
		this.sendQueue = new LinkedBlockingQueue<MqttFullMessage>();
		this.receiveQueue = new LinkedBlockingQueue<MqttFullMessage>();

		// send- and receive worker threads
		Thread receiveWorker = new Thread(new MqttReceiver(receiveQueue,
				receiveHandler));
		receiveWorker.start();
		Thread sendWorker = new Thread(new MqttSender(sendQueue, client,
				this.waitForConnect));
		sendWorker.start();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * If the given broker URL is null, the broker URL of the last call is used
	 * (or "tcp://localhost" if there weren't any).
	 */
	@Override
	public void connect(String brokerUrl) throws MqttException {
		// Disconnect if connected
		if (this.client.isConnected()) {
			this.client.disconnect();
			this.receiveHandler.onMqttStateChange(false);
		}

		// Set broker URL
		if (brokerUrl != null)
			this.connOpt.setServerURIs(new String[] { brokerUrl });

		// MQTT client connect
		client.connect(connOpt);

		for (String topic : this.subscribeTopics) {
			client.subscribe(topic);
		}

		// Notify send worker and Main Controller
		synchronized (this.waitForConnect) {
			this.waitForConnect.notifyAll();
		}
		receiveHandler.onMqttStateChange(true);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method adds the message to send queue which is processed by the send
	 * worker thread.
	 */
	@Override
	public void sendMessage(String topic, String message, boolean retained) {
		byte[] rawMessage = (message == null) ? new byte[] {} : message
				.getBytes(StandardCharsets.UTF_8);

		MqttMessage mqttMessage = new MqttMessage(rawMessage);
		mqttMessage.setRetained(retained);

		try {
			this.sendQueue.put(new MqttFullMessage(topic, mqttMessage));
		} catch (InterruptedException e) {
		}
	}

}
