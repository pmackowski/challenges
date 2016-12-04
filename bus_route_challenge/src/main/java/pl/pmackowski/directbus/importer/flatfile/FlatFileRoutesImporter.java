package pl.pmackowski.directbus.importer.flatfile;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.pmackowski.directbus.importer.RoutesImporter;
import pl.pmackowski.directbus.importer.RoutesImporterException;
import pl.pmackowski.directbus.route.BusRoutes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.String.format;
import static pl.pmackowski.directbus.importer.RoutesImporterPreconditions.check;

/**
 * Created by pmackowski on 2016-12-03.
 */
@Component
public class FlatFileRoutesImporter implements RoutesImporter {

    private static final String PATH_DOES_NOT_EXIST_MESSAGE = "Input file %s does not exist";
    private static final String PATH_IS_NOT_A_REGULAR_FILE_MESSAGE = "Input file %s is not a regular file";
    private static final String PATH_IS_NOT_READABLE_MESSAGE = "Application has not appropriate privileges to open file %s for reading";
    private static final String INCORRECT_HEADER_MESSAGE = "Input file %s has incorrect header. Header should be a positive number";
    private static final String FILE_CANNOT_BE_PROCESSED = "Input file %s cannot be processed";

    @VisibleForTesting
    String routesFilePath;
    private final FlatFileBusRouteExtractor flatFileBusRouteExtractor;

    @Autowired
    public FlatFileRoutesImporter(@Value("${routes.file.path}") String routesFilePath,
                                  FlatFileBusRouteExtractor flatFileBusRouteExtractor) {
        this.routesFilePath = routesFilePath;
        this.flatFileBusRouteExtractor = flatFileBusRouteExtractor;
    }

    @Override
    public BusRoutes importBusRoutes() {
        Path path = Paths.get(routesFilePath);

        check(Files.exists(path), PATH_DOES_NOT_EXIST_MESSAGE, routesFilePath);
        check(Files.isRegularFile(path), PATH_IS_NOT_A_REGULAR_FILE_MESSAGE, routesFilePath);
        check(Files.isReadable(path), PATH_IS_NOT_READABLE_MESSAGE, routesFilePath);

        int numberOfRoutes = getNumberOfRoutes(path);
        check(numberOfRoutes > 0, INCORRECT_HEADER_MESSAGE, routesFilePath);
        BusRoutes busRoutes = new BusRoutes(numberOfRoutes);
        importBusRoutes(path, busRoutes, numberOfRoutes);
        return busRoutes;
    }

    private int getNumberOfRoutes(Path path) {
        String errorMessage = format(INCORRECT_HEADER_MESSAGE, routesFilePath);
        try (Stream<String> stream = Files.lines(path)) {
            return stream
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .findFirst()
                    .orElseThrow(() -> new RoutesImporterException(errorMessage));
        } catch (NumberFormatException exc) {
            throw new RoutesImporterException(errorMessage);
        } catch (IOException exc) {
            throw new RoutesImporterException(format(FILE_CANNOT_BE_PROCESSED, routesFilePath), exc);
        }
    }

    private void importBusRoutes(Path path, BusRoutes busRoutes, int numberOfRoutes) {
        try (Stream<String> stream = Files.lines(path)) {
            stream
                .skip(1)
                .limit(numberOfRoutes) // further routes are ignored
                .map(flatFileBusRouteExtractor::extract)
                .forEach(busRoutes::addBusRoute);
        } catch (IOException exc) {
            throw new RoutesImporterException(format(FILE_CANNOT_BE_PROCESSED, routesFilePath), exc);
        }

    }
}
