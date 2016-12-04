package pl.pmackowski.directbus.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by pmackowski on 2016-12-03.
 */
@Component
public class RoutesImporterListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(RoutesImporterListener.class);
    private static final int ERROR_EXIT_CODE = -1;

    private RoutesImporter routesImporter;

    @Autowired
    public RoutesImporterListener(RoutesImporter routesImporter) {
        this.routesImporter = routesImporter;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Routes import has started");
        try {
            routesImporter.importBusRoutesToCache();
            log.info("Routes import has finished successfully");
        } catch (RoutesImporterException exc) {
            log.error(exc.getMessage());
            System.exit(ERROR_EXIT_CODE);
        } catch (Exception exc) {
            log.error("Routes import has not succeeded", exc);
            System.exit(ERROR_EXIT_CODE);
        }
    }


}
