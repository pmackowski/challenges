package pl.pmackowski.directbus.route;

import com.google.common.annotations.VisibleForTesting;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by pmackowski on 2016-12-04.
 */
public class BusRoutes {

    private int numberOfRoutes;
    private Map<Integer, MutableIntList> busRoutes = new HashMap<>();

    public BusRoutes(int numberOfRoutes) {
        this.numberOfRoutes = numberOfRoutes;
    }

    @VisibleForTesting
    BusRoutes(int numberOfRoutes, BusRoute ... busRoute) {
        this.numberOfRoutes = numberOfRoutes;
        Stream.of(busRoute).forEach(this::addBusRoute);
    }

    public void addBusRoute(BusRoute busRoute) {
        int[] busStationsId  = busRoute.getBusStationsId().stream().mapToInt(i->i).toArray();
        busRoutes.put(busRoute.getBusRouteId(), new IntArrayList(busStationsId));
    }

    public int getNumberOfRoutes() {
        return numberOfRoutes;
    }

    public Map<Integer, MutableIntList> getBusRoutes() {
        return busRoutes;
    }
}
