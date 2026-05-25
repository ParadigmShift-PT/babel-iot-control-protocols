package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * One-shot request asking {@code I2CInputControlProtocol} for the
 * latest gesture detected by a Grove gesture detector.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetGestureRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2CInputControlProtocol} (id 2100).
     */
    public static final short REQUEST_ID = 2103;

    /** @param h the handle of the gesture detector to read */
    public GetGestureRequest(DeviceHandle h) {
        super(REQUEST_ID, h);
    }
}
