package de.ingenhaag.tinkerwstation.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import de.ingenhaag.tinkerwstation.domain.SensorState;
import de.ingenhaag.tinkerwstation.repository.SensorStateRepository;
import de.ingenhaag.tinkerwstation.web.rest.util.HeaderUtil;
import de.ingenhaag.tinkerwstation.web.rest.util.PaginationUtil;

/**
 * REST controller for managing SensorState.
 */
@RestController
@RequestMapping("/api")
public class SensorStateResource {

    private final Logger log = LoggerFactory.getLogger(SensorStateResource.class);

    @Inject
    private SensorStateRepository sensorStateRepository;

    /**
     * POST  /sensorStates -> Create a new sensorState.
     */
    @RequestMapping(value = "/sensorStates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorState> createSensorState(@RequestBody SensorState sensorState) throws URISyntaxException {
        log.debug("REST request to save SensorState : {}", sensorState);
        if (sensorState.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sensorState", "idexists", "A new sensorState cannot already have an ID")).body(null);
        }
        SensorState result = sensorStateRepository.save(sensorState);
        return ResponseEntity.created(new URI("/api/sensorStates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sensorState", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensorStates -> Updates an existing sensorState.
     */
    @RequestMapping(value = "/sensorStates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorState> updateSensorState(@RequestBody SensorState sensorState) throws URISyntaxException {
        log.debug("REST request to update SensorState : {}", sensorState);
        if (sensorState.getId() == null) {
            return createSensorState(sensorState);
        }
        SensorState result = sensorStateRepository.save(sensorState);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sensorState", sensorState.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensorStates -> get all the sensorStates.
     */
    @RequestMapping(value = "/sensorStates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SensorState>> getAllSensorStates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SensorStates");
        Page<SensorState> page = sensorStateRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensorStates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensorStates/:id -> get the "id" sensorState.
     */
    @RequestMapping(value = "/sensorStates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SensorState> getSensorState(@PathVariable Long id) {
        log.debug("REST request to get SensorState : {}", id);
        SensorState sensorState = sensorStateRepository.findOne(id);
        return Optional.ofNullable(sensorState)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sensorStates/:id -> delete the "id" sensorState.
     */
    @RequestMapping(value = "/sensorStates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSensorState(@PathVariable Long id) {
        log.debug("REST request to delete SensorState : {}", id);
        sensorStateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sensorState", id.toString())).build();
    }

    /**
     * GET  /sensorStatesBetween -> get all SensorStates between to ZonedDateTime values
     */
    @RequestMapping(value = "/sensorStatesBetween",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SensorState>> findByCreatedDateBetween(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") DateTime start,
			@RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm") DateTime end) {
    	log.debug("start {}",start);
    	ZonedDateTime s = start.toGregorianCalendar().toZonedDateTime();
    	ZonedDateTime e = end.toGregorianCalendar().toZonedDateTime();
    	log.debug("REST request to get all SensorStates between {} and {}",s, e);

    	List<SensorState> results = sensorStateRepository.findByCreateddateBetween(s, e);
    	log.debug("results {}", results.size());

//    	return new ResponseEntity<List<SensorState>>(sensorStateRepository.findAll(), HttpStatus.OK);
    	return Optional.ofNullable(results)
    			.map(sensorState -> new ResponseEntity<>(
    					sensorState,
    					HttpStatus.OK))
    			.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
