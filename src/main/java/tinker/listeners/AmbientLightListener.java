package tinker.listeners;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletLCD20x4;

public class AmbientLightListener implements
		BrickletAmbientLight.IlluminanceListener {

	public void illuminance(int illuminance) {
		if (brickletLCD != null) {
			String text = String.format("Helligkeit %6.2f lx",
					illuminance / 10.0);
			try {
				brickletLCD.writeLine((short) 0, (short) 0, text);

//				if (illuminance / 10.0 < 100) {
//					brickletLCD.backlightOn();
//				} else {
//					brickletLCD.backlightOff();
//				}

			} catch (com.tinkerforge.TinkerforgeException e) {
				System.out.println(e);
			}

			System.out.println("Write to line 0: " + text);
		}

	}

	private BrickletLCD20x4 brickletLCD;

	public AmbientLightListener(IPConnectionListener ipConnectionListener) {
		this.brickletLCD = ipConnectionListener.getBrickletLCD();
	}

}
