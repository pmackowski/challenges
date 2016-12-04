package pl.pmackowski.directbus.importer.flatfile;

import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;
import pl.pmackowski.directbus.importer.RoutesImporterException;
import pl.pmackowski.directbus.route.BusRoute;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pmackowski on 2016-12-03.
 */
@Component
public class FlatFileBusRouteExtractor {

    private static final String BUS_ROUTE_ONLY_NUMBERS_MESSAGE = "Bus route \"%s\" should contain numbers separated by spaces";
    private static final char NUMBERS_SEPARATOR = ' ';

    public BusRoute extract(String line) {
        checkNotNull(line);
        String incorrectErrorMessage = String.format(BUS_ROUTE_ONLY_NUMBERS_MESSAGE, line);
        try {
            List<String> lineNumbers = getNumberFromLine(line);
            int busRouteId = getBusRouteId(lineNumbers).orElseThrow(() -> new RoutesImporterException(incorrectErrorMessage));
            List<Integer> busStationsId = getBusStationsId(lineNumbers);
            return new BusRoute(busRouteId, busStationsId);
        } catch (NumberFormatException exc) {
            throw new RoutesImporterException(incorrectErrorMessage);
        }
    }

    private List<String> getNumberFromLine(String line) {
        return Splitter.on(NUMBERS_SEPARATOR).omitEmptyStrings().trimResults().splitToList(line);
    }

    private OptionalInt getBusRouteId(List<String> numbers) {
        return numbers
                .stream()
                .mapToInt(Integer::parseInt)
                .findFirst();
    }

    private List<Integer> getBusStationsId(List<String> numbers) {
        return numbers
                .stream()
                .skip(1)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
