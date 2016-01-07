package org.j55.paragoniarz.upload.web;

import org.j55.paragoniarz.processing.ReceiptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author johnnyFiftyFive
 */
@Controller
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private ReceiptHandler receiptReader;

    @RequestMapping(value = "/hello", produces = "text/plain")
    public @ResponseBody String hello() {
        logger.info("logujemy laleczko  ");
        return "dsadsa";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(@RequestParam("file") MultipartFile file) throws IOException {
        receiptReader.read(ImageIO.read(file.getInputStream()));
        return "ok";
    }
}
