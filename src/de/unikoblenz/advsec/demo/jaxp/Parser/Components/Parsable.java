package de.unikoblenz.advsec.demo.jaxp.Parser.Components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Parsable {
    public Element toXML(Document doc);
}
