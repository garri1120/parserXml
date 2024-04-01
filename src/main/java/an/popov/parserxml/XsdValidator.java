package an.popov.parserxml;


import java.io.IOException;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Service
@Slf4j
public class XsdValidator {

  private Schema getSchema(MultipartFile xsdFile)
      throws ParserConfigurationException, IOException, SAXException {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    return schemaFactory.newSchema(
        new DOMSource(getDocumentBuilder().parse(xsdFile.getInputStream())));
  }

  private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    return factory.newDocumentBuilder();
  }

  public boolean isValid(List<MultipartFile> xmlFiles, MultipartFile xsdFile)
      throws ParserConfigurationException, IOException, SAXException {

    Schema schema = getSchema(xsdFile);
    Validator validator = schema.newValidator();

    for (MultipartFile file : xmlFiles) {
      Source source;
      try {
        source = new DOMSource(getDocumentBuilder().parse(file.getInputStream()));
        validator.validate(source);
        log.info("File with name = [{}] is valid", file.getOriginalFilename());
      } catch (SAXException e) {
        throw new RuntimeException(String.format("File with name = %s is not valid. Error = %s",
            file.getOriginalFilename(), e.getMessage()), e);
      }
    }
    return true;
  }
}
