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
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.  
 * 
 * @author Robert Savage
 */
public class Pi2GoWheelTest {
    
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput[] pin = {
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "MyLED", PinState.HIGH),
        		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, "MyLED", PinState.HIGH),
        		};
        
        for (GpioPinDigitalOutput gpioPin : pin) {
        	System.out.println("testing pin " + gpioPin.getPin());
//        	gpioPin.high();
//            System.out.println("--> GPIO state should be: ON");
//            Thread.sleep(500);
//        	gpioPin.low();
//            System.out.println("--> GPIO state should be: OFF");
		}

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
    }
}
//END SNIPPET: control-gpio-snippet
