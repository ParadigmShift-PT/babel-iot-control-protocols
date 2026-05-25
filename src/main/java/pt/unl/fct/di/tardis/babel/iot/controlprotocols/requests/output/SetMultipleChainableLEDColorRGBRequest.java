package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code DigitalOutputControlProtocol} to drive several
 * pixels of a Grove chainable RGB LED strip in one shot, each with its
 * own 8-bit RGB colour. Pixels are addressed by index; positions can be
 * populated either through the multi-arg constructor or incrementally
 * through the {@code addValuesForPosition} / {@code setPosition*}
 * helpers.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class SetMultipleChainableLEDColorRGBRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code DigitalOutputControlProtocol} (id 2300).
     */
    public static final short REQUEST_ID = 2304;

    private final Map<Byte,byte[]> values;

    /**
     * Builds an empty request — populate it with
     * {@link #addValuesForPosition(byte, byte[])} or
     * {@link #setValuesForPosition(byte, byte, byte, byte)}.
     *
     * @param h the handle of the chainable RGB LED strip
     */
    public SetMultipleChainableLEDColorRGBRequest(DeviceHandle h) {
    	 super(REQUEST_ID, h);
         values = new TreeMap<Byte, byte[]>();
    }

    /**
     * Builds a request pre-populated with a single pixel.
     *
     * @param h     the handle of the chainable RGB LED strip
     * @param idx   index of the first pixel to set
     * @param red   red component
     * @param green green component
     * @param blue  blue component
     */
    public SetMultipleChainableLEDColorRGBRequest(DeviceHandle h, byte idx, byte red,
                                           byte green, byte blue) {
        super(REQUEST_ID, h);
        values = new TreeMap<Byte, byte[]>();
        values.put(idx, new byte[]{red,green,blue});
    }

    /**
     * Sets the colour of a pixel from a 3-element {@code [red, green,
     * blue]} array. Excess elements are ignored.
     */
    public void addValuesForPosition(byte idx, byte[] values) {
    	byte[] v = new byte[3];
    	System.arraycopy(values, 0, v, 0, Math.min(values.length, v.length));
    	this.values.put(idx, v);
    }

    /** Sets the full RGB triplet for a given pixel index. */
    public void setValuesForPosition(byte idx, byte red, byte green, byte blue) {
    	this.values.put(idx, new byte[] {red,green,blue});
    }

    /**
     * Sets only the red component of a given pixel; green and blue
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionRed(byte idx, byte red) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[0] = red;
    }

    /**
     * Sets only the green component of a given pixel; red and blue
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionGreen(byte idx, byte green) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[1] = green;
    }

    /**
     * Sets only the blue component of a given pixel; red and green
     * default to {@code 0} when the pixel is not yet present.
     */
    public void setPositionBlue(byte idx, byte blue) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[2] = blue;
    }

    /** @return iterator over pixel indices populated in this request. */
    public Iterator<Byte> getPositionsIterator() {
    	return values.keySet().iterator();
    }

    /** @return the {@code [red, green, blue]} triplet for the given pixel. */
    public byte[] getPositionValues(byte idx) {
    	return this.values.get(idx);
    }

    /** @return the red component for the given pixel. */
    public byte getPositionRed(byte idx) {
    	return this.values.get(idx)[0];
    }

    /** @return the green component for the given pixel. */
    public byte getPositionGreen(byte idx) {
    	return this.values.get(idx)[1];
    }

    /** @return the blue component for the given pixel. */
    public byte getPositionBlue(byte idx) {
    	return this.values.get(idx)[2];
    }
}
