package de.ingenhaag.tinkerwstation.service.util;

import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;

public class HumidityListener implements BrickletHumidity.HumidityListener {

	public void humidity(int humidity) {
		if (brickletLCD != null) {
			String text = String.format("Luftfeucht %6.2f %%", humidity / 10.0);
			try {
				brickletLCD.writeLine((short) 1, (short) 0, text);
			} catch (com.tinkerforge.TinkerforgeException e) {
				System.out.println(e);
			}

			System.out.println("Write to line 1: " + text);
		}
	}
	
	private BrickletLCD20x4 brickletLCD;
	
	public HumidityListener(IPConnectionListener ipConnectionListener) {
		this.brickletLCD = ipConnectionListener.getBrickletLCD();
	}

}
