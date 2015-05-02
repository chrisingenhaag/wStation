package tinker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableAsync
@EnableScheduling
@EnableJpaRepositories
@Import(RestMvcConfiguration.class)
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        SensorStateRepository repository = context.getBean(SensorStateRepository.class);
//
//        for(int i = 0; i < 100; i++) {
//        	SensorState s = new SensorState();
//        	s.setAirpressure(i);
//        	s.setHumidity(i);
//        	s.setTemperature(i);
//        	s.setIlluminance(i);
//        	s.setCreatedDate(DateTime.now());
//        	repository.save(s);
//        	try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        
//        Iterable<SensorState> states = repository.findAll();
//        
//        for(SensorState state : states) {
//        	System.out.println(state);
//        }
//        
//        context.close();
//        
    }
}
