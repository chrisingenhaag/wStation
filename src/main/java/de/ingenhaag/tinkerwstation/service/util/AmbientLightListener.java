package de.ingenhaag.tinkerwstation.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletLCD20x4;

public class AmbientLightListener implements
		BrickletAmbientLight.IlluminanceListener {

    private final Logger log = LoggerFactory.getLogger(AmbientLightListener.class);

	
	public void illuminance(int illuminance) {
		if (brickletLCD != null) {
			String text = String.format("Helligkeit %6.2f lx",
					illuminance / 10.0);
			try {
				brickletLCD.writeLine((short) 0, (short) 0, text);
				log.debug("Write to line 0: " + text);
			} catch (com.tinkerforge.TinkerforgeException e) {
				log.error("error writing illuminance to lcd display", e);
			}
		}
	}

	private BrickletLCD20x4 brickletLCD;

	public AmbientLightListener(IPConnectionListener ipConnectionListener) {
		this.brickletLCD = ipConnectionListener.getBrickletLCD();
	}

}
