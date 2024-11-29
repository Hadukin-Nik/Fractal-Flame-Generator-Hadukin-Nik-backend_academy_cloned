package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;

public class LinearVariation implements ITransformation {
    @Override
    public Point apply(Point point) {
        return point;
    }
}
