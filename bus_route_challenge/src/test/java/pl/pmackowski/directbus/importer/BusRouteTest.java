package pl.pmackowski.directbus.importer;

import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.pmackowski.directbus.importer.RoutesImporterException;
import pl.pmackowski.directbus.importer.BusRoute;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RunWith(MockitoJUnitRunner.class)
public class BusRouteTest {

    @Test
    public void shouldVerifyThatBusRouteContainsAtLeastTwoStations() {
        // when
        assertThatThrownBy(() -> new BusRoute(12, IntArrayList.newListWith(1)))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route 12 should contain at least 2 stations");
    }

    @Test
    public void shouldVerifyUniqueBusStations() {
        // when
        assertThatThrownBy(() -> new BusRoute(12, IntArrayList.newListWith(10, 1, 2, 3, 4, 5, 10)))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route 12 should contain unique stations");
    }

}