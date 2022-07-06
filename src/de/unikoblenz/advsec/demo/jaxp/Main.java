package de.unikoblenz.advsec.demo.jaxp;

import de.unikoblenz.advsec.demo.jaxp.TUV.TuvRheinland;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, ParseException, TransformerException {
        // Inject xml file to parser
        File xmlFile = new File("./res/checkheft.xml");
        // Creation of specific parser instance automatically extracts information form provided file
        TuvRheinland tuv = new TuvRheinland(xmlFile, TimeZone.getTimeZone("Europe/Berlin"));

        // display current checkheft entries
        tuv.printCheckheft();

        // add some investigations
        tuv.checkTiers(5.4);
        tuv.checkEmission(12.0);
        tuv.defectiveBreak(false, false, true, true);

        // persist investigations again
        tuv.storeCheckheft();
    }
}
