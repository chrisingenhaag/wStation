package de.ingenhaag.tinkerwstation.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;

public class HumidityListener implements BrickletHumidity.HumidityListener {

    private final Logger log = LoggerFactory.getLogger(HumidityListener.class);
	
	public void humidity(int humidity) {
		if (brickletLCD != null) {
			String text = String.format("Luftfeucht %6.2f %%", humidity / 10.0);
			try {
				brickletLCD.writeLine((short) 1, (short) 0, text);
				log.debug("Write to line 1: " + text);
			} catch (com.tinkerforge.TinkerforgeException e) {
				log.error("error writing humidity to lcd");
			}
		}
	}
	
	private BrickletLCD20x4 brickletLCD;
	
	public HumidityListener(IPConnectionListener ipConnectionListener) {
		this.brickletLCD = ipConnectionListener.getBrickletLCD();
	}

}
