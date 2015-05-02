package tinker;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.boot.autoconfigure.data.rest.SpringBootRepositoryRestMvcConfiguration;

import tinker.data.entities.SensorState;

@Configuration
public class RestMvcConfiguration extends SpringBootRepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    	System.out.println("something will be logged here");
        super.configureRepositoryRestConfiguration(config);
    	config.exposeIdsFor(SensorState.class);
    }
}