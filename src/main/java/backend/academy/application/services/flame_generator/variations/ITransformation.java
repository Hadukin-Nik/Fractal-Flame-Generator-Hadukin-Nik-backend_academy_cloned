package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;
import java.util.function.Function;

public interface ITransformation extends Function<Point, Point> {
}
