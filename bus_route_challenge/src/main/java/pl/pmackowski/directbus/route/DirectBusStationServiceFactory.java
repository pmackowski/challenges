package pl.pmackowski.directbus.route;

import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.pmackowski.directbus.api.BusRoutes;
import pl.pmackowski.directbus.api.DirectBusStationService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pmackowski on 2016-12-04.
 */
@Configuration
public class DirectBusStationServiceFactory {

    @Bean
    public DirectBusStationService create(@Autowired BusRoutes busRoutes) {
        Map<Integer, IntList> busRouteToStations = busRoutes.getBusRoutes();
        Map<Integer, MutableIntList> busStationToSortedRoutes = createBusStationToSortedRoutes(busRoutes);
        return new PrimitiveDirectBusStationService(busRouteToStations, busStationToSortedRoutes);
    }

    private Map<Integer, MutableIntList> createBusStationToSortedRoutes(BusRoutes busRoutes) {
        Map<Integer, IntList> busRoutesMap = busRoutes.getBusRoutes();
        Map<Integer, MutableIntList> busStations = new HashMap<>();

        busRoutesMap.entrySet().stream().forEach(routeEntry -> {
            int routeId = routeEntry.getKey();
            IntList busStationsId = routeEntry.getValue();
            busStationsId.forEach(busStationId -> {
                busStations.putIfAbsent(busStationId, new IntArrayList());
                busStations.get(busStationId).add(routeId);
            });
        });
        busStations.values().forEach(MutableIntList::sortThis);
        return busStations;
    }

}
