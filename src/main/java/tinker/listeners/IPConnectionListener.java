package tinker.listeners;

import org.springframework.beans.factory.annotation.Value;

import com.tinkerforge.IPConnection;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletBarometer;

public class IPConnectionListener implements IPConnection.EnumerateListener,
		IPConnection.ConnectedListener {
	private IPConnection ipcon = null;

	private BrickletLCD20x4 brickletLCD;
	private BrickletAmbientLight brickletAmbientLight;
	private BrickletHumidity brickletHumidity;
	private BrickletBarometer brickletBarometer;

	private static long callackInterval = 10000;
	
	public IPConnectionListener(IPConnection ipcon) {
		this.ipcon = ipcon;
	}

	public void enumerate(String uid, String connectedUid, char position,
			short[] hardwareVersion, short[] firmwareVersion,
			int deviceIdentifier, short enumerationType) {		
		if (enumerationType == IPConnection.ENUMERATION_TYPE_CONNECTED
				|| enumerationType == IPConnection.ENUMERATION_TYPE_AVAILABLE) {
			if (deviceIdentifier == BrickletLCD20x4.DEVICE_IDENTIFIER) {
				try {
					brickletLCD = new BrickletLCD20x4(uid, ipcon);
					brickletLCD.clearDisplay();
					brickletLCD.backlightOn();
					System.out.println("LCD 20x4 initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletLCD = null;
					System.out.println("LCD 20x4 init failed: " + e);
				}
			} else if (deviceIdentifier == BrickletAmbientLight.DEVICE_IDENTIFIER) {
				try {
					brickletAmbientLight = new BrickletAmbientLight(uid, ipcon);
					brickletAmbientLight.setIlluminanceCallbackPeriod(callackInterval);
					brickletAmbientLight
							.addIlluminanceListener(new AmbientLightListener(
									this));
					System.out.println("Ambient Light initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletAmbientLight = null;
					System.out.println("Ambient Light init failed: " + e);
				}
			} else if (deviceIdentifier == BrickletHumidity.DEVICE_IDENTIFIER) {
				try {
					brickletHumidity = new BrickletHumidity(uid, ipcon);
					brickletHumidity.setHumidityCallbackPeriod(callackInterval);
					brickletHumidity.addHumidityListener(new HumidityListener(
							this));
					System.out.println("Humidity initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletHumidity = null;
					System.out.println("Humidity init failed: " + e);
				}
			} else if (deviceIdentifier == BrickletBarometer.DEVICE_IDENTIFIER) {
				try {
					brickletBarometer = new BrickletBarometer(uid, ipcon);
					brickletBarometer.setAirPressureCallbackPeriod(callackInterval);
					brickletBarometer
							.addAirPressureListener(new BarometerListener(
									brickletBarometer, this));
					System.out.println("Barometer initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletBarometer = null;
					System.out.println("Barometer init failed: " + e);
				}
			}
		}
	}

	public void connected(short connectedReason) {
		if (connectedReason == IPConnection.CONNECT_REASON_AUTO_RECONNECT) {
			System.out.println("Auto Reconnect");
			
			while (true) {
				try {
					ipcon.enumerate();
					break;
				} catch (com.tinkerforge.NotConnectedException e) {
					System.out.println(e);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ei) {
					System.out.println(ei);
				}
			}
		}
	}

	public BrickletLCD20x4 getBrickletLCD() {
		return brickletLCD;
	}

	public BrickletAmbientLight getBrickletAmbientLight() {
		return brickletAmbientLight;
	}

	public BrickletHumidity getBrickletHumidity() {
		return brickletHumidity;
	}

	public BrickletBarometer getBrickletBarometer() {
		return brickletBarometer;
	}
	
	public boolean areAllBrickletsConnected() {
		return brickletAmbientLight != null &&
				brickletBarometer != null &&
				brickletHumidity != null &&
				brickletLCD != null;
	}

}