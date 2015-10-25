package tinker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import tinker.data.entities.SensorState;

@Configuration
public class RepositoryConfiguration extends RepositoryRestMvcConfiguration {

	@Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(SensorState.class);
    }
}