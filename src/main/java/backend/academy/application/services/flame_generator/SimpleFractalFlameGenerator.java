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
public class SimpleFractalFlameGenerator {
    private final SynchronizedPixelMap pixelMap;
    private final List<AffineFunctionParameters> coeff;
    private final CopyOnWriteArrayList<ITransformation> transformations;
    private final SymmetryEnum symmetryEnum;

    private final ScreenParameters screenParameters;

    private final long iterations;

    private final int maxBucketSize = 1_000_000;

    public Pixel[][] generate() {
        SimpleFlameFrameGenerator simpleFlameFrameGenerator =
            new SimpleFlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, maxBucketSize,
                screenParameters);

        LongStream.range(0, iterations / maxBucketSize).forEach(s -> simpleFlameFrameGenerator.generator());

        SimpleFlameFrameGenerator simpleFlameFrameGeneratorLastPart =
            new SimpleFlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, iterations % maxBucketSize,
                screenParameters);

        simpleFlameFrameGeneratorLastPart.generator();

        return getPixels(screenParameters);
    }

    public Pixel[][] parallelGenerate() {
        int availableProcessors = Runtime.getRuntime().availableProcessors() * 2;

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(availableProcessors);

        long buckets = iterations / maxBucketSize;

        SimpleFlameFrameGenerator simpleFlameFrameGenerator =
            new SimpleFlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, maxBucketSize,
                screenParameters);

        while (buckets > 0) {
            executor.execute(simpleFlameFrameGenerator::generator);
            buckets--;
        }

        long lastBucket = iterations % maxBucketSize;
        if (lastBucket != 0) {
            SimpleFlameFrameGenerator simpleFlameFrameGenerator1 =
                new SimpleFlameFrameGenerator(pixelMap, transformations, coeff, symmetryEnum, lastBucket,
                    screenParameters);

            executor.execute(simpleFlameFrameGenerator1::generator);
        }

        executor.shutdown();

        try {
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
