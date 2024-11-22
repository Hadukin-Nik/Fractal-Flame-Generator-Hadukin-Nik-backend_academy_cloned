package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;

public class HeartVariation implements ITransformation {
    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double c = Math.atan(point.x() / point.y()) * r;
        return new Point(Math.sin(c) * r, -Math.cos(c) * r);
    }
}
