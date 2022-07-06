package de.unikoblenz.advsec.demo.jaxp.TUV.Components;

import de.unikoblenz.advsec.demo.jaxp.Parser.Components.Parsable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

public class TUVComponent implements Parsable {
    private final String company;

    private String timestamp;
    private Map<String, Double> checked;
    private Map<String, List<String>> defects;

    public TUVComponent(String companyName, String timestamp) {
        this.company = companyName;
        this.timestamp = timestamp;
        checked = new HashMap<>();
        defects = new HashMap<>();
    }

    @Override
    public Element toXML(Document doc) {
        Element activity = doc.createElement(MyUtils.MY_TAG);
        activity.setAttribute(MyUtils.DESCRIPTION_ATT, MyUtils.MY_DESC);
        activity.setAttribute(MyUtils.DATE_ATT, this.timestamp);
        activity.setAttribute(MyUtils.COMPANY_ATT, this.company);

        Element checks = doc.createElement(MyUtils.CHECKS_TAG);
        checks.setAttribute(MyUtils.DESCRIPTION_ATT, MyUtils.CHECKS_DESC);

        Element tmp = doc.createElement(MyUtils.TIERS_TAG);
        tmp.setAttribute(MyUtils.DESCRIPTION_ATT, MyUtils.TIERS_DESC);
        tmp.setTextContent(this.checked.get(MyUtils.TIERS_TAG).toString());
        checks.appendChild(tmp);

        tmp = doc.createElement(MyUtils.EMISSION_TAG);
        tmp.setAttribute(MyUtils.DESCRIPTION_ATT, MyUtils.EMISSION_DESC);
        tmp.setTextContent(this.checked.get(MyUtils.EMISSION_TAG).toString());
        checks.appendChild(tmp);

        activity.appendChild(checks);

        if(this.defects.size() > 0) {
            Element defects = doc.createElement(MyUtils.DEFECTS_TAG);
            defects.setAttribute(MyUtils.DESCRIPTION_ATT, MyUtils.DEFECTS_DESC);

            boolean validEntry = false;

            List<String> positions = this.defects.get(MyUtils.BREAKS_TAG);
            if(positions != null && positions.size() > 0) {
                tmp = doc.createElement(MyUtils.BREAKS_TAG);
                tmp.setAttribute(MyUtils.POSITION_ATT, String.join("|",this.defects.get(MyUtils.BREAKS_TAG)));
                defects.appendChild(tmp);

                validEntry = true;
            }

            positions = this.defects.get(MyUtils.AIRBAG_TAG);
            if(positions != null && positions.size() > 0) {
                tmp = doc.createElement(MyUtils.AIRBAG_TAG);
                tmp.setAttribute(MyUtils.POSITION_ATT, String.join("|",this.defects.get(MyUtils.AIRBAG_TAG)));
                defects.appendChild(tmp);

                validEntry = true;
            }

            if(validEntry) {
                activity.appendChild(defects);
            }
        }
        return activity;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                String.format("Regulary MOT investigation. Conducted by %s at %s.\n", this.company, this.timestamp)
        );
        result.append("Checked components:\n");
        result.append("  ⤷ measured tiere tread depth: ").append(this.checked.get(MyUtils.TIERS_TAG)).append('\n');
        result.append("  ⤷ measured co2 emission: ").append(this.checked.get(MyUtils.EMISSION_TAG)).append('\n');

        if(this.defects.keySet().size() > 0) {
            result.append("Detected defects:\n");
            if(this.defects.get(MyUtils.BREAKS_TAG) != null) {
                result.append("  ⤷ breaks must be changed at position ").append(String.join(",", this.defects.get(MyUtils.BREAKS_TAG))).append('\n');
            }
            if(this.defects.get(MyUtils.AIRBAG_TAG) != null) {
                result.append("  ⤷ airbag functionality not guaranteed at position ").append(String.join(",", this.defects.get(MyUtils.AIRBAG_TAG))).append('\n');
            }
        }

        return result.toString();
    }

    public static TUVComponent parse(Element element) {
        String companyName = element.getAttribute(MyUtils.COMPANY_ATT);

        TUVComponent investigation = new TUVComponent(companyName, element.getAttribute(MyUtils.DATE_ATT));

        Element checks = (Element) element.getElementsByTagName(MyUtils.CHECKS_TAG).item(0);
        Element defects = (Element) element.getElementsByTagName(MyUtils.DEFECTS_TAG).item(0);

        investigation.checked(MyUtils.TIERS_TAG,
                Double.parseDouble(
                        checks.getElementsByTagName(MyUtils.TIERS_TAG).item(0)
                                .getTextContent()
                )
        );

        investigation.checked(MyUtils.EMISSION_TAG,
                Double.parseDouble(
                        checks.getElementsByTagName(MyUtils.EMISSION_TAG).item(0)
                                .getTextContent()
                )
        );

        List<String> tmp;
        if(defects.getElementsByTagName(MyUtils.BREAKS_TAG).getLength() > 0) {
            tmp = Arrays.asList(
                    ((Element) defects.getElementsByTagName(MyUtils.BREAKS_TAG).item(0))
                            .getAttribute(MyUtils.POSITION_ATT)
                            .split("\\|")
            );
            investigation.defectDetected(MyUtils.BREAKS_TAG, tmp);
        }

        if(defects.getElementsByTagName(MyUtils.AIRBAG_TAG).getLength() > 0) {
            tmp = Arrays.asList(
                    ((Element) defects.getElementsByTagName(MyUtils.AIRBAG_TAG).item(0))
                            .getAttribute(MyUtils.POSITION_ATT)
                            .split("\\|")
            );
            investigation.defectDetected(MyUtils.AIRBAG_TAG, tmp);
        }

        return investigation;
    }

    public void checked(String key, Double value) {
        this.checked.put(key, value);
    }

    public void defectDetected(String key, List<String> value) {
        this.defects.put(key, value);
    }
}
