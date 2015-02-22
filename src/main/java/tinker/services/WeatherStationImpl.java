package tinker.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tinker.data.SensorStateRepository;
import tinker.data.entities.SensorState;
import tinker.listeners.IPConnectionListener;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

@Service
@Scope("singleton")
public class WeatherStationImpl implements WeatherStation {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static IPConnection ipcon = null;
	private static IPConnectionListener ipConnectionListener = null;

	@PostConstruct
	private void init() {
		ipcon = new IPConnection();

		while (true) {
			try {
				ipcon.connect(HOST, PORT);
				break;
			} catch (java.net.UnknownHostException e) {
				System.out.println(e);
			} catch (java.io.IOException e) {
				System.out.println(e);
			} catch (com.tinkerforge.AlreadyConnectedException e) {
				System.out.println(e);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ei) {
				System.out.println(ei);
			}
		}

		ipConnectionListener = new IPConnectionListener(ipcon);
		ipcon.addEnumerateListener(ipConnectionListener);
		ipcon.addConnectedListener(ipConnectionListener);

		// while(!ipConnectionListener.areAllBrickletsConnected()) {
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

	@PreDestroy
	private void destroy() {
		try {
			System.out.println("closing connection");
			ipcon.disconnect();
		} catch (com.tinkerforge.NotConnectedException e) {
			System.out.println(e);
		}
	}

	@Override
	public BrickletLCD20x4 getBrickletLCD() {
		return ipConnectionListener.getBrickletLCD();
	}

	@Override
	public BrickletAmbientLight getBrickletAmbientLight() {
		return ipConnectionListener.getBrickletAmbientLight();
	}

	@Override
	public BrickletHumidity getBrickletHumidity() {
		return ipConnectionListener.getBrickletHumidity();
	}

	@Override
	public BrickletBarometer getBrickletBarometer() {
		return ipConnectionListener.getBrickletBarometer();
	}
	
	@Autowired
	SensorStateRepository sensorStateRepository;
	
	@Scheduled(fixedRate=10000)
	public void saveState() {
		double temperature;
		double airpressure;
		double humidity;
		double illuminance;
		try {
			temperature = getBrickletBarometer().getChipTemperature() / 100.0;
			airpressure = getBrickletBarometer().getAirPressure() / 1000.0;
			humidity = getBrickletHumidity().getHumidity() / 10.0;
			illuminance = getBrickletAmbientLight().getIlluminance() / 10.0;
			
			
			SensorState s = new SensorState();
			s.setAirpressure(airpressure);
			s.setHumidity(humidity);
			s.setIlluminance(illuminance);
			s.setTemperature(temperature);
			s.setCreatedDate(DateTime.now());
			System.out.println(s);
			sensorStateRepository.save(s);
			
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		

		
		
	}
	
	
}
