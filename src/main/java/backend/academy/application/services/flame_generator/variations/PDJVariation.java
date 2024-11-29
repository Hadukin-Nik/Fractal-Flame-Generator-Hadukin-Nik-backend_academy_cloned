package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;
import java.util.List;

@SuppressWarnings("MagicNumber")
public class PDJVariation implements ITransformation {
    private final List<Double> parameters;

    public PDJVariation(List<Double> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Point apply(Point point) {
        return new Point(
            Math.sin(parameters.get(0) * point.y()) - Math.cos(parameters.get(1) * point.x()),
            Math.sin(parameters.get(2) * point.x()) - Math.cos(parameters.get(3) * point.y())
        );
    }
}
