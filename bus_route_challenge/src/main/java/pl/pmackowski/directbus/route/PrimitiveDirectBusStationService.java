package pl.pmackowski.directbus.route;

import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.set.primitive.IntSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * GS collections are used to optimize memory usage.
 * IntSet and IntList occupy at least 4 times less memory than JDK collections (e.g. ArrayList)
 *
 * Created by pmackowski on 2016-12-03.
 */
@Component
public class PrimitiveDirectBusStationService implements DirectBusStationService {

    private Map<Integer, MutableIntSet> busStations = new HashMap<>();
    private Map<Integer, MutableIntList> busRoutes = new HashMap<>();

    @Override
    public void addBusRoute(BusRoute busRoute) {
        int[] busStationsArrayInt  = busRoute.getBusStationsId().stream().mapToInt(i->i).toArray();
        IntArrayList busStationsArray = new IntArrayList(busStationsArrayInt);
        busRoutes.put(busRoute.getBusRouteId(), busStationsArray);
        busStationsArray.forEach(busStationId -> {
            busStations.putIfAbsent(busStationId, new IntHashSet());
            busStations.get(busStationId).add(busRoute.getBusRouteId());
        });
    }

    /**
     * In pessimistic scenario, two stations belong to every route.
     * In such case, we need to intersect two 100,000 elements sets which could be too slow.
     *
     * TODO for large pairs <departureRoutes,arrivalRoutes> pre-compute isDirectRoute method and store its results in cache
     *
     */
    @Override
    @Cacheable("directRoutes")
    public boolean isDirectRoute(int departureId, int arrivalId) {
        IntSet departureRoutes = busStations.get(departureId);
        IntSet arrivalRoutes = busStations.get(arrivalId);

        if (departureRoutes == null || arrivalRoutes == null) {
            return false;
        }

        int[] dep = departureRoutes.select(arrivalRoutes::contains).toArray();
        int[] arr = arrivalRoutes.select(departureRoutes::contains).toArray();
        MutableIntSet intersect = new IntHashSet();
        intersect.addAll(dep);
        intersect.addAll(arr);

        return intersect.anySatisfy(routeId -> {
            MutableIntList busStations = busRoutes.get(routeId);
            return busStations.indexOf(departureId) < busStations.indexOf(arrivalId);
        });
    }
}
