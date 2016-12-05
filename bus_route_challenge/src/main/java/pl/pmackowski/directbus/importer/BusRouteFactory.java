package pl.pmackowski.directbus.importer;

import com.google.common.base.Splitter;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.impl.list.immutable.primitive.ImmutableIntListFactoryImpl;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.OptionalInt;

import static com.google.common.base.Preconditions.checkNotNull;
import static pl.pmackowski.directbus.importer.RoutesImporterPreconditions.check;

/**
 * Created by pmackowski on 2016-12-03.
 */
@Component
class BusRouteFactory {

    private static int MINIMUM_NUMBER_OF_STATIONS = 2;
    private static final String BUS_TWO_STATIONS_MESSAGE = "Bus route %s should contain at least 2 stations";
    private static final String UNIQUE_BUS_STATIONS_MESSAGE = "Bus route %s should contain unique stations";
    private static final String BUS_ROUTE_ONLY_NUMBERS_MESSAGE = "Bus route \"%s\" should contain numbers separated by spaces";
    private static final char NUMBERS_SEPARATOR = ' ';

    public BusRoute from(String line) {
        checkNotNull(line);
        String incorrectErrorMessage = String.format(BUS_ROUTE_ONLY_NUMBERS_MESSAGE, line);
        try {
            List<String> lineNumbers = getNumberFromLine(line);
            int busRouteId = getBusRouteId(lineNumbers).orElseThrow(() -> new RoutesImporterException(incorrectErrorMessage));
            IntList busStationsId = getBusStationsId(lineNumbers);
            check(containsMinimumTwoStations(busStationsId), BUS_TWO_STATIONS_MESSAGE, busRouteId);
            check(doesNotContainDuplicates(busStationsId), UNIQUE_BUS_STATIONS_MESSAGE, busRouteId);
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

    private IntList getBusStationsId(List<String> numbers) {
        int[] busStationsId = numbers
                .stream()
                .skip(1)
                .mapToInt(Integer::parseInt)
                .toArray();
        return new ImmutableIntListFactoryImpl().of(busStationsId);
    }

    private boolean containsMinimumTwoStations(IntList busStationsId) {
        return busStationsId.size() >= MINIMUM_NUMBER_OF_STATIONS;
    }

    private boolean doesNotContainDuplicates(IntList busStationsId) {
        return new IntHashSet(busStationsId.toArray()).size() == busStationsId.size();
    }
}
