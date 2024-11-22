package backend.academy.application.services.exports;

import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.input.ExportFormats;
import backend.academy.application.services.converter.FractalImageToColorArrayConverter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ImageExportTest {
    private Color[][] expected;
    private String filePath;

    @BeforeEach
    void setUp() throws URISyntaxException {
        filePath = Paths.get(ClassLoader.getSystemResource("").toURI()).toString();
        expected = new Color[][]
            {
                {new Color(0, 255, 0), new Color(13, 0, 0), new Color(90, 0, 255)},
                {new Color(255, 255, 0), new Color(255, 0, 255), new Color(255, 0, 255)},
                {new Color(255, 0, 255), new Color(90, 255, 255), new Color(255, 255, 90)}
            };
    }

    @ParameterizedTest
    @MethodSource("getFormatsToCheck")
    void export_happy_path(ExportFormats format) throws IOException {
        //Arrange
        //Act
        ImageExport imageExport = new ImageExport();
        imageExport.export(expected, format, filePath + "." + format.name());

        //Assert
        File file = new File(
            filePath + "." + format.name());
        BufferedImage read = ImageIO.read(file);

        boolean isEquals = true;

        for (int i = 0; i < read.getHeight() && isEquals; i++) {
            for (int j = 0; j < read.getWidth() && isEquals; j++) {
                if (read.getRGB(i, j) != expected[i][j].getRGB()) {
                    isEquals = false;
                }
            }
        }

        Assertions.assertTrue(isEquals);
    }

    static List<ExportFormats> getFormatsToCheck() {
        return List.of(ExportFormats.values());
    }

    @ParameterizedTest
    @MethodSource("getFormatsToCheck")
    void export_from_fractal_image(ExportFormats format) throws IOException {
        //Arrange
        Pixel[][] pixels = new Pixel[][] {
            {new Pixel(expected[0][0], 0), new Pixel(expected[0][1], 0), new Pixel(expected[0][2], 0)},
            {new Pixel(expected[1][0], 0), new Pixel(expected[1][1], 0), new Pixel(expected[1][2], 0)},
            {new Pixel(expected[2][0], 0), new Pixel(expected[2][1], 0), new Pixel(expected[2][2], 0)}

        };

        //Act
        ImageExport imageExport = new ImageExport();
        imageExport.export(FractalImageToColorArrayConverter.convert(pixels), ExportFormats.png,
            filePath + "." + format.name());

        //Assert
        File file = new File(filePath + "." + format.name());
        BufferedImage read = ImageIO.read(file);

        boolean isEquals = true;

        for (int i = 0; i < read.getHeight(); i++) {
            for (int j = 0; j < read.getWidth(); j++) {
                if (read.getRGB(i, j) != expected[j][i].getRGB()) {
                    isEquals = false;
                    break;
                }
            }
        }

        Assertions.assertTrue(isEquals);
    }
}
