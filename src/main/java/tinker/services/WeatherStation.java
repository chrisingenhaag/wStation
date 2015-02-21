package tinker.services;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;

public interface WeatherStation {
	public BrickletLCD20x4 getBrickletLCD();
	public BrickletAmbientLight getBrickletAmbientLight();

	public BrickletHumidity getBrickletHumidity();

	public BrickletBarometer getBrickletBarometer();
	
}
