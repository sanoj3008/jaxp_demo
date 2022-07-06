package de.unikoblenz.advsec.demo.jaxp.TUV;

import de.unikoblenz.advsec.demo.jaxp.Parser.Components.ActivityComponent;
import de.unikoblenz.advsec.demo.jaxp.Parser.Parser;
import de.unikoblenz.advsec.demo.jaxp.TUV.Components.TUVComponent;
import de.unikoblenz.advsec.demo.jaxp.TUV.Components.MyUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class TuvRheinland extends Parser<TUVComponent> {
    TUVComponent myInvestigation;

    public TuvRheinland(File checkHeftFile, TimeZone timeZone) throws ParserConfigurationException, SAXException, IOException, ParseException {
        super(checkHeftFile, timeZone);
        this.myInvestigation = new TUVComponent(MyUtils.COMPANY_NAME, this.dateToString(new Date()));
    }

    @Override
    public void parseActivity(Element element) {
        if(element.getTagName().equals(MyUtils.MY_TAG)) {
            this.knownOperations.add(TUVComponent.parse(element));
        } else {
            this.unknownOperations.add(new ActivityComponent(element));
        }
    }

    @Override
    public Element activityToXML() throws ParserConfigurationException {
        return myInvestigation.toXML(this.document);
    }

    @Override
    public void printCheckheft() {
        System.out.println("Checkheft entries \n\n");
        System.out.println(this.customerCar.toString());
        for(ActivityComponent unknownOp: this.unknownOperations) {
            System.out.println(unknownOp.toString());
        }
        for(TUVComponent mot: this.knownOperations) {
            System.out.println(mot.toString());
        }
    }



    public void checkTiers(Double tread) {
        this.myInvestigation.checked(MyUtils.TIERS_TAG, tread);
    }

    public void checkEmission(Double emission) {
        this.myInvestigation.checked(MyUtils.EMISSION_TAG, emission);
    }

    public void defectiveBreak(boolean fl, boolean fr, boolean rl, boolean rr) {
        this.myInvestigation.defectDetected(
                MyUtils.BREAKS_TAG, determinePosition(fl, fr, rl, rr)
        );
    }

    public void defectiveAirbag(boolean fl, boolean fr, boolean rl, boolean rr) {
        this.myInvestigation.defectDetected(
                MyUtils.AIRBAG_TAG, determinePosition(fl, fr, rl, rr)
        );
    }

    private List<String> determinePosition(boolean fl, boolean fr, boolean rl, boolean rr) {
        List<String> positions = new ArrayList<>();
        if(fl) {
            positions.add(MyUtils.POS_FRONT_LEFT);
        }
        if(fr) {
            positions.add(MyUtils.POS_FRONT_RIGHT);
        }
        if(rl) {
            positions.add(MyUtils.POS_REAR_LEFT);
        }
        if(rr) {
            positions.add(MyUtils.POS_REAR_RIGHT);
        }

        return positions;
    }
}
