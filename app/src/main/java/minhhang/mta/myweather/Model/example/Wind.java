package minhhang.mta.myweather.Model.example;

import java.util.HashMap;
import java.util.Map;


public class Wind {

    private double speed;
    private int deg;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Wind() {
    }

    /**
     * 
     * @param speed
     * @param deg
     */
    public Wind(double speed, int deg) {
        this.speed = speed;
        this.deg = deg;
    }

    /**
     * 
     * @return
     *     The speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The deg
     */
    public int getDeg() {
        return deg;
    }

    /**
     * 
     * @param deg
     *     The deg
     */
    public void setDeg(int deg) {
        this.deg = deg;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
