package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;

public class HorseshoeVariation implements ITransformation {
    @Override
    public Point apply(Point point) {
        double r = 1 / Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(r * (point.x() - point.y()) * (point.x() + point.y()), r * point.x() * point.y());
    }
}
