package pl.pmackowski.directbus.importer;

import com.gs.collections.api.list.primitive.IntList;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pmackowski on 2016-12-03.
 */
class BusRoute {

    private int busRouteId;
    private IntList busStationsId;

    public BusRoute(int busRouteId, IntList busStationsId) {
        checkNotNull(busStationsId);
        this.busRouteId = busRouteId;
        this.busStationsId = busStationsId;
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public IntList getBusStationsId() {
        return busStationsId;
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
