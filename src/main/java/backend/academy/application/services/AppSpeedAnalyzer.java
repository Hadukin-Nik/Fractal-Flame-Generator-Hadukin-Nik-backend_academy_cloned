package backend.academy.application.services;

import backend.academy.application.models.flame_picture.AppConfigurationData;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class AppSpeedAnalyzer implements IApp {
    private final Logger logger;

    private final AppConfigurationData appConfigurationData;

    public void start() {
        logger.info("Starting AppSpeedAnalyzer");
        App app = new App(appConfigurationData);

        double speeds = 0;

        logger.info("Parallel started");
        appConfigurationData.userCommand().isParallelOn(true);
        for (int i = 0; i < appConfigurationData.userCommand().speedTestsCount(); i++) {
            app.start();
            speeds += app.avgSpeed();
        }

        logger.info(String.format("Parallel tests ended, speed is: %.5f frames per second",
            speeds / appConfigurationData.userCommand().speedTestsCount()));

        speeds = 0;

        logger.info("Non-Parallel started");

        app = new App(appConfigurationData);
        appConfigurationData.userCommand().isParallelOn(false);

        for (int i = 0; i < appConfigurationData.userCommand().speedTestsCount(); i++) {
            app.start();
            speeds += app.avgSpeed();
        }

        logger.info(String.format("Non-Parallel tests ended, speed is: %.5f frames per second",
            speeds / appConfigurationData.userCommand().speedTestsCount()));
    }
}
