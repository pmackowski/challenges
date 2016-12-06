package pl.pmackowski.directbus;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by pmackowski on 2016-12-03.
 */
@SpringBootApplication
@EnableCaching
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch(Exception e) {
            Throwable rootCause = Throwables.getRootCause(e);
            log.error(rootCause.getMessage());
            System.exit(1);
        }
    }

}
