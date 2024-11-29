package backend.academy.application.services.util;

import backend.academy.application.models.flame_picture.Pixel;
import backend.academy.application.models.flame_picture.PixelPoint;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SynchronizedPixelMap {
    private final ConcurrentHashMap<PixelPoint, Pixel> map;

    public SynchronizedPixelMap() {
        map = new ConcurrentHashMap<>();
    }

    public void put(PixelPoint point, Color color) {
        PixelAvgFunction pixelAvgFunction = new PixelAvgFunction(color.getRed(), color.getGreen(), color.getBlue());
        map.computeIfPresent(point, pixelAvgFunction::applyAvg);

        map.putIfAbsent(point, new Pixel(color, 1));

    }

    public Map<PixelPoint, Pixel> getMap() {
        return map;
    }

    public void drop() {
        map.clear();
    }
}
