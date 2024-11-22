package backend.academy.application.services.configuration;

import backend.academy.application.models.flame_picture.AppConfigurationData;
import backend.academy.application.models.flame_picture.ScreenParameters;
import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.exports.ImageExport;
import backend.academy.application.services.flame_generator.FractalFlame;
import backend.academy.application.services.input.InputFromParameters;

public class AppConfigurator {
    private static final double X_MIN = -1.77;
    private static final double X_MAX = 1.77;
    private static final double Y_MIN = -1;
    private static final double Y_MAX = 1;

    public AppConfigurationData init(String[] args) {
        InputFromParameters inputFromParameters = new InputFromParameters();
        inputFromParameters.init(args);

        UserCommand userCommand = inputFromParameters.command();

        FractalFlame fractalFlame = new FractalFlame(
            userCommand,
            inputFromParameters,
            new ScreenParameters(X_MIN, X_MAX, Y_MIN, Y_MAX, userCommand.width(), userCommand.height())
        );

        return new AppConfigurationData(userCommand, fractalFlame, new ImageExport());
    }
}
