package backend.academy.application.services.util;

import backend.academy.application.models.input.VariationsEnum;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("PATH_TRAVERSAL_IN")
public class NameGenerator {

    public static String generateName(List<VariationsEnum> variations, String path, String format, int id) {
        StringBuilder name = new StringBuilder(path).append('/');
        for (var i : variations) {
            name.append(i).append('_');
        }

        return name.append(id).append('.').append(format).toString();
    }
}
