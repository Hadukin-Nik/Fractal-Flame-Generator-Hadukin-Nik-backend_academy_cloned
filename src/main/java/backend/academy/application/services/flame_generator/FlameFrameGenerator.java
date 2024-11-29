package backend.academy.application.services.flame_generator;

import backend.academy.application.models.flame_picture.AffineFunctionParameters;
import backend.academy.application.models.flame_picture.PixelPoint;
import backend.academy.application.models.flame_picture.Point;
import backend.academy.application.models.flame_picture.ScreenParameters;
import backend.academy.application.models.input.SymmetryEnum;
import backend.academy.application.services.flame_generator.variations.ITransformation;
import backend.academy.application.services.util.SynchronizedPixelMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlameFrameGenerator {
    private final SynchronizedPixelMap pixels;

    private final CopyOnWriteArrayList<ITransformation> transformations;
    private final List<AffineFunctionParameters> affineFunctionParameters;

    private final SymmetryEnum symmetryEnum;

    private final long it;
    private final int skipDots = 20;

    private final ScreenParameters screenParameters;

    @SuppressWarnings("MagicNumber")
    @SuppressFBWarnings(value = {"CLI_CONSTANT_LIST_INDEX"})
    public void generator() {
        ThreadLocalRandom threadSafeRandom = ThreadLocalRandom.current();

        double xMin = screenParameters.xmin();
        double xMax = screenParameters.xmax();
        double yMin = screenParameters.ymin();
        double yMax = screenParameters.ymax();

        double xRes = screenParameters.xRes();
        double yRes = screenParameters.yRes();

        Point startPoint = new Point(
            threadSafeRandom.nextDouble(-1, 1),
            threadSafeRandom.nextDouble(-1, 1)
        );

        for (int step = skipDots * (-1); step < it; step++) {
            double randomC = threadSafeRandom.nextDouble(0, 1);

            int chosenFunc = 0;

            double sum = 0;
            for (int i = 0; i < affineFunctionParameters.size(); i++) {
                sum += affineFunctionParameters.get(i).weight();
                if (randomC <= sum) {
                    chosenFunc = i;
                    break;
                }
            }

            Point p = new Point(
                affineFunctionParameters.get(chosenFunc).rotation()[0] * startPoint.x()
                    + affineFunctionParameters.get(chosenFunc).rotation()[1] * startPoint.y()
                    + affineFunctionParameters.get(chosenFunc).translation()[0],
                affineFunctionParameters.get(chosenFunc).rotation()[2] * startPoint.x()
                    + affineFunctionParameters.get(chosenFunc).rotation()[3] * startPoint.y()
                    + affineFunctionParameters.get(chosenFunc).translation()[1]
            );

            Point newP;
            newP = transformations.getFirst().apply(p);

            newP = new Point(
                newP.x() * affineFunctionParameters.get(chosenFunc).v()[0],
                newP.y() * affineFunctionParameters.get(chosenFunc).v()[0]
            );

            for (int j = 1; j < affineFunctionParameters.get(chosenFunc).v().length; j++) {
                Point addP;
                addP = transformations.get(j).apply(p);

                newP = new Point(
                    newP.x() + addP.x() * affineFunctionParameters.get(chosenFunc).v()[j],
                    newP.y() + addP.y() * affineFunctionParameters.get(chosenFunc).v()[j]
                );
            }

            if (step >= 0 && xMin < newP.x() && newP.x() < xMax && yMin < newP.y() && newP.y() < yMax) {

                int x1 = (int) (xRes - Math.round(((xMax - newP.x()) / (xMax - xMin)) * xRes));
                int y1 = (int) (yRes - Math.round(((yMax - newP.y()) / (yMax - yMin)) * yRes));

                if (x1 < xRes && y1 < yRes) {
                    PixelPoint fp = new PixelPoint(x1, y1);
                    pixels.put(fp, affineFunctionParameters.get(chosenFunc).color());

                    if (symmetryEnum == SymmetryEnum.xAxis) {
                        newP = new Point(-newP.x(), newP.y());
                        addNewPoint(screenParameters, step, chosenFunc, newP);
                    } else if (symmetryEnum == SymmetryEnum.yAxis) {
                        newP = new Point(newP.x(), -newP.y());
                        addNewPoint(screenParameters, step, chosenFunc, newP);
                    }
                }
            }
            startPoint = newP;
        }
    }

    private void addNewPoint(
        ScreenParameters screenParameters,
        int step,
        int i,
        Point newP
    ) {
        double xMin = screenParameters.xmin();
        double xMax = screenParameters.xmax();
        double yMin = screenParameters.ymin();
        double yMax = screenParameters.ymax();

        double xRes = screenParameters.xRes();
        double yRes = screenParameters.yRes();

        int x1;
        int y1;
        if (step >= 0 && xMin < newP.x() && newP.x() < xMax && yMin < newP.y() && newP.y() < yMax) {

            x1 = (int) (xRes - Math.round(((xMax - newP.x()) / (xMax - xMin)) * xRes));
            y1 = (int) (yRes - Math.round(((yMax - newP.y()) / (yMax - yMin)) * yRes));

            if (x1 < xRes && y1 < yRes) {
                PixelPoint sp = new PixelPoint(x1, y1);

                pixels.put(sp, affineFunctionParameters.get(i).color());
            }
        }
    }
}
