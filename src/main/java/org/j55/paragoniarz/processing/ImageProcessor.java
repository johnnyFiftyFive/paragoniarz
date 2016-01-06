package org.j55.paragoniarz.processing;

import Catalano.Imaging.Concurrent.Filters.Grayscale;
import Catalano.Imaging.Concurrent.Filters.RosinThreshold;
import Catalano.Imaging.Concurrent.Filters.Sharpen;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BlobsFiltering;
import Catalano.Imaging.Filters.ContrastCorrection;
import Catalano.Imaging.IBaseInPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author johnnyFiftyFive
 */
@Component
public class ImageProcessor {

    private final Logger logger = LoggerFactory.getLogger(ImageProcessor.class);

    public FastBitmap process(InputStream imageStrm) {
        try {
            long start = System.currentTimeMillis();
            FastBitmap bitmap = new FastBitmap(ImageIO.read(imageStrm));

            List<IBaseInPlace> filters = Arrays.asList(
                    new Grayscale(Grayscale.Algorithm.GeometricMean),
                    new ContrastCorrection(90),
                    new RosinThreshold(),
                    new Sharpen(),
                    new BlobsFiltering(1, 4)
            );
            filters.stream().forEachOrdered(f -> f.applyInPlace(bitmap));
            logger.info(String.format("Image preprocessing time  %d ms", System.currentTimeMillis() - start));

            return bitmap;
        } catch (IOException e) {
            logger.error("Can't read stream", e);
        }

        return null;
    }
}
