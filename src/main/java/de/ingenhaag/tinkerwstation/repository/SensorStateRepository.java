package de.ingenhaag.tinkerwstation.repository;

import de.ingenhaag.tinkerwstation.domain.SensorState;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SensorState entity.
 */
public interface SensorStateRepository extends JpaRepository<SensorState,Long> {

}
