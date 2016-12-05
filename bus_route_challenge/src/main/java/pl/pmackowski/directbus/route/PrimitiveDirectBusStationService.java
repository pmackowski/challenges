package pl.pmackowski.directbus.route;

import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import org.springframework.cache.annotation.Cacheable;
import pl.pmackowski.directbus.api.DirectBusStationService;

import java.util.HashMap;
import java.util.Map;

/**
 * GS collections are used to optimize memory usage (IntList occupies at least 4 times less memory than ArrayList)
 *
 * Created by pmackowski on 2016-12-03.
 */
public class PrimitiveDirectBusStationService implements DirectBusStationService {

    private Map<Integer, IntList> busRouteToStations = new HashMap<>();
    private Map<Integer, MutableIntList> busStationToSortedRoutes = new HashMap<>();

    PrimitiveDirectBusStationService(Map<Integer, IntList> busRouteToStations, Map<Integer, MutableIntList> busStationToSortedRoutes) {
        this.busRouteToStations = busRouteToStations;
        this.busStationToSortedRoutes = busStationToSortedRoutes;
    }

    /**
     * In pessimistic scenario, two stations belong to every route.
     * In such case, we need to intersect two 100,000 sorted IntList
     *
     */
    @Override
    @Cacheable("directRoutes")
    public boolean isDirectRoute(int departureId, int arrivalId) {
        IntList sortedDepartureRoutes = busStationToSortedRoutes.get(departureId);
        IntList sortedArrivalRoutes = busStationToSortedRoutes.get(arrivalId);

        if (sortedDepartureRoutes == null || sortedArrivalRoutes == null) {
            return false;
        }
        return intersectRoutes(sortedDepartureRoutes, sortedArrivalRoutes).anySatisfy(routeId -> {
            IntList busStations = busRouteToStations.get(routeId);
            return busStations.indexOf(departureId) < busStations.indexOf(arrivalId);
        });
    }

    /**
     * Because both lists are sorted, time complexity is O(m+n)
     *
     *  TODO verify
     *      int initialCapacity = Math.min(departureSize,arrivalSize);
     *  to gain performance
     */
    private IntList intersectRoutes(IntList sortedDepartureRoutes, IntList sortedArrivalRoutes) {
        int departureSize = sortedDepartureRoutes.size();
        int arrivalSize = sortedArrivalRoutes.size();
        MutableIntList intersectRoutes = new IntArrayList();
        int i = 0, j = 0;
        while (i < departureSize && j < arrivalSize) {
            if (sortedDepartureRoutes.get(i) > sortedArrivalRoutes.get(j)) {
                j++;
            } else if (sortedArrivalRoutes.get(j) > sortedDepartureRoutes.get(i)) {
                i++;
            } else {
                intersectRoutes.add(sortedDepartureRoutes.get(i));
                i++;
                j++;
            }
        }
        return intersectRoutes;
    }
}
