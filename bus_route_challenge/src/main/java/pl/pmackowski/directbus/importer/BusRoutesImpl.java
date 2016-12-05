package pl.pmackowski.directbus.importer;

import com.gs.collections.api.list.primitive.IntList;
import pl.pmackowski.directbus.api.BusRoutes;

import java.util.Map;

/**
 * Created by pmackowski on 2016-12-04.
 */
class BusRoutesImpl implements BusRoutes {

    private int numberOfRoutes;
    private Map<Integer, IntList> busRoutes;

    public BusRoutesImpl(int numberOfRoutes, Map<Integer, IntList> busRoutes) {
        this.numberOfRoutes = numberOfRoutes;
        this.busRoutes = busRoutes;
    }

    public int getNumberOfRoutes() {
        return numberOfRoutes;
    }

    public Map<Integer, IntList> getBusRoutes() {
        return busRoutes;
    }
}
