package de.ingenhaag.tinkerwstation.service.util;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletLCD20x4;

public class BarometerListener implements BrickletBarometer.AirPressureListener {

	private BrickletBarometer brickletBarometer = null;

	public void airPressure(int airPressure) {
		if (brickletLCD != null) {
			String text = String.format("Druck     %7.2f mb",
					airPressure / 1000.0);
			try {
				brickletLCD.writeLine((short) 2, (short) 0, text);
			} catch (com.tinkerforge.TinkerforgeException e) {
			}

			System.out.println("Write to line 2: " + text);

			int temperature;
			try {
				temperature = brickletBarometer.getChipTemperature();
			} catch (com.tinkerforge.TinkerforgeException e) {
				System.out.println("Could not get temperature: " + e);
				return;
			}

			// 0xDF == ° on LCD 20x4 charset
			text = String.format("Temperatur  %5.2f %cC", temperature / 100.0,
					0xDF);
			try {
				brickletLCD.writeLine((short) 3, (short) 0, text);
			} catch (com.tinkerforge.TinkerforgeException e) {
				System.out.println(e);
			}

			System.out.println("Write to line 3: "
					+ text.replace((char) 0xDF, '°'));
		}
	}

	private BrickletLCD20x4 brickletLCD;
	
	public BarometerListener(BrickletBarometer brickletBarometer, IPConnectionListener ipConnectionListener) {
		this.brickletBarometer = brickletBarometer;
		this.brickletLCD = ipConnectionListener.getBrickletLCD();
	}
	
}
