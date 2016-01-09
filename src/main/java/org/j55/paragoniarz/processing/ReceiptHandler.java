package org.j55.paragoniarz.processing;

import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.j55.paragoniarz.db.RawReceiptRepository;
import org.j55.paragoniarz.db.ReceiptRepository;
import org.j55.paragoniarz.processing.parser.ParserSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * @author johnnyFiftyFive
 */
@Service
public class ReceiptHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptHandler.class);

    @Autowired
    private ImagePreprocessor imageProcessor;

    @Autowired
    private ParserSuite parserSuite;

    @Autowired
    private RawReceiptRepository rawReceiptRepo;

    @Autowired
    private ReceiptRepository receiptRepo;

    private Tesseract1 ocr;

    public ReceiptHandler() {
        ocr = new Tesseract1();
        ocr.setLanguage("pol");
    }

    @Async
    public void read(BufferedImage inputImg) {
        BufferedImage image = imageProcessor.process(inputImg);
        try {
            RawReceipt raw = new RawReceipt(ocr.doOCR(image));
            rawReceiptRepo.save(raw);
            Receipt receipt = parserSuite.parse(raw.getText());
            receipt.setSourceId(raw.getId());
            receiptRepo.save(receipt);
            logger.info(receipt.toString());
        } catch (TesseractException e) {
            logger.error("Error during character recognition!", e);
        }

    }
}
