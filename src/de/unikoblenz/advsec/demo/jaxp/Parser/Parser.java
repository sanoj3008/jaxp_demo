package de.unikoblenz.advsec.demo.jaxp.Parser;

import de.unikoblenz.advsec.demo.jaxp.Parser.Components.CarComponent;
import de.unikoblenz.advsec.demo.jaxp.Parser.Components.ActivityComponent;
import de.unikoblenz.advsec.demo.jaxp.Parser.Components.Utils;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class Parser<K> {

    protected final File checkheft;

    protected final Document document;

    protected CarComponent customerCar;
    protected List<ActivityComponent> unknownOperations;
    protected List<K> knownOperations;

    private SimpleDateFormat sdf;

    public Parser(File checkheft, TimeZone timeZone) throws IOException, SAXException, ParserConfigurationException {
        this.checkheft = checkheft;

        this.unknownOperations = new ArrayList<>();
        this.knownOperations = new ArrayList<>();

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(timeZone);

        this.document = extractDocument();
        this.parse();
    }

    abstract public void parseActivity(Element activity);
    abstract public Element activityToXML() throws ParserConfigurationException;
    abstract public void printCheckheft();

    public void storeCheckheft() throws ParserConfigurationException, TransformerException, IOException {
        Element root = this.getDocumentElement();
        Element tmp = (Element) root.getElementsByTagName(Utils.CAR_TAG).item(0);

        root.removeChild(tmp);
        root.appendChild(this.customerCar.toXML(this.document));

        tmp = (Element) root.getElementsByTagName(Utils.ACTIVITIES_TAG).item(0);
        tmp.appendChild(this.activityToXML());

        DOMImplementationLS domImplementationLS =
                (DOMImplementationLS) this.document.getImplementation().getFeature("LS","3.0");
        LSOutput lsOutput = domImplementationLS.createLSOutput();
        FileOutputStream outputStream = new FileOutputStream(this.checkheft);
        lsOutput.setByteStream((OutputStream) outputStream);
        LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
        lsSerializer.write(this.document, lsOutput);
        outputStream.close();
    }

    protected Element getDocumentElement() {
        return this.document.getDocumentElement();
    }


    protected String dateToString(Date date) {
        return sdf.format(date);
    }

    private Document extractDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = this.processing(dbf, false);
        return db.parse(this.checkheft);
    }

    private void parse() {
        Element docElement = this.getDocumentElement();
        this.customerCar = CarComponent.parse((Element) docElement.getElementsByTagName(Utils.CAR_TAG).item(0));


        List<Element> childList = Utils.filterElements(
                docElement.getElementsByTagName(Utils.ACTIVITIES_TAG).item(0)
                        .getChildNodes()
        );

        for(Element elem: childList) {
            this.parseActivity(elem);
        }
    }

    private DocumentBuilder processing(DocumentBuilderFactory dbf) throws ParserConfigurationException {
        return this.secureProcessing(dbf);
    }

    private DocumentBuilder processing(DocumentBuilderFactory dbf, boolean unsecured) throws ParserConfigurationException {
        if(unsecured) {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
            dbf.setAttribute("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", 0);

            return dbf.newDocumentBuilder();
        }

        return this.processing(dbf);
    }

    private DocumentBuilder secureProcessing(DocumentBuilderFactory dbf) throws ParserConfigurationException {
        dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setAttribute("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", 2);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "http");

//        return dbf.newDocumentBuilder();

        dbf.setValidating(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });
        return db;
    }
}
