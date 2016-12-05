package pl.pmackowski.directbus.importer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by pmackowski on 2016-12-03.
 */
public class BusRouteFactoryTest {

    private BusRouteFactory busRouteFactory = new BusRouteFactory();

    @Test
    public void shouldExtractBusRouteFromCorrectLine() {
        // given
        String line = "12 32 44 55 ";

        // when
        BusRoute actual = busRouteFactory.from(line);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getBusRouteId()).isEqualTo(12);
        assertThat(actual.getBusStationsId().toArray()).containsExactly(32, 44, 55);
    }

    @Test
    public void shouldVerifyBlankLines() {
        // given
        String line = "    ";

        // when
        assertThatThrownBy(() -> busRouteFactory.from(line))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"    \" should contain numbers separated by spaces");
    }

    @Test
    public void shouldVerifyIfRouteIdIsNumber() {
        // given
        String lineWith10BusStationDuplicates = "a 1 2";

        // when
        assertThatThrownBy(() -> busRouteFactory.from(lineWith10BusStationDuplicates))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"a 1 2\" should contain numbers separated by spaces");
    }

    @Test
    public void shouldVerifyIfBusStationsAreNumbers() {
        // given
        String lineWith10BusStationDuplicates = "1 1 a 4";

        // when
        assertThatThrownBy(() -> busRouteFactory.from(lineWith10BusStationDuplicates))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"1 1 a 4\" should contain numbers separated by spaces");
    }

    @Test
    public void shouldVerifyThatBusRouteContainsAtLeastTwoStations() {
        // given
        String line = "12 2";

        // when
        assertThatThrownBy(() -> busRouteFactory.from(line))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route 12 should contain at least 2 stations");
    }

    @Test
    public void shouldVerifyUniqueBusStations() {
        // given
        String line = "12 10 1 2 3 4 5 10";

        // when
        assertThatThrownBy(() -> busRouteFactory.from(line))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route 12 should contain unique stations");
    }

}