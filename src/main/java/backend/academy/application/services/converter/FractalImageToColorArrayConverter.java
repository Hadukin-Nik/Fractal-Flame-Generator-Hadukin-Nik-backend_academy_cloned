package backend.academy.application.services.converter;

import backend.academy.application.models.flame_picture.Pixel;
import java.awt.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FractalImageToColorArrayConverter {
    public static Color[][] convert(Pixel[][] data) {
        Color[][] array = new Color[data[0].length][data.length];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != null) {
                    array[j][i] = data[i][j].color();
                }
            }
        }

        return array;
    }
}
