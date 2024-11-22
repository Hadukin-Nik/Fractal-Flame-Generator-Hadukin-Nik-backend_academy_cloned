package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;
import java.util.List;

public class PerspectiveVariation implements ITransformation {
    private final List<Double> parameters;
    private final double preCalculatedSinP1;
    private final double preCalculatedCosP2;

    public PerspectiveVariation(List<Double> parameters) {
        this.parameters = parameters;

        preCalculatedSinP1 = Math.sin(parameters.get(0));
        preCalculatedCosP2 = Math.cos(parameters.get(0));
    }

    @Override
    public Point apply(Point point) {
        double correction = parameters.get(1) / (parameters.get(1) - point.y() * preCalculatedSinP1);
        return new Point(point.x() * correction, point.y() * preCalculatedCosP2 * correction);
    }
}
