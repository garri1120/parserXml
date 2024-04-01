package an.popov.parserxml;


import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;


@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
  private final XsdValidator xsdValidator;
  @PostMapping("/file")
  public boolean get(@RequestParam("xmlFiles") List<MultipartFile> xmlFiles, @RequestParam("xsd") MultipartFile xsdFile)
      throws ParserConfigurationException, IOException, SAXException {
    log.info("Received a request to validate xml files");
    boolean result = xsdValidator.isValid(xmlFiles,xsdFile);
    log.info("Xml files were successfully validated");
    return  result;
  }
}
