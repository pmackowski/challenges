package pl.pmackowski.directbus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.pmackowski.directbus.importer.RoutesImporter;
import pl.pmackowski.directbus.route.BusRoutes;
import pl.pmackowski.directbus.route.DirectBusStationService;
import pl.pmackowski.directbus.route.DirectBusStationServiceFactory;

/**
 * Created by pmackowski on 2016-12-04.
 */
@Configuration
public class AppConfig {

    @Bean @Lazy(value = false)
    public DirectBusStationService directBusStationService(@Autowired RoutesImporter routesImporter,
                                                           @Autowired DirectBusStationServiceFactory directBusStationServiceFactory) {
        BusRoutes busRoutes = routesImporter.importBusRoutes();
        return directBusStationServiceFactory.create(busRoutes);
    }

}
