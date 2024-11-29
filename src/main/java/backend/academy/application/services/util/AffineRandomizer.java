package backend.academy.application.services.util;

import backend.academy.application.models.flame_picture.AffineFunctionParameters;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import lombok.experimental.UtilityClass;
import static java.awt.Color.WHITE;

@UtilityClass
public class AffineRandomizer {
    private static final SplittableRandom RANDOM = new SplittableRandom();

    @SuppressWarnings("MagicNumber")
    public static List<AffineFunctionParameters> generate(int affinesLength, int variationsCount) {
        List<AffineFunctionParameters> affines = new ArrayList<>(affinesLength);
        double[] weights = new double[affinesLength];

        double sum = 0;
        for (int i = 0; i < affinesLength; i++) {
            weights[i] = RANDOM.nextDouble(0, 1);
            sum += weights[i];
        }

        for (int i = 0; i < affinesLength; i++) {
            weights[i] /= sum;
        }

        int maxColor = WHITE.getBlue();

        double a;
        double b;
        double c;
        double d;
        double e;
        double f;

        int i = 0;
        while (i < affinesLength) {
            Color color = new Color(RANDOM.nextInt(maxColor), RANDOM.nextInt(maxColor), RANDOM.nextInt(maxColor));
            double[] v = generateBlendingVector(variationsCount);

            while (true) {
                a = RANDOM.nextDouble(-1, 1);
                b = RANDOM.nextDouble(-1, 1);
                c = RANDOM.nextDouble(-2, 2);

                d = randr(a * a, 1);
                if (RANDOM.nextBoolean()) {
                    d *= -1;
                }

                e = randr(b * b, 1);
                if (RANDOM.nextBoolean()) {
                    e *= -1;
                }

                f = RANDOM.nextDouble(-2, 2);

                if (a * a + d * d < 1 && b * b + e * e < 1
                    && a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d)) {
                    break;
                }
            }

            affines.add(
                new AffineFunctionParameters(color, v, weights[i], new double[] {a, b, d, e}, new double[] {c, f}));

            i++;
        }

        return affines;
    }

    private static double randr(double lo, double hi) {
        return ((lo) + (((hi) - (lo)) * RANDOM.nextDouble(0, 1)));
    }

    private static double[] generateBlendingVector(int variationsCount) {
        double sum;
        double[] v = new double[variationsCount];
        sum = 0;

        for (int j = 0; j < variationsCount; j++) {
            v[j] = RANDOM.nextDouble(0, 1);
            sum += v[j];
        }

        for (int j = 0; j < variationsCount; j++) {
            v[j] /= sum;
        }

        return v;
    }
}
