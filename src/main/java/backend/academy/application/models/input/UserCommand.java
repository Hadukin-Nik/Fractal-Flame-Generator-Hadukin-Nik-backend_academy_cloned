package backend.academy.application.models.input;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@SuppressWarnings({"MagicNumber", "PATH_TRAVERSAL_IN"})
public class UserCommand {
    @Parameter(
        names = {"--variation", "-V"}
    )
    private List<VariationsEnum> variation = List.of(VariationsEnum.sinusoidal);

    @Parameter(
        names = {"--parameters", "-P"}
    )
    private List<Double> parameters = new ArrayList<>();

    @Parameter(names = "--iterations-count")
    private Long iterationsCount = 50000000L;

    @Parameter(names = "--affines-count")
    private Integer affinesCount = 10;

    @Parameter(names = "--speed-tests-count")
    private Integer speedTestsCount = 10;

    @Parameter(names = "--frames-count")
    private Integer framesCount = 1;

    @Parameter(names = "--width")
    private Integer width = 1920;

    @Parameter(names = "--height")
    private Integer height = 1080;

    @Parameter(names = "--file-prefix", description = "will be used to generate names of pictures")
    private String prefix = null;

    @Parameter(names = "--image-format")
    private ExportFormats imageFormat = ExportFormats.png;

    @Parameter(names = "--symmetric")
    private SymmetryEnum symmetryEnum = SymmetryEnum.none;

    @Parameter(names = "--render-off")
    private boolean isRenderOn = true;

    @Parameter(names = "--parallel-off")
    @Setter
    private boolean isParallelOn = true;

    @Parameter(names = "--speed-analyze-on")
    private boolean isSpeedAnalyzeOn = false;

    @Parameter(names = "--help")
    private boolean help = false;
}
