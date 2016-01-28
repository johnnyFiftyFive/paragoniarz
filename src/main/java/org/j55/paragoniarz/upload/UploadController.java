package org.j55.paragoniarz.upload;

import org.j55.paragoniarz.db.ReceiptRepository;
import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.ReceiptHandler;
import org.j55.paragoniarz.processing.parser.CashIdParser;
import org.j55.paragoniarz.processing.parser.DateAndReceiptNumberParser;
import org.j55.paragoniarz.processing.parser.TaxNumberParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author johnnyFiftyFive
 */
@Controller
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private ReceiptHandler receiptReader;

    @Autowired
    private ReceiptRepository receiptRepo;

    @RequestMapping("/")
    public ModelAndView main() {
        return new ModelAndView("upload");
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file[]") MultipartFile[] file) throws IOException {
        for (MultipartFile multipartFile : file) {
            receiptReader.read(ImageIO.read(multipartFile.getInputStream()));
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
    public String uploadByForm(HttpServletRequest request){
        Receipt receipt = createReceipt(request.getParameterMap());
        logger.info("Received receipt: " + receipt.toString());
        receiptRepo.save(receipt);

        return "redirect:/";
    }

    private Receipt createReceipt(Map<String, String[]> data) {
        Receipt r = new Receipt();
        new TaxNumberParser().parse(data.get("taxNumber")[0], r);
        new DateAndReceiptNumberParser().parse(data.get("transactionDate")[0] + " " + data.get("receiptNumber")[0], r);
        new CashIdParser().parse(data.get("cashId")[0], r);
        r.setTotal(data.get("total")[0]);
        r.setStatus(10);

        return r;
    }
}
