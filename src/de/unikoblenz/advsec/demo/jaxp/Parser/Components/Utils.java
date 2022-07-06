package de.unikoblenz.advsec.demo.jaxp.Parser.Components;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String CAR_TAG = "car";
    public static final String ACTIVITIES_TAG = "activities";

    public static final String DATE_ATT = "date";
    public static final String COMPANY_ATT = "company";
    public static final String DESCRIPTION_ATT = "desc";

    public static List<Element> filterElements(NodeList childNodes) {
        List<Element> nodes = new ArrayList<>();

        for(int i=0; i<childNodes.getLength(); i++) {
            if (!(childNodes.item(i) instanceof Element)) continue;
            nodes.add((Element)childNodes.item(i));
        }
        return nodes;
    }
}
