package de.unikoblenz.advsec.demo.jaxp.Parser.Components;

import de.unikoblenz.advsec.demo.jaxp.Parser.dom.ChildNodes;
import de.unikoblenz.advsec.demo.jaxp.Parser.dom.PCData;
import de.unikoblenz.advsec.demo.jaxp.Parser.dom.XMLElement;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.util.List;

public class ActivityComponent extends ChildNodes{
    private String date;
    private String company;

    public ActivityComponent(Element element) {
        super(element);
        this.date = "";
        this.company = "";

        this.parse();
    }

    @Override
    public String toString() {
        String description = this.desc.equals("") ? "UNKNOWN" : this.desc;
        StringBuilder result = new StringBuilder(
                String.format("Activity %s (description: %s)\n", this.id, description));
        result.append(
                String.format(" -date: %s\n", this.date));
        result.append(
                String.format(" -company: %s\n", this.company));

        for(XMLElement element: this.getChildList()) {
            result.append(element.print(1));
        }

        return result.toString();
    }

    public void parse() {
        List<Element> childNodes = Utils.filterElements(this.myXMLElement.getChildNodes());

        NamedNodeMap list = this.myXMLElement.getAttributes();
        this.date = list.getNamedItem(Utils.DATE_ATT).getTextContent();
        this.date = this.myXMLElement.getAttribute(Utils.DATE_ATT);
        this.company = this.myXMLElement.getAttribute(Utils.COMPANY_ATT);

        for(Element elem: childNodes) {
            this.addChild(parseChildren(elem));
        }
    }

    private XMLElement parseChildren(Element element) {

        List<Element> childNodes = Utils.filterElements(element.getChildNodes());

        if(childNodes.size() == 0) {
            PCData xmlElement = new PCData(element);
            xmlElement.addData(element.getTextContent());

            return xmlElement;
        }

        ChildNodes xmlElement = new ChildNodes(element);

        for(Element elem: childNodes) {
            xmlElement.addChild(parseChildren(elem));
        }

        return xmlElement;
    }
}
