package de.unikoblenz.advsec.demo.jaxp.Parser.dom;

import org.w3c.dom.Element;

public class PCData extends XMLElement{
    String nodeContent;

    public PCData(Element xmlElement) {
        super(xmlElement);
        this.nodeContent = "";
    }

    @Override
    public String print(int level) {
        String blanks = this.getLevelBlanks(level);
        return String.format(blanks + "⤷ Operation %s (description: %s): %s\n", this.id, this.desc, this.nodeContent);
    }

    public void addData(String data) {
        this.nodeContent = data;
    }
}
