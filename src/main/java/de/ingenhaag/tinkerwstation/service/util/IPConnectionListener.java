package de.ingenhaag.tinkerwstation.service.util;

import com.tinkerforge.IPConnection;

import com.tinkerforge.BrickletLCD20x4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletBarometer;

public class IPConnectionListener implements IPConnection.EnumerateListener,
		IPConnection.ConnectedListener {
	
    private final Logger log = LoggerFactory.getLogger(IPConnectionListener.class);
	
	private IPConnection ipcon = null;

	private BrickletLCD20x4 brickletLCD;
	private BrickletAmbientLight brickletAmbientLight;
	private BrickletHumidity brickletHumidity;
	private BrickletBarometer brickletBarometer;

	private long callackInterval;
	
	public IPConnectionListener(IPConnection ipcon, int callBackInterval) {
		this.ipcon = ipcon;
		this.callackInterval = callBackInterval;
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
					log.info("LCD 20x4 initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletLCD = null;
					log.error("LCD 20x4 init failed: ", e);
				}
			} else if (deviceIdentifier == BrickletAmbientLight.DEVICE_IDENTIFIER) {
				try {
					brickletAmbientLight = new BrickletAmbientLight(uid, ipcon);
					brickletAmbientLight.setIlluminanceCallbackPeriod(callackInterval);
					brickletAmbientLight
							.addIlluminanceListener(new AmbientLightListener(this));
					log.info("Ambient Light initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletAmbientLight = null;
					log.error("Ambient Light init failed: ", e);
				}
			} else if (deviceIdentifier == BrickletHumidity.DEVICE_IDENTIFIER) {
				try {
					brickletHumidity = new BrickletHumidity(uid, ipcon);
					brickletHumidity.setHumidityCallbackPeriod(callackInterval);
					brickletHumidity.addHumidityListener(new HumidityListener(this));
					log.info("Humidity initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletHumidity = null;
					log.error("Humidity init failed: ", e);
				}
			} else if (deviceIdentifier == BrickletBarometer.DEVICE_IDENTIFIER) {
				try {
					brickletBarometer = new BrickletBarometer(uid, ipcon);
					brickletBarometer.setAirPressureCallbackPeriod(callackInterval);
					brickletBarometer
							.addAirPressureListener(new BarometerListener(this));
					log.info("Barometer initialized");
				} catch (com.tinkerforge.TinkerforgeException e) {
					brickletBarometer = null;
					log.error("Barometer init failed: ", e);
				}
			}
		}
	}

	public void connected(short connectedReason) {
		if (connectedReason == IPConnection.CONNECT_REASON_AUTO_RECONNECT) {
			log.info("Auto Reconnect");
			
			while (true) {
				try {
					ipcon.enumerate();
					break;
				} catch (com.tinkerforge.NotConnectedException e) {
					log.error("Error reconnecting tinkerforge statio");
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ei) {
					log.error("Reconnection interrupted", ei);
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