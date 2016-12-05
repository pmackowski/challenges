package pl.pmackowski.directbus.api;

import com.gs.collections.api.list.primitive.IntList;

import java.util.Map;

/**
 * Created by pmackowski on 2016-12-04.
 */
public interface BusRoutes {

    int getNumberOfRoutes();

    Map<Integer, IntList> getBusRoutes();

}
