package pl.pmackowski.directbus.importer;

import com.google.common.base.Splitter;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.OptionalInt;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pmackowski on 2016-12-03.
 */
@Component
class BusRouteFactory {

    private static final String BUS_ROUTE_ONLY_NUMBERS_MESSAGE = "Bus route \"%s\" should contain numbers separated by spaces";
    private static final char NUMBERS_SEPARATOR = ' ';

    public BusRoute from(String line) {
        checkNotNull(line);
        String incorrectErrorMessage = String.format(BUS_ROUTE_ONLY_NUMBERS_MESSAGE, line);
        try {
            List<String> lineNumbers = getNumberFromLine(line);
            int busRouteId = getBusRouteId(lineNumbers).orElseThrow(() -> new RoutesImporterException(incorrectErrorMessage));
            MutableIntList busStationsId = getBusStationsId(lineNumbers);
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

    private MutableIntList getBusStationsId(List<String> numbers) {
        int[] busStationsId = numbers
                .stream()
                .skip(1)
                .mapToInt(Integer::parseInt)
                .toArray();
        return new IntArrayList(busStationsId);
    }

}
