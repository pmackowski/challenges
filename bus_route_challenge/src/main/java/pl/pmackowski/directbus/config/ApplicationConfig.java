package pl.pmackowski.directbus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.pmackowski.directbus.api.BusRoutes;
import pl.pmackowski.directbus.api.RoutesImporter;

/**
 * Created by pmackowski on 2016-12-04.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public BusRoutes busRoutes(@Autowired RoutesImporter routesImporter) {
        return routesImporter.importBusRoutes();
    }

}
