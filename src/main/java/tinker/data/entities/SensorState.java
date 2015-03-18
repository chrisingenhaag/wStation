package tinker.data.entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SensorState implements Serializable {

	private static final long serialVersionUID = 6868556014672422515L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private double temperature;
	private double airpressure;
	private double humidity;
	private double illuminance;
	
	private Date createdDate;

	public SensorState() {
	}

	public String toString() {
		return String
				.format("SensorState[id='%d',temperature='%f',airpressure='%f',humidity='%f',illuminance='%f',createdDate='%s']",
						id, temperature, airpressure, humidity, illuminance,createdDate);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getAirpressure() {
		return airpressure;
	}

	public void setAirpressure(double airpressure) {
		this.airpressure = airpressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getIlluminance() {
		return illuminance;
	}

	public void setIlluminance(double illuminance) {
		this.illuminance = illuminance;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
