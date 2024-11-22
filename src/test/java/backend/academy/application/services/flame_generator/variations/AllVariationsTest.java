package backend.academy.application.services.flame_generator.variations;

import backend.academy.application.models.flame_picture.Point;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AllVariationsTest {
    private static class Pair {
        ITransformation transformation;
        Point expected;

        Pair(ITransformation transformation, Point expected) {
            this.transformation = transformation;
            this.expected = expected;
        }

        @Override
        public String toString() {
            return transformation.getClass().getSimpleName() + ", expected=" + expected;
        }
    }

    @ParameterizedTest
    @MethodSource("getAllTransforms")
    public void happy_path(Pair pair) {
        //Arrange
        Point input = new Point(0.2, 0.1);
        Point expected = pair.expected;

        //Act
        Point ans = pair.transformation.apply(input);

        //Equals
        Assertions.assertEquals(expected, ans);
    }

    static List<Pair> getAllTransforms() {
        return List.of(
            new Pair(new DiscVariation(), new Point(0.22770103059290117, 0.2689787114536887)),
            new Pair(new HeartVariation(), new Point(0.05479370001418294, -0.21678941496013068)),
            new Pair(new HorseshoeVariation(), new Point(0.1341640786499874, 0.08944271909999157)),
            new Pair(new LinearVariation(), new Point(0.2, 0.1)),
            new Pair(new PDJVariation(List.of(1.1, 2.3, 2.4, 1.0)),
                new Point(-0.7862741966883504, -0.5332249897365429)),
            new Pair(new PerspectiveVariation(List.of(1.1, 2.3)), new Point(0.2080620174477688, 0.047188062065144365)),
            new Pair(new SinusoidalVariation(), new Point(0.19866933079506122, 0.09983341664682815)),
            new Pair(new SphericalVariation(), new Point(3.9999999999999996, 1.9999999999999998)),
            new Pair(new SwirlVariation(), new Point(-0.08987919218536097, 0.2047479690060611))
        );
    }
}
