package pl.pmackowski.directbus.importer;

/**
 * Created by pmackowski on 2016-12-03.
 */
class RoutesImporterPreconditions {

    public static void check(boolean b, String errorMessage) {
        if (!b) {
            throw new RoutesImporterException(errorMessage);
        }
    }

    public static void check(boolean b, String errorMessage, Object... errorMessageArgs) {
        if (!b) {
            throw new RoutesImporterException(String.format(errorMessage, errorMessageArgs));
        }
    }

}
