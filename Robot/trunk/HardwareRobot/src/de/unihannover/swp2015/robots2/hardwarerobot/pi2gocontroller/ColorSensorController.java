package de.unihannover.swp2015.robots2.hardwarerobot.pi2gocontroller;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * Java TCS34725 Color Sensor Class for Reading Values with Pi4J.
 * As Singleton.
 * Code converted from C++: https://github.com/whiteneon/Adafruit_TCS34725
 * @author Lenard Spiecker
 *
 */
public class ColorSensorController {

	private static ColorSensorController instance;
	
	private boolean DEBUG = true;

	private static final int TCS34725_ADDRESS          = 0x29;

	private static final int TCS34725_COMMAND_BIT      = 0x80;

	private static final int TCS34725_ENABLE           = 0x00;
	private static final int TCS34725_ENABLE_AIEN      = 0x10;    /* RGBC Interrupt Enable */
	private static final int TCS34725_ENABLE_WEN       = 0x08;    /* Wait enable - Writing 1 activates the wait timer */
	private static final int TCS34725_ENABLE_AEN       = 0x02;    /* RGBC Enable - Writing 1 actives the ADC, 0 disables it */
	private static final int TCS34725_ENABLE_PON       = 0x01;    /* Power on - Writing 1 activates the internal oscillator, 0 disables it */
	private static final int TCS34725_ATIME            = 0x01;    /* Integration time */
	private static final int TCS34725_WTIME            = 0x03;    /* Wait time (if TCS34725_ENABLE_WEN is asserted) */
	private static final int TCS34725_WTIME_2_4MS      = 0xFF;    /* WLONG0 = 2.4ms   WLONG1 = 0.029s */
	private static final int TCS34725_WTIME_204MS      = 0xAB;    /* WLONG0 = 204ms   WLONG1 = 2.45s  */
	private static final int TCS34725_WTIME_614MS      = 0x00;    /* WLONG0 = 614ms   WLONG1 = 7.4s   */
	private static final int TCS34725_AILTL            = 0x04;    /* Clear channel lower interrupt threshold */
	private static final int TCS34725_AILTH            = 0x05;
	private static final int TCS34725_AIHTL            = 0x06;    /* Clear channel upper interrupt threshold */
	private static final int TCS34725_AIHTH            = 0x07;
	private static final int TCS34725_PERS             = 0x0C;    /* Persistence register - basic SW filtering mechanism for interrupts */
	private static final int TCS34725_PERS_NONE        = 0b0000;  /* Every RGBC cycle generates an interrupt                                */
	private static final int TCS34725_PERS_1_CYCLE     = 0b0001;  /* 1 clean channel value outside threshold range generates an interrupt   */
	private static final int TCS34725_PERS_2_CYCLE     = 0b0010;  /* 2 clean channel values outside threshold range generates an interrupt  */
	private static final int TCS34725_PERS_3_CYCLE     = 0b0011;  /* 3 clean channel values outside threshold range generates an interrupt  */
	private static final int TCS34725_PERS_5_CYCLE     = 0b0100;  /* 5 clean channel values outside threshold range generates an interrupt  */
	private static final int TCS34725_PERS_10_CYCLE    = 0b0101;  /* 10 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_15_CYCLE    = 0b0110;  /* 15 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_20_CYCLE    = 0b0111;  /* 20 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_25_CYCLE    = 0b1000;  /* 25 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_30_CYCLE    = 0b1001;  /* 30 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_35_CYCLE    = 0b1010;  /* 35 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_40_CYCLE    = 0b1011;  /* 40 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_45_CYCLE    = 0b1100;  /* 45 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_50_CYCLE    = 0b1101;  /* 50 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_55_CYCLE    = 0b1110;  /* 55 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_PERS_60_CYCLE    = 0b1111;  /* 60 clean channel values outside threshold range generates an interrupt */
	private static final int TCS34725_CONFIG           = 0x0D;
	private static final int TCS34725_CONFIG_WLONG     = 0x02;    /* Choose between short and long (12x) wait times via TCS34725_WTIME */
	private static final int TCS34725_CONTROL          = 0x0F;    /* Set the gain level for the sensor */
	private static final int TCS34725_ID               = 0x12;    /* 0x44 = TCS34721/TCS34725, 0x4D = TCS34723/TCS34727 */
	private static final int TCS34725_STATUS           = 0x13;
	private static final int TCS34725_STATUS_AINT      = 0x10;    /* RGBC Clean channel interrupt */
	private static final int TCS34725_STATUS_AVALID    = 0x01;    /* Indicates that the RGBC channels have completed an integration cycle */
	private static final int TCS34725_CDATAL           = 0x14;    /* Clear channel data */
	private static final int TCS34725_CDATAH           = 0x15;
	private static final int TCS34725_RDATAL           = 0x16;    /* Red channel data */
	private static final int TCS34725_RDATAH           = 0x17;
	private static final int TCS34725_GDATAL           = 0x18;    /* Green channel data */
	private static final int TCS34725_GDATAH           = 0x19;
	private static final int TCS34725_BDATAL           = 0x1A;    /* Blue channel data */
	private static final int TCS34725_BDATAH           = 0x1B;
	
	private enum IntegrationTime  
	{
	  INTEGRATIONTIME_2_4MS (0xFF),   /**<  2.4ms - 1 cycle    - Max Count: 1024  */
	  INTEGRATIONTIME_24MS   (0xF6),   /**<  24ms  - 10 cycles  - Max Count: 10240 */
	  INTEGRATIONTIME_50MS  (0xEB),   /**<  50ms  - 20 cycles  - Max Count: 20480 */
	  INTEGRATIONTIME_101MS  (0xD5),   /**<  101ms - 42 cycles  - Max Count: 43008 */
	  INTEGRATIONTIME_154MS  (0xC0),   /**<  154ms - 64 cycles  - Max Count: 65535 */
	  INTEGRATIONTIME_700MS  (0x00);    /**<  700ms - 256 cycles - Max Count: 65535 */
	  
	  int time;
	  
	  private IntegrationTime(int time)
	  {
		  this.time = time;
	  }
	} ;
	private IntegrationTime integrationTime;
	
	private enum Gain
	{
	  GAIN_1X                (0x00),   /**<  No gain  */
	  GAIN_4X                (0x01),   /**<  2x gain  */
	  GAIN_16X               (0x02),   /**<  16x gain */
	  GAIN_60X               (0x03);    /**<  60x gain */
		
		int gain;
		private Gain(int gain)
		{
			this.gain = gain;
		}
	}
	private Gain gain;

	private I2CBus bus;
	private I2CDevice device;

	boolean initialised = false;

	//private static final int ADDRESS = 0x44;

	/**
	 * Initializes I2C and configures the sensor (call this function before
	 * doing anything else)
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private ColorSensorController() throws IOException {
		bus = I2CFactory.getInstance(I2CBus.BUS_1);

		// Init PCA9685
		device = bus.getDevice(TCS34725_ADDRESS);

		/* Make sure we're actually connected */
		int x = read8(TCS34725_ID);
		if (DEBUG) {
			System.out.println(x);
		}
		if (x != 0x44) {
			return;
		}
		initialised = true;

		/* Set default integration time and gain */
		setIntegrationTime(IntegrationTime.INTEGRATIONTIME_50MS);
		setGain(Gain.GAIN_4X);
		
		enable();
	}
	
	public static ColorSensorController getInstance()
	{
		if(instance == null)
		{
			try {
				instance = new ColorSensorController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * Writes a register and an 8 bit value over I2C
	 * @param reg
	 * @param value
	 * @throws IOException
	 */
	private void write(int reg, int value) throws IOException
	{
		device.write((byte) (TCS34725_COMMAND_BIT | reg));
		device.write((byte) (value & 0xff));
	}
	
	/**
	 * Reads an 8 bit value over I2C
	 * @param reg
	 * @return
	 * @throws IOException 
	 */
	private int read8(int reg) throws IOException
	{
		device.write((byte) (TCS34725_COMMAND_BIT | reg));
		return device.read();
	}
	
	/**
	 * Reads an 16 bit value over I2C
	 * @param reg
	 * @return
	 * @throws IOException 
	 */
	private int read16(int reg) throws IOException
	{
		device.write((byte) (TCS34725_COMMAND_BIT | reg));
		int t = device.read();
		int x = device.read();
		x <<= 8;
		x |= t;
		return x;
	}

	/**
	 * Enables the device
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void enable() throws IOException {
		  write(TCS34725_ENABLE, TCS34725_ENABLE_PON); 
		  try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		  write(TCS34725_ENABLE, (TCS34725_ENABLE_PON | TCS34725_ENABLE_AEN));   
	}
	
	/**
	 * Disables the device
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void diable() throws IOException {
		/* Turn the device off to save power */
		  int reg = 0;
		  reg = read8(TCS34725_ENABLE);
		  write(TCS34725_ENABLE, (reg & ~(TCS34725_ENABLE_PON | TCS34725_ENABLE_AEN)));

	}

	/**
	 * Adjusts the gain on the TCS34725 (adjusts the sensitivity to light)
	 * 
	 * @param gain
	 * @throws IOException
	 */
	public void setGain(Gain gain) throws IOException {
		this.gain = gain;

		if (!initialised) {
			return;
		}
		/* Update the timing register */
		write(TCS34725_CONTROL, gain.gain);
	}

	/**
	 * Sets the integration time for the TC34725
	 * 
	 * @param integrationTime
	 * @throws IOException
	 */
	public void setIntegrationTime(IntegrationTime integrationTime) throws IOException {
		this.integrationTime = integrationTime;

		if (!initialised) {
			return;
		}

		/* Update the timing register */
		write(TCS34725_ATIME, (byte) integrationTime.time);
	}

	/**
	 * @throws InterruptedException
	 * @throws IOException
	 *             Reads the raw red, green, blue and clear channel values
	 */
	public void getRawData(int[] rgbc) throws InterruptedException, IOException {
		rgbc[3] = read16(TCS34725_CDATAL);
		rgbc[0] = read16(TCS34725_RDATAL);
		rgbc[1] = read16(TCS34725_GDATAL);
		rgbc[2] = read16(TCS34725_BDATAL);

		/* Set a delay for the integration time */
		switch (integrationTime) {
		case INTEGRATIONTIME_2_4MS:
			Thread.sleep(3);
			break;
		case INTEGRATIONTIME_24MS:
			Thread.sleep(24);
			break;
		case INTEGRATIONTIME_50MS:
			Thread.sleep(50);
			break;
		case INTEGRATIONTIME_101MS:
			Thread.sleep(101);
			break;
		case INTEGRATIONTIME_154MS:
			Thread.sleep(154);
			break;
		case INTEGRATIONTIME_700MS:
			Thread.sleep(700);
			break;
		}
	}

	/**
	 * Converts the raw R/G/B values to color temperature in degrees Kelvin
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public int calculateColorTemperature(int r, int g, int b) {
		double X, Y, Z; /* RGB to XYZ correlation */
		double xc, yc; /* Chromaticity co-ordinates */
		double n; /* McCamy's formula */
		double cct;

		/* 1. Map RGB values to their XYZ counterparts. */
		/* Based on 6500K fluorescent, 3000K fluorescent */
		/* and 60W incandescent values for a wide range. */
		/* Note: Y = Illuminance or lux */
		X = (-0.14282F * r) + (1.54924F * g) + (-0.95641F * b);
		Y = (-0.32466F * r) + (1.57837F * g) + (-0.73191F * b);
		Z = (-0.68202F * r) + (0.77073F * g) + (0.56332F * b);

		/* 2. Calculate the chromaticity co-ordinates */
		xc = (X) / (X + Y + Z);
		yc = (Y) / (X + Y + Z);

		/* 3. Use McCamy's formula to determine the CCT */
		n = (xc - 0.3320F) / (0.1858F - yc);

		/* Calculate the final CCT */
		cct = (449.0F * Math.pow(n, 3)) + (3525.0F * Math.pow(n, 2)) + (6823.3F * n) + 5520.33F;

		/* Return the results in degrees Kelvin */
		return (int) cct;
	}

	/**
	 * Calculates the Lux value
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public int calculateLux(int r, int g, int b) {
		float illuminance;

		/* This only uses RGB ... how can we integrate clear or calculate lux */
		/* based exclusively on clear since this might be more reliable? */
		illuminance = (-0.32466F * r) + (1.57837F * g) + (-0.73191F * b);

		return (int) illuminance;
	}

	/**
	 * Turn the LED ON or OFF!
	 * 
	 * @param i
	 * @throws IOException
	 */
	public void setInterrupt(boolean i) throws IOException {
		int r = read8(TCS34725_ENABLE);
		if (i) {
			r |= TCS34725_ENABLE_AIEN;
		} else {
			r &= ~TCS34725_ENABLE_AIEN;
		}
		write(TCS34725_ENABLE, (byte) r);
	}

	public void clearInterrupt() throws IOException {
		device.write((byte) 0x66);
	}

	public void setIntLimits(int low, int high) throws IOException {
		write(0x04, (byte) (low & 0xFF));
		write(0x05, (byte) (low >> 8));
		write(0x06, (byte) (high & 0xFF));
		write(0x07, (byte) (high >> 8));
	}

	public void shutdown() throws IOException {
		if (bus != null) {
			bus.close();
		}
	}

}
