package de.ingenhaag.tinkerwstation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SensorState.
 */
@Entity
@Table(name = "sensor_state")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SensorState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "airpressure")
    private Double airpressure;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "illuminance")
    private Double illuminance;

    @Column(name = "createddate")
    private ZonedDateTime createddate;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getAirpressure() {
        return airpressure;
    }

    public void setAirpressure(Double airpressure) {
        this.airpressure = airpressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getIlluminance() {
        return illuminance;
    }

    public void setIlluminance(Double illuminance) {
        this.illuminance = illuminance;
    }

    public ZonedDateTime getCreateddate() {
        return createddate;
    }

    public void setCreateddate(ZonedDateTime createddate) {
        this.createddate = createddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SensorState sensorState = (SensorState) o;
        return Objects.equals(id, sensorState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SensorState{" +
            "id=" + id +
            ", temperature='" + temperature + "'" +
            ", airpressure='" + airpressure + "'" +
            ", humidity='" + humidity + "'" +
            ", illuminance='" + illuminance + "'" +
            ", createddate='" + createddate + "'" +
            '}';
    }
}
