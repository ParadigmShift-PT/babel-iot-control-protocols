package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.novasys.iot.device.digital.GroveEncoder;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTReactiveEventRequest;

/**
 * Reactive request asking {@code DigitalInputControlProtocol} to
 * monitor a Grove encoder and emit an
 * {@link pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications.EncoderNotification}
 * whenever the supplied {@link Threshold} accepts the observed rotation.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetReactiveEncoderRequest
    extends IoTReactiveEventRequest<GroveEncoder.Rotation> {

    /** Default Babel request id used by this class. */
    public static final short REQUEST_ID = 8015;

    /**
     * @param h the handle of the encoder to monitor
     * @param t predicate that gates notification delivery
     */
    public GetReactiveEncoderRequest(DeviceHandle h,
                                     Threshold<GroveEncoder.Rotation> t) {
        super(REQUEST_ID, h, t);
    }
}
