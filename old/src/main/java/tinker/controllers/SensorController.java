package tinker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import tinker.services.WeatherStation;

@RequestMapping("/sensors")
@Controller
@EnableAutoConfiguration
public class SensorController {	
	
	@RequestMapping("/humidity")
	@ResponseBody
	double getHumidity() {
		try {
			return weatherStation.getBrickletHumidity().getHumidity()/10.0;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return -1;
	}
    
	@RequestMapping("/illuminance")
	@ResponseBody
	double getIlluminance() {
		try {
			return weatherStation.getBrickletAmbientLight().getIlluminance()/10.0;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@RequestMapping("/airpressure")
	@ResponseBody
	double getAirPressure() {
		try {
			return weatherStation.getBrickletBarometer().getAirPressure()/1000.0;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@RequestMapping("/temperature")
	@ResponseBody
	double getTemperature() {
		try {
			return weatherStation.getBrickletBarometer().getChipTemperature()/100.0;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Autowired
	private WeatherStation weatherStation;

}