package pl.pmackowski.directbus.route;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RunWith(Theories.class)
public class PrimitiveDirectBusResponseStationServiceTest {

    @Mock
    private BusRoute busRoute1, busRoute2, busRoute3;

    private DirectBusStationService directBusStationService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unused")
    @DataPoints
    public static BusRouteTestCase[] testCases() {
        return new BusRouteTestCase[]{
                new BusRouteTestCase(1, 2, true),
                new BusRouteTestCase(1, 3, true),
                new BusRouteTestCase(1, 4, true),
                new BusRouteTestCase(4, 8, true),
                new BusRouteTestCase(10, 3, true),

                new BusRouteTestCase(1, 8, false), // indirect route
                new BusRouteTestCase(4, 1, false), // opposite direction
                new BusRouteTestCase(1000, 1, false), // departure not exist
                new BusRouteTestCase(1, 103, false), // arrival not exist
                new BusRouteTestCase(132, 103, false), // departure and arrival not exist
        };
    }

    @Theory
    public void shouldCheckDirectRoute(BusRouteTestCase testCase) {
        // given
        given(busRoute1.getBusRouteId()).willReturn(1);
        given(busRoute2.getBusRouteId()).willReturn(2);
        given(busRoute3.getBusRouteId()).willReturn(3);

        given(busRoute1.getBusStationsId()).willReturn(of(1, 2, 3, 4));
        given(busRoute2.getBusStationsId()).willReturn(of(4, 8));
        given(busRoute3.getBusStationsId()).willReturn(of(10, 3, 9));

        BusRoutes busRoutes = new BusRoutes(3, busRoute1, busRoute2, busRoute3);
        directBusStationService = new DirectBusStationServiceFactory().create(busRoutes);

        // when
        boolean actual = directBusStationService.isDirectRoute(testCase.getDepartureId(), testCase.getArrivalId());

        // then
        assertThat(actual).isEqualTo(testCase.isDirectRoute());
    }

    public static class BusRouteTestCase {
        private final int departureId;
        private final int arrivalId;
        private final boolean directRoute;

        public BusRouteTestCase(int departureId, int arrivalId, boolean directRoute) {
            this.departureId = departureId;
            this.arrivalId = arrivalId;
            this.directRoute = directRoute;
        }

        public int getDepartureId() {
            return departureId;
        }

        public int getArrivalId() {
            return arrivalId;
        }

        public boolean isDirectRoute() {
            return directRoute;
        }
    }
}