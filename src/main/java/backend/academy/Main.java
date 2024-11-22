package backend.academy;

import backend.academy.application.models.flame_picture.AppConfigurationData;
import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.App;
import backend.academy.application.services.AppSpeedAnalyzer;
import backend.academy.application.services.IApp;
import backend.academy.application.services.configuration.AppConfigurator;
import com.beust.jcommander.JCommander;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class Main {

    public static void main(String[] args) {
        try {
            AppConfigurator appConfigurator = new AppConfigurator();
            AppConfigurationData initData = appConfigurator.init(args);

            if (initData.userCommand().help()) {
                JCommander jCommander = new JCommander(new UserCommand());
                jCommander.usage();

                log.info("some example of usage of parameters");
                log.info(
                    "--variation pdj -P 1 -P 2 -P 1 -P 1.4 --variation heart "
                        + "--iterations-count 300000000 --frames-count 5 --affines-count 10");
                return;
            }

            IApp app;
            if (initData.userCommand().isSpeedAnalyzeOn()) {
                app = new AppSpeedAnalyzer(log, initData);
            } else {
                app = new App(initData);
            }

            app.start();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }
}
