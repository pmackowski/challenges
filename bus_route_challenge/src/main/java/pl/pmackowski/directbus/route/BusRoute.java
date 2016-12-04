package pl.pmackowski.directbus.route;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static pl.pmackowski.directbus.importer.RoutesImporterPreconditions.check;

/**
 * Created by pmackowski on 2016-12-03.
 */
public class BusRoute {

    private static int MINIMUM_NUMBER_OF_STATIONS = 2;
    private static final String BUS_TWO_STATIONS_MESSAGE = "Bus route %s should contain at least 2 stations";
    private static final String UNIQUE_BUS_STATIONS_MESSAGE = "Bus route %s should contain unique stations";

    private int busRouteId;
    private List<Integer> busStationsId;

    public BusRoute(int busRouteId, List<Integer> busStationsId) {
        checkNotNull(busStationsId);
        check(containsMinimumTwoStations(busStationsId), BUS_TWO_STATIONS_MESSAGE, busRouteId);
        check(doesNotContainDuplicates(busStationsId), UNIQUE_BUS_STATIONS_MESSAGE, busRouteId);
        this.busRouteId = busRouteId;
        this.busStationsId = busStationsId;
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public List<Integer> getBusStationsId() {
        return busStationsId;
    }

    private boolean containsMinimumTwoStations(List<Integer> busStationsId) {
        return busStationsId.size() >= MINIMUM_NUMBER_OF_STATIONS;
    }

    private boolean doesNotContainDuplicates(List<Integer> busStationsId) {
        return newHashSet(busStationsId).size() == busStationsId.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusRoute busRoute = (BusRoute) o;
        return Objects.equals(busRouteId, busRoute.busRouteId) &&
                Objects.equals(busStationsId, busRoute.busStationsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busRouteId, busStationsId);
    }

}
