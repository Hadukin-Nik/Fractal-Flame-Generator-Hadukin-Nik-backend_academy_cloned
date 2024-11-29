package backend.academy.application.services.flame_generator.renderer;

import backend.academy.application.models.flame_picture.Pixel;
import java.awt.Color;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;
import static java.awt.Color.WHITE;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

@UtilityClass
@SuppressWarnings("MagicNumber")
public class Renderer {
    public static void correction(Pixel[][] pixels, int xRes, int yRes) {
        double max = 0.0;
        double gamma = 2.2;

        double[][] normals = new double[pixels.length][pixels[0].length];

        for (int row = 0; row < yRes; row++) {
            for (int col = 0; col < xRes; col++) {
                if (pixels[row][col] != null) {
                    normals[row][col] = log10(pixels[row][col].hitCount());
                    if (normals[row][col] > max) {
                        max = normals[row][col];
                    }
                }
            }
        }

        for (int row = 0; row < yRes; row++) {
            for (int col = 0; col < xRes; col++) {
                transformatePixel(pixels, max, gamma, normals, row, col);
            }
        }

    }

    public static void parallelCorrection(Pixel[][] pixels, int xRes, int yRes) {
        double gamma = 2.2;

        double[][] normals = new double[pixels.length][pixels[0].length];

        final double max = IntStream.range(0, yRes)
            .parallel().mapToDouble(
                row -> IntStream.range(0, xRes).parallel().mapToDouble(col -> {
                    if (pixels[row][col] != null) {
                        normals[row][col] = log10(pixels[row][col].hitCount());
                        return normals[row][col];
                    } else {
                        return 0;
                    }
                }).max().orElse(0)
            ).max().orElse(0);

        IntStream.range(0, yRes)
            .parallel().forEach(row ->
                IntStream.range(0, xRes).parallel().forEach(col -> {
                    transformatePixel(pixels, max, gamma, normals, row, col);
                }));
    }

    private static void transformatePixel(
        Pixel[][] pixels,
        double max,
        double gamma,
        double[][] normals,
        int row,
        int col
    ) {
        int colorsCount = WHITE.getBlue();
        if (pixels[row][col] != null) {
            normals[row][col] /= max;

            pixels[row][col] = new Pixel(
                new Color(
                    (int) (pixels[row][col].color().getRed() * pow(normals[row][col], (1.0 / gamma)))
                        % colorsCount,
                    (int) (pixels[row][col].color().getGreen() * pow(normals[row][col], (1.0 / gamma)))
                        % colorsCount,
                    (int) (pixels[row][col].color().getBlue() * pow(normals[row][col], (1.0 / gamma)))
                        % colorsCount
                ),
                pixels[row][col].hitCount()
            );
        }
    }
}
