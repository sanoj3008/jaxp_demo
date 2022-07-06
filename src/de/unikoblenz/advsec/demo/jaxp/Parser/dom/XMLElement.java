package de.unikoblenz.advsec.demo.jaxp.Parser.dom;

import de.unikoblenz.advsec.demo.jaxp.Parser.Components.Utils;
import org.w3c.dom.Element;

public abstract class XMLElement {
    protected Element myXMLElement;
    protected String id;
    protected String desc;

    public XMLElement(Element xmlElement) {
        this.myXMLElement = xmlElement;
        this.extractMyInformation();
    }

    abstract public String print(int level);

    protected String getLevelBlanks(int level) {
        return new String(new char[level*4]).replace('\0', ' ');
    }

    private void extractMyInformation() {
        this.id = myXMLElement.getNodeName();
        this.desc = myXMLElement.getAttribute(Utils.DESCRIPTION_ATT) != null ? myXMLElement.getAttribute(Utils.DESCRIPTION_ATT) : "";
    }
}
