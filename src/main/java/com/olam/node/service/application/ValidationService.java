package com.olam.node.service.application;

import com.olam.node.exceptions.UBLMessageNotValidException;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

@Service
public class ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);
    public final String CBC_NAMESPACE_PREFIX = "cbc";
    public final String CBC_NAMESPACE_URI = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
    public final String DOCUMENT_STATUS_CODE_ELEM = "DocumentStatusCode";
    public final String TRANSPORT_EXECUTION_PLAN = "TransportExecutionPlan";
    public final Namespace CBC_NAMESPACE = Namespace.getNamespace(CBC_NAMESPACE_PREFIX, CBC_NAMESPACE_URI);
    public final String SCHEMA_DIR = "xsd/maindoc";


    @Autowired
    public EthereumNodeService nodeService;

    public boolean validateBusinessMessage(String message, String shipmentId) throws FileNotFoundException, UBLMessageNotValidException {

        try {
            //convert user input (UBL XML) string to XML document (after converting to inputStream)
            InputStream messageInputStream = new ByteArrayInputStream(message.getBytes());
            String messageType = this.getUBLMessageType(messageInputStream);
            File schemaFile = this.getSchemaFile(messageType);
            Document doc = this.validateAndGetXMLDocument(message, schemaFile);
            //TODO: finish function logic
//            validateState(doc, messageType, shipmentId);
            return true;

        } catch (SAXException | IOException | JDOMException e) {
            LOG.error("instance document is invalid!");
            throw new UBLMessageNotValidException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Document validateAndGetXMLDocument(String message, File schemaFile) throws SAXException, JDOMException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schemaObj = schemaFactory.newSchema(schemaFile);
        // create an XMLReaderJDOMFactory by passing the schemaObj
        XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schemaObj);
        // create a SAXBuilder using the XMLReaderJDOMFactory
        SAXBuilder sb = new SAXBuilder(factory);
        // validating and building the XML document
        LOG.debug("validating xml with schema: "+ schemaFile.getName());
        return sb.build(new ByteArrayInputStream(message.getBytes()));
    }

    private void validateState(Document doc, String messageType, String shipmentId) throws Exception {
        //if its a booking message we check state in shipment contract
        if (messageType.equals(TRANSPORT_EXECUTION_PLAN)) {
            //extracting document status from XML
            Element root = doc.getRootElement();
            //TODO: check that document status is in OLAM code list
            String documentStatus = root.getChild(DOCUMENT_STATUS_CODE_ELEM, CBC_NAMESPACE).getTextNormalize();
            LOG.debug(String.format("UBL message: %s is in state of: %s", root.getName(), documentStatus));
            String shipmentStatus = nodeService.getShipmentStatus(shipmentId);
            //TODO: check if document status is valid against the shipment status
            //TODO: if not please throw detailed exception
        }
    }

    private File getSchemaFile(String messageType) throws FileNotFoundException {
        if (messageType != null) {
            File schemaDir = ResourceUtils.getFile("classpath:" + SCHEMA_DIR);

            File[] files = schemaDir.listFiles(((dir, name) -> name.contains(messageType.concat("-"))));
            if (files.length > 0) {
                return files[0];
            }
        }
        return null;
    }

    //TODO: possible to create xml doc (JDOM2) without SchemaFactory
    private String getUBLMessageType(InputStream message) {
        org.w3c.dom.Document document = null;
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = parser.parse(message);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return document.getDocumentElement().getTagName();
    }
}
