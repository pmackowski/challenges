package pl.pmackowski.directbus.route;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.pmackowski.directbus.api.BusRoutes;
import pl.pmackowski.directbus.api.DirectBusStationService;

import static com.gs.collections.impl.list.mutable.primitive.IntArrayList.newListWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RunWith(Theories.class)
public class PrimitiveDirectBusResponseStationServiceTest {

    @Mock
    private BusRoutes busRoutes;

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

        given(busRoutes.getNumberOfRoutes()).willReturn(3);
        given(busRoutes.getBusRoutes()).willReturn(ImmutableMap.of(
                1, newListWith(1, 2, 3, 4),
                2, newListWith(4, 8),
                3, newListWith(10, 3, 9)
        ));

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