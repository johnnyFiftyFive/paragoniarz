package org.j55.paragoniarz.processing;

import Catalano.Imaging.Concurrent.Filters.*;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BlobsFiltering;
import Catalano.Imaging.Filters.ContrastCorrection;
import Catalano.Imaging.IBaseInPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * @author johnnyFiftyFive
 */
@Component
public class ImagePreprocessor {

    private static final  Logger logger = LoggerFactory.getLogger(ImagePreprocessor.class);

    public BufferedImage process(BufferedImage imageStrm) {
        long start = System.currentTimeMillis();
        FastBitmap bitmap = new FastBitmap(imageStrm);

        List<IBaseInPlace> filters = Arrays.asList(
                new Grayscale(Grayscale.Algorithm.GeometricMean),
                new ContrastCorrection(90),
                //new RosinThreshold(),
                new SauvolaThreshold(5),
                new Sharpen(),
                new BlobsFiltering(1, 4)
        );
        filters.stream().forEachOrdered(f -> f.applyInPlace(bitmap));
        logger.info(String.format("Image preprocessing time  %d ms", System.currentTimeMillis() - start));

        return bitmap.toBufferedImage();
    }
}
