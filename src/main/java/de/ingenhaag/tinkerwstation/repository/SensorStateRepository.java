package de.ingenhaag.tinkerwstation.repository;

import de.ingenhaag.tinkerwstation.domain.SensorState;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the SensorState entity.
 */
public interface SensorStateRepository extends JpaRepository<SensorState,Long> {

	public List<SensorState> findByCreateddateBetween(
			@Param("start") ZonedDateTime start,
			@Param("end") ZonedDateTime end);

//	@Query("select avg(x.temperature) from SensorState x where x.createddate between :start and :end")
//	public double getAverageTemperatureBetween(
//			@Param("start") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") ZonedDateTime start,
//			@Param("end") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") ZonedDateTime end);
//
//	public List<SensorState> findByCreateddateBetweenOrderByTemperatureDesc(
//			@Param("start") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") ZonedDateTime start,
//			@Param("end") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") ZonedDateTime end);
//	
}
