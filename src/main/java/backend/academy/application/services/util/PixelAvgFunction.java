package backend.academy.application.services.util;

import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.flame_picture.PixelPoint;
import java.awt.Color;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PixelAvgFunction {
    private final int red;
    private final int green;
    private final int blue;

    public Pixel applyAvg(PixelPoint pixelPoint, Pixel pixel) {
        Color colorB = new Color(
            (pixel.color().getRed() + red) / 2,
            (pixel.color().getBlue() + blue) / 2,
            (pixel.color().getGreen() + green) / 2
        );

        return new Pixel(colorB, pixel.hitCount() + 1);
    }
}
