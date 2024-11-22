package backend.academy.application.services.flame_generator;

import backend.academy.application.models.flame_picture.AffineFunctionParameters;
import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.flame_picture.ScreenParameters;
import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.flame_generator.renderer.Renderer;
import backend.academy.application.services.input.InputFromParameters;
import backend.academy.application.services.util.SynchronizedPixelMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class FractalFlame {
    private final UserCommand userCommand;
    private final InputFromParameters inputFromParameters;

    private final ScreenParameters screenParameters;

    @Setter
    private List<AffineFunctionParameters> affineFunctionParameters;

    @Getter
    private double avgTime = 0;

    public Pixel[][] start() {
        long from = System.currentTimeMillis();
        Pixel[][] pixels = generateImage();

        long to = System.currentTimeMillis();
        avgTime = userCommand.iterationsCount() * 1.0 / (to - from);

        render(pixels);

        return pixels;
    }

    private Pixel[][] generateImage() {
        Pixel[][] pixels;
        SynchronizedPixelMap synchronizedPixelMap = new SynchronizedPixelMap();
        SimpleFractalFlameGenerator simpleFractalFlameGenerator =
            new SimpleFractalFlameGenerator(synchronizedPixelMap, affineFunctionParameters,
                new CopyOnWriteArrayList<>(inputFromParameters.transformations()), userCommand.symmetryEnum(),
                screenParameters,
                userCommand.iterationsCount());

        if (userCommand.isParallelOn()) {
            pixels = simpleFractalFlameGenerator.parallelGenerate();
        } else {
            pixels = simpleFractalFlameGenerator.generate();
        }

        synchronizedPixelMap.drop();

        return pixels;
    }

    private void render(Pixel[][] pixels) {
        if (userCommand.isRenderOn()) {
            if (userCommand.isParallelOn()) {
                Renderer.parallelCorrection(pixels, userCommand.width(), userCommand.height());
            } else {
                Renderer.correction(pixels, userCommand.width(), userCommand.height());
            }
        }

    }
}
