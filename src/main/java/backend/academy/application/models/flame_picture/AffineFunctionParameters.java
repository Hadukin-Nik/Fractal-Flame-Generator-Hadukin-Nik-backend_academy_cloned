package backend.academy.application.models.flame_picture;

import java.awt.Color;

public record AffineFunctionParameters(
    Color color,
    double[] v,
    double weight,
    double[] rotation,
    double[] translation) {

}
