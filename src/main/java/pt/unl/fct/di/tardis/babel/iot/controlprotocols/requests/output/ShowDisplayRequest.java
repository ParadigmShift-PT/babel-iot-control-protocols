package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code I2COutputControlProtocol} to render an
 * arbitrary bitmap on a Grove LED matrix. The {@code displayMap} byte
 * array is forwarded verbatim to the device driver.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class ShowDisplayRequest extends IoTEventRequest {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8028;

    private final byte[] displayMap;

    /**
     * @param h          the handle of the LED matrix
     * @param displayMap raw bitmap forwarded to the device driver
     */
    public ShowDisplayRequest(DeviceHandle h, byte[] displayMap) {
        super(ShowDisplayRequest.REQUEST_ID, h);
        this.displayMap = displayMap;
    }

    /** @return the raw bitmap. */
    public byte[] getDisplay() { return this.displayMap; }
}
