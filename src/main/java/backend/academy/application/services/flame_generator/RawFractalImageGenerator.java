package backend.academy.application.services.flame_generator;

import backend.academy.application.models.flame_picture.AffineFunctionParameters;
import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.flame_picture.PixelPoint;
import backend.academy.application.models.flame_picture.ScreenParameters;
import backend.academy.application.models.input.SymmetryEnum;
import backend.academy.application.services.flame_generator.variations.ITransformation;
import backend.academy.application.services.util.SynchronizedPixelMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RawFractalImageGenerator {
    private final SynchronizedPixelMap pixelMap;
    private final List<AffineFunctionParameters> coeff;
    private final CopyOnWriteArrayList<ITransformation> transformations;
    private final SymmetryEnum symmetryEnum;

    private final ScreenParameters screenParameters;

    private final long iterations;

    private final int maxBucketSize = 1_000_000;

    public Pixel[][] generate() {
        FlameFrameGenerator flameFrameGenerator =
            new FlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, maxBucketSize,
                screenParameters);

        LongStream.range(0, iterations / maxBucketSize).forEach(s -> flameFrameGenerator.generator());

        FlameFrameGenerator flameFrameGeneratorLastPart =
            new FlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, iterations % maxBucketSize,
                screenParameters);

        flameFrameGeneratorLastPart.generator();

        return getPixels(screenParameters);
    }

    public Pixel[][] parallelGenerate() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        try (ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(availableProcessors)) {
            long buckets = iterations / maxBucketSize;

            FlameFrameGenerator flameFrameGenerator =
                new FlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, maxBucketSize,
                    screenParameters);

            while (buckets > 0) {
                executor.execute(flameFrameGenerator::generator);
                buckets--;
            }

            long lastBucket = iterations % maxBucketSize;
            if (lastBucket != 0) {
                FlameFrameGenerator flameFrameGenerator1 =
                    new FlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, lastBucket,
                        screenParameters);

                executor.execute(flameFrameGenerator1::generator);
            }

            executor.shutdown();

            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return getPixels(screenParameters);
    }

    private Pixel[][] getPixels(ScreenParameters screenParameters) {
        Map<PixelPoint, Pixel> map = pixelMap.getMap();

        Pixel[][] ans = new Pixel[screenParameters.yRes()][screenParameters.xRes()];

        for (var j : map.entrySet()) {
            ans[j.getKey().y()][j.getKey().x()] = j.getValue();
        }

        return ans;
    }
}
