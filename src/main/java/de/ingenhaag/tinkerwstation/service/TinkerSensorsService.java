package de.ingenhaag.tinkerwstation.service;

import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import de.ingenhaag.tinkerwstation.domain.SensorState;
import de.ingenhaag.tinkerwstation.repository.SensorStateRepository;
import de.ingenhaag.tinkerwstation.service.util.IPConnectionListener;

@Service
@Transactional
public class TinkerSensorsService {

    private final Logger log = LoggerFactory.getLogger(TinkerSensorsService.class);

    @Value("${tinker.ip}")
  	private String HOST;
  	@Value("${tinker.port}")
  	private int PORT;
  	private static IPConnection ipcon = null;
  	private static IPConnectionListener ipConnectionListener = null;

  	@Autowired
  	SensorStateRepository sensorStateRepository;
  	
  	@PostConstruct
  	private void init() {
  		ipcon = new IPConnection();

  		while (true) {
  			try {
  				ipcon.connect(HOST, PORT);
  				break;
  			} catch (java.net.UnknownHostException e) {
  				log.error("Error connecting to tinkerforge module", e);
  			} catch (java.io.IOException e) {
  				log.error("Error connecting to tinkerforge module", e);
  			} catch (com.tinkerforge.AlreadyConnectedException e) {
  				log.error("Error connecting to tinkerforge module", e);
  			}

  			try {
  				Thread.sleep(1000);
  			} catch (InterruptedException ei) {
  				log.error("Error connecting to tinkerforge module", ei);
  			}
  		}

  		ipConnectionListener = new IPConnectionListener(ipcon);
  		ipcon.addEnumerateListener(ipConnectionListener);
  		ipcon.addConnectedListener(ipConnectionListener);

  		while (true) {
  			try {
  				ipcon.enumerate();
  				break;
  			} catch (com.tinkerforge.NotConnectedException e) {
  				log.error("Error connecting to tinkerforge module", e);
  			}

  			try {
  				Thread.sleep(1000);
  			} catch (InterruptedException ei) {
  				log.error("Error connecting to tinkerforge module", ei);
  			}
  		}

  	}

  	@PreDestroy
  	private void destroy() {
  		try {
  			log.info("closing connection");
  			ipcon.disconnect();
  		} catch (com.tinkerforge.NotConnectedException e) {
  			log.error("Error connecting to tinkerforge module", e);
  		}
  	}

  	@Scheduled(cron="${tinker.cron.saveinterval}")
  	@Timed
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

  			s.setCreateddate(ZonedDateTime.now());
  			log.debug("created SensorState object", s);
  			sensorStateRepository.save(s);

  		} catch (TimeoutException e) {
  			log.error("timeout ",e);
  		} catch (NotConnectedException e) {
  			log.error("notconnected ",e);
  		} catch( NullPointerException e) {
  			log.error("npe ", e);
  		}
  	}
  	
  	private BrickletLCD20x4 getBrickletLCD() {
  		return ipConnectionListener.getBrickletLCD();
  	}

  	private BrickletAmbientLight getBrickletAmbientLight() {
  		return ipConnectionListener.getBrickletAmbientLight();
  	}

  	private BrickletHumidity getBrickletHumidity() {
  		return ipConnectionListener.getBrickletHumidity();
  	}

  	private BrickletBarometer getBrickletBarometer() {
  		return ipConnectionListener.getBrickletBarometer();
  	}
}
