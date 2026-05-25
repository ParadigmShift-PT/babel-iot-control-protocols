package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * One-shot request asking {@code DigitalInputControlProtocol} for the
 * current rotation observed on a Grove encoder.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetEncoderRotationRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code DigitalInputControlProtocol} (id 2200).
     */
    public static final short REQUEST_ID = 2201;

    /** @param h the handle of the encoder to read */
    public GetEncoderRotationRequest(DeviceHandle h) {
        super(REQUEST_ID, h);
    }
}
