package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;

public class SwirlVariation implements ITransformation {
    @Override
    public Point apply(Point point) {
        double r = point.x() * point.x() + point.y() * point.y();

        double cosR = Math.cos(r);
        double sinR = Math.sin(r);

        return new Point(point.x() * sinR - point.y() * cosR, point.x() * cosR + point.y() * sinR);
    }
}
