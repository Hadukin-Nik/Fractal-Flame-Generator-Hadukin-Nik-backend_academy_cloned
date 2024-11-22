package backend.academy.application.services.input;

import backend.academy.application.models.input.UserCommand;
import backend.academy.application.services.flame_generator.variations.DiscVariation;
import backend.academy.application.services.flame_generator.variations.HeartVariation;
import backend.academy.application.services.flame_generator.variations.HorseshoeVariation;
import backend.academy.application.services.flame_generator.variations.ITransformation;
import backend.academy.application.services.flame_generator.variations.LinearVariation;
import backend.academy.application.services.flame_generator.variations.PDJVariation;
import backend.academy.application.services.flame_generator.variations.PerspectiveVariation;
import backend.academy.application.services.flame_generator.variations.SinusoidalVariation;
import backend.academy.application.services.flame_generator.variations.SphericalVariation;
import backend.academy.application.services.flame_generator.variations.SwirlVariation;
import com.beust.jcommander.JCommander;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class InputFromParameters {
    private UserCommand command;
    private List<ITransformation> transformations;

    public void init(String[] args) {
        this.command = new UserCommand();

        JCommander.newBuilder()
            .addObject(command)
            .build()
            .parse(args);

        transformations = new ArrayList<>(command.variation().size());
        initTransformations();
    }

    @SuppressWarnings("MultipleStringLiterals")
    private void initTransformations() {
        int j = 0;
        for (var i : command.variation()) {
            if (i.parametersCount() > 0 && j + i.parametersCount() > command.parameters().size()) {
                throw new IllegalArgumentException("Not enough parameters: " + i.name());
            }

            List<Double> params = new ArrayList<>(i.parametersCount());
            if (i.parametersCount() > 0) {
                for (int p = 0; p < j + i.parametersCount(); p++) {
                    params.add(command.parameters().get(p));
                }
            }

            switch (i) {
                case pdj -> transformations.add(new PDJVariation(params));
                case perspective -> transformations.add(new PerspectiveVariation(params));
                case linear -> transformations.add(new LinearVariation());
                case sinusoidal -> transformations.add(new SinusoidalVariation());
                case spherical -> transformations.add(new SphericalVariation());
                case heart -> transformations.add(new HeartVariation());
                case disc -> transformations.add(new DiscVariation());
                case swirl -> transformations.add(new SwirlVariation());
                case horseshoe -> transformations.add(new HorseshoeVariation());
                default -> throw new IllegalArgumentException("Unexpected variation" + i.name());
            }
        }
    }
}
