package pl.pmackowski.directbus.route;

import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pmackowski on 2016-12-04.
 */
@Component
public class DirectBusStationServiceFactory {

    public DirectBusStationService create(BusRoutes busRoutes) {
        Map<Integer, MutableIntList> busRoutesMap = busRoutes.getBusRoutes();
        Map<Integer, MutableIntList> busStationsMap = createBusStations(busRoutes);
        return new PrimitiveDirectBusStationService(busRoutesMap, busStationsMap);
    }

    private Map<Integer, MutableIntList> createBusStations(BusRoutes busRoutes) {
        Map<Integer, MutableIntList> busRoutesMap = busRoutes.getBusRoutes();
        Map<Integer, MutableIntList> busStations = new HashMap<>();

        busRoutesMap.entrySet().stream().forEach(routeEntry -> {
            int routeId = routeEntry.getKey();
            MutableIntList busStationsId = routeEntry.getValue();
            busStationsId.forEach(busStationId -> {
                busStations.putIfAbsent(busStationId, new IntArrayList());
                busStations.get(busStationId).add(routeId);
            });
        });
        return busStations;
    }

}
