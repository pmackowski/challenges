package pl.pmackowski.directbus.importer.flatfile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pmackowski.directbus.importer.RoutesImporterException;
import pl.pmackowski.directbus.route.DirectBusStationService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

/**
 * Created by pmackowski on 2016-12-03.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlatFileRoutesImporterTest {

    private static final String RESOURCES_DIRECTORY = "src/test/resources/";

    @Autowired
    private FlatFileRoutesImporter flatFileRoutesImporter;

    @MockBean
    @SuppressWarnings("unused")
    private DirectBusStationService directBusStationService;

    @Before
    public void setup() {
        Mockito.doNothing().when(directBusStationService).addBusRoute(any());
    }

    @Test
    public void shouldImportBusRoutesFromCorrectFile() {
        // given
        flatFileRoutesImporter.routesFilePath = file("bus_routes_correct_file");

        // when
        flatFileRoutesImporter.importBusRoutesToCache();

        // then
        verify(directBusStationService, atLeast(10)).addBusRoute(any());
    }

    @Test
    public void shouldVerifyIfFileExists() {
        // given
        flatFileRoutesImporter.routesFilePath = file("bus_routes_file_does_not_exists");

        // when
        assertThatThrownBy(flatFileRoutesImporter::importBusRoutesToCache)
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Input file src/test/resources/bus_routes_file_does_not_exists does not exist");
    }

    @Test
    public void shouldVerifyIfFileIsRegularFile() {
        // given
        flatFileRoutesImporter.routesFilePath = file("");

        // when
        assertThatThrownBy(flatFileRoutesImporter::importBusRoutesToCache)
                .isInstanceOf(RoutesImporterException.class)
                .hasMessage("Input file src/test/resources/ is not a regular file");
    }

    @Test
    public void shouldStopImportingIfHeaderIsIncorrect() {
        // given
        flatFileRoutesImporter.routesFilePath = file("bus_routes_incorrect_header");

        // when
        assertThatThrownBy(flatFileRoutesImporter::importBusRoutesToCache)
                .isInstanceOf(RoutesImporterException.class);
    }

    @Test
    public void shouldStopImportingOnFirstIncorrectRoute() {
        // given
        flatFileRoutesImporter.routesFilePath = file("bus_routes_with_many_incorrect_routes");

        // when
        assertThatThrownBy(flatFileRoutesImporter::importBusRoutesToCache)
                .isInstanceOf(RoutesImporterException.class);
    }

    private String file(String fileName) {
        return RESOURCES_DIRECTORY + fileName;
    }

}