package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code I2COutputControlProtocol} to set the backlight
 * colour of a Grove RGB LCD. Each component is clamped to {@code [0, 255]}
 * on construction.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class SetDisplayColorRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8024;

    private final int red;
    private final int green;
    private final int blue;

    /**
     * @param h     the handle of the RGB LCD
     * @param red   red component (clamped to {@code [0, 255]})
     * @param green green component (clamped to {@code [0, 255]})
     * @param blue  blue component (clamped to {@code [0, 255]})
     */
    public SetDisplayColorRequest(DeviceHandle h, int red, int green,
                                  int blue) {
        super(REQUEST_ID, h);
        this.red = Math.max(0, Math.min(255, red));
        this.green = Math.max(0, Math.min(255, green));
        this.blue = Math.max(0, Math.min(255, blue));
    }

    /** @return the (clamped) red component. */
    public int getRed() { return this.red; }

    /** @return the (clamped) green component. */
    public int getGreen() { return this.green; }

    /** @return the (clamped) blue component. */
    public int getBlue() { return this.blue; }
}
