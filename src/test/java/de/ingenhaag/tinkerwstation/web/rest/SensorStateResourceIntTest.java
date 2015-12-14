package de.ingenhaag.tinkerwstation.web.rest;

import de.ingenhaag.tinkerwstation.Application;
import de.ingenhaag.tinkerwstation.domain.SensorState;
import de.ingenhaag.tinkerwstation.repository.SensorStateRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SensorStateResource REST controller.
 *
 * @see SensorStateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensorStateResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final Double DEFAULT_AIRPRESSURE = 1D;
    private static final Double UPDATED_AIRPRESSURE = 2D;

    private static final Double DEFAULT_HUMIDITY = 1D;
    private static final Double UPDATED_HUMIDITY = 2D;

    private static final Double DEFAULT_ILLUMINANCE = 1D;
    private static final Double UPDATED_ILLUMINANCE = 2D;

    private static final ZonedDateTime DEFAULT_CREATEDDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATEDDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATEDDATE_STR = dateTimeFormatter.format(DEFAULT_CREATEDDATE);

    @Inject
    private SensorStateRepository sensorStateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSensorStateMockMvc;

    private SensorState sensorState;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensorStateResource sensorStateResource = new SensorStateResource();
        ReflectionTestUtils.setField(sensorStateResource, "sensorStateRepository", sensorStateRepository);
        this.restSensorStateMockMvc = MockMvcBuilders.standaloneSetup(sensorStateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sensorState = new SensorState();
        sensorState.setTemperature(DEFAULT_TEMPERATURE);
        sensorState.setAirpressure(DEFAULT_AIRPRESSURE);
        sensorState.setHumidity(DEFAULT_HUMIDITY);
        sensorState.setIlluminance(DEFAULT_ILLUMINANCE);
        sensorState.setCreateddate(DEFAULT_CREATEDDATE);
    }

    @Test
    @Transactional
    public void createSensorState() throws Exception {
        int databaseSizeBeforeCreate = sensorStateRepository.findAll().size();

        // Create the SensorState

        restSensorStateMockMvc.perform(post("/api/sensorStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorState)))
                .andExpect(status().isCreated());

        // Validate the SensorState in the database
        List<SensorState> sensorStates = sensorStateRepository.findAll();
        assertThat(sensorStates).hasSize(databaseSizeBeforeCreate + 1);
        SensorState testSensorState = sensorStates.get(sensorStates.size() - 1);
        assertThat(testSensorState.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testSensorState.getAirpressure()).isEqualTo(DEFAULT_AIRPRESSURE);
        assertThat(testSensorState.getHumidity()).isEqualTo(DEFAULT_HUMIDITY);
        assertThat(testSensorState.getIlluminance()).isEqualTo(DEFAULT_ILLUMINANCE);
        assertThat(testSensorState.getCreateddate()).isEqualTo(DEFAULT_CREATEDDATE);
    }

    @Test
    @Transactional
    public void getAllSensorStates() throws Exception {
        // Initialize the database
        sensorStateRepository.saveAndFlush(sensorState);

        // Get all the sensorStates
        restSensorStateMockMvc.perform(get("/api/sensorStates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensorState.getId().intValue())))
                .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
                .andExpect(jsonPath("$.[*].airpressure").value(hasItem(DEFAULT_AIRPRESSURE.doubleValue())))
                .andExpect(jsonPath("$.[*].humidity").value(hasItem(DEFAULT_HUMIDITY.doubleValue())))
                .andExpect(jsonPath("$.[*].illuminance").value(hasItem(DEFAULT_ILLUMINANCE.doubleValue())))
                .andExpect(jsonPath("$.[*].createddate").value(hasItem(DEFAULT_CREATEDDATE_STR)));
    }

    @Test
    @Transactional
    public void getSensorState() throws Exception {
        // Initialize the database
        sensorStateRepository.saveAndFlush(sensorState);

        // Get the sensorState
        restSensorStateMockMvc.perform(get("/api/sensorStates/{id}", sensorState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensorState.getId().intValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.airpressure").value(DEFAULT_AIRPRESSURE.doubleValue()))
            .andExpect(jsonPath("$.humidity").value(DEFAULT_HUMIDITY.doubleValue()))
            .andExpect(jsonPath("$.illuminance").value(DEFAULT_ILLUMINANCE.doubleValue()))
            .andExpect(jsonPath("$.createddate").value(DEFAULT_CREATEDDATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSensorState() throws Exception {
        // Get the sensorState
        restSensorStateMockMvc.perform(get("/api/sensorStates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorState() throws Exception {
        // Initialize the database
        sensorStateRepository.saveAndFlush(sensorState);

		int databaseSizeBeforeUpdate = sensorStateRepository.findAll().size();

        // Update the sensorState
        sensorState.setTemperature(UPDATED_TEMPERATURE);
        sensorState.setAirpressure(UPDATED_AIRPRESSURE);
        sensorState.setHumidity(UPDATED_HUMIDITY);
        sensorState.setIlluminance(UPDATED_ILLUMINANCE);
        sensorState.setCreateddate(UPDATED_CREATEDDATE);

        restSensorStateMockMvc.perform(put("/api/sensorStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensorState)))
                .andExpect(status().isOk());

        // Validate the SensorState in the database
        List<SensorState> sensorStates = sensorStateRepository.findAll();
        assertThat(sensorStates).hasSize(databaseSizeBeforeUpdate);
        SensorState testSensorState = sensorStates.get(sensorStates.size() - 1);
        assertThat(testSensorState.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testSensorState.getAirpressure()).isEqualTo(UPDATED_AIRPRESSURE);
        assertThat(testSensorState.getHumidity()).isEqualTo(UPDATED_HUMIDITY);
        assertThat(testSensorState.getIlluminance()).isEqualTo(UPDATED_ILLUMINANCE);
        assertThat(testSensorState.getCreateddate()).isEqualTo(UPDATED_CREATEDDATE);
    }

    @Test
    @Transactional
    public void deleteSensorState() throws Exception {
        // Initialize the database
        sensorStateRepository.saveAndFlush(sensorState);

		int databaseSizeBeforeDelete = sensorStateRepository.findAll().size();

        // Get the sensorState
        restSensorStateMockMvc.perform(delete("/api/sensorStates/{id}", sensorState.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorState> sensorStates = sensorStateRepository.findAll();
        assertThat(sensorStates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
