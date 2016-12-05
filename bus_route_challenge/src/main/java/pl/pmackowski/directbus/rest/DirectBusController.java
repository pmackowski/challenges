package pl.pmackowski.directbus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pmackowski.directbus.api.DirectBusStationService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RestController
public class DirectBusController {

    static final String API_DIRECT = "/api/direct";
    static final String DEPARTURE_SID_PARAM = "dep_sid";
    static final String ARRIVAL_SID_PARAM = "arr_sid";

    private final DirectBusStationService directBusStationService;

    @Autowired
    public DirectBusController(DirectBusStationService directBusStationService) {
        this.directBusStationService = directBusStationService;
    }

    @RequestMapping(value = API_DIRECT, method = GET, produces = APPLICATION_JSON_VALUE)
    public DirectBusResponse isDirectRoute(@RequestParam(value = DEPARTURE_SID_PARAM) int departureSid,
                                   @RequestParam(value = ARRIVAL_SID_PARAM) int arrivalSid) {
        boolean isDirectRoute = directBusStationService.isDirectRoute(departureSid, arrivalSid);
        return new DirectBusResponse(departureSid, arrivalSid, isDirectRoute);
    }

}
