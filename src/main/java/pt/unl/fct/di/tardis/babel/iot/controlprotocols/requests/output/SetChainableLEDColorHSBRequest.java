package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code DigitalOutputControlProtocol} to drive a single
 * pixel of a Grove chainable RGB LED strip using HSB colour
 * coordinates.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class SetChainableLEDColorHSBRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8022;

    private final byte idx;
    private final float hue;
    private final float saturation;
    private final float brightness;

    /**
     * @param h          the handle of the chainable RGB LED strip
     * @param idx        index of the pixel to set
     * @param hue        hue component
     * @param saturation saturation component
     * @param brightness brightness component
     */
    public SetChainableLEDColorHSBRequest(DeviceHandle h, byte idx, float hue,
                                           float saturation, float brightness) {
        super(REQUEST_ID, h);
        this.idx = idx;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    /** @return the index of the targeted pixel. */
    public byte getIdx() { return idx; }

    /** @return the hue component. */
    public float getHue() { return this.hue; }

    /** @return the saturation component. */
    public float getSaturation() { return this.saturation; }

    /** @return the brightness component. */
    public float getBrightness() { return this.brightness; }
}
