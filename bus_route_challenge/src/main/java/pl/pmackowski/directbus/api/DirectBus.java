package pl.pmackowski.directbus.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pmackowski on 2016-12-03.
 */
public class DirectBus {

    @JsonProperty(value = "dep_sid")
    private final int departureSid;
    @JsonProperty(value = "arr_sid")
    private final int arrivalSid;
    @JsonProperty(value = "direct_bus_route")
    private final boolean directBusRoute;

    public DirectBus(int departureSid, int arrivalSid, boolean directBusRoute) {
        this.departureSid = departureSid;
        this.arrivalSid = arrivalSid;
        this.directBusRoute = directBusRoute;
    }

    public int getDepartureSid() {
        return departureSid;
    }

    public int getArrivalSid() {
        return arrivalSid;
    }

    public boolean isDirectBusRoute() {
        return directBusRoute;
    }
}
