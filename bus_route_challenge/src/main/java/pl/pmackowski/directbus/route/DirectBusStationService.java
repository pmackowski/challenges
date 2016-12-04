package pl.pmackowski.directbus.route;

/**
 * Created by pmackowski on 2016-12-03.
 */
public interface DirectBusStationService {

    boolean isDirectRoute(int departureId, int arrivalId);

}
