package de.unikoblenz.advsec.demo.jaxp.Parser.dom;

import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;

public class ChildNodes extends XMLElement{
    List<XMLElement> childList;

    public ChildNodes(Element xmlElement) {
        super(xmlElement);
        childList = new ArrayList<>();
    }

    @Override
    public String print(int level) {
        String blanks = this.getLevelBlanks(level);
        StringBuilder result = new StringBuilder(
                String.format(blanks + "⤷ Operation %s (description: %s):\n", this.id, this.desc));
        for(XMLElement element: this.childList) {
            result.append(element.print(level+1));
        }
        result.append('\n');
        return result.toString();
    }

    public void addChild(XMLElement child) {
        this.childList.add(child);
    }

    public List<XMLElement> getChildList() {
        return this.childList;
    }
}
