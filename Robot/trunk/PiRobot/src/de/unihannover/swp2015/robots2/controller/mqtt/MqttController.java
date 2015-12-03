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
	private final BlockingQueue<MqttFullMessage> receiveQueue;
	private final BlockingQueue<MqttFullMessage> sendQueue;
	private final MqttClient client;

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
	 * @param subscribeTopcis
	 *            A list of MQTT topic names to subscribe. May contain wildcards
	 *            according to the MQTT standard.
	 */
	public MqttController(String mqttBrokerUrl, String mqttClientId,
			IMqttMessageHandler receiveHandler, List<String> subscribeTopcis)
			throws MqttException {

		this.client = new MqttClient(mqttBrokerUrl, mqttClientId,
				new MemoryPersistence());

		// mqtt connection options
		MqttConnectOptions connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(10);
		// TODO Set last will
		// connOpt.setWill("error/connection",
		// "The important client lost its connection.".getBytes(), 0, false);
		connOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

		// mqttclient connect
		client.connect(connOpt);

		// mqttclient subscribes
		for (String topic : subscribeTopcis) {
			client.subscribe(topic);
		}

		// mqtt client callbacks
		client.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				try {
					MqttController.this.receiveQueue.put(new MqttFullMessage(
							topic, message));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {

			}

			@Override
			public void connectionLost(Throwable arg0) {
				// TODO propagate to MessageHandler and Model
				// TODO try reconnect by interval and propagate on successful reconnect
			}
		});

		// message queues
		this.sendQueue = new LinkedBlockingQueue<MqttFullMessage>();
		this.receiveQueue = new LinkedBlockingQueue<MqttFullMessage>();
		
		// send- and receive worker threads
		Thread receiveWorker = new Thread(new MqttReceiver(receiveQueue,
				receiveHandler));
		receiveWorker.start();
		Thread sendWorker = new Thread(new MqttSender(sendQueue, client));
		sendWorker.start();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method adds the message to send queue which is processed
	 * by the send worker thread.
	 */
	@Override
	public void sendMessage(String topic, String message, boolean retained) {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
		mqttMessage.setRetained(retained);
		try {
			this.sendQueue.put(new MqttFullMessage(topic, mqttMessage));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteRetainedMessage(String topic) {
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setRetained(true);
		try {
			this.sendQueue.put(new MqttFullMessage(topic, mqttMessage));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
