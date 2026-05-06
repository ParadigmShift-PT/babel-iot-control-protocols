package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code DigitalOutputControlProtocol} to drive a single
 * pixel of a Grove chainable RGB LED strip using 8-bit RGB components.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class SetChainableLEDColorRGBRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8023;

    private final byte idx;
    private final byte red;
    private final byte green;
    private final byte blue;

    /**
     * @param h     the handle of the chainable RGB LED strip
     * @param idx   index of the pixel to set
     * @param red   red component (0-255 as a signed byte)
     * @param green green component (0-255 as a signed byte)
     * @param blue  blue component (0-255 as a signed byte)
     */
    public SetChainableLEDColorRGBRequest(DeviceHandle h, byte idx, byte red,
                                           byte green, byte blue) {
        super(REQUEST_ID, h);
        this.idx = idx;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /** @return the index of the targeted pixel. */
    public byte getIdx() { return idx; }

    /** @return the red component. */
    public byte getRed() { return this.red; }

    /** @return the green component. */
    public byte getGreen() { return this.green; }

    /** @return the blue component. */
    public byte getBlue() { return this.blue; }
}
