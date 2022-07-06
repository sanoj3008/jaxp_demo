package de.unikoblenz.advsec.demo.jaxp.Parser.Components;

import org.w3c.dom.*;

public class CarComponent implements Parsable {
    public static final String MODEL_ENTITY = "model";
    public static final String MODEL_TAG = "model";
    public static final String MODEL_ATT = "name";

    public static final String MANUFACTURING_TAG = "manufacturing";
    public static final String ENGINE_TAG = "engine";
    public static final String MAN_YEAR_TAG = "year";
    public static final String MAN_LOCATIION_TAG= "location";
    public static final String MAN_BATCH_NUMBER_TAG = "batchNumber";
    public static final String ENG_POWER_TAG= "power";
    public static final String ENG_DISPLACEMENT_TAG = "displacement";
    public static final String ENG_RPM_TAG = "rpm";

    public static final String FIN_ATT = "fin";
    public static final String LABEL_ATT = "label";

    private final String fin;
    private String model;
    private String[] manufacturing;

    private String engineLabel;
    private Double[] enginePerformance;

    private CarComponent(String fin, String model) {
        this.fin = fin;
        this.model = model;
        this.manufacturing = new String[3];
        this.enginePerformance = new Double[3];
    }

    @Override
    public Element toXML(Document doc) {
        Element car = doc.createElement(Utils.CAR_TAG);
        car.setAttribute(CarComponent.FIN_ATT, this.fin);

        car.appendChild(doc.createEntityReference(CarComponent.MODEL_ENTITY));

        return car;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Customer car information:\n");
        result.append(
                String.format("  -model: %s\n", this.model)
        );
        result.append(
                String.format("  -fin: %s\n", this.fin)
        );
        result.append(String.format("  -engine (%s):\n", this.engineLabel));
        result.append("     ⤷ power: ").append(enginePerformance[0]).append("ps\n");
        result.append("     ⤷ displacement: ").append(enginePerformance[1]).append("ccm\n");
        result.append("     ⤷ maximal revolutions: ").append(enginePerformance[2]).append("rmp\n");

        return result.toString();
    }

    public static CarComponent parse(Element element) {
        String fin = element.getAttribute(CarComponent.FIN_ATT);
        element = (Element) (element.getElementsByTagName(CarComponent.MODEL_TAG).item(0));

        CarComponent customerCar = new CarComponent(fin, element.getAttribute(CarComponent.MODEL_ATT));
        Element manufacturing = (Element) element.getElementsByTagName(CarComponent.MANUFACTURING_TAG).item(0);
        Element engine = (Element) element.getElementsByTagName(CarComponent.ENGINE_TAG).item(0);

        customerCar.setManufacturing(
                manufacturing.getElementsByTagName(CarComponent.MAN_YEAR_TAG).item(0).getTextContent(),
                manufacturing.getElementsByTagName(CarComponent.MAN_LOCATIION_TAG).item(0).getTextContent(),
                manufacturing.getElementsByTagName(CarComponent.MAN_BATCH_NUMBER_TAG).item(0).getTextContent()
        );

        customerCar.setLabel(engine.getAttribute(CarComponent.LABEL_ATT));
        customerCar.setEnginePerformance(
                Double.parseDouble(engine.getElementsByTagName(CarComponent.ENG_POWER_TAG).item(0).getTextContent()),
                Double.parseDouble(engine.getElementsByTagName(CarComponent.ENG_DISPLACEMENT_TAG).item(0).getTextContent()),
                Double.parseDouble(engine.getElementsByTagName(CarComponent.ENG_RPM_TAG).item(0).getTextContent())
        );
        return customerCar;
    }

    public void setManufacturing(String mYear, String mLocation, String batchNumber) {
        this.manufacturing[0] = mYear;
        this.manufacturing[1] = mLocation;
        this.manufacturing[2] = batchNumber;
    }

    public void setEnginePerformance(Double power, Double displacement, Double rpm) {
        this.enginePerformance[0] = power;
        this.enginePerformance[1] = displacement;
        this.enginePerformance[2] = rpm;
    }

    public void setLabel(String label) {
        this.engineLabel = label;
    }
}
