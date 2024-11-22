package backend.academy.application.models.flame_picture;

import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.exports.ImageExport;
import backend.academy.application.services.flame_generator.FractalFlame;

public record AppConfigurationData(UserCommand userCommand, FractalFlame fractalFlame, ImageExport imageExport) {
}
