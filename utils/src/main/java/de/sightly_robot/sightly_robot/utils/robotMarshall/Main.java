package de.sightly_robot.sightly_robot.utils.robotMarshall;

public class Main {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of cli arguments.");
			System.out.println("Please specify exactly 1 argument: The MQTT broker location in format");
			System.out.println("tcp://HostnameOrIP");
			return;
		}
		
		MarshalMainController controller = new MarshalMainController();
		
		try {
			controller.startMqtt(args[0]);
		} catch (Exception e) {
			System.out.println("Could not connect to broker.");
			System.out.println(e.toString());
			return;
		}
		
		try {
			while(true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println("Marshal is terminating.");
		}
	}
}
