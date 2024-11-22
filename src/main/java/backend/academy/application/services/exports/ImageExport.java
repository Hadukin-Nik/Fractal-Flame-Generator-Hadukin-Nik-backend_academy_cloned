package backend.academy.application.services.exports;

import backend.academy.application.models.input.ExportFormats;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import static java.awt.Color.BLACK;

@SuppressFBWarnings("PATH_TRAVERSAL_IN")
public class ImageExport {
    @SuppressWarnings(value = {"CLI_CONSTANT_LIST_INDEX"})
    public void export(Color[][] image, ExportFormats format, String file) throws IOException {
        BufferedImage bi = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == null) {
                    bi.setRGB(i, j, BLACK.getRGB());
                } else {
                    bi.setRGB(i, j, image[i][j].getRGB());
                }
            }
        }
        Path path = FileSystems.getDefault().getPath(file);
        File outputFile = new File(path.toString());

        switch (format) {
            case bmp -> ImageIO.write(bi, "bmp", outputFile);
            case png -> ImageIO.write(bi, "png", outputFile);
            default -> throw new IllegalArgumentException("Unsupported format");
        }
    }
}
