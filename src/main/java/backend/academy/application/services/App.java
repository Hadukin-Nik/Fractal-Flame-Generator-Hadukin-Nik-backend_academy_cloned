package backend.academy.application.services;

import backend.academy.application.models.flame_picture.AppConfigurationData;
import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.converter.FractalImageToColorArrayConverter;
import backend.academy.application.services.exports.ImageExport;
import backend.academy.application.services.flame_generator.FinalFractalImageGenerator;
import backend.academy.application.services.util.AffineRandomizer;
import backend.academy.application.services.util.NameGenerator;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("PATH_TRAVERSAL_IN")
public class App implements IApp {
    private final AppConfigurationData appConfigurationData;
    private final String path = "src/main/resources";
    @Getter
    private double avgSpeed;

    public void start() {
        UserCommand userCommand = appConfigurationData.userCommand();
        FinalFractalImageGenerator finalFractalImageGenerator = appConfigurationData.finalFractalImageGenerator();
        ImageExport imageExport = appConfigurationData.imageExport();

        double sum = 0;

        for (int i = 0; i < userCommand.framesCount(); i++) {
            finalFractalImageGenerator.affineFunctionParameters(
                AffineRandomizer.generate(
                    userCommand.affinesCount(),
                    userCommand.variation().size()
                )
            );

            Pixel[][] pixels = finalFractalImageGenerator.start();
            sum += finalFractalImageGenerator.avgTime();

            String saveName = path;
            if (userCommand.prefix() == null) {
                saveName = NameGenerator.generateNameWithoutPrefix(
                    userCommand.variation(),
                    path,
                    userCommand.imageFormat().name(), i
                );
            } else {
                saveName = NameGenerator.generateNameWithPrefix(saveName, userCommand.prefix(),
                    userCommand.imageFormat().name(), i);
            }

            try {
                imageExport.export(
                    FractalImageToColorArrayConverter.convert(pixels),
                    userCommand.imageFormat(),
                    saveName
                );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        avgSpeed = sum / userCommand.framesCount();
    }
}
