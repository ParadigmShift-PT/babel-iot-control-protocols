package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code DigitalOutputControlProtocol} to drive several
 * pixels of a Grove chainable RGB LED strip in one shot, each with its
 * own HSB colour. Pixels are addressed by index; positions can be
 * populated either through the multi-arg constructor or incrementally
 * through the {@code addValuesForPosition} / {@code setPosition*}
 * helpers.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class SetMultipleChainableLEDColorHSBRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code DigitalOutputControlProtocol} (id 2300).
     */
    public static final short REQUEST_ID = 2303;

    private final Map<Byte,float[]> values;

    /**
     * Builds an empty request — populate it with
     * {@link #addValuesForPosition(byte, float[])} or one of the
     * {@code setPosition*} setters.
     *
     * @param h the handle of the chainable RGB LED strip
     */
    public SetMultipleChainableLEDColorHSBRequest(DeviceHandle h) {
    	super(REQUEST_ID, h);
    	values = new TreeMap<Byte, float[]>();
    }

    /**
     * Builds a request pre-populated with a single pixel.
     *
     * @param h          the handle of the chainable RGB LED strip
     * @param idx        index of the first pixel to set
     * @param hue        hue component
     * @param saturation saturation component
     * @param brightness brightness component
     */
    public SetMultipleChainableLEDColorHSBRequest(DeviceHandle h, byte idx, float hue,
                                           float saturation, float brightness) {
        super(REQUEST_ID, h);
        values = new TreeMap<Byte, float[]>();
        values.put(idx, new float[]{hue, saturation, brightness});

    }

    /**
     * Sets the colour of a pixel from a 3-element {@code [hue,
     * saturation, brightness]} array. Excess elements are ignored.
     */
    public void addValuesForPosition(byte idx, float[] values) {
    	float[] v = new float[3];
    	System.arraycopy(values, 0, v, 0, Math.min(values.length, v.length));
    	this.values.put(idx, v);
    }

    /** Sets the full HSB triplet for a given pixel index. */
    public void addValuesForPosition(byte idx, float hue, float saturation, float brightness) {
    	this.values.put(idx, new float[] {hue, saturation, brightness});
    }

    /**
     * Sets only the hue of a given pixel; saturation and brightness
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionHue(byte idx, float hue) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[0] = hue;
    }

    /**
     * Sets only the saturation of a given pixel; hue and brightness
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionSaturation(byte idx, float saturation) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[1] = saturation;
    }

    /**
     * Sets only the brightness of a given pixel; hue and saturation
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionBrightness(byte idx, float brightness) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[2] = brightness;
    }

    /** @return iterator over pixel indices populated in this request. */
    public Iterator<Byte> getPositionsIterator() {
    	return values.keySet().iterator();
    }

    /** @return the {@code [hue, saturation, brightness]} triplet for the given pixel. */
    public float[] getPositionValues(byte idx) {
    	return this.values.get(idx);
    }

    /** @return the hue component for the given pixel. */
    public float getPositionHue(byte idx) {
    	return this.values.get(idx)[0];
    }

    /** @return the saturation component for the given pixel. */
    public float getPositionSaturation(byte idx) {
    	return this.values.get(idx)[1];
    }

    /** @return the brightness component for the given pixel. */
    public float getPositionBrightness(byte idx) {
    	return this.values.get(idx)[2];
    }
}
