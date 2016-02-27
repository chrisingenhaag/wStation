package de.ingenhaag.tinkerwstation.service;

import java.time.ZonedDateTime;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import de.ingenhaag.tinkerwstation.config.tinker.TinkerConfiguration;
import de.ingenhaag.tinkerwstation.config.tinker.TinkerScheduling;
import de.ingenhaag.tinkerwstation.domain.SensorState;
import de.ingenhaag.tinkerwstation.repository.SensorStateRepository;
import de.ingenhaag.tinkerwstation.service.util.IPConnectionListener;

@Service
@Transactional
public class TinkerSensorsService {

    private final Logger log = LoggerFactory.getLogger(TinkerSensorsService.class);

    @Inject
    private TinkerConfiguration tinkerConfig;
  	
    @Inject
    private TinkerScheduling scheduling;
    
  	private static IPConnection ipcon = null;
  	private static IPConnectionListener ipConnectionListener = null;
  	private ScheduledFuture<?> task;
  	
  	
  	@Autowired
  	SensorStateRepository sensorStateRepository;
  	
  	@PostConstruct
  	private void init() {
  		initTinkerForgeStation();
  		
  		task = scheduling.taskScheduler().schedule(new Runnable() {
			@Override
			public void run() {
				saveState();
			}
		}, new CronTrigger(tinkerConfig.getCron().getSaveinterval()));
  		
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

  	private void initTinkerForgeStation() {
  		ipcon = new IPConnection();

  		while (true) {
  			try {
  				ipcon.connect(tinkerConfig.getHost(), tinkerConfig.getPort());
  				break;
  			} catch (java.net.UnknownHostException e) {
  				log.error("Error connecting - unkown host", e);
  			} catch (java.io.IOException e) {
  				log.error("Error connecting - io exception", e);
  			} catch (com.tinkerforge.AlreadyConnectedException e) {
  				log.error("Error connecting - already connected", e);
  			}

  			try {
  				Thread.sleep(1000);
  			} catch (InterruptedException ei) {
  				log.error("Error waiting", ei);
  			}
  		}

  		ipConnectionListener = new IPConnectionListener(ipcon, tinkerConfig.getCallback().getInterval());
  		ipcon.addEnumerateListener(ipConnectionListener);
  		ipcon.addConnectedListener(ipConnectionListener);

  		while (true) {
  			try {
  				ipcon.enumerate();
  				break;
  			} catch (com.tinkerforge.NotConnectedException e) {
  				log.error("Error broadcasting an enumerate request, not connected to tinkerforge", e);
  			}

  			try {
  				Thread.sleep(1000);
  			} catch (InterruptedException ei) {
  				log.error("Error waiting", ei);
  			}
  		}

  	}
  	
  	//@Scheduled(cron="${tinker.cron.saveinterval}")
  	//@Timed
  	private void saveState() {
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
