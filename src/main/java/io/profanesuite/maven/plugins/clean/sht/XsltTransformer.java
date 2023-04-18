package io.profanesuite.maven.plugins.clean.sht;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class XsltTransformer implements Transformer<String, String> {

    private final String xsltAsString;

    public XsltTransformer(final String xslt) {
        xsltAsString = xslt;
    }

    @Override
    public String apply(String source) {
        try (InputStream xslt = new ByteArrayInputStream(xsltAsString.getBytes(UTF_8));
             InputStream xml = new ByteArrayInputStream(source.getBytes(UTF_8));
             OutputStream transformedXml = new ByteArrayOutputStream()) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(xml);
            Templates templates = TransformerFactory.newInstance().newTemplates(new StreamSource(xslt));

            Result result = new StreamResult(transformedXml);
            templates.newTransformer().transform(new DOMSource(document), result);
            return transformedXml.toString();
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException exception) {
            throw new RuntimeException("Railed to transform XML using Template", exception);
        }
    }
}
