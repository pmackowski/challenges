package pl.pmackowski.directbus.importer.flatfile;

import org.junit.Test;
import pl.pmackowski.directbus.importer.RoutesImporterException;
import pl.pmackowski.directbus.route.BusRoute;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by pmackowski on 2016-12-03.
 */
public class FlatFileBusRouteExtractorTest {

    private FlatFileBusRouteExtractor flatFileBusRouteExtractor = new FlatFileBusRouteExtractor();

    @Test
    public void shouldExtractBusRouteFromCorrectLine() {
        // given
        String line = "12 32 44 55 ";

        // when
        BusRoute actual = flatFileBusRouteExtractor.extract(line);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getBusRouteId()).isEqualTo(12);
        assertThat(actual.getBusStationsId()).containsExactly(32, 44, 55);
    }

    @Test
    public void shouldVerifyBlankLines() {
        // given
        String line = "    ";

        // when
        assertThatThrownBy(() -> flatFileBusRouteExtractor.extract(line))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"    \" should contain numbers separated by spaces");
    }

    @Test
    public void shouldVerifyIfRouteIdIsNumber() {
        // given
        String lineWith10BusStationDuplicates = "a 1 2";

        // when
        assertThatThrownBy(() -> flatFileBusRouteExtractor.extract(lineWith10BusStationDuplicates))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"a 1 2\" should contain numbers separated by spaces");
    }

    @Test
    public void shouldVerifyIfBusStationsAreNumbers() {
        // given
        String lineWith10BusStationDuplicates = "1 1 a 4";

        // when
        assertThatThrownBy(() -> flatFileBusRouteExtractor.extract(lineWith10BusStationDuplicates))
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Bus route \"1 1 a 4\" should contain numbers separated by spaces");
    }

}