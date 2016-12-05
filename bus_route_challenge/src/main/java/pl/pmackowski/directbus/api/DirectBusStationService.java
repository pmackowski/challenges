package pl.pmackowski.directbus.api;

/**
 * Created by pmackowski on 2016-12-03.
 */
public interface DirectBusStationService {

    boolean isDirectRoute(int departureId, int arrivalId);

}
