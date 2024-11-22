package backend.academy.application.models.input;

import lombok.Getter;

@Getter
public enum VariationsEnum {
    linear(0),
    sinusoidal(0),
    spherical(0),
    heart(0),
    disc(0),
    swirl(0),
    horseshoe(0),
    pdj(4),
    perspective(2);

    private final int parametersCount;

    VariationsEnum(int parametersCount) {
        this.parametersCount = parametersCount;
    }
}
