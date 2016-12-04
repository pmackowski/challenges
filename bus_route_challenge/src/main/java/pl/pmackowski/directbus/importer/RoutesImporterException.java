package pl.pmackowski.directbus.importer;

/**
 * Created by pmackowski on 2016-12-03.
 */
public class RoutesImporterException extends RuntimeException {

    public RoutesImporterException(String message) {
        super(message);
    }

    public RoutesImporterException(String message, Throwable cause) {
        super(message, cause);
    }
}
