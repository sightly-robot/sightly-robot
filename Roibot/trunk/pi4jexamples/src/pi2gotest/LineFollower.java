package pi2gotest;
// START SNIPPET: control-gpio-snippet

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  ControlGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Simple line following program.
 * 
 * @author Joel Greenyer
 */
public class LineFollower {
    
    static final int nlfwd = 11;
    static final int nlbwd = 10;
    static final int nrfwd = 12;
    static final int nrbwd = 13;
	
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        
//		L1 = 26 		pi4j: 11
//		L2 = 24 		pi4j: 10
//		R1 = 19 		pi4j: 12
//		R2 = 21 		pi4j: 13
        
//		irFL = 7		pi4j: 7
//		irFR = 11		pi4j: 0
//		lineRight = 13	pi4j: 2
//		lineLeft = 12	pi4j: 1       
        
//		frontLED = 15	pi4j: 3
//		rearLED = 16	pi4j: 4
        
//		sonar = 8		pi4j: 15

        
        // provision gpio pin #01 as an output pin and turn on
//        final GpioPinDigitalOutput l1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "L1", PinState.HIGH);
//        final GpioPinDigitalOutput l2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "L2", PinState.HIGH);
//        final GpioPinDigitalOutput r1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "R1", PinState.HIGH);
//        final GpioPinDigitalOutput r2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "R2", PinState.HIGH);
//
//        final GpioPinDigitalInput irFL = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "irFL",  PinPullResistance.PULL_DOWN);
//        final GpioPinDigitalInput irFR = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, "irFR",  PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput lineRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, "lineRight",  PinPullResistance.PULL_UP);
        final GpioPinDigitalInput lineLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "lineLeft",  PinPullResistance.PULL_UP);

        
//        final GpioPinDigitalOutput frontLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "frontLED", PinState.HIGH);
//        final GpioPinDigitalOutput rearLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "rearLED", PinState.HIGH);
//
//        final GpioPinDigitalOutput sonar = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "sonar", PinState.HIGH);

        
        // initialize wiringPi library
        Gpio.wiringPiSetup();
 
        // create soft-pwm pins (min=0 ; max=100)
        SoftPwm.softPwmCreate(4, 0, 100);

        for (int j = 0; j < 10; j++) {
    		// fade LED to fully OFF
    		for (int i = 100; i >= 0; i--) {
    			SoftPwm.softPwmWrite(4, i);
    	        System.out.println("--> rearLED state should be: " + i);
    			Thread.sleep(1);
    		}
    		// fade LED to fully ON
    		for (int i = 0; i <= 100; i++) {
    			SoftPwm.softPwmWrite(4, i);
    	        System.out.println("--> rearLED state should be: " + i);
    			Thread.sleep(1);
    		}
		}
        
        Pi2Go pi2go = new Pi2Go();
        
        final int goSpeed = 40; 
        final int stopSpeed = 0; 
        
        pi2go.go(goSpeed);
        
        // create and register gpio pin listener
        lineLeft.addListener(
        		new GpioPinListenerDigital() {
			        @Override
			        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				        // display pin state on console
				        System.out.println(" --> line LEFT  STATE CHANGE: " + event.getPin() + " = " + event.getState());
				        if (event.getState() == PinState.LOW)
				        	pi2go.setLeftSpeed(goSpeed);
				        if (event.getState() == PinState.HIGH)
				        	pi2go.setLeftSpeed(stopSpeed);
			        }
				}
        );
        // create and register gpio pin listener
        lineRight.addListener(
        		new GpioPinListenerDigital() {
			        @Override
			        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				        // display pin state on console
				        System.out.println(" --> line RIGHT STATE CHANGE: " + event.getPin() + " = " + event.getState());
				        if (event.getState() == PinState.LOW)
				        	pi2go.setRightSpeed(goSpeed);
				        if (event.getState() == PinState.HIGH)
				        	pi2go.setRightSpeed(stopSpeed);

			        }
				}
        );


        

        // keep program running for 100*100 ms, show progress in log console

        for (int i = 0; i < 100; i++) {
      			Thread.sleep(100);
      			
      			System.out.println("cycle " + i);
		}

        System.out.println("done, turning wheels off.");

        lineLeft.removeAllListeners();
        lineRight.removeAllListeners();
        
        pi2go.go(0);
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }

  
}
