package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking the matching output protocol to clear a display
 * (LCD, LED matrix, four-digit display, etc.) — turning all pixels
 * or segments off.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class ClearDisplayRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8021;

    /** @param h the handle of the display to clear */
    public ClearDisplayRequest(DeviceHandle h) {
        super(REQUEST_ID, h);
    }
}
