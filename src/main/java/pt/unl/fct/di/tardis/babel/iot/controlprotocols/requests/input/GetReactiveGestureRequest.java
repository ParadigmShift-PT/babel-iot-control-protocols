package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.input;

import pt.unl.fct.di.novasys.iot.device.i2c.GroveGestureDetector.PAJ7620GestureType;
import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.Threshold;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTReactiveEventRequest;

/**
 * Reactive request asking {@code I2CInputControlProtocol} to monitor a
 * Grove gesture detector and emit a
 * {@link pt.unl.fct.di.tardis.babel.iot.controlprotocols.notifications.GestureNotification}
 * whenever the supplied {@link Threshold} accepts the detected gesture.
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class GetReactiveGestureRequest
    extends IoTReactiveEventRequest<PAJ7620GestureType> {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2CInputControlProtocol} (id 2100).
     */
    public static final short REQUEST_ID = 2104;

    /**
     * @param h the handle of the gesture detector to monitor
     * @param t predicate that gates notification delivery
     */
    public GetReactiveGestureRequest(DeviceHandle h,
                                     Threshold<PAJ7620GestureType> t) {
        super(REQUEST_ID, h, t);
    }
}
