package tinker.data;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import tinker.data.entities.SensorState;

@RepositoryRestResource(collectionResourceRel = "sensorState", path = "sensorState")
public interface SensorStateRepository extends PagingAndSortingRepository<SensorState, Long> {

	public List<SensorState> findByCreatedDateBetween(@Param("start") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm") Date start, @Param("end") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm") Date end);
	
	@Query("select avg(x.temperature) from SensorState x where x.createdDate between :start and :end")
	public double getAverageTemperatureBetween(@Param("start") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm") Date start, @Param("end") @DateTimeFormat(pattern="yyyy-MM-dd-HH-mm") Date end);
	
}
